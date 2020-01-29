package database.model

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object User : Table("user") {
    val id: Column<String> = varchar("id_telegram", 100)
    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

object Show : Table("show") {
    val name: Column<String> = varchar("name", 100)
    override val primaryKey: PrimaryKey = PrimaryKey(name)
}

object UserShow: Table("user_show") {
    val userId: Column<String> = varchar("user_pk", 100)
    val showId: Column<String> = varchar("show_pk", 100)
    override val primaryKey = PrimaryKey(userId, showId)
}