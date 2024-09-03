package n1.codingtime.poliswagmc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;

import java.io.File;
import java.io.IOException;
import java.util.Set;


public class PoliswagMC extends JavaPlugin {

    private ScoreboardManager manager;
    private Scoreboard scoreboard;
    private Objective objective;

    private FileConfiguration playerData;
    private File dataFile;

    @Override
    public void onEnable() {
        createOrLoadFile();
        setupScoreboard();
        getServer().getPluginManager().registerEvents(new DeathListener(playerData, dataFile), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(scoreboard), this);
        getLogger().info("onEnable has been called.");
    }

    @Override
    public void onDisable() {
        getLogger().info("onDisable is called!");
        saveData();
    }

    private void saveData() {
        try {
            playerData.save(dataFile);
            getLogger().info("Saved data to playerData.yml file.");
        } catch (IOException e) {
            e.printStackTrace();
            getLogger().severe("Failed to save playerdata.yml file.");
        }
    }

    private void createOrLoadFile() {
        // Get the data folder for the plugin
        File dataFolder = getDataFolder();
        if (!dataFolder.exists()) {
            getLogger().info("Data yml does not exist, creating file.");
            dataFolder.mkdirs(); // Create the folder if it does not exist
        }

        // Define the YAML file
        dataFile = new File(dataFolder, "playerdata.yml");

        if (!dataFile.exists()) {
            // File does not exist, so create it
            try {
                dataFile.createNewFile();
                getLogger().info("playerdata.yml file created.");
            } catch (IOException e) {
                e.printStackTrace();
                getLogger().severe("Failed to create playerdata.yml file.");
            }
        }

        // Load the YAML file into FileConfiguration
        playerData = YamlConfiguration.loadConfiguration(dataFile);
    }


    private void setupScoreboard() {
        // Get the scoreboard manager
        manager = Bukkit.getScoreboardManager();

        // Create a new scoreboard
        scoreboard = manager.getNewScoreboard();

        // Create a new objective and set its display slot and title
        int restart = playerData.getInt("restarts", 0);
        objective = scoreboard.registerNewObjective("restarts", "restart #", ChatColor.RED + "Restarts: " + restart);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        // Populating scoreboard with yml data of player deaths
        ConfigurationSection deathsSection = playerData.getConfigurationSection("deaths");
        if (deathsSection != null) {
            // Retrieve all player names under 'deaths'
            Set<String> playerNames = deathsSection.getKeys(false);
            for (String player : playerNames) {
                int deathCount = deathsSection.getInt(player);
                Score p = objective.getScore(ChatColor.WHITE + player + ":");
                p.setScore(deathCount);
            }
        } else {
            getLogger().warning("'deaths' section not found in the configuration.");
        }
    }
}
