package denniss17.bukkitRadar;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
	
	private BukkitRadar plugin;

	public PlayerListener(BukkitRadar plugin){
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerJoinBefore(PlayerJoinEvent event){
		// Set a new player specific scoreboard
		event.getPlayer().setScoreboard(plugin.getServer().getScoreboardManager().getNewScoreboard());
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerJoinAfter(PlayerJoinEvent event){
		//plugin.showMobRadar(event.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerquit(PlayerQuitEvent event){
		plugin.hideRadar(event.getPlayer());
	}
}
