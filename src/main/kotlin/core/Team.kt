package fr.loockeeer.fallenkingdoms.core

import fr.loockeeer.fallenkingdoms.FallenKingdoms
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.entity.Player


class Team(val id: String, val name: String, val region: Region?) {
    companion object {
        val teams: List<Team>
            get() {
                val t = FallenKingdoms.instance.config.getMapList("teams")
                return t.map { Team(it["id"] as String, it["name"] as String, if (it["playable"] as Boolean) Region(
                    Location(Bukkit.getWorld(it["baseWorld"] as String)!!, it["baseX1"] as Double,
                        Bukkit.getWorld(it["baseWorld"] as String)!!.minHeight.toDouble(), it["baseZ1"] as Double),
                    Location(Bukkit.getWorld(it["baseWorld"] as String)!!, it["baseX2"] as Double,
                        Bukkit.getWorld(it["baseWorld"] as String)!!.maxHeight.toDouble(), it["baseZ2"] as Double)
                )  else null) }
            }
        fun getPlayerTeam(player: Player): Team? {
            return teams.find { t -> player in t.players}
        }
    }

    val players: List<Player>
    get() {
        return Bukkit.getScoreboardManager()?.mainScoreboard?.getTeam(id)?.entries?.mapNotNull {
            Bukkit.getOnlinePlayers().find { p -> p.name.toString() == it }
        } ?: listOf()
    }

    val color: ChatColor?
    get() {
        return Bukkit.getScoreboardManager()?.mainScoreboard?.getTeam(id)?.color
    }
}

val Player.gameTeam: Team?
    get() = Team.getPlayerTeam(this)