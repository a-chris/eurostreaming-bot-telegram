# Eurostreaming Bot Telegram

#### A Telegram Bot to follow your preferred shows episodes uploaded on the [Eurostreaming Site](https://eurostreaming.pink)
<p align="center">made with :heart: using Kotlin</p>

### WHY
**I'm not the owner or a contributor of the eurostreaming site, all the data are gathered using a web scraper. I have nothing to do with any illegal content uploaded on the site.**

The project started as a hobby project to try some funny technologies like Kotlin, Exposed and Koin, plus I really like automation, so here we are.

### USAGE
You can find the bot with the name **EuroStreaming_thebug_bot** or by using [this link](https://t.me/EuroStreaming_thebug_bot)
<p align="center">
    <img width="300" height="250" src="readme/find_the_bot.jpg">
</p>

Just run the bot on your Telegram app and send **/follow show_name** to start following a show. You will receive a notification when a new episode of the show is online.

Available commands are:
- **/follow**: Start following a show. Actually you must type the exact show name otherwise you will receive an error.
- **/unfollow**: Stop following a show. Same rules as the /follow command.
- **/list**: Get a list of the shows you are following.

### WHAT TO EXPECT
I have a few improvements and new features in mind, e.g.:
- [x] Better welcome message with command list and a breif explanation
- [x] Better notification with styled text and a link to the show page
- [ ] Smarter search result that suggests you the right show name if available
- [x] Summary of how the bot works under the hood
- [ ] /stop command to remove all the user data

### HOW IT WORKS
#### Technologies
- [Jsoup](https://jsoup.org/): to scrape the data from the site
- [Exposed](https://github.com/JetBrains/Exposed): to manage the DB
- [Jackson](https://github.com/FasterXML/jackson): to manage the config.json file
- [Koin](https://jsoup.org/): to polite inject dependecies
- [Java-telegram-bot](https://github.com/pengrad/java-telegram-bot-api): to works with Telegram Bot APIs

#### The core
As I've already said, all the data are gathered from the eurostreaming site by scraping the information with Jsoup.

[The core of the application is a loop that checks for new episodes every 30 minutes](src/main/kotlin/Application.kt), it just checks the current day and not the previous ones: I tried to keep it as stateless as possible.

After getting the list of the new episodes the bot checks if any user if actually following one of the shows whose episode has been uploaded, these users will get a notification and the episode will be set as "already notitied" in the database.

#### User management
A user if added to the database only when he starts following his first show.\
When a user is added to the database only his **chat id** is stored, no other personal data (nickname, name, etc..) are stored.
Then, a user is removed from the database when he's not following any show.

### WHY THE BOT SPEAKS ITALIAN
The streaming site is Italian and share shows with Italian language, so I think it's obvious that the bot will only have italian users.
