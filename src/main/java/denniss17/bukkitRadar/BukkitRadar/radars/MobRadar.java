package denniss17.bukkitRadar.radars;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import denniss17.bukkitRadar.BukkitRadar;
import denniss17.bukkitRadar.BukkitRadar.RadarType;

public class MobRadar extends BaseRadar {
	private Set<EntityType> toRemove;
	private Map<EntityType, Integer> entityDistances;
	
	public static Set<EntityType> mobsShowed;

	public MobRadar(BukkitRadar plugin, Player player) {
		super(plugin, player, plugin.getConfig().getInt("mobradar.radius"));
		this.type = RadarType.MOB_RADAR;
		this.entityDistances = new HashMap<EntityType, Integer>();
		this.toRemove = new HashSet<EntityType>();
		if(mobsShowed == null){
			loadMobsShowed();
		}
	}
	
	private void loadMobsShowed(){
		mobsShowed = new HashSet<EntityType>();
		for(String entityname : plugin.getConfig().getStringList("mobradar.mobs")){
			try{
				mobsShowed.add(EntityType.valueOf(entityname));
			}catch(IllegalArgumentException e){
				plugin.getLogger().warning("Entity '" + entityname + "' in mobradar.mobs (config.yml) is not a valid entityname!");
			}
		}
	}

	@Override
	public void init() {
		this.objective = player.getScoreboard().getObjective("mobradar");
		if(this.objective==null){
			this.objective = player.getScoreboard().registerNewObjective("mobradar", "dummy");
		}
		this.objective.setDisplayName(parseObjectiveName(plugin.getConfig().getString("mobradar.name")));
		this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
	}
	
	@Override
	public void reload(){
		loadMobsShowed();
		this.radius = plugin.getConfig().getInt("mobradar.radius");
		this.objective.setDisplayName(parseObjectiveName(plugin.getConfig().getString("mobradar.name")));
	}

	@Override
	public void updateRadar() {
		// Reset counts
		for(EntityType type: entityDistances.keySet()){
			entityDistances.put(type, -1);
		}
		
		// Count entities
		List<Entity> entities = player.getNearbyEntities(radius, radius, radius);
		for(Entity entity : entities){
			if(mobsShowed.contains(entity.getType())){
				int distance = (int)player.getLocation().distance(entity.getLocation());
				if(distance<=radius){
					if(entityDistances.containsKey(entity.getType())){
						int current = entityDistances.get(entity.getType());
						if(current==-1 || distance < current){
							entityDistances.put(entity.getType(), distance);
						}
					}else{
						entityDistances.put(entity.getType(), distance);
					}
				}
			}
		}
		
		// Remove score from list or send score
		for(Entry<EntityType, Integer> entry: entityDistances.entrySet()){
			if(entry.getValue()==-1){
				// Remove from scoreboard
				this.removeCustomScore(entry.getKey().toString().toLowerCase());
				// Remove from mapping
				// Don't remove directly: causes ConcurrentModificationException
				toRemove.add(entry.getKey());
			}else{
				// Send score
				this.sendCustomScore(entry.getKey().toString().toLowerCase(), entry.getValue());
			}
		}
		// Clean up
		for(EntityType type : toRemove){
			entityDistances.remove(type);
		}
		toRemove.clear();
	}

	@Override
	public void destroy() {
		this.objective.setDisplaySlot(null);		
	}
	
}
