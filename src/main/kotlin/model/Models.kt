package model

data class User(val chatId: Long)

data class Show(val showName: String)

data class Episode(
    val showName: String,
    val url: String,
    val title: String
)