package config

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.io.File

data class TelegramConfiguration(
    val token: String
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
        val file = try {
            File(".\\config.json")
        } catch (exception: RuntimeException) {
            null
        }
        return if (file != null && file.exists()) {
            //DEV mode
            return objectMapper.readValue(file, Configuration::class.java)
        } else {
            // PROD mode
            return with(System.getenv()) {
                val dbConfiguration = DbConfiguration(
                    get("DB_URL").toString(),
                    get("DB_DRIVER").toString(),
                    get("DB_USER").toString(),
                    get("DB_PSWD").toString()
                )
                val telegramConfiguration = TelegramConfiguration(get("TELEGRAM_TOKEN").toString())
                Configuration(telegramConfiguration, dbConfiguration)
            }
        }
    }
}