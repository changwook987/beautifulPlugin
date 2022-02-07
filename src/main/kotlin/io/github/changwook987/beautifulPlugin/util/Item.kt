package io.github.changwook987.beautifulPlugin.util

import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataHolder
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin

fun PersistentDataHolder.value(plugin: JavaPlugin) = Value(plugin, this)

class Value(private val plugin: JavaPlugin, holder: PersistentDataHolder) {
    private val container = holder.persistentDataContainer

    operator fun <T, Z> set(key: String, type: PersistentDataType<T, Z>, value: Z) {
        if (value == null) return

        container.set(NamespacedKey(plugin, key), type, value)
    }

    operator fun <T, Z> get(key: String, type: PersistentDataType<T, Z>): Z? {
        return container.get(NamespacedKey(plugin, key), type)
    }
}