package com.example.vktrainee052023

import com.example.vktrainee052023.AnalyticsClasses.EventAnalytics
import com.example.vktrainee052023.AnalyticsClasses.IpAnalytics
import com.example.vktrainee052023.AnalyticsClasses.StatusAnalytics
import com.google.gson.Gson
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.query
import org.springframework.stereotype.Repository
import java.lang.IllegalArgumentException
import java.sql.ResultSet
import java.time.LocalDate

@Repository
class TablesRepository(val jdbcTemplate: JdbcTemplate) {
    var gson = Gson()
    fun addEvent(name: String, status: Boolean, data: String) {
        val dataSplit = data.split(" ", ignoreCase = false, limit = 2)
        if (!Regex("""^((25[0-5]|(2[0-4]|1[0-9]|[1-9]|)[0-9])(\.(?!$)|$)){4}$""").matches(dataSplit.first())) {
            throw IllegalArgumentException("Incorrect ip address")
        }
        jdbcTemplate.update("INSERT INTO events (date, event_name, user_auth, user_ip, data) VALUES (?,?,?,?,?)",
            LocalDate.now(),
            name,
            status,
            dataSplit.first(),
            dataSplit.last()
        )
    }

    fun returnIpAnalytics(
        name: String,
        startingDate: LocalDate = LocalDate.of(1980, 1, 1),
        endingDate: LocalDate = LocalDate.now()
    ): String {
        val result = jdbcTemplate.query(
            "SELECT user_ip, count(*)\n" +
                    "FROM events\n" +
                    "WHERE (date <= ? and date >= ? and event_name = ?)\n" +
                    "GROUP BY user_ip",
            endingDate, startingDate, name
        ) {
            result: ResultSet, _: Int ->
            IpAnalytics(
                result.getString("user_ip"),
                result.getInt("count")
            )
        }
        return gson.toJson(result)
    }

    fun returnIpAnalytics(
        startingDate: LocalDate = LocalDate.of(1980, 1, 1),
        endingDate: LocalDate = LocalDate.now()
    ): String {
        val result = jdbcTemplate.query(
            "SELECT user_ip, count(*)\n" +
                    "FROM events\n" +
                    "WHERE (date <= ? and date >= ?)\n" +
                    "GROUP BY user_ip",
            endingDate, startingDate
        ) {
                result: ResultSet, _: Int ->
            IpAnalytics(
                result.getString("user_ip"),
                result.getInt("count")
            )
        }
        return gson.toJson(result)
    }

    fun returnEventAnalytics(
        name: String,
        startingDate: LocalDate = LocalDate.of(1980, 1, 1),
        endingDate: LocalDate = LocalDate.now()
    ): String {
        val result = jdbcTemplate.query(
            "SELECT event_name, count(*)\n" +
                    "FROM events\n" +
                    "WHERE (date <= ? and date >= ?)\n" +
                    "GROUP BY event_name",
            endingDate, startingDate, name
        ) {
                result: ResultSet, _: Int ->
            EventAnalytics(
                result.getString("event_name"),
                result.getInt("count")
            )
        }
        return gson.toJson(result)
    }

    fun returnStatusAnalytics(
        name: String,
        startingDate: LocalDate = LocalDate.of(1980, 1, 1),
        endingDate: LocalDate = LocalDate.now()
    ): String {
        val result = jdbcTemplate.query(
            "SELECT user_auth, count(*)\n" +
                "FROM events\n" +
                "WHERE (date <= ? and date >= ? and event_name = ?)\n" +
                "GROUP BY user_auth",
            endingDate, startingDate, name
        ) {
            result: ResultSet, _: Int ->
            StatusAnalytics(
                result.getBoolean("user_auth"),
                result.getInt("count")
            )
        }
        return gson.toJson(result)
    }

    fun returnStatusAnalytics(
        startingDate: LocalDate = LocalDate.of(1980, 1, 1),
        endingDate: LocalDate = LocalDate.now()
    ): String {
        val result = jdbcTemplate.query(
            "SELECT user_auth, count(*)\n" +
                    "FROM events\n" +
                    "WHERE (date <= ? and date >= ?)\n" +
                    "GROUP BY user_auth",
            endingDate, startingDate
        ) {
            result: ResultSet, _: Int ->
            StatusAnalytics(
                result.getBoolean("user_auth"),
                result.getInt("count")
            )
        }
        return gson.toJson(result)
    }
}