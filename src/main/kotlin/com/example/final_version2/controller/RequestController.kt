package com.example.final_version2.controller

import com.example.final_version2.base.BaseResponse
import com.example.final_version2.model.Request
import com.example.final_version2.service.RequestService
import com.example.final_version2.utils.MyStringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/request")
class RequestController {

    @Autowired
    private lateinit var requestService: RequestService

    @GetMapping("get_my_requests")
    fun getMyRequest(
        @RequestParam("user_email") userEmail: String
    ): BaseResponse<List<Request>> {
        val email = MyStringUtils().escapeDoubleQuotes(userEmail)
        return requestService.getUserRequest(email)
    }

    @PostMapping("accept_request")
    fun acceptRequest(
        @RequestParam("request_id") requestId: Long
    ): BaseResponse<List<Request>> {
        return requestService.acceptRequest(requestId)
    }

    @PostMapping("deny_request")
    fun denyRequest(
        @RequestParam("request_id") requestId: Long
    ): BaseResponse<List<Request>> {
        return requestService.denyRequest(requestId)
    }


}