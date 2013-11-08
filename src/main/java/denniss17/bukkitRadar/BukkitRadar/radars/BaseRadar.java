package denniss17.bukkitRadar.radars;

import net.minecraft.server.v1_6_R3.Packet207SetScoreboardScore;

import org.bukkit.craftbukkit.v1_6_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;

import denniss17.bukkitRadar.BukkitRadar;
import denniss17.bukkitRadar.BukkitRadar.RadarType;

public abstract class BaseRadar {
	protected Player player;
	protected Objective objective;
	protected BukkitRadar plugin;
	protected RadarType type;
	protected int radius;
	
	public BaseRadar(BukkitRadar plugin, Player player, int radius){
		this.plugin = plugin;
		this.player = player;
		this.radius = radius;
	}
	
	public RadarType getType(){
		return this.type;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void sendCustomScore(String name, int value){
		Packet207SetScoreboardScore pack = new Packet207SetScoreboardScore();
        pack.a = name; // Item name
        pack.c = value; // Item score
        pack.d = 0; // 0 for create/update, 1 for remove
        pack.b = objective.getName(); // Scoreboard name
        
       ((CraftPlayer)player).getHandle().playerConnection.sendPacket(pack);
	}
	
	public void removeCustomScore(String name){
		Packet207SetScoreboardScore pack = new Packet207SetScoreboardScore();
        pack.a = name; // Item name
        //pack.c = value; // Item score - not used on remove
        pack.d = 1; // 0 for create/update, 1 for remove
        pack.b = objective.getName(); // Scoreboard name
        
       ((CraftPlayer)player).getHandle().playerConnection.sendPacket(pack);
	}
	
	public String parseObjectiveName(String name){
		return name.replace("{radius}", String.valueOf(radius));
	}
	
	/*public Set<Chunk> getChunksWithinRadius(){
		Set<Chunk> result = new HashSet<Chunk>();
		
		Chunk chunk = player.getLocation().add(-radius, 0, -radius).getChunk();
		int minx = chunk.getX();
		int minz = chunk.getZ();
		chunk = player.getLocation().add(radius, 0, radius).getChunk();
		int maxx = chunk.getX();
		int maxz = chunk.getZ();
		for(int i = minx; i<=maxx; i++){
			for(int j = minz; j<=maxz; j++){
				result.add(player.getWorld().getChunkAt(i, j));
			}
		}
		plugin.getLogger().info("Found " + result.size() + " chucnks within " + radius + " radius of " + player.getName());
		return result;
	}*/

	public abstract void updateRadar();
	
	public abstract void init();
	
	public abstract void reload();
	
	public abstract void destroy();
}
