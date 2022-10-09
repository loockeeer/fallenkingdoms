package fr.loockeeer.fallenkingdoms.listeners

import fr.loockeeer.fallenkingdoms.FallenKingdoms
import fr.loockeeer.fallenkingdoms.core.gameTeam
import fr.loockeeer.fallenkingdoms.utils.Scoreboard
import fr.loockeeer.fallenkingdoms.utils.updatePlayer
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class ChatEvents : Listener {
    @EventHandler
    fun playerChat(event: AsyncPlayerChatEvent) {
        val team = event.player.gameTeam ?: return
        event.format = "${team.color}%s${ChatColor.GRAY}${ChatColor.BOLD}:${ChatColor.RESET} %s"
    }

    @EventHandler
    fun playerJoin(event: PlayerJoinEvent) {
        event.joinMessage = null
        event.player.server.broadcastMessage("[${ChatColor.GREEN}+${ChatColor.RESET}] ${event.player.displayName}")

        Scoreboard.updateBoard(event.player)

        updatePlayer(event.player)
    }

    @EventHandler
    fun playerQuit(event: PlayerQuitEvent) {
        event.quitMessage = null
        event.player.server.broadcastMessage("[${ChatColor.RED}-${ChatColor.RESET}] ${event.player.displayName}")
        Scoreboard.removeBoard(event.player)
    }
}