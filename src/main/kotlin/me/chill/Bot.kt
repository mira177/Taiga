package me.chill

import me.chill.database.setupDatabase
import me.chill.embed.EmbedManager
import me.chill.embed.interactive.InteractiveEmbedManager
import me.chill.events.*
import me.chill.framework.CommandContainer
import me.chill.json.configuration.Credentials
import me.chill.json.configuration.isHerokuRunning
import me.chill.json.configuration.loadConfigurations
import me.chill.json.help.CommandInfo
import me.chill.json.help.loadHelp
import me.chill.raid.RaidManager
import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDABuilder
import net.dv8tion.jda.core.OnlineStatus
import net.dv8tion.jda.core.entities.Game

var credentials: Credentials? = null
var commandInfo: List<CommandInfo>? = null
val raidManger = RaidManager()
val interactiveEmbedManager = InteractiveEmbedManager()
val embedManager = EmbedManager()
fun main(args: Array<String>) {
  setup()

  val jda = JDABuilder(AccountType.BOT)
    .setStatus(OnlineStatus.ONLINE)
    .setToken(credentials!!.token)
    .setGame(Game.playing("${credentials!!.defaultPrefix}help"))
    .addEventListener(
      OnJoinEvent(),
      OnLeaveEvent(),
      OnChannelCreationEvent(),
      InputEvent(),
      InteractiveEmbedEvent(),
      OnUserActivityEvent()
    )
    .build()
  credentials!!.botOwnerId = jda.asBot().applicationInfo.complete().owner.id
}

private fun setup() {
  credentials = if (isHerokuRunning()) {
    Credentials(null)
  } else {
    val configurations = loadConfigurations() ?: return
    Credentials(configurations)
  }

  setupDatabase(credentials!!.database!!)
  CommandContainer.loadContainer()
  commandInfo = loadHelp()

  println("Bot has loaded")
}