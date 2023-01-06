package com.example.final_version2.base

import com.example.final_version2.common.Constants

class BaseResponse<T> {
    var code: Int = 0
    var description: String = ""
    var message: String = ""
    var data: T? = null
}

fun <T> resultExisted(): BaseResponse<T> {
    val result = BaseResponse<T>()
    result.code = Constants.Code.ERROR
    result.message = Constants.Message.EMAIL_EXISTED
    return result
}

fun <T> resultSuccess(data: T): BaseResponse<T> {
    val result = BaseResponse<T>()
    result.code = Constants.Code.SUCCESS
    result.message = Constants.Message.SUCCESS
    result.data = data
    return result
}

fun <T> resultError(error: String): BaseResponse<T> {
    val result = BaseResponse<T>()
    result.code = Constants.Code.ERROR
    result.message = error
    return result
}

fun <T> resultEmpty(): BaseResponse<T> {
    val result = BaseResponse<T>()
    result.code = Constants.Code.EMPTY
    result.message = Constants.Message.EMPTY
    return result
}

fun <T> resultNeedUpload(data: T): BaseResponse<T> {
    val result = BaseResponse<T>()
    result.code = Constants.Code.NEED_IMAGE
    result.message = Constants.Message.SUCCESS
    result.data = data
    return result
}
