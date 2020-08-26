package service.database

import config.DbConfiguration
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager
import java.sql.Connection

class MyDatabase(configuration: DbConfiguration) {

    private val db = Database.connect(
        configuration.url,
        configuration.driver,
        configuration.user ?: "",
        configuration.password ?: ""
    )

    init {
        TransactionManager.manager.defaultIsolationLevel =
            Connection.TRANSACTION_SERIALIZABLE
    }

    fun get() = db

}