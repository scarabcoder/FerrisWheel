package net.clownercraft.ferriswheel.listeners;

import net.clownercraft.ferriswheel.Wheel;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignListeners implements Listener {
	
	@EventHandler
	public void onSignPlace(SignChangeEvent e){
		if(e.getLine(0).equals("[joinwheel]")){
			e.setLine(0, "[Click to Ride]");
			e.setLine(1, ChatColor.BOLD + "Mega Wheel");
			e.setLine(2, ChatColor.GRAY + "for");
			e.setLine(3, ChatColor.BOLD.toString() + ChatColor.GOLD + "4 Tokens");
		}
	}
	
	@EventHandler
	public void onSignInteract(PlayerInteractEvent e){
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && isSign(e.getClickedBlock())){
			Sign s = (Sign) e.getClickedBlock().getState();
			if(s.getLine(0).equals("[Click to Ride]") && s.getLine(1).equals(ChatColor.BOLD + "Mega Wheel")){
				Wheel.startRide(e.getPlayer());
				
			}
		}
	}
	
	public boolean isSign(Block b){
		return b.getType().equals(Material.SIGN) || b.getType().equals(Material.WALL_SIGN) || b.getType().equals(Material.SIGN_POST);
	}
	
}
