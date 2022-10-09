package fr.loockeeer.fallenkingdoms.commands

import fr.loockeeer.fallenkingdoms.FallenKingdoms
import fr.loockeeer.fallenkingdoms.utils.formatMessage
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class NextCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        FallenKingdoms.instance.cycleDays()
        sender.sendMessage(formatMessage("Le jour a été avancé"))
        return true
    }
}