package telegram

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.UpdatesListener
import com.pengrad.telegrambot.model.MessageEntity
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.SendMessage
import config.TelegramConfiguration
import service.ShowServiceImpl
import service.database.MyDatabase

class MyTelegramBot(telegramConfiguration: TelegramConfiguration, private val myDb: MyDatabase) {
    private val bot = TelegramBot(telegramConfiguration.token)

    fun start() {
        bot.setUpdatesListener { updates ->
            updates?.forEach {
                val entities = it.message().entities()
                if (!entities.isNullOrEmpty() && entities[0].type() == MessageEntity.Type.bot_command) {
                    commandHandler(it)
                } else {
                    val text = it.message().text()
                    val request = SendMessage(it.message().chat().id(), text)
                    bot.execute(request)
                }
            }
            UpdatesListener.CONFIRMED_UPDATES_ALL
        }
    }

    private fun commandHandler(update: Update) {
        val text = update.message().text()
        val showName = removeCommandText(text)
        val showExists = ShowServiceImpl(myDb).showExists(showName)
        val responseText = if (showExists) {
            "Following $showName"
        } else {
            "Show $showName doesn't exist yet"
        }
        bot.execute(SendMessage(update.message().chat().id(), responseText))
    }

    private fun removeCommandText(message: String) =
        message.removeRange(0, message.indexOf(" "))
            .removePrefix(" ")
}