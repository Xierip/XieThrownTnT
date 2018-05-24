package pl.xierip.xiethrowntnt.util

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.material.MaterialData
import pl.xierip.xiethrowntnt.XieThrownTnT
import java.util.*

/**
 * Created by xierip on 19.05.18.
 * Web: http://xierip.pl
 */

private val numbers = "0123456789"

fun String.fixColors(): String {
    return ChatColor.translateAlternateColorCodes('&', this);
}

fun CommandSender.sendColoredMessage(message: String) {
    this.sendMessage(message.fixColors())
}

fun Long.formatTimeDiff(): String {
    if (this <= 0) {
        return "1 sekunde"
    }
    var seconds = this / 1000L
    var minutes = seconds / 60L
    var hours = minutes / 60L
    val days = hours / 24L
    seconds -= minutes * 60L
    minutes -= hours * 60L
    hours -= days * 24L
    var time = ""
    if (days != 0L) {
        time = time + days + (if (days == 1L) " dzien " else " dni ")
    }
    if (hours != 0L) {
        time = time + hours + (when (hours) {
            1L -> " godzine "
            2L, 3L, 4L, 22L, 23L, 24L -> " godziny "
            else -> " godzin "
        })
    }
    if (minutes != 0L) {
        time = time + minutes + (when (minutes) {
            1L -> " minute "
            2L, 3L, 4L, 22L, 23L, 24L, 32L, 33L, 34L, 42L, 43L, 44L, 52L, 53L, 54L -> " minuty "
            else -> " minut "
        })
    }
    if (seconds != 0L) {
        time = time + seconds + (when (seconds) {
            1L -> " sekunde "
            2L, 3L, 4L, 22L, 23L, 24L, 32L, 33L, 34L, 42L, 43L, 44L, 52L, 53L, 54L -> " sekundy "
            else -> " sekund "
        })
    }
    if (time == "") {
        return "1 sekunde"
    }
    return time.trim()
}

fun Long.formatTimeTo(): String {
    return (this - System.currentTimeMillis()).formatTimeDiff()
}

fun FileConfiguration.registerCrafting(path: String, name: String, item: ItemStack): Boolean {
    val shape = ShapedRecipe(NamespacedKey(XieThrownTnT.instance, name), item)
    val shapeOfShape = StringBuilder()
    val ingredients = HashMap<Char, Material>()
    val ingredientsData = HashMap<Char, MaterialData>()
    var i = 0
    val list = this.getStringList(path)
    if (list.size != 3) {
        return false
    }
    for (s in list) {
        for (st in s.split("|")) {
            i++
            if (st == " ") {
                shapeOfShape.append(" |")
            } else {
                shapeOfShape.append(numbers[i]).append("|")
                val itemS = ItemStack(if (st.contains(":")) st.split(":")[0].toInt() else st.toInt(), 1, if (st.contains(":")) st.split(":")[1].toShort() else 0)
                ingredients[numbers[i]] = itemS.type
                ingredientsData[numbers[i]] = itemS.data
            }
        }
    }

    val stringShape = shapeOfShape.toString().split("|")
    shape.shape(stringShape[0] + stringShape[1] + stringShape[2], stringShape[3] + stringShape[4] + stringShape[5], stringShape[6] + stringShape[7] + stringShape[8])
    ingredients.forEach { key, ingredient -> shape.setIngredient(key, ingredient) }
    ingredientsData.forEach { key, ingredient -> shape.setIngredient(key, ingredient) }
    Bukkit.addRecipe(shape)
    return true
}