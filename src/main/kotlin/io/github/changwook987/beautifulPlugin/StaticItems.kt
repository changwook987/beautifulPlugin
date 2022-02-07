package io.github.changwook987.beautifulPlugin

import io.github.changwook987.beautifulPlugin.util.value
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

object StaticItems {
    fun randomBox(plugin: JavaPlugin, amount: Int): ItemStack {
        var color: NamedTextColor
        val material: Material

        while (true) {
            color = NamedTextColor.nearestTo(TextColor.color((0x0..0xffffff).random()))
            material = Material.getMaterial("${color}_shulker_box".uppercase(Locale.getDefault())) ?: continue
            break
        }

        return ItemStack(material).apply {
            itemMeta = itemMeta.apply {
                displayName(text().content("재미있는 랜덤상자").color(color).build())
                lore(
                    listOf(
                        text().content("랜덤박스이다").color(NamedTextColor.WHITE).build(),
                        text().content("우클릭시 사용됨").color(NamedTextColor.WHITE).build()
                    )
                )

                val value = value(plugin)

                value["isRandomBox", PersistentDataType.BYTE] = 1
                value["amount", PersistentDataType.BYTE] = amount.toByte()
            }
        }
    }
}