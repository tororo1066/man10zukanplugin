package man10zukanplugin.man10zukanplugin


import man10zukanplugin.man10zukanplugin.Man10ZukanPlugin.Companion.plugin
import man10zukanplugin.man10zukanplugin.Man10ZukanPlugin.Companion.prefix
import man10zukanplugin.man10zukanplugin.Util.itemFromBase64
import man10zukanplugin.man10zukanplugin.Util.itemToBase64
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.io.File

object ZukanCommand : CommandExecutor {



    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (sender !is Player){
            sender.sendMessage(prefix + "プレイヤーでのみ実行できます！")
            return true
        }
        when(args[0]){
            "help"->{

            }

            "show"->{
                if (args.size != 2)return true
                val file = File(plugin.dataFolder,"${args[1]}.yml")
                if (!file.exists()){
                    sender.sendMessage("この図鑑は存在しません！")
                    return true
                }
                val con = YamlConfiguration.loadConfiguration(file)
                if (!con.contains("zukan")){
                    sender.sendMessage(prefix + "このconfigファイルは図鑑ファイルではありません！")
                    return true
                }
                val inv = Bukkit.createInventory(null, con.getInt("zukan.slot") * 9, Component.text("$prefix ${args[1]} (Page 1)"))
                val l = con.getStringList("zukan.p1")
                if (l.size != con.getInt("zukan.slot") * 9){
                    sender.sendMessage(prefix + "configファイルが壊れています")
                    return true
                }
                for (list in 0 until l.size) {
                    inv.setItem(list, itemFromBase64(l[list]))
                }
                sender.openInventory(inv)
            }

            "create"->{
                if (!sender.hasPermission("admin"))return true
                if (args.size != 4)return true
                try {
                    if (args[2].toInt() !in 1..6){
                        sender.sendMessage(prefix + "slotの大きさは1~6で指定してください！")
                        return true
                    }
                    if (args[3].toInt() !in 1..10){
                        sender.sendMessage(prefix + "pageの大きさは1~10で指定してください！")
                        return true
                    }
                }catch (e : NumberFormatException){
                    sender.sendMessage(prefix + "slot、pageは数字で指定してください！")
                    return true
                }

                val file = File(plugin.dataFolder,"${args[1]}.yml")
                if (file.exists()){
                    sender.sendMessage("この図鑑はすでに作成されています！")
                    return true
                }
                file.createNewFile()
                val con = YamlConfiguration.loadConfiguration(file)
                con.set("zukan.slot",args[2].toInt())
                con.set("zukan.page",args[3].toInt())
                for (i in 1..args[3].toInt()){
                    val list = arrayListOf<String>()
                    for (l in 0 until args[2].toInt()*9){
                        list.add(itemToBase64(ItemStack(Material.AIR)))
                    }
                    con.set("zukan.p$i",list)
                }
                con.save(file)
                sender.sendMessage(prefix + "図鑑${args[1]}(slot:${args[2]}、page:${args[3]})が作成されました")
                return true
            }
            "setting"->{
                if (args.size != 2)return true
                val file = File(plugin.dataFolder,"${args[1]}.yml")
                if (!file.exists()){
                    sender.sendMessage(prefix + "この図鑑ファイルは存在しません！")
                    return true
                }
                val con = YamlConfiguration.loadConfiguration(file)
                if (!con.contains("zukan")){
                    sender.sendMessage(prefix + "このconfigファイルは図鑑ファイルではありません！")
                    return true
                }
                val inv = Bukkit.createInventory(null,con.getInt("zukan.slot") * 9 ,Component.text("$prefix ${args[1]} (Page 1) (設定画面)"))
                val l = con.getStringList("zukan.p1")
                if (l.size != con.getInt("zukan.slot") * 9){
                    sender.sendMessage(prefix + "configファイルが壊れています")
                    return true
                }
                for (list in 0 until l.size) {
                    inv.setItem(list, itemFromBase64(l[list]))
                }
                sender.openInventory(inv)
            }
        }
        return true
    }
}