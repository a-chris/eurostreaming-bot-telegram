package telegram

import MyStrings
import model.User
import model.UserShow
import org.koin.core.KoinComponent
import org.koin.core.inject
import service.EpisodeService
import service.ShowService
import service.UserService
import service.UserShowService

abstract class CommandHandler : KoinComponent {
    val userService: UserService by inject()
    val showService: ShowService by inject()
    val episodeService: EpisodeService by inject()
    val userShowService: UserShowService by inject()

    abstract fun handleCommand(chatId: Long, textAfterCommand: String): String
}

class StartHandler : CommandHandler() {
    override fun handleCommand(chatId: Long, textAfterCommand: String): String =
        "Benvenuto!"
}

class FollowingHandler : CommandHandler() {
    override fun handleCommand(chatId: Long, textAfterCommand: String): String {
        if (!showService.showExists(textAfterCommand)) {
            return MyStrings.Error.BAD_SHOW_NAME
        }
        if (!userService.userExists(chatId)) {
            // adds user to the db if it doesn't exist
            userService.addUser(User(chatId))
        }
        val currentUserShow = UserShow(chatId, textAfterCommand)
        if (userShowService.userShowExists(currentUserShow)) {
            return MyStrings.Error.USER_SHOW_ALREADY_EXISTS
        } else {
            userShowService.addUserFollowingShow(currentUserShow)
        }
        return "Following $textAfterCommand"
    }
}

class UnfollowingHandler : CommandHandler() {
    override fun handleCommand(chatId: Long, textAfterCommand: String): String {
        if (!showService.showExists(textAfterCommand)) {
            return MyStrings.Error.BAD_SHOW_NAME
        }
        val userShow = UserShow(chatId, textAfterCommand)
        if (!userService.userExists(chatId) || !userShowService.userShowExists(userShow)) {
            return MyStrings.Error.USER_NOT_FOLLOWING_SHOW
        }
        // if the user is following only one show then remove the user too
        if (userShowService.showsFollowedByUser(chatId).size == 1) {
            userShowService.removeUserFollowingShow(userShow)
            userService.removeUser(chatId)
        }
        userShowService.removeUserFollowingShow(userShow)
        return "Hai smesso di seguire $textAfterCommand"
    }
}

class ListHandler : CommandHandler() {
    override fun handleCommand(chatId: Long, textAfterCommand: String): String {
        if (!userService.userExists(chatId)) {
            return MyStrings.Error.USER_NOT_FOLLOWING_SHOW
        }
        val showsFollowed = userShowService.showsFollowedByUser(chatId)
        return "Stai seguendo ${showsFollowed.size} serie:\n" +
                showsFollowed.joinToString("\n") { "- ${it.name}" }
    }
}
