package service

import model.Show
import model.User
import model.UserShow
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import service.database.MyDatabase
import service.database.model.UserShowTable

interface UserShowService {
    fun getUsersFollowingShow(showName: String): List<User>
    fun userShowExists(userShow: UserShow): Boolean
    fun addUserFollowingShow(userShow: UserShow)
    fun removeUserFollowingShow(userShow: UserShow)
    fun showsFollowedByUser(userId: Long): List<Show>
}

class UserShowServiceImpl(private val myDb: MyDatabase) : UserShowService {

    override fun getUsersFollowingShow(showName: String): List<User> =
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

    override fun removeUserFollowingShow(userShow: UserShow) {
        transaction(myDb.get()) {
            UserShowTable.deleteWhere {
                (UserShowTable.userId eq userShow.userId) and (UserShowTable.showId eq userShow.showId)
            }
        }
    }

    override fun showsFollowedByUser(userId: Long) =
        transaction(myDb.get()) {
            UserShowTable.select { UserShowTable.userId eq userId }.map { Show(it[UserShowTable.showId]) }
        }
}