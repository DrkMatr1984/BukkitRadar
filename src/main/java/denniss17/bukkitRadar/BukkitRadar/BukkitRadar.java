package denniss17.bukkitRadar;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import denniss17.bukkitRadar.radars.BaseRadar;
import denniss17.bukkitRadar.radars.MobRadar;
import denniss17.bukkitRadar.radars.PlayerRadar;
import denniss17.bukkitRadar.utils.ChatStyler;
import denniss17.bukkitRadar.utils.VersionChecker;

public class BukkitRadar extends JavaPlugin implements Runnable {
	public static VersionChecker versionChecker;
	private Set<BaseRadar> currentRadars;
	
	private BukkitTask timerTask;
	
	public enum RadarType{
		PLAYER_RADAR, MOB_RADAR, ORE_RADAR
	}
	
	public void onEnable(){
		// Register PlayerListener
		this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
		
		if(getConfig().getBoolean("check_updates")){
			versionChecker = new VersionChecker(this);
			versionChecker.activate(getConfig().getInt("check_updates_interval")*60*20);
		}		
		
		currentRadars = new HashSet<BaseRadar>();
		
		BukkitRadarCommands executor = new BukkitRadarCommands(this);
		this.getCommand("bukkitradar").setExecutor(executor);
		
		getConfig().options().copyDefaults(true);
		saveConfig();
	}
	
	public void onDisable(){
		for(BaseRadar radar : currentRadars){
			radar.destroy();
		}
		currentRadars.clear();
		stopTimer();
	}
	
	public void reload() {
		reloadConfig();
		for(BaseRadar radar : currentRadars){
			radar.reload();
		}
	}

	public void updateRadars(){
		for(BaseRadar radar: currentRadars){
			radar.updateRadar();
		}
	}
	
	// Show the player radar
	public void showPlayerRadar(Player player){
		BaseRadar current = getRadarByPlayer(player);
		if(current!=null){
			if(current.getType().equals(RadarType.PLAYER_RADAR)){
				// Radar is already player radar
				return;
			}else{
				current.destroy();
				currentRadars.remove(current);
			}
		}
		
		BaseRadar radar = new PlayerRadar(this, player);
		radar.init();
		
		currentRadars.add(radar);
		
		startTimer();
	}
	
	// Show the mob radar
	public void showMobRadar(Player player){
		BaseRadar current = getRadarByPlayer(player);
		if(current!=null){
			if(current.getType().equals(RadarType.MOB_RADAR)){
				// Radar is already player radar
				return;
			}else{
				current.destroy();
				currentRadars.remove(current);
			}
		}
		
		BaseRadar radar = new MobRadar(this, player);
		radar.init();
		
		currentRadars.add(radar);
		
		startTimer();
	}

	public BaseRadar getRadarByPlayer(Player player){
		for(BaseRadar radar: currentRadars){
			if(radar.getPlayer().equals(player)) return radar;
		}
		return null;
	}
	
	public void hideRadar(Player player){
		BaseRadar current = getRadarByPlayer(player);
		if(current!=null){
			current.destroy();
			currentRadars.remove(current);
		}
		if(currentRadars.size()==0){
			stopTimer();
		}
	}
	
	public void startTimer(){
		if(timerTask==null){
			int interval = getConfig().getInt("radar_update_interval");
			timerTask = getServer().getScheduler().runTaskTimer(this, this, interval, interval);
		}
	}
	
	public void stopTimer(){
		if(timerTask!=null){
			timerTask.cancel();
			timerTask = null;
		}
	}

	public void run() {
		updateRadars();		
	}

	public void sendConfigMessage(CommandSender sender, String key) {
		sender.sendMessage(ChatStyler.setTotalStyle(getConfig().getString("messages."+key)));		
	}
}
