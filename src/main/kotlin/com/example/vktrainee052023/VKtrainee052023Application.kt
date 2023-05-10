package com.example.vktrainee052023

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.jdbc.core.JdbcTemplate
import java.lang.IllegalArgumentException
import java.time.LocalDate


@SpringBootApplication
@EnableCaching
class VKtrainee052023Application(@Autowired val jdbcTemplate: JdbcTemplate) {
	@Bean
	fun commandLineRunner(ctx: ApplicationContext): CommandLineRunner? {
		return CommandLineRunner { args: Array<String> ->
			val statUtils = StatUtils(jdbcTemplate)
			if (args.size < 2) {
				throw IllegalArgumentException("No less than 2 parameters required")
			}
			when {
				args.first() == "1" -> {
					statUtils.firstFun(
						args[1],
						args[2].toBooleanStrict(),
						args.filterIndexed { index, _ -> index >= 3 }.joinToString(" ")
					)
				}
				args.first() == "2" -> {
					val result =
					when (args.size) {
						2 -> statUtils.secondFun(args[1])
						3 -> statUtils.secondFun(args[1], args[2])
						4 -> statUtils.secondFun(args[1], args[2], LocalDate.parse(args[3]))
						5 -> statUtils.secondFun(args[1], args[2], LocalDate.parse(args[3]), LocalDate.parse(args[4]))
						else -> throw IllegalArgumentException("Too many parameters")
					}
					println(result)
				}
				else -> {
					throw IllegalArgumentException("First parameter of program specifies method: " +
							"\"1\" for the first method, \"2\" for the second")
				}
			}
		}
	}
}

fun main(args: Array<String>) {
	runApplication<VKtrainee052023Application>(*args)
}



