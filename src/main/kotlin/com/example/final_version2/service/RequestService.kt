package com.example.final_version2.service

import com.example.final_version2.base.BaseResponse
import com.example.final_version2.base.resultEmpty
import com.example.final_version2.base.resultError
import com.example.final_version2.base.resultSuccess
import com.example.final_version2.common.Constants
import com.example.final_version2.model.Request
import com.example.final_version2.repository.AddKidRequestRepository
import com.example.final_version2.repository.AddParentRequestRepository
import com.example.final_version2.repository.ChildRepository
import com.example.final_version2.repository.ParentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RequestService {

    @Autowired
    private lateinit var addKidRequestRepository: AddKidRequestRepository

    @Autowired
    private lateinit var addParentRequestRepository: AddParentRequestRepository

    @Autowired
    private lateinit var childRepository: ChildRepository

    @Autowired
    private lateinit var parentRepository: ParentRepository

    fun getUserRequest(email: String): BaseResponse<List<Request>> {
        val allKidRequest = addKidRequestRepository.findAll()
        val allParentRequest = addParentRequestRepository.findAll()

        val result = mutableListOf<Request>()

        for (request in allKidRequest) {
            if (request.child.email == email && (request.action == Constants.RequestType.REQUEST_ADD_CHILD || request.action == Constants.RequestType.REQUEST_ADD_PARTNER)) {
                result.add(request)
            }
        }

        for (request in allParentRequest) {
            if (request.partner.email == email && (request.action == Constants.RequestType.REQUEST_ADD_CHILD || request.action == Constants.RequestType.REQUEST_ADD_PARTNER)) {
                result.add(request)
            }
        }

        return if (result.isEmpty()) {
            resultEmpty()
        } else {
            resultSuccess(result)
        }
    }

    fun acceptRequest(requestId: Long): BaseResponse<List<Request>> {
        val kidRequest = addKidRequestRepository.findById(requestId)
        val parentRequest = addParentRequestRepository.findById(requestId)
        if (!kidRequest.isPresent && !parentRequest.isPresent) {
            return resultError("Cannot find request")
        }
        if (kidRequest.isPresent) {
            val child = kidRequest.get().child
            if (child.superUser1 == null) {
                child.superUser1 = kidRequest.get().requester
            } else if (child.superUser2 == null) {
                child.superUser2 = kidRequest.get().requester
            }
            childRepository.save(child)
            addKidRequestRepository.deleteById(requestId)
            return getUserRequest(child.email)
        } else {
            val partner = parentRequest.get().partner
            val requester = parentRequest.get().requester
            requester.partnerEmail = partner.email
            partner.partnerEmail = requester.email
            parentRepository.save(requester)
            parentRepository.save(partner)
            addParentRequestRepository.deleteById(requestId)
            return getUserRequest(partner.email)
        }

    }

    fun denyRequest(requestId: Long): BaseResponse<List<Request>> {
        val kidRequest = addKidRequestRepository.findById(requestId)
        val parentRequest = addParentRequestRepository.findById(requestId)
        if (!kidRequest.isPresent && !parentRequest.isPresent) {
            return resultError("Cannot find request")
        }
        if (kidRequest.isPresent) {
            addKidRequestRepository.deleteById(requestId)
            return getUserRequest(kidRequest.get().child.email)
        } else {
            addParentRequestRepository.deleteById(requestId)
            return getUserRequest(parentRequest.get().partner.email)
        }
    }
}