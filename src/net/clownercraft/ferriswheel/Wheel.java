package net.clownercraft.ferriswheel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import net.clownercraft.tokenmanager.TokenManager;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;

public class Wheel {
	
	private static int radius;
	private static double speedModifier;
	private static Location center;
	private static int cost;
	private static String axis;
	private static int cartsAmount;
	private static FerrisWheelRunnable runnable;
	private static boolean enabled;
	private static Location leaveLocation;
	
	private static HashMap<Integer, UUID> carts = new HashMap<Integer, UUID>();
	
	private static List<UUID> playerBuffer = new ArrayList<UUID>();
	
	
	public static void init(){
		FileConfiguration f = FerrisWheel.getPlugin().getConfig();
		if(f.contains("radius"))
			radius = f.getInt("radius");
		if(f.contains("speedModifier"))
			speedModifier = f.getDouble("speedModifier");
		if(f.contains("center"))
			center = (Location) f.get("center");
		if(f.contains("carts"))
			cartsAmount = f.getInt("carts");
		if(f.contains("cost"))
			cost = f.getInt("cost");
		if(f.contains("enabled"))
			enabled = f.getBoolean("enabled");
		if(f.contains("leaveLoc"))
			leaveLocation = (Location) f.get("leaveLoc");
		if(f.contains("axis"))
			axis = f.getString("axis");
		if(!FerrisWheel.containsAll("radius", "speedModifier", "center", "cost", "axis", "carts", "leaveLoc"))
			System.out.println("[Ferris Wheel] One or more of the following settings have not been set: \nradius, speedModifier, center, cost, axis, carts, leaveLocation.");
		if(enabled){
			start();
		}
	}
	
	public static void start(){
		if(!FerrisWheel.containsAll("radius", "speedModifier", "center", "cost", "axis", "carts", "leaveLoc")) return;
		runnable = new FerrisWheelRunnable();
		
		runnable.runTaskTimer(FerrisWheel.getPlugin(), 0, 1);
		

	}
	
	public static boolean isInRange(double i1, double i2, double i3){
		return i1 > i2 - i3 / 2 && i1 < i2 + i3 / 2;
	}
	
	public static void stop(){
		runnable.cancel();
		for(Minecart m : Wheel.getCarts()){
			m.remove();
		}
		carts.clear();
	}
	
	public static List<Player> getPlayerBuffer(){
		List<Player> players = new ArrayList<Player>();
		for(UUID u : playerBuffer){
			players.add(Bukkit.getPlayer(u));
		}
		return players;
	}
	
	public static void removeFromBuffer(Player p){
		playerBuffer.remove(p.getUniqueId());
	}
	
	public static boolean isInBuffer(Entity e){
		return playerBuffer.contains(e.getUniqueId());
	}
	
	public static void removeFromBuffer(UUID id){
		playerBuffer.remove(id);
	}
	
	public static void addToBuffer(Player p){
		playerBuffer.add(p.getUniqueId());
	}
	
	public static void startRide(Player p){
		if(FerrisWheel.containsAll("radius", "speedModifier", "center", "cost", "axis")){
			if(isClass("net.clownercraft.tokenmanager.TokenManager") && TokenManager.getInstance().getMoney(p.getUniqueId()) >= Wheel.getTokenCost() || p.hasPermission("ferriswheel.admin")){
				if(!p.hasPermission("ferriswheel.admin")){
					TokenManager.getInstance().setMoney(p.getUniqueId(), TokenManager.getInstance().getMoney(p.getUniqueId()) - Wheel.getTokenCost());
					p.sendMessage(ChatColor.RED + "-" + Wheel.getTokenCost() + " tokens.");
				}
				p.sendMessage(ChatColor.GREEN + "Ride queued, please wait for Wheel to come around.");
				addToBuffer(p);
			}else{
				p.sendMessage(ChatColor.RED + "This ride requires " + Wheel.getTokenCost() + " tokens!");
			}
		}else{
			p.sendMessage(ChatColor.RED + "Ferris Wheel is currently out of service.");
			if(p.hasPermission("ferriswheel.admin")){
				p.sendMessage(ChatColor.RED + "Please complete plugin setup.");
			}
		}
		
	}
	
	public static boolean isClass(String className) {
	    try  {
	        Class.forName(className);
	        return true;
	    }  catch (ClassNotFoundException e) {
	        return false;
	    }
	}
	
	public static boolean isEnabled(){
		return enabled;
	}
	
	public static int getCartsAmount(){
		return cartsAmount;
	}
	
	public static boolean isVehicle(Minecart m){
		for(Minecart cart : getCarts()){
			if(cart.getUniqueId().equals(m.getUniqueId()));
		}
		return getCarts().contains(m);
	}
	
	public static List<Minecart> getCarts(){
		List<Minecart> carts = new ArrayList<Minecart>();
		for(int key : Wheel.carts.keySet()){
			carts.add((Minecart) Bukkit.getEntity(Wheel.carts.get(key)));
		}
		return carts;
	}
	
	public static HashMap<Integer, UUID> getIDCarts(){
		return carts;
	}
	
	public static void addMinecarts(List<Minecart> carts){
		int x = 0;
		for(Minecart cart : carts){
			Wheel.carts.put(x, cart.getUniqueId());
			x++;
		}
	}
	
	public static Minecart getCart(int id){
		return (Minecart) Bukkit.getEntity(carts.get(id));
	}
	
	public static Location getLeaveLocation(){
		return leaveLocation;
	}
	
	public static void addMinecart(int key, Minecart cart){
		carts.put(key, cart.getUniqueId());
	}
	
	public static void removeMinecart(Minecart cart){
		carts.remove(cart);
	}
	
	public static int getRadius(){
		return radius;
	}
	
	public static double getSpeedModifier(){
		return speedModifier;
	}
	
	public static Location getCenter(){
		return center;
	}
	
	public static int getTokenCost(){
		return cost;
	}
	
	public static String getAxis(){
		return axis;
	}
	
	public static boolean setAxis(String axis){
		if(axis.equalsIgnoreCase("X") || axis.equalsIgnoreCase("Z")){
			FerrisWheel.getPlugin().getConfig().set("axis", axis);
			FerrisWheel.getPlugin().saveConfig();
			Wheel.axis = axis;
			return true;
		}
		return false;
		
	}
	
	public static void setRadius(int radius){
		FerrisWheel.getPlugin().getConfig().set("radius", radius);
		FerrisWheel.getPlugin().saveConfig();
		Wheel.radius = radius;
	}
	
	public static void setSpeedModifier(double modifier){
		FerrisWheel.getPlugin().getConfig().set("speedModifier", modifier);
		FerrisWheel.getPlugin().saveConfig();
		speedModifier = modifier;
	}

	public static void setEnabled(boolean enabled){
		FerrisWheel.getPlugin().getConfig().set("enabled", enabled);
		FerrisWheel.getPlugin().saveConfig();
		if(enabled)
			Wheel.start();
		else
			Wheel.stop();
		Wheel.enabled = enabled;
	}
	
	public static void setLeaveLocation(Location l){
		FerrisWheel.getPlugin().getConfig().set("leaveLoc", l);
		FerrisWheel.getPlugin().saveConfig();
		leaveLocation = l;
	}
	
	public static void setCenter(Location center){
		FerrisWheel.getPlugin().getConfig().set("center", center);
		FerrisWheel.getPlugin().saveConfig();
		Wheel.center = center;
	}
	
	public static void setCarts(int size){
		FerrisWheel.getPlugin().getConfig().set("carts", size);
		FerrisWheel.getPlugin().saveConfig();
		Wheel.cartsAmount = size;
	}
	
	public static void setCost(int cost){
		FerrisWheel.getPlugin().getConfig().set("cost", cost);
		FerrisWheel.getPlugin().saveConfig();
		Wheel.cost = cost;
	}
	
}
