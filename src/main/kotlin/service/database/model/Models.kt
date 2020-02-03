package service.database.model

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object UserTable : Table("user") {
    val id: Column<Long> = long("id_telegram")
    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

object ShowTable : Table("show") {
    val name: Column<String> = varchar("name", 100)
    override val primaryKey: PrimaryKey = PrimaryKey(name)
}

object EpisodeTable: Table("episode") {
    val episodeName: Column<String> = varchar("name", 50)
    val url: Column<String?> = varchar("url", 100).nullable()
    val showName: Column<String> = varchar("show_pk", 100)

    override val primaryKey: PrimaryKey = PrimaryKey(episodeName)
}

object UserShowTable: Table("user_show") {
    val userId: Column<Long> = long("user_pk")
    val showId: Column<String> = varchar("show_pk", 100)
    override val primaryKey = PrimaryKey(userId, showId)
}

object UserEpisodeNotifiedTable: Table("user_episode_notified") {
    val userId: Column<Long> = long("user_pk")
    val episodeId: Column<String> = varchar("episode_pk", 100)
    override val primaryKey = PrimaryKey(userId, episodeId)
}