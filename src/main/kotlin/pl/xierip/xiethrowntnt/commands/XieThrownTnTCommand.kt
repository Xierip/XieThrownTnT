package pl.xierip.xiethrowntnt.commands

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import pl.xierip.xiethrowntnt.XieThrownTnT
import pl.xierip.xiethrowntnt.util.sendColoredMessage
import java.util.*

/**
 * Created by xierip on 19.05.18.
 * Web: http://xierip.pl
 */
class XieThrownTntCommand : Command("xiethrowntnt", "", "", Arrays.asList("xtt", "throwntnt")) {
    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        if (args.size == 2) {
            val player: Player? = Bukkit.getPlayer(args[0])
            if (player == null) {
                sender.sendColoredMessage("&cGracz ${args[0]} jest offline!")
                return true
            }
            val amount = args[1].toIntOrNull()
            if (amount == null) {
                sender.sendColoredMessage("&cPodana wartosc ${args[1]} nie jest liczba!")
                return true
            }
            val clone = XieThrownTnT.instance!!.item.clone()
            clone.amount = amount
            player.inventory.addItem(clone).forEach {
                player.world.dropItem(player.location, it.value)
            }
            sender.sendColoredMessage("&aGracz ${args[0]} otrzymal rzucane tnt &7x$amount")
            return true
        }
        sender.sendColoredMessage("&f»»»» &a&lXieThrownTnT &f««««")
        sender.sendColoredMessage(" &7» &a/xtt &f<nick> <ilosc> &7« komenda do give")
        return true
    }

}