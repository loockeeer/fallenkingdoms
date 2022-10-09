package fr.loockeeer.fallenkingdoms.core

import org.bukkit.Bukkit
import org.bukkit.Location
import java.lang.Math.round
import kotlin.math.roundToInt

class Region(val posA: Location, val posB: Location) {
    init {
        if(posA.world != posB.world) throw Error("Invalid region : Cannot create region between two different worlds !")
    }

    fun isInRegion(location: Location): Boolean {
        return location.world!!.name == posA.world!!.name && location.toVector().isInAABB(posA.toVector(), posB.toVector())
    }

    val center: Location
    get() {
        val centerX = (posA.x + posB.x)/2
        val centerZ = (posA.z + posB.z)/2
        return posA.world!!.getHighestBlockAt(centerX.toInt(), centerZ.toInt()).location
    }

    operator fun contains(loc: Location) = isInRegion(loc)
}

