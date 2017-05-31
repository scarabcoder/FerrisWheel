package net.clownercraft.ferriswheel;

import java.util.ArrayList;
import java.util.List;

import net.clownercraft.tokenmanager.TokenManager;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_11_R1.EntityMinecartAbstract;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftMinecart;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.clownercraft.animations.data.Animations;

public class Wheel {
	
	private static int radius;
	private static double speedModifier;
	private static Location center;
	private static int cost;
	private static String axis;
	
	private static List<Minecart> carts = new ArrayList<Minecart>();
	
	
	public static void init(){
		FileConfiguration f = FerrisWheel.getPlugin().getConfig();
		if(f.contains("radius"))
			radius = f.getInt("radius");
		if(f.contains("speedModifier"))
			speedModifier = f.getDouble("speedModifier");
		if(f.contains("center"))
			center = (Location) f.get("center");
		if(f.contains("cost"))
			cost = f.getInt("cost");
		if(f.contains("axis"))
			axis = f.getString("axis");
		if(!FerrisWheel.containsAll("radius", "speedModifier", "center", "cost", "axis"))
			System.out.println("[Ferris Wheel] One or more of the following settings have not been set: \nradius, speedModifier, center, cost, axis.");
	}
	
	public static void startRide(Player p){
		if(FerrisWheel.containsAll("radius", "speedModifier", "center", "cost", "axis")){
			if(TokenManager.getInstance().getMoney(p.getUniqueId()) >= Wheel.getTokenCost() || p.hasPermission("ferriswheel.admin")){
				if(!p.hasPermission("ferriswheel.admin")){
					TokenManager.getInstance().setMoney(p.getUniqueId(), TokenManager.getInstance().getMoney(p.getUniqueId()) - Wheel.getTokenCost());
					p.sendMessage(ChatColor.RED + "-" + Wheel.getTokenCost() + " tokens.");
				}
				Animations.getAnimation("ferris").animate(1);
				Location from = p.getLocation().clone();
				double current = 3 * Math.PI / 2;
				Location l = center.clone();
				l.setY(l.getY() + getRadius()*Math.sin(current));
				Minecart m = (Minecart) l.getWorld().spawnEntity(l, EntityType.MINECART);
				m.setGravity(false);
				m.setInvulnerable(true);
				Wheel.carts.add(m);
				m.addPassenger(p);
				EntityMinecartAbstract cm = ((CraftMinecart) m).getHandle();
				BukkitRunnable r = new BukkitRunnable(){
	
					double curr = 3 *Math.PI / 2;
					
					
					@Override
					public void run() {
	
						Location l = center.clone();
						
						
						double theta = 0;  // angle that will be increased each loop
						double h = (getAxis().equalsIgnoreCase("X") ? l.getX() : l.getZ());      // x/z coordinate of circle center
						double k = l.getY();      // y coordinate of circle center
						double step = ((double)radius) / 200 * speedModifier;  // amount to add to theta each time (degrees)
						//System.out.println(step);
						int ra = radius;
						while(theta < 360){
							Location l2 = m.getLocation();
							
							double x = (h + ra*Math.cos(theta));
							double z = (k + ra*Math.sin(theta));
							if(getAxis().equalsIgnoreCase("X")) l.setX(x); else l.setZ(x);;
							l.setY(z);
							if(theta > curr){
								//l.getWorld().spawnParticle(Particle.REDSTONE, l, 25);
								l2.setDirection(l.toVector().subtract(l2.toVector()));
								cm.setPosition(l.getX(), l.getY(), l.getZ());
								//m.setVelocity(l2.getDirection().multiply(l.distance(l2) * 2.5));
								break;
							}
							theta += step;
						}
						//System.out.println(curr);
						curr += step;
						if(curr > 11){
							m.remove();
							p.teleport(from);
							this.cancel();
						}
						
					}
				};
				r.runTaskTimer(FerrisWheel.getPlugin(), 0, 1);
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
	
	public static boolean isVehicle(Minecart m){
		return carts.contains(m);
	}
	
	public static List<Minecart> getCarts(){
		return carts;
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
	
	public static void setCenter(Location center){
		FerrisWheel.getPlugin().getConfig().set("center", center);
		FerrisWheel.getPlugin().saveConfig();
		Wheel.center = center;
	}
	
	public static void setCost(int cost){
		FerrisWheel.getPlugin().getConfig().set("cost", cost);
		FerrisWheel.getPlugin().saveConfig();
		Wheel.cost = cost;
	}
	
}
