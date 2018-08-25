package me.chill.commands.categories

import me.chill.commands.container.CommandContainer
import me.chill.commands.container.command
import me.chill.commands.container.commands
import me.chill.database.viewPermissions
import me.chill.utility.green
import me.chill.utility.jda.embed
import me.chill.utility.jda.send
import net.dv8tion.jda.core.entities.Guild

fun permissionCommands() = commands {
	command("setpermission") {
		expects(String, String)
		execute {
			val arguments = getArguments()
			val channel = getChannel()
			val guild = getGuild()

			val commandName = arguments[0]
			val roleId = arguments[1]


		}
	}

	command("viewpermissions") {
		execute {
			val guild = getGuild()
			val channel = getChannel()	

			val permissionMap = generatePermissionsMap(guild)
			val permissionsList = generatePermissionsList(guild, permissionMap.toSortedMap())
			channel.send(listPermissionsEmbed(guild, permissionsList))
		}
	}
}

private fun generatePermissionsMap(guild: Guild): MutableMap<String, String> {
	val highestRole = guild.roles[0].id

	val assignedPermissions = viewPermissions(guild.id)
	val commandNames = CommandContainer.getCommandNames()

	val permissionMap = mutableMapOf<String, String>()
	permissionMap.putAll(assignedPermissions)
	commandNames.forEach { command ->
		if (!assignedPermissions.keys.contains(command)) permissionMap[command] = highestRole
	}

	return permissionMap
}

private fun generatePermissionsList(guild: Guild, permissionMap: Map<String, String>) =
	permissionMap
		.map { "**${it.key}** :: ${guild.getRoleById(it.value).name}" }
		.joinToString("\n") { "- $it" }


private fun listPermissionsEmbed(guild: Guild, message: String) =
	embed {
		title = "${guild.name} Permissions"
		color = green
		thumbnail = guild.iconUrl
		description = message
	}