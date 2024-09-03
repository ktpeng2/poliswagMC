package n1.codingtime.poliswagmc;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import java.io.File;

public class DeathListener implements Listener {

    private FileConfiguration playerData;
    private File dataFile;
    boolean accessedEnd = false;

    public DeathListener(FileConfiguration playerData, File dataFile) {
        this.playerData = playerData;
        this.dataFile = dataFile;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Location deathLocation = player.getLocation();

        String worldName = null;

        //want to check where the player died
        if (deathLocation.getWorld() != null) {
            if (deathLocation.getWorld().getName() != null && !deathLocation.getWorld().getName().isEmpty()) {
                worldName = deathLocation.getWorld().getName();
            }
        }

        // If player dies in the end, we dont want to boot everyone out as we're in the endgame now
        if (worldName != null && worldName.equals("world_the_end")) {
            Bukkit.getLogger().info("player died in the end");
        }
        else if(accessedEnd) {
            Bukkit.getLogger().info("Player has died, but other players have accessed the end, continue run until all die");
        }
        else {
            //if a player dies in anywhere that isnt the end, and the end has not been accessed
            //      update yml file with the player that died
            //      increment the number of total restarts respectively
            //      boot all players since we did not make it to the end
            //      shut down server, to restart with a new world
            //          this is a WIP, as creating a new world cannot be done through this jar - likely will have to be
            //              done through a script (renaming world files or sum idk)
            Bukkit.getLogger().info(player.getName() + " died in world " + worldName);
            Bukkit.broadcastMessage(player.getName() + " died in world " + worldName);

            String playerName = player.getName();

            // Update player death count
            int deathCount = playerData.getInt("deaths." + playerName, 0);
            playerData.set("deaths." + playerName, deathCount + 1);

            int restarts = playerData.getInt("restarts", 0);
            playerData.set("restarts", restarts + 1);

            for (Player p : Bukkit.getOnlinePlayers()) {
                p.kickPlayer(String.format(
                        "%s has died\n" +
                        "%s" +
                        "RIP to run %s",
                        playerName, getPlayerDeathInfo(event), playerData.getInt("restarts", 0)));
            }
            Bukkit.shutdown();
        }
    }



    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer(); // Get the player who changed worlds
        World world = player.getWorld(); // Get the new world the player is in

        // Check if the player has entered "The End"
        if (world.getEnvironment() == World.Environment.THE_END) {
            Bukkit.getLogger().info(player.getName() + " has entered the end.");
            accessedEnd = true;
        }
    }

    private String getPlayerDeathInfo (PlayerDeathEvent event) {
        Player player = event.getEntity();
        String playerName = player.getName();
        Location deathLocation = player.getLocation();
        Entity killer = player.getKiller();

        String deathMessage = event.getDeathMessage();

        String txt = "";
        txt += deathMessage + "\n";
        txt += playerName + " took " + player.getLastDamage() + " damage on death\n";
        if (player.getLastDamageCause() != null &&
            player.getLastDamageCause().getDamageSource() != null &&
            player.getLastDamageCause().getDamageSource().getCausingEntity() != null &&
            player.getLastDamageCause().getDamageSource().getCausingEntity().getName() != null) {
            txt += " from " + player.getLastDamageCause().getDamageSource().getCausingEntity().getName();
        }

        if (killer != null) {
            txt += "\naint no way we lost due to " + killer.getName() + " killing " + playerName;
        }

        return txt;
    }

}
