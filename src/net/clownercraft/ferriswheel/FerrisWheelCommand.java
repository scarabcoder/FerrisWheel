package net.clownercraft.ferriswheel;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FerrisWheelCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2,
			String[] args) {
		
		if(sender.hasPermission("ferriswheel.admin")){
			if(sender instanceof Player){
				Player p = (Player) sender;
				if(args.length > 0){
					if(args.length == 1){
						if(args[0].equals("center")){
							Wheel.setCenter(p.getLocation());
							p.sendMessage(ChatColor.GREEN + "Set center of Ferris Wheel to current location.");
						}else if(args[0].equals("ride")){
							Wheel.startRide(p);
						}else{
							p.sendMessage(ChatColor.RED + "Invalid arguments, used /fw for help.");
						}
					}else if(args.length == 2){
						if(args[0].equals("cost")){
							try {
								Wheel.setCost(Integer.valueOf(args[1]));
								p.sendMessage(ChatColor.GREEN + "Set token cost to " + args[1] + ".");
							} catch (NumberFormatException e){
								p.sendMessage(ChatColor.RED + "Number expected, got string.");
							}
						}else if(args[0].equals("speedModifier")){
							try {
								Wheel.setSpeedModifier(Double.valueOf(args[1]));
								p.sendMessage(ChatColor.GREEN + "Set speed modifier to " + args[1] + ".");
							} catch (NumberFormatException e){
								p.sendMessage(ChatColor.RED + "Double/float expected, got string.");
							}
						}else if(args[0].equalsIgnoreCase("axis")){
							boolean changed = Wheel.setAxis(args[1]);
							if(changed){
								p.sendMessage(ChatColor.GREEN + "Set axis.");
							}else{
								p.sendMessage(ChatColor.RED + "Invalid axis! Use X or Z.");
							}
						}else if(args[0].equalsIgnoreCase("radius")){
							try {
								Wheel.setRadius(Integer.valueOf(args[1]));
								p.sendMessage(ChatColor.GREEN + "Set radius to " + args[1] + ".");
							} catch (NumberFormatException e){
								p.sendMessage(ChatColor.RED + "Number expected, got string.");
							}
						}else{
							p.sendMessage(ChatColor.RED + "Invalid arguments, used /fw for help.");
						}
					}else{
						p.sendMessage(ChatColor.RED + "Invalid arguments, used /fw for help.");
					}
				}else{
					
				}
			}else{
				sender.sendMessage(ChatColor.RED + "Player-only command!");
			}
		}else{
			sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
		}
		
		return true;
	}

}
