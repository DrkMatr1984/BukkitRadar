package denniss17.bukkitRadar;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class BukkitRadarCommands implements CommandExecutor {
	
	private BukkitRadar plugin;
	
	public BukkitRadarCommands(BukkitRadar plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(!sender.hasPermission("bukkitradar.bukkitradar")){
			plugin.sendConfigMessage(sender, "error_no_permission");
			return true;
		}
		
		if(cmd.getName().equals("bukkitradar")){
    		if(args.length==0){
    			return commandMenu(sender, cmd, label, args);
    		}else{
    			if(args[0].equals("set")){
    				return commandSet(sender, cmd, label, args);
    			}else if(args[0].equals("clear")){
    				return commandClear(sender, cmd, label, args);
    			}else if(args[0].equals("reload")){
    				return commandReload(sender, cmd, label, args);
    			}else{
    				return commandMenu(sender, cmd, label, args);
    			}
    		}
    	}
    	return false;
    }
	
	public boolean commandMenu(CommandSender sender, Command cmd, String label, String[] args){
		plugin.sendConfigMessage(sender, "menu_header");
		plugin.sendConfigMessage(sender, "menu_set");
		plugin.sendConfigMessage(sender, "menu_clear");
		if(sender.hasPermission("bukkitradar.reload")){
			plugin.sendConfigMessage(sender, "menu_reload");
		}
		plugin.sendConfigMessage(sender, "menu_footer");
		
		return true;
	}
	
	public boolean commandSet(CommandSender sender, Command cmd, String label, String[] args){
		if(args.length<2){
			plugin.sendConfigMessage(sender, "menu_set");
			return true;
		}
		if(!(sender instanceof Player)){
			plugin.sendConfigMessage(sender, "error_not_a_player");
			return true;
		}
		
		Player player = (Player)sender;
		
		if(args[1].equals("player")){
			if(player.hasPermission("bukkitradar.playerradar")){
				plugin.showPlayerRadar(player);
				plugin.sendConfigMessage(player, "radar_player_set");
			}else{
				plugin.sendConfigMessage(player, "error_no_permission");
			}
			return true;
		}else if(args[1].equals("mob")){
			if(player.hasPermission("bukkitradar.mobradar")){
				plugin.showMobRadar(player);
				plugin.sendConfigMessage(player, "radar_mob_set");
			}else{
				plugin.sendConfigMessage(player, "error_no_permission");
			}
			return true;
		}else{
			plugin.sendConfigMessage(player, "radar_type_not_valid");
			return true;
		}
	}
	
	public boolean commandClear(CommandSender sender, Command cmd, String label, String[] args){
		if(!(sender instanceof Player)){
			plugin.sendConfigMessage(sender, "error_not_a_player");
			return true;
		}
		Player player = (Player)sender;
		
		if(player.hasPermission("bukkitradar.clearradar")){
			plugin.hideRadar(player);
			plugin.sendConfigMessage(player, "radar_cleared");
		}else{
			plugin.sendConfigMessage(player, "error_no_permission");
		}
			
		return true;
	}
	
	public boolean commandReload(CommandSender sender, Command cmd, String label, String[] args){		
		if(sender.hasPermission("bukkitradar.reload")){
			plugin.reload();
			plugin.sendConfigMessage(sender, "plugin_reloaded");
		}else{
			plugin.sendConfigMessage(sender, "error_no_permission");
		}
			
		return true;
	}

}
