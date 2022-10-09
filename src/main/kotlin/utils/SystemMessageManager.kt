package fr.loockeeer.fallenkingdoms.utils

import fr.loockeeer.fallenkingdoms.FallenKingdoms
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap
import kotlin.system.measureTimeMillis
import kotlin.time.Duration

enum class SystemMessageKind {
    CHAT_SYSTEM_MESSAGE {
        override fun sendMessage(message: String, player: Player) {
            player.sendMessage(message)
        }
    },
    ACTION_BAR_SYSTEM_MESSAGE {
        override fun sendMessage(message: String, player: Player) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent(message))
        }
    };

    abstract fun sendMessage(message: String, player: Player)
}

enum class SystemMessage(val message: String, val cooldown: Long = (5e3).toLong(), val kind: SystemMessageKind = SystemMessageKind.CHAT_SYSTEM_MESSAGE) {
    CANNOT_PLACE_BLOCK(";Vous ne pouvez pas construire ici"),
    CANNOT_PLACE_ENTITY(";Vous ne pouvez pas placer d'entité ici"),
    CANNOT_EMPTY_BUCKET(";Vous ne pouvez pas vider votre seau ici"),
    CANNOT_PVP(";Vous ne pouvez pas ${ChatColor.GRAY}PvP; actuellement"),
    CANNOT_BUILD_PORTAL(";Vous ne pouvez pas construire de ${ChatColor.GRAY}portail; ici"),
    DIMENSION_NOT_ENABLED(";Vous ne pouvez pas vous rendre dans ${ChatColor.GRAY}{0}; actuellement"),
    ENTERING_ZONE(";Vous entrez dans la zone {0}{1};", (5e3).toLong(), SystemMessageKind.ACTION_BAR_SYSTEM_MESSAGE),
    LEAVING_ZONE(";Vouus quittez la zone {0}{1};", (5e3).toLong(), SystemMessageKind.ACTION_BAR_SYSTEM_MESSAGE),
    ENDER_PEARLS_DISABLED(";Les perles de l'end sont désactivées"),
    CHORUS_FRUIT_DISABLED(";Les chorus fruits sont désactivés"),
    ELYTRA_DISABLED(";Les elytres sont désactivés"),
    CRAFTING_NOT_ALLOWED(";Le craft n'est pas autorisé en dehors de votre zone"),
    ASSAULT_NOT_ENABLED(";Les assaults ne sont pas encore activés !"),
    CANNOT_BREAK_HERE(";Vous ne pouvez pas casser de blocs ici");
}

object SystemMessageManager {
    val cooldowns = HashMap<UUID, HashMap<SystemMessage, Long>>()
    fun sendMessage(message: SystemMessage, player: Player, vararg arguments: String): Boolean {
        val formatted = message.message.replace(Regex("\\{([0-9])}")) { r -> arguments[r.groupValues[1].toInt()] }
        if(!cooldowns.contains(player.uniqueId)) cooldowns[player.uniqueId] = hashMapOf()
        if(((cooldowns[player.uniqueId]?.get(message) ?: -1L) + message.cooldown) < Date().time) {
            message.kind.sendMessage(formatMessage(formatted), player)
            cooldowns[player.uniqueId]!![message] = Date().time
            return true
        }
        return false
    }
}

fun formatMessage(message: String): String {
    return FallenKingdoms.instance.config.getString("prefix")!! + message.replace(Regex(";"), "${ChatColor.GOLD}")
}

fun Player.sendSystemMessage(message: SystemMessage, vararg arguments: String) = SystemMessageManager.sendMessage(message, this, *arguments)