package telegram

import MyStrings
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.UpdatesListener
import com.pengrad.telegrambot.model.MessageEntity
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.model.request.ParseMode
import com.pengrad.telegrambot.request.GetChatMembersCount
import com.pengrad.telegrambot.request.GetMe
import com.pengrad.telegrambot.request.SendMessage
import com.pengrad.telegrambot.response.SendResponse
import config.TelegramConfiguration

class MyTelegramBot(telegramConfiguration: TelegramConfiguration) {
    private val bot = TelegramBot(telegramConfiguration.token)

    fun sendMessage(chatId: Long, message: String): SendResponse =
        bot.execute(
            SendMessage(chatId, message)
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
        )

    fun start() {
        val id = bot.execute(GetMe()).user().id()
        val usersCount = bot.execute(GetChatMembersCount(id)).count()
        println("Users count: $usersCount")

        bot.setUpdatesListener { updates ->
            updates?.forEach { update ->
                val entities = update?.message()?.entities()
                val response = if (!entities.isNullOrEmpty() && entities[0].type() == MessageEntity.Type.bot_command) {
                    executeCommand(update)
                } else {
                    MyStrings.Error.BAD_COMMAND
                }
                update?.message()?.chat()?.id()?.let { chatId ->
                    sendMessage(chatId, response)
                }
            }
            UpdatesListener.CONFIRMED_UPDATES_ALL
        }
    }

    private fun executeCommand(update: Update): String {
        val words = update.message().text().split(" ")
        if (words.isEmpty()) {
            return MyStrings.Error.MISSING_ARGUMENT
        }
        val chatId = update.message().chat().id()
        val textAfterCommand = words.subList(1, words.size).joinToString(" ")
        return when (words[0]) {
            "/help" -> HelpHandler().handleCommand(chatId, textAfterCommand)
            "/follow" -> FollowingHandler().handleCommand(chatId, textAfterCommand)
            "/unfollow" -> UnfollowingHandler().handleCommand(chatId, textAfterCommand)
            "/list" -> ListHandler().handleCommand(chatId, textAfterCommand)
            else -> MyStrings.Error.BAD_COMMAND
        }
    }
}