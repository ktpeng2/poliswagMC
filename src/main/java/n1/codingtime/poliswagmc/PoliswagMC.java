package n1.codingtime.poliswagmc;

import org.bukkit.plugin.java.JavaPlugin;

public class PoliswagMC extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        getLogger().info("onEnable has been called.");
    }
    @Override
    public void onDisable() {
        getLogger().info("onDisable is called!");
    }

    public void onPlayerDeathEvent() {

    }

}
