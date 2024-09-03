package n1.codingtime.poliswagmc;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Scoreboard;

public class PlayerJoinListener implements Listener {

    // this listener is just to assign the scoreboard as people join the server
    private Scoreboard scoreboard;

    public PlayerJoinListener(Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.setScoreboard(scoreboard);
    }
}
