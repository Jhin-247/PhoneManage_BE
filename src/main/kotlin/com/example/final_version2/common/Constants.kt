package com.example.final_version2.common

object Constants {

    const val API_QUERY_DATE_FORMAT = "YYYY-MM-dd HH:mm:ss"
    const val DEFAULT_END_DATE_DAYS = 7

    object Code {
        const val SUCCESS = 200
        const val ERROR = 201
        const val EMPTY = 202
        const val NEED_IMAGE = 203
    }

    object Message {
        const val LOGIN_SOMEWHERE_ELSE = "Logged in somewhere else"
        const val SUCCESS = "Success"

        const val EMPTY_PICTURE = "Please chose picture to register user"

        const val EMAIL_EXISTED = "Email existed, please chose another email"

        const val NO_LONGER_LOGGED_IN = "This account is logged in in somewhere else, please log in again"

        const val LOGIN_FAIL = "Wrong email or password, please try again"
        const val EMPTY = "Nothing found"
    }

    object Description {
        const val SUCCESS = "Success"
        const val ERROR = "Error"
        const val EMPTY = "Empty"
    }

    object Role {
        const val PERSONAL = 1
        const val PARENT = 3
        const val TEACHER = 2
        const val BOSS = 4
        const val CHILD = 5
        const val STUDENT = 6
        const val EMPLOYEE = 7
    }

    object RequestType {
        const val REQUEST_ADD_PARTNER = 0
        const val ACCEPT_ADD_PARTNER = 1
        const val REJECT_ADD_PARTNER = 2

        const val REQUEST_ADD_CHILD = 3
    }

    object NotificationType {
        const val REQUEST_PARTNER = 1
        const val REQUEST_PARTNER_RESPONSE = 2
        const val REQUEST_KID = 100
    }

    object AppUsageQueryType {
        const val TODAY = 1
        const val LAST_7_DAY = 2
        const val LAST_MONTH = 3
        const val ALL = 4
    }

    object V2 {
        const val PREFIX = "api/v2"
    }

}