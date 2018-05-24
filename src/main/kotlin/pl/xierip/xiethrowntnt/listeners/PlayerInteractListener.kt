package pl.xierip.xiethrowntnt.listeners

import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import pl.xierip.xiethrowntnt.XieThrownTnT
import pl.xierip.xiethrowntnt.util.formatTimeTo
import pl.xierip.xiethrowntnt.util.sendColoredMessage
import java.util.*
import kotlin.collections.HashMap

/**
 * Created by xierip on 19.05.18.
 * Web: http://xierip.pl
 */
class PlayerInteractListener : Listener {
    private val delays: HashMap<UUID, Long> = HashMap()

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onEvent(event: PlayerInteractEvent) {
        if (!event.hasItem()) return
        if (!XieThrownTnT.instance.item.isSimilar(event.item)) return
        event.isCancelled = true
        if (event.action != Action.RIGHT_CLICK_AIR) return
        if (!event.player.inventory.containsAtLeast(XieThrownTnT.instance.itemOne, 1)) return
        if (delays.containsKey(event.player.uniqueId)) {
            val time = delays[event.player.uniqueId]!! + XieThrownTnT.instance.delay
            if (time > System.currentTimeMillis()) {
                event.player.sendColoredMessage(XieThrownTnT.instance.messageDelay.replace("{TIME}", time.formatTimeTo()))
                return
            }
        }
        delays[event.player.uniqueId] = System.currentTimeMillis()
        event.player.inventory.removeItem(XieThrownTnT.instance.itemOne)
        val spawnEntity = event.player.world.spawnEntity(event.player.eyeLocation, EntityType.PRIMED_TNT)
        spawnEntity.velocity = event.player.eyeLocation.direction.multiply(XieThrownTnT.instance.multiply)
    }
}