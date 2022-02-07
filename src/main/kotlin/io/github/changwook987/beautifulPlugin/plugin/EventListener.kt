package io.github.changwook987.beautifulPlugin.plugin

import io.github.changwook987.beautifulPlugin.StaticItems
import io.github.changwook987.beautifulPlugin.util.value
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class EventListener(private val plugin: BeautifulPlugin) : Listener {
    //<editor-fold desc="조합이 개판이야!">
    @EventHandler(ignoreCancelled = false)
    fun onCraft(event: CraftItemEvent) {
        event.inventory.result = StaticItems.randomBox(plugin, event.recipe.result.amount)
    }

    @EventHandler(ignoreCancelled = false)
    fun onUseRandomBox(event: PlayerInteractEvent) = with(event) {
        if (action.isLeftClick) return@with

        val item = player.inventory.itemInMainHand
        val value = item.itemMeta.value(plugin)

        //check is randomBox
        if (
            value["isRandomBox", PersistentDataType.BYTE] == 0.toByte()
        ) {
            isCancelled = true
            player.inventory.apply {
                val type = Material.values().random()
                val amount = value["amount", PersistentDataType.BYTE]?.toInt() ?: 0

                itemInMainHand.amount = 0
                addItem(ItemStack(type, amount.coerceAtMost(type.maxStackSize)))
            }
        }
    }
    //</editor-fold>

    //<editor-fold desc="몸이 너무 무거워...">
    @EventHandler(ignoreCancelled = false)
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        val queue: Queue<Location> = LinkedList()

        object : BukkitRunnable() {
            override fun run() {
                if (!player.isOnline) cancel()
                else {
                    val location = player.location.block.location
                        .run { if (block.isEmpty) subtract(.0, 1.0, .0) else this }

                    if (location !in queue && !location.block.type.isEmpty) {
                        queue += location

                        object : BukkitRunnable() {
                            override fun run() {
                                queue.poll().block.type = Material.AIR
                            }
                        }.runTaskLater(plugin, 20L)
                    }
                }
            }
        }.runTaskTimer(plugin, 1L, 0L)
    }
    //</editor-fold>
}