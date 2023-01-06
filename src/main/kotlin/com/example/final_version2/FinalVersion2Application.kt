package com.example.final_version2

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication
class FinalVersion2Application

fun main(args: Array<String>) {
    runApplication<FinalVersion2Application>(*args)
}
