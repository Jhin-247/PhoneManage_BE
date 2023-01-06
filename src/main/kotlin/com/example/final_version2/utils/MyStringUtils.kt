package com.example.final_version2.utils

class MyStringUtils {

    fun escapeDoubleQuotes(original: String): String {
        var result = ""
        result = if (original.startsWith("\"") && original.endsWith("\"")) {
            original.substring(1, original.length - 1)
        } else {
            original
        }
        return result
    }

}