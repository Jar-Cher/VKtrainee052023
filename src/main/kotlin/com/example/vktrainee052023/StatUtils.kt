package com.example.vktrainee052023

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import java.lang.IllegalArgumentException
import java.time.LocalDate

@Component
class StatUtils(jdbcTemplate: JdbcTemplate) {
    var tablesRepository: TablesRepository = TablesRepository(jdbcTemplate)

    fun firstFun(name: String, status: Boolean, data: String) {
        tablesRepository.addEvent(name, status, data)
    }

    fun secondFun(
        counterType: String,
        name: String,
        startingDate: LocalDate = LocalDate.of(1980, 1, 1),
        endingDate: LocalDate = LocalDate.now()
    ): String {
        val result =
            when (counterType) {
                "Ip" -> tablesRepository.returnIpAnalytics(name, startingDate, endingDate)
                "Event" -> tablesRepository.returnEventAnalytics(name, startingDate, endingDate)
                "Status" -> tablesRepository.returnStatusAnalytics(name, startingDate, endingDate)
                else -> throw IllegalArgumentException("Second method requires counter type " +
                        "(\"Ip\", \"Event\", \"Status\") as second parameter")
            }
        return result
    }

    fun secondFun(
        counterType: String,
        startingDate: LocalDate = LocalDate.of(1980, 1, 1),
        endingDate: LocalDate = LocalDate.now()
    ): String {
        val result =
            when (counterType) {
                "Ip" -> tablesRepository.returnIpAnalytics(startingDate, endingDate)
                "Event" -> throw IllegalArgumentException("Second method requires event_name as third parameter")
                "Status" -> tablesRepository.returnStatusAnalytics(startingDate, endingDate)
                else -> throw IllegalArgumentException("Second method requires counter type " +
                        "(\"Ip\", \"Event\", \"Status\") as second parameter")
            }
        return result
    }
}