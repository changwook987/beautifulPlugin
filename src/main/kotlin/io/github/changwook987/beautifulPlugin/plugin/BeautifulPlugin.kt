package io.github.changwook987.beautifulPlugin.plugin

import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class BeautifulPlugin : JavaPlugin() {
    override fun onEnable() {
        EventListener(this).register()
    }

    private fun Listener.register() = server.pluginManager.registerEvents(this, this@BeautifulPlugin)
}