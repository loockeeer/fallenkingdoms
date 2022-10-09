package fr.loockeeer.fallenkingdoms.listeners

import fr.loockeeer.fallenkingdoms.core.Team
import fr.loockeeer.fallenkingdoms.utils.SystemMessage
import fr.loockeeer.fallenkingdoms.utils.sendSystemMessage
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

class PlayerMovement : Listener {
    @EventHandler
    fun playerMove(event: PlayerMoveEvent) {
        for(team in Team.teams) {
            if(team.region == null) continue
            if(event.from in team.region && event.to != null && event.to!! !in team.region) {
                event.player.sendSystemMessage(SystemMessage.LEAVING_ZONE, "${team.color}", team.name)
            } else if(event.to != null && event.to!! in team.region && event.from !in team.region) {
                event.player.sendSystemMessage(SystemMessage.ENTERING_ZONE, "${team.color}", team.name)
            }
        }
    }
}