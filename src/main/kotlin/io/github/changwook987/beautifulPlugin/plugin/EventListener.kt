package io.github.changwook987.beautifulPlugin.plugin

import io.github.changwook987.beautifulPlugin.StaticItems
import io.github.changwook987.beautifulPlugin.util.value
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.entity.Zombie
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class EventListener(private val plugin: BeautifulPlugin) : Listener {

    @EventHandler(ignoreCancelled = true)
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player

        Heavy(plugin, player).runTaskTimer(plugin, 1L, 0L)
    }

    //<editor-fold desc="조합이 개판이야!">
    @EventHandler(ignoreCancelled = false)
    fun onCraft(event: CraftItemEvent) {
        event.inventory.result = StaticItems.randomBox(plugin, event.recipe.result.amount)
    }

    @EventHandler(ignoreCancelled = false)
    fun onUseRandomBox(event: PlayerInteractEvent) {
        if (event.action.isLeftClick) return

        val player = event.player
        val item = event.player.inventory.itemInMainHand
        val value = item.itemMeta?.value(plugin) ?: return

        //check is randomBox
        if (
            value["isRandomBox", PersistentDataType.BYTE] == 1.toByte()
        ) {
            player.inventory.apply {
                val type = Material.values().random()
                val amount = value["amount", PersistentDataType.BYTE]?.toInt() ?: 0

                itemInMainHand.amount = 0
                addItem(ItemStack(type, amount.coerceAtMost(type.maxStackSize)))
            }

            event.isCancelled = true
        }
    }
    //</editor-fold>

    //<editor-fold desc="몸이 너무 무거워...">
    class Heavy(private val plugin: BeautifulPlugin, private val player: Player) : BukkitRunnable() {
        private val queue: Queue<Location> = LinkedList()

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
    }
    //</editor-fold>

    @EventHandler(ignoreCancelled = false)
    fun onDropItem(event: PlayerDropItemEvent) {
        val player = event.player
        val item = event.itemDrop

        val world = item.world

        val zombie = world.spawn(item.location, Zombie::class.java)
        zombie.apply {
            equipment.apply {
                setItemInMainHand(item.itemStack)
                itemInMainHandDropChance = 0f
            }

            attack(player)

            velocity = item.velocity.multiply(2)
        }

        item.remove()
    }
}
