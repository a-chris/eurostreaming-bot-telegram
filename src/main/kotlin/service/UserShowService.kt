package service

import model.User
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import service.database.MyDatabase
import service.database.model.UserShowTable

interface UserShowService {
    fun getUserFollowingShow(showName: String): List<User>
}

class UserShowServiceImpl(private val myDb: MyDatabase) : UserShowService {

    override fun getUserFollowingShow(showName: String): List<User> =
        transaction(myDb.get()) {
            UserShowTable.select { UserShowTable.showId eq showName }
                .map { User(it[UserShowTable.userId]) }
        }

}