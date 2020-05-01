package service.database

import config.DbConfiguration
import service.database.model.UserShowTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class MyDatabase(configuration: DbConfiguration) {

    private val db = Database.connect(
        configuration.url,
        configuration.driver,
        configuration.user,
        configuration.password
    )

    fun get() = db

}