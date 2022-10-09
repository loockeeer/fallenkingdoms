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
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityPlaceEvent
import org.bukkit.event.entity.EntityPortalEvent
import org.bukkit.event.player.PlayerBucketEmptyEvent
import org.bukkit.event.player.PlayerBucketFillEvent
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.world.PortalCreateEvent

class WorldProtector : Listener {
    @EventHandler
    fun blockPlaced(event: BlockPlaceEvent) {
        if(event.blockPlaced.world.name != "world") return
        if(event.player.isOp) return
        val team = Team.teams.find { team -> team.region?.isInRegion(event.blockPlaced.location) == true}
        if(event.player in (team?.players ?: listOf())) return
        if (event.itemInHand.type.name !in FallenKingdoms.instance.config.getStringList("ok_blocks")) {
            event.isCancelled = true
            event.player.sendSystemMessage(SystemMessage.CANNOT_PLACE_BLOCK)
        }
    }

    @EventHandler
    fun blockBreak(event: BlockBreakEvent) {
        if(event.block.world.name != "world") return
        if(event.player.isOp) return
        val team = Team.teams.find { team -> team.region?.isInRegion(event.block.location) == true} ?: return
        if(event.player in team.players) return
        event.isCancelled = true
        event.player.sendSystemMessage(SystemMessage.CANNOT_BREAK_HERE)
    }

    @EventHandler
    fun entityPlaced(event: EntityPlaceEvent) {
        if(event.entity.world.name != "world") return
        if(event.player?.isOp == true) return
        val team = Team.teams.find { team -> team.region?.isInRegion(event.entity.location) == true}
        if(event.player in (team?.players ?: listOf())) return
        if(event.entity.type.name !in FallenKingdoms.instance.config.getStringList("ok_entities")) {
            event.isCancelled = true
            event.player?.sendSystemMessage(SystemMessage.CANNOT_PLACE_ENTITY)
        }
    }

    @EventHandler
    fun bucketEmpty(event: PlayerBucketEmptyEvent) {
        if(event.block.world.name != "world") return
        if(event.player.isOp) return
        val team = Team.teams.find { team -> team.region?.isInRegion(event.block.location) == true}
        if(event.player in (team?.players ?: listOf())) return
        event.player.sendSystemMessage(SystemMessage.CANNOT_EMPTY_BUCKET)
    }

    @EventHandler
    fun portalCreate(event: PortalCreateEvent) {
        if(event.entity?.type != EntityType.PLAYER) return
        if((event.entity as Player).isOp) return
        if(event.reason == PortalCreateEvent.CreateReason.NETHER_PAIR) return
        if(event.reason == PortalCreateEvent.CreateReason.END_PLATFORM) return
        event.isCancelled = true
        (event.entity as Player).sendSystemMessage(SystemMessage.CANNOT_BUILD_PORTAL)
    }
}