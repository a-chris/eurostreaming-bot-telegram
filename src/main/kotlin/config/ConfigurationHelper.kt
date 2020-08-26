package config

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.io.File

data class TelegramConfiguration(
    val token: String
)

data class DbConfiguration(
    val url: String,
    val driver: String,
    val user: String?,
    val password: String?
)

data class Configuration(
    val interval: Long,
    val telegram: TelegramConfiguration,
    val database: DbConfiguration,
    val siteUrl: String
)

private const val DEFAULT_INTERVAL = 1_800_000L // default interval is half an hour

object ConfigurationHelper {

    private val objectMapper = jacksonObjectMapper()

    fun get(): Configuration {
        val file = try {
            File(".${File.separator}config.json")
        } catch (exception: RuntimeException) {
            null
        }
        return if (file != null && file.exists()) {
            //DEV mode
            objectMapper.readValue(file, Configuration::class.java)
        } else {
            // PROD mode
            return with(System.getenv()) {
                val interval = get("INTERVAL")?.toLong() ?: DEFAULT_INTERVAL
                val dbConfiguration = DbConfiguration(
                    get("DB_URL").toString(),
                    get("DB_DRIVER").toString(),
                    get("DB_USER").toString(),
                    get("DB_PSWD").toString()
                )
                val telegramConfiguration = TelegramConfiguration(get("TELEGRAM_TOKEN").toString())
                val siteUrl = get("SITE_URL") ?: ""
                Configuration(interval, telegramConfiguration, dbConfiguration, siteUrl)
            }
        }
    }
}