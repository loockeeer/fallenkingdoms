package fr.loockeeer.fallenkingdoms.listeners

import fr.loockeeer.fallenkingdoms.FallenKingdoms
import fr.loockeeer.fallenkingdoms.core.DayModules
import fr.loockeeer.fallenkingdoms.core.Team
import fr.loockeeer.fallenkingdoms.utils.SystemMessage
import fr.loockeeer.fallenkingdoms.utils.formatMessage
import fr.loockeeer.fallenkingdoms.utils.sendSystemMessage
import org.bukkit.Bukkit
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityPortalEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.player.PlayerPortalEvent
import java.util.*

class DaysListeners : Listener {
    @EventHandler
    fun playerDamage(event: EntityDamageByEntityEvent) {
        if(event.entity.type != EntityType.PLAYER || event.damager.type != EntityType.PLAYER) return
        if((event.damager as Player).isOp) return
        if(DayModules.PVP.isActivated(FallenKingdoms.instance.day)) return
        event.isCancelled = true
        (event.damager as Player).sendSystemMessage(SystemMessage.CANNOT_PVP)
    }

    @EventHandler
    fun portalCross(event: PlayerPortalEvent) {
        if(event.player.isOp) return
        println(event.to?.world)
        println(event.to?.world?.name)
        println(event.to?.world?.name?.contains("nether"))
        if(event.to?.world?.name?.contains("nether") == true && !DayModules.NETHER.isActivated(FallenKingdoms.instance.day)) {
            event.isCancelled = true
            event.player.sendSystemMessage(SystemMessage.DIMENSION_NOT_ENABLED, "le nether")
        }
        if(event.to?.world?.name?.contains("end") == true && !DayModules.END.isActivated(FallenKingdoms.instance.day)) {
            event.isCancelled = true
            event.player.sendSystemMessage(SystemMessage.DIMENSION_NOT_ENABLED, "l'end")
        }
    }
}