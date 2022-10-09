package fr.loockeeer.fallenkingdoms.core

import fr.loockeeer.fallenkingdoms.FallenKingdoms
import fr.loockeeer.fallenkingdoms.utils.formatMessage
import org.bukkit.Bukkit
import org.bukkit.ChatColor

enum class DayModules(val id: String, val message: String) {
    PVP("pvp", ";Le ${ChatColor.GRAY}PvP; est maintenant activé !"),
    ASSAULT("assaults",";Les ${ChatColor.GRAY}assaults; sont maintenant activés !"),
    NETHER("nether", ";Le ${ChatColor.GRAY}neether; est maintenant activé !"),
    END("end", ";L'${ChatColor.GRAY}end; est maintenant activé !");

    open fun onActivated() {}
    open fun isActivated(day: Int): Boolean = day >= activateDay()
    open fun activateDay(): Int = FallenKingdoms.instance.config.getInt("days.$id", -1)

    companion object {
        fun startDay(day: Int) {
            Bukkit.broadcastMessage(formatMessage(";Passage au jour ${ChatColor.GRAY}${day}"))
            for(module in values()) {
                if(module.activateDay() == day) {
                    Bukkit.broadcastMessage(formatMessage(module.message))
                }
            }
        }
    }
}