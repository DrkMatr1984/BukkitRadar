package denniss17.bukkitRadar.radars;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import denniss17.bukkitRadar.BukkitRadar;
import denniss17.bukkitRadar.BukkitRadar.RadarType;

public class PlayerRadar extends BaseRadar {


	public PlayerRadar(BukkitRadar plugin, Player player) {
		super(plugin, player, plugin.getConfig().getInt("playerradar.radius"));
		this.type = RadarType.PLAYER_RADAR;
	}

	@Override
	public void init() {
		this.objective = player.getScoreboard().getObjective("playerradar");
		if(this.objective==null){
			this.objective = player.getScoreboard().registerNewObjective("playerradar", "dummy");
		}
		this.objective.setDisplayName(parseObjectiveName(plugin.getConfig().getString("playerradar.name")));
		this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
	}
	
	@Override
	public void reload(){
		this.radius = plugin.getConfig().getInt("playerradar.radius");
		this.objective.setDisplayName(parseObjectiveName(plugin.getConfig().getString("playerradar.name")));
	}

	@Override
	public void updateRadar() {
		for(Player other : plugin.getServer().getOnlinePlayers()){
			int distance = (int)player.getLocation().distance(other.getLocation());
			if(!other.equals(player) && distance <= radius){
				objective.getScore(other).setScore(distance);
			}else{
				// Use this as you can't remove scores yet
				// https://bukkit.atlassian.net/browse/BUKKIT-4014
				this.removeCustomScore(other.getName());
			}
		}
	}

	@Override
	public void destroy() {
		this.objective.setDisplaySlot(null);		
	}
	
}
