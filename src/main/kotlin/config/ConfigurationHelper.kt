package config

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.io.File

data class TelegramConfiguration(
    val token: String,
    val appUrl: String
)

data class DbConfiguration(
    val url: String,
    val driver: String,
    val user: String,
    val password: String
)

data class Configuration(
    val telegram: TelegramConfiguration,
    val database: DbConfiguration
)

object ConfigurationHelper {

    private val objectMapper = jacksonObjectMapper()

    fun get(): Configuration {
        val file = File(".\\config.json")
        return objectMapper.readValue(file, Configuration::class.java)
    }
}