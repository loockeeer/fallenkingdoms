package fr.loockeeer.fallenkingdoms.commands

import fr.loockeeer.fallenkingdoms.utils.formatMessage
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class Invsee : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage(formatMessage("Vous devez être un joueur pour faire cela !"))
            return true
        }

        if (args.isEmpty()) {
            sender.sendMessage(formatMessage("Un argument est requis : ${ChatColor.GRAY}joueur"))
            return true
        }

        val target = Bukkit.getPlayer(args[0])
        if(target == null) {
            sender.sendMessage(formatMessage("L'argument ${ChatColor.GRAY}joueur${ChatColor.RED} est incorrect"))
            return true
        }

        if (target.uniqueId == sender.uniqueId) {
            sender.sendMessage(formatMessage("Vous ne pouvez pas faire cette commande sur vous même !"))
            return true
        }

        sender.openInventory(target.inventory)
        sender.sendMessage(formatMessage("Ouverture de l'inventaire de ${target.displayName}"))
        return true
    }
}