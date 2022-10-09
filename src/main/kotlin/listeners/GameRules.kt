package fr.loockeeer.fallenkingdoms.listeners

import fr.loockeeer.fallenkingdoms.FallenKingdoms
import fr.loockeeer.fallenkingdoms.core.DayModules
import fr.loockeeer.fallenkingdoms.core.Team
import fr.loockeeer.fallenkingdoms.utils.SystemMessage
import fr.loockeeer.fallenkingdoms.utils.sendSystemMessage
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityToggleGlideEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.event.player.PlayerToggleFlightEvent
import java.security.KeyStore

class GameRules : Listener {
    @EventHandler
    fun playerUsePearl(event: PlayerTeleportEvent) {
        if(event.player.isOp) return
        if(event.cause == PlayerTeleportEvent.TeleportCause.ENDER_PEARL ||
            event.cause == PlayerTeleportEvent.TeleportCause.CHORUS_FRUIT) {
            event.isCancelled = true
            if(event.cause == PlayerTeleportEvent.TeleportCause.ENDER_PEARL)
                event.player.sendSystemMessage(SystemMessage.ENDER_PEARLS_DISABLED)
            else event.player.sendSystemMessage(SystemMessage.CHORUS_FRUIT_DISABLED)
        }
    }

    @EventHandler
    fun playerUseElytra(event: EntityToggleGlideEvent) {
        if(event.entityType == EntityType.PLAYER && !event.isGliding) {
            if((event.entity as Player).isOp) return
            event.isCancelled = true
            (event.entityType as Player).sendSystemMessage(SystemMessage.ELYTRA_DISABLED)
        }
    }

    @EventHandler
    fun playerCraft(event: InventoryOpenEvent) {
        if(event.player.world.name != "world") return
        if((event.player as Player).isOp) return
        if(event.inventory.type == InventoryType.CRAFTING && event.player.type == EntityType.PLAYER) {
            val team = Team.teams.find { team -> team.region?.isInRegion(event.player.location) == true}
            if(event.player in (team?.players ?: listOf())) return
            event.isCancelled = true
            (event.player as Player).sendSystemMessage(SystemMessage.CRAFTING_NOT_ALLOWED)
        }
    }
}