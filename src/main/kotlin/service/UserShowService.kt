package service

import model.User
import model.UserShow
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import service.database.MyDatabase
import service.database.model.UserShowTable

interface UserShowService {
    fun getUserFollowingShow(showName: String): List<User>
    fun userShowExists(userShow: UserShow): Boolean
    fun addUserFollowingShow(userShow: UserShow)
}

class UserShowServiceImpl(private val myDb: MyDatabase) : UserShowService {

    override fun getUserFollowingShow(showName: String): List<User> =
        transaction(myDb.get()) {
            UserShowTable.select { UserShowTable.showId eq showName }
                .map { User(it[UserShowTable.userId]) }
        }

    override fun userShowExists(userShow: UserShow): Boolean =
        transaction(myDb.get()) {
            UserShowTable
                .select { UserShowTable.showId eq userShow.showId }
                .andWhere { UserShowTable.userId eq userShow.userId }
                .count() > 0
        }

    override fun addUserFollowingShow(userShow: UserShow) {
        transaction(myDb.get()) {
            UserShowTable.insert {
                it[userId] = userShow.userId
                it[showId] = userShow.showId
            }
        }
    }
}