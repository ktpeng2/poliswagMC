package n1.codingtime.poliswagmc;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class PlayerDeathListener implements Listener {

    boolean accessedEnd = false;

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity(); // Get the player who died
        Location deathLocation = player.getLocation(); // Get the location of death


        String worldName = null;

        if (deathLocation.getWorld() != null) {
            if (deathLocation.getWorld().getName() != null && !deathLocation.getWorld().getName().isEmpty()) {
                worldName = deathLocation.getWorld().getName();
            }
        }

        if (worldName != null && worldName.equals("THE_END")) {
            Bukkit.getLogger().info("player died in the end");
        }
        else {
            //want to kick all the players in 5 seconds
            //need to also update tally of deaths
        }

        Bukkit.getLogger().info(player.getName() + " died in world " + worldName + " at [" + x + ", " + y + ", " + z + "]");



    }



    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer(); // Get the player who changed worlds
        World world = player.getWorld(); // Get the new world the player is in

        // Check if the player has entered "The End"
        if (world.getEnvironment() == World.Environment.THE_END) {
            accessedEnd = true;
        }
    }

}
