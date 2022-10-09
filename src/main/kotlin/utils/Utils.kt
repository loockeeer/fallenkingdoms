package fr.loockeeer.fallenkingdoms.utils

import fr.loockeeer.fallenkingdoms.core.Team
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player

fun boolToString(condition: Boolean) = if (condition) "${ChatColor.GREEN}✔" else "${ChatColor.RED}✖"
fun updatePlayer(player: Player) {
        updateTabList(player)
        player.setPlayerListName(player.displayName)
}

fun updateTabList(player: Player) {
    var s = if (Bukkit.getOnlinePlayers().size > 1) "s" else ""
    player.setPlayerListHeaderFooter(
        "${ChatColor.GRAY}${ChatColor.BOLD}FallenKingdoms\n",
        """
            
            ${ChatColor.GRAY}${Bukkit.getOnlinePlayers().size}${ChatColor.GOLD} joueur${s} connecté${s}${ChatColor.RESET}
            ${ChatColor.GOLD}Votre ping: ${ChatColor.GRAY}${player.ping} ms${ChatColor.RESET}
    """.trimIndent()
    )
}