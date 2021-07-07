package man10zukanplugin.man10zukanplugin

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class Man10ZukanPlugin : JavaPlugin() {


    companion object{
        lateinit var plugin : Man10ZukanPlugin
        var prefix = "[§dM§eZukan§f]"
        val zd = ArrayList<ArrayList<Pair<String,Boolean>>>()
    }
    override fun onEnable() {
        plugin = this
        saveDefaultConfig()
        server.pluginManager.registerEvents(EventListener,plugin)
        getCommand("mz")?.setExecutor(ZukanCommand)
        val mysql = MySQLManager(plugin,"mz reload")
        val rs = mysql.query("SELECT * FROM man10zukan;")
        while (rs?.next() == true){
            if (zd.)
            if (rs.getInt("boolean") == 1){

            }

        }
    }

}