package model

data class User(val id: Long)

data class Show(val name: String)

data class Episode(
    val showName: String,
    val url: String,
    val episodeName: String
)

data class UserShow(
    val userId: Long,
    val showId: String
)