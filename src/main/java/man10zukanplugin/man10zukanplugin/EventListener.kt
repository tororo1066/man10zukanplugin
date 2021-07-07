package man10zukanplugin.man10zukanplugin

import man10zukanplugin.man10zukanplugin.Man10ZukanPlugin.Companion.plugin
import man10zukanplugin.man10zukanplugin.Man10ZukanPlugin.Companion.prefix
import man10zukanplugin.man10zukanplugin.Util.itemFromBase64
import man10zukanplugin.man10zukanplugin.Util.itemToBase64
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack
import java.io.File

object EventListener : Listener {
    @EventHandler
    fun click(e: InventoryClickEvent) {
        val p = e.whoClicked as Player
        if (e.view.title().toString().contains(prefix) && e.view.title().toString().contains("(設定画面)")) {

            val sp = e.view.title().toString().split(" ")
            val page = sp[3].replace(")", "").toInt()
            val file = File(plugin.dataFolder, sp[1] + ".yml")
            val con = YamlConfiguration.loadConfiguration(file)
            when (e.hotbarButton) {
                0 -> {
                    p.sendMessage(page.toString())
                    if (page == 1){
                        e.isCancelled = true
                        return
                    }
                    val inv = Bukkit.createInventory(null, e.inventory.size, Component.text("$prefix ${sp[1]} (Page ${page - 1}) (設定画面)"))
                    val l = con.getStringList("zukan.p${page - 1}")
                    for (list in 0 until l.size) {
                        inv.setItem(list, itemFromBase64(l[list]))
                    }
                    e.isCancelled = true
                    p.closeInventory()
                    p.playSound(p.location, Sound.UI_BUTTON_CLICK, 1f, 1f)
                    p.openInventory(inv)
                }
                1 -> {
                    if (page == con.getInt("zukan.page")){
                        e.isCancelled = true
                        return
                    }
                    val inv = Bukkit.createInventory(null, e.inventory.size, Component.text("$prefix ${sp[1]} (Page ${page + 1}) (設定画面)"))
                    val l = con.getStringList("zukan.p${page + 1}")
                    for (list in 0 until l.size) {
                        inv.setItem(list, itemFromBase64(l[list]))
                    }
                    e.isCancelled = true
                    p.closeInventory()
                    p.playSound(p.location, Sound.UI_BUTTON_CLICK, 1f, 1f)
                    p.openInventory(inv)
                }
            }
        }
    }


    @EventHandler
    fun invclose(e: InventoryCloseEvent) {
        if (e.view.title().toString().contains(prefix) && e.view.title().toString().contains("(設定画面)")) {
            val sp = e.view.title().toString().split(" ")
            val page = sp[3].replace(")", "").toInt()
            val file = File(plugin.dataFolder, sp[1] + ".yml")
            val con = YamlConfiguration.loadConfiguration(file)
            val savel = con.getStringList("zukan.p$page")
            savel.clear()
            for (list in e.inventory.contents) {
                if (list == null){
                    savel.add(itemToBase64(ItemStack(Material.AIR)))
                }else{
                    savel.add(itemToBase64(list))
                }

            }
            con.set("zukan.p$page", savel)
            con.save(file)
            e.player.sendMessage(prefix + "configの保存が完了しました")
        }
    }
}