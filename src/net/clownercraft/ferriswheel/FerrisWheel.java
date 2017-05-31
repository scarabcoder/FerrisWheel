package net.clownercraft.ferriswheel;


import net.clownercraft.ferriswheel.listeners.SignListeners;
import net.clownercraft.ferriswheel.listeners.VehicleListeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Minecart;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class FerrisWheel extends JavaPlugin {
	
	private static Plugin plugin;
	
	@Override
	public void onEnable(){
		plugin = this;
		this.getConfig().options().copyDefaults(true);
		this.getCommand("ferriswheel").setExecutor(new FerrisWheelCommand());
		Bukkit.getPluginManager().registerEvents(new SignListeners(), this);
		Bukkit.getPluginManager().registerEvents(new VehicleListeners(), this);
		this.saveDefaultConfig();
		Wheel.init();
	}
	
	@Override
	public void onDisable(){
		for(Minecart m : Wheel.getCarts()){
			m.remove();
		}
	}
	
	
	public static Plugin getPlugin(){
		return plugin;
	}
	
	
	
	public static boolean containsAll(String...strings){
		for(String str : strings){
			if(!plugin.getConfig().contains(str))
				return false;
		}
		return true;
	}
	
}
