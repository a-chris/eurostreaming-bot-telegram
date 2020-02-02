package telegram

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.UpdatesListener
import com.pengrad.telegrambot.model.MessageEntity
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.SendMessage
import com.pengrad.telegrambot.response.SendResponse
import config.TelegramConfiguration
import model.User
import service.ShowService
import service.UserService

class MyTelegramBot(
    telegramConfiguration: TelegramConfiguration,
    private val showService: ShowService,
    private val userService: UserService
) {
    private val bot = TelegramBot(telegramConfiguration.token)

    fun sendMessage(chatId: Long, message: String): SendResponse =
        bot.execute(SendMessage(chatId, message))

    fun start() {
        bot.setUpdatesListener { updates ->
            updates?.forEach {
                val entities = it.message().entities()
                if (!entities.isNullOrEmpty() && entities[0].type() == MessageEntity.Type.bot_command) {
                    commandHandler(it)
                } else {
                    val text = it.message().text()
                    sendMessage(it.message().chat().id(), text)
                }
            }
            UpdatesListener.CONFIRMED_UPDATES_ALL
        }
    }

    private fun commandHandler(update: Update) {
        val text = update.message().text()
        val showName = removeCommandText(text)
        val showExists = showService.showExists(showName)
        val userId = update.message().chat().id()
        if (!userService.userExists(userId)) {
            // adds user to the db if it doesn't exist
            userService.addUser(User(update.message().chat().id()))
        }
        val responseText = if (showExists) {
            "Following $showName"
        } else {
            "Show $showName doesn't exist yet"
        }
        sendMessage(update.message().chat().id(), responseText)
    }

    private fun removeCommandText(message: String) =
        message.removeRange(0, message.indexOf(" "))
            .removePrefix(" ")
}