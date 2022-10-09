package fr.loockeeer.fallenkingdoms

import fr.loockeeer.fallenkingdoms.commands.Invsee
import fr.loockeeer.fallenkingdoms.commands.NextCommand
import fr.loockeeer.fallenkingdoms.commands.StartCommand
import fr.loockeeer.fallenkingdoms.core.DayModules
import fr.loockeeer.fallenkingdoms.core.Team
import fr.loockeeer.fallenkingdoms.listeners.*
import fr.loockeeer.fallenkingdoms.utils.Scoreboard
import fr.loockeeer.fallenkingdoms.utils.updatePlayer
import fr.loockeeer.fallenkingdoms.utils.updateTabList
import org.bukkit.*
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitTask


class FallenKingdoms : JavaPlugin() {
	var running = false
	var day = 0
	var dayTask: BukkitTask? = null

	companion object {
		lateinit var instance: FallenKingdoms
			private set

		val prefix = "${ChatColor.GRAY}${ChatColor.BOLD}FallenKingdoms |${ChatColor.RESET}"
	}

	override fun onEnable() {
		instance = this
		saveDefaultConfig()

		server.pluginManager.registerEvents(ChatEvents(), this)
		server.pluginManager.registerEvents(DaysListeners(), this)
		server.pluginManager.registerEvents(GameRules(), this)
		server.pluginManager.registerEvents(PlayerMovement(), this)
		server.pluginManager.registerEvents(WorldProtector(), this)

		getCommand("fkstart")?.apply {
			setExecutor(StartCommand())
		}

		getCommand("fkinvsee")?.apply {
			setExecutor(Invsee())
		}

		getCommand("fknext")?.apply {
			setExecutor(NextCommand())
		}

		Bukkit.getOnlinePlayers().forEach {
			Scoreboard.updateBoard(it)
			updatePlayer(it)
		}

		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, {
			Bukkit.getOnlinePlayers().forEach {
				Scoreboard.updateBoard(it)
				updateTabList(it)
			}
		}, 0L, 20L)

		logger.info("${description.name} version ${description.version} enabled!")
	}

	override fun onDisable() {
		logger.info("${description.name} version ${description.version} disabled!")
	}

	fun cycleDays() {
		dayTask?.cancel()
		DayModules.startDay(++day)
		config.set("day", day)
		saveConfig()
		dayTask = Bukkit.getScheduler().runTaskLater(this, Runnable {
			cycleDays()
		}, 24000)
	}

	fun start() {
		server.worlds.forEach {
			it.apply {
				difficulty = Difficulty.HARD
				setGameRule(GameRule.KEEP_INVENTORY, false)
				setGameRule(GameRule.NATURAL_REGENERATION, true)
				setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true)
				setGameRule(GameRule.COMMAND_BLOCK_OUTPUT, false)
				setGameRule(GameRule.DO_MOB_SPAWNING, true)
				setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, true)
				setGameRule(GameRule.RANDOM_TICK_SPEED, 3)
				setGameRule(GameRule.MOB_GRIEFING, true)
				setGameRule(GameRule.DO_FIRE_TICK, false)
				setGameRule(GameRule.SHOW_DEATH_MESSAGES, true)
				setGameRule(GameRule.PLAYERS_SLEEPING_PERCENTAGE, 1 / 3)
				setGameRule(GameRule.DO_INSOMNIA, true)
				setGameRule(GameRule.FALL_DAMAGE, true)
				setGameRule(GameRule.FREEZE_DAMAGE, true)
				setGameRule(GameRule.LOG_ADMIN_COMMANDS, false)
				it.time = 0
			}
		}
		running = true

		cycleDays()
		val overworld = Bukkit.getWorld("world")!!
		Bukkit.getOnlinePlayers().forEach { player ->
			val team = Team.getPlayerTeam(player) ?: return@forEach
			if(team.region == null) return@forEach
			player.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, 15*20, 3))
			player.teleport(overworld.spawnLocation)
			player.inventory.clear()
			player.activePotionEffects.clear()
			player.health = 20.0
			player.exp = 0.0F
			player.level = 0

			player.setStatistic(Statistic.PLAYER_KILLS, 0)
			player.setStatistic(Statistic.DEATHS, 0)

			player.server.advancementIterator().forEach { advancement ->
				run {
					val progress = player.getAdvancementProgress(advancement)
					progress.awardedCriteria.forEach {
						progress.revokeCriteria(it)
					}
				}
			}

			player.isOp = false
			player.gameMode = GameMode.SURVIVAL
			player.sendTitle(
				"${ChatColor.GRAY}${ChatColor.BOLD}Fallen Kingdoms",
				"${ChatColor.GOLD}Bonne partie !",
				10,
				70,
				20
			)
			player.playSound(player.location, Sound.EVENT_RAID_HORN, 70f, 1f)
		}
	}
}
