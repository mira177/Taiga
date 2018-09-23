package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ArgumentParseMap
import net.dv8tion.jda.core.entities.Guild
import java.io.File

class ChangeLog : Argument {
	override fun check(guild: Guild, arg: String): ArgumentParseMap {
		val hasChangelog = File("changelogs/")
			.listFiles()
			.map { file ->
				val fileName = file.name
				fileName.substring(fileName.indexOf("_") + 1, fileName.lastIndexOf("."))
			}
			.contains(arg)
		if (!hasChangelog) {
			return ArgumentParseMap(false, "Changelog: **$arg** does not exist")
		}

		return ArgumentParseMap(true, parsedValue = arg)
	}
}