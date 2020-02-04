package telegram

import MyStrings
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.UpdatesListener
import com.pengrad.telegrambot.model.MessageEntity
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.SendMessage
import com.pengrad.telegrambot.response.SendResponse
import config.TelegramConfiguration
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
                val response = if (!entities.isNullOrEmpty() && entities[0].type() == MessageEntity.Type.bot_command) {
                    executeCommand(it)
                } else {
                    MyStrings.Error.BAD_COMMAND
                }
                sendMessage(it.message().chat().id(), response)
            }
            UpdatesListener.CONFIRMED_UPDATES_ALL
        }
    }

    private fun executeCommand(update: Update): String {
        val words = update.message().text().split(" ")
        if (words.isEmpty() || words.size == 1) {
            return MyStrings.Error.MISSING_ARGUMENT
        }
        val chatId = update.message().chat().id()
        val textAfterCommand = words.toMutableList().apply { removeAt(0) }.joinToString(" ")
        return when (words[0]) {
            "/start" -> StartHandler().handleCommand(chatId, textAfterCommand)
            "/follow" -> FollowingHandler().handleCommand(chatId, textAfterCommand)
            else -> MyStrings.Error.BAD_COMMAND
        }
    }

    private fun removeCommandText(message: String) =
        message.removeRange(0, message.indexOf(" "))
            .removePrefix(" ")
}