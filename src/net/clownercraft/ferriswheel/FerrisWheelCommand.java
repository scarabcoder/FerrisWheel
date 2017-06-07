package net.clownercraft.ferriswheel;

import java.util.Arrays;
import java.util.List;

import net.md_5.bungee.api.ChatColor;

import org.apache.commons.lang.StringUtils;
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
						}else if(args[0].equalsIgnoreCase("disable") || args[0].equalsIgnoreCase("enable")){
							Wheel.setEnabled(args[0].equalsIgnoreCase("enable"));
							p.sendMessage(ChatColor.GREEN + StringUtils.capitalize(args[0].toLowerCase()) + "d Ferris Wheel.");
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
						}else if(args[0].equalsIgnoreCase("carts")){
							
							try {
								int amnt = Integer.valueOf(args[1]);
								Wheel.setCarts(amnt);
								p.sendMessage(ChatColor.GREEN + "Set carts amount to " + amnt + ".");
							} catch(NumberFormatException e){
								p.sendMessage(ChatColor.RED + "Invalid input, number expected.");
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
					List<String> help = Arrays.asList(ChatColor.GRAY + "--- " + ChatColor.DARK_AQUA + "Ferris Wheel" + ChatColor.GRAY + " ---",
							"/fw center" + ChatColor.GRAY + ": Set center of Ferris Wheel.",
							"/fw radius <radius>" + ChatColor.GRAY + ": Set the radius of the Ferris Wheel.",
							"/fw cost <tokens>" + ChatColor.GRAY + ": Set the token cost to ride the Ferris Wheel.",
							"/fw speedModifier <modifier>" + ChatColor.GRAY + ": Set the speed modifier. 1 is none, 1.5 is 50% faster, etc.",
							"/fw axis <X/Z>" + ChatColor.GRAY + ": Set the axis to rotate on. Must be X or Z.",
							"/fw enable/disable" + ChatColor.GRAY + ": Disable or enable the rides moving.",
							"/fw carts <amount>" + ChatColor.GRAY + ": Set the amount of carts/rides.",
							"/fw ride" + ChatColor.GRAY + ": Ride the ferris wheel.");
					for(String m : help){
						p.sendMessage(m);
					}
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
