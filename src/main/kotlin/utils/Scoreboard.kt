package fr.loockeeer.fallenkingdoms.utils

import fr.loockeeer.fallenkingdoms.FallenKingdoms
import fr.loockeeer.fallenkingdoms.core.DayModules
import fr.loockeeer.fallenkingdoms.core.Team
import fr.mrmicky.fastboard.FastBoard
import org.apache.commons.lang.time.DurationFormatUtils
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Statistic
import org.bukkit.entity.Player
import java.util.*
import kotlin.collections.HashMap

object Scoreboard {
    private val board = HashMap<UUID, FastBoard>()


    private fun generateBoard(player: Player) = FastBoard(player).also { board[player.uniqueId] = it }

    fun removeBoard(player: Player) = board.remove(player.uniqueId).also { it?.delete() }

    fun updateBoard(player: Player) = updateBoard(getBoard(player))

    private fun updateBoard(board: FastBoard): FastBoard {
        val team = Team.getPlayerTeam(board.player) ?: return board
        val midnight = DurationFormatUtils.formatDuration(((24000 - Bukkit.getWorld("world")!!.time)*1000)/20, "mm:ss")
        board.updateTitle("${ChatColor.BOLD}${ChatColor.GRAY}FallenKingdoms")
        board.updateLines(
            "",
            "${ChatColor.GOLD}Equipe: ${team.color}${team.name}",
            "${ChatColor.GOLD}Jour:${ChatColor.GRAY} ${FallenKingdoms.instance.day}",
            "${ChatColor.GOLD}Temps:${ChatColor.GRAY} ${if (FallenKingdoms.instance.running) midnight else "--:--"}",
            "${ChatColor.DARK_GRAY}--------------------",
            "${ChatColor.GOLD}PvP:${ChatColor.RESET} ${boolToString(DayModules.PVP.isActivated(FallenKingdoms.instance.day))}",
            "${ChatColor.GOLD}Nether:${ChatColor.RESET} ${boolToString(DayModules.NETHER.isActivated(FallenKingdoms.instance.day))}",
            "${ChatColor.GOLD}Assauts:${ChatColor.RESET} ${boolToString(DayModules.ASSAULT.isActivated(FallenKingdoms.instance.day))}",
            "${ChatColor.GOLD}End:${ChatColor.RESET} ${boolToString(DayModules.END.isActivated(FallenKingdoms.instance.day))}",
            "${ChatColor.DARK_GRAY}--------------------",
            "${ChatColor.GOLD}Kills:${ChatColor.GRAY} ${board.player.getStatistic(Statistic.PLAYER_KILLS)}",
            "${ChatColor.GOLD}Morts:${ChatColor.GRAY} ${board.player.getStatistic(Statistic.DEATHS)}",
            "${ChatColor.DARK_GRAY}--------------------"
        )
        return board
    }

    private fun getBoard(player: Player) = board[player.uniqueId] ?: generateBoard(player)

}