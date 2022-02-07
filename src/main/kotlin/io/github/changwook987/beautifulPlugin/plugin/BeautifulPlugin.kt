package io.github.changwook987.beautifulPlugin.plugin

import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class BeautifulPlugin : JavaPlugin() {
    override fun onEnable() {
        val listener = EventListener(this)

        logger.info("init")

        listener.register()

        val players = server.onlinePlayers

        for (player in players) {
            if (player is Player) {
                EventListener.Heavy(this, player).runTaskTimer(this, 1L, 0L)
                logger.info("apply ${player.name}")
            }
        }

        logger.info("done!")
    }

    private fun Listener.register() = server.pluginManager.registerEvents(this, this@BeautifulPlugin)
}