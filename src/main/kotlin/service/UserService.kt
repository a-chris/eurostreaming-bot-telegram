package service

import model.User
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import service.database.MyDatabase
import service.database.model.UserTable

interface UserService {
    fun addUser(user: User)
    fun userExists(userId: Long): Boolean
    fun removeUser(userId: Long)
}

class UserServiceImpl(private val myDb: MyDatabase) : UserService {

    override fun addUser(user: User) {
        transaction(myDb.get()) {
            UserTable.insert {
                it[id] = user.id
            }
        }
    }

    override fun userExists(userId: Long) =
        transaction(myDb.get()) {
            UserTable.select { UserTable.id eq userId }.count() > 0
        }

    override fun removeUser(userId: Long) {
        transaction(myDb.get()) {
            UserTable.deleteWhere { UserTable.id eq userId }
        }
    }
}