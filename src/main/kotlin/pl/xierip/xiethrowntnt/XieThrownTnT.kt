package pl.xierip.xiethrowntnt

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.CommandMap
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import pl.xierip.xiethrowntnt.commands.XieThrownTntCommand
import pl.xierip.xiethrowntnt.listeners.PlayerInteractListener
import pl.xierip.xiethrowntnt.util.fixColors
import pl.xierip.xiethrowntnt.util.registerCrafting
import java.util.stream.Collectors

/**
 * Created by xierip on 17.05.18.
 * Web: http://xierip.pl
 */
class XieThrownTnT : JavaPlugin() {
    companion object {
        var instance: XieThrownTnT? = null
            private set
    }

    lateinit var item: ItemStack
    lateinit var itemOne: ItemStack
    var delay: Int = 0
    lateinit var messageDelay: String
    var multiply: Double = 1.0

    override fun onLoad() {
        instance = this
    }

    override fun onEnable() {
        saveDefaultConfig()
        if (config.isInt("Xierip.XieThrownTnT.Item.id")) {
            item = ItemStack(Material.getMaterial(config.getInt("Xierip.XieThrownTnT.Item.id")), if (config.isInt("Xierip.XieThrownTnT.Item.amount")) config.getInt("Xierip.XieThrownTnT.Item.amount").toInt() else 1, if (config.isInt("Xierip.XieThrownTnT.Item.data")) config.getInt("Xierip.XieThrownTnT.Item.data").toShort() else 0)
        } else {
            print("Przedmiot w configu jest zly!")
            this.pluginLoader.disablePlugin(this)
            return
        }
        val itemMeta = item.itemMeta
        if (config.isList("Xierip.XieThrownTnT.Item.lore")) {
            itemMeta.lore = config.getStringList("Xierip.XieThrownTnT.Item.lore").stream().map { s -> s.fixColors() }.collect(Collectors.toList())
        }
        if (config.isString("Xierip.XieThrownTnT.Item.name")) {
            itemMeta.displayName = config.getString("Xierip.XieThrownTnT.Item.name").fixColors()
        }
        if (config.isList("Xierip.XieThrownTnT.Item.itemFlags")) {
            config.getStringList("Xierip.XieThrownTnT.Item.itemFlags").forEach {
                try {
                    itemMeta.addItemFlags(ItemFlag.valueOf(it.toUpperCase()))
                } catch (e: Exception) {
                    print("Blad podczas ladowania ItemFlag: $it, pomijanie...")
                }
            }
        }
        item.itemMeta = itemMeta
        if (config.getConfigurationSection("Xierip.XieThrownTnT.Item.enchantments") != null) {
            config.getConfigurationSection("Xierip.XieThrownTnT.Item.enchantments").getKeys(false).forEach {
                val byName = Enchantment.getByName(it)
                if (byName != null && config.isInt("Xierip.XieThrownTnT.Item.enchantments.$it"))
                    item.addUnsafeEnchantment(byName, config.getInt("Xierip.XieThrownTnT.Item.enchantments.$it"))
            }
        }
        itemOne = item.clone()
        itemOne.amount = 1
        config.registerCrafting("Xierip.XieThrownTnT.Crafting", "xiethrowntnt", item)
        if (config.isInt("Xierip.XieThrownTnT.Multiply") || config.isDouble("Xierip.XieThrownTnT.Multiply")) {
            multiply = config.getDouble("Xierip.XieThrownTnT.Multiply")
        } else {
            print("Niepoprawny format 'Multiply' w configu!")
        }
        delay = config.getInt("Xierip.XieThrownTnT.Delay", 0) * 1000
        messageDelay = config.getString("Xierip.XieThrownTnT.Messages.next", "&cNastepne tnt mozesz rzucic za {TIME}")
        Bukkit.getPluginManager().registerEvents(PlayerInteractListener(), this)
        try {
            val field = Bukkit.getServer().javaClass.getDeclaredField("commandMap")
            field.isAccessible = true
            val commandMap = field.get(Bukkit.getServer()) as CommandMap
            commandMap.register("xiethrowntnt", XieThrownTntCommand())
        } catch (e: Exception) {
            print("Nie udalo sie zarejestowac komendy!")
            e.printStackTrace()
        }
    }

    override fun onDisable() {

    }
}