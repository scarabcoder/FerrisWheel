package net.clownercraft.ferriswheel;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftMinecart;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class FerrisWheelRunnable extends BukkitRunnable {
	
	private double current = 0;
	
	int time = 0;
	int revolutions = 0;
	
	public FerrisWheelRunnable(){
		Location center = Wheel.getCenter().clone();
		
		center.setY(center.getY() + Math.sin(270 * Math.PI / 180));
		if(Wheel.getAxis().equalsIgnoreCase("X"))
			center.setX(center.getX() + Math.cos(270 * Math.PI / 180));
		else
			center.setZ(center.getZ() + Math.cos(270 * Math.PI / 180));
		
		
		for(int x = 0; x <= Wheel.getCartsAmount(); x++){
			Minecart m = (Minecart) center.getWorld().spawnEntity(center, EntityType.MINECART);
			m.setGravity(false);
			Wheel.addMinecart(x, m);
		}
		
		
	}

	@Override
	public void run() {
		double theta = 0.0;
		if(time == 0){
			for(theta = 0; theta <= Math.PI * 2; theta += Math.PI / 180 * Wheel.getSpeedModifier()){
				if(theta == current){
					for(double x = 0; x != Wheel.getCarts().size(); x++){
						CraftMinecart m = (CraftMinecart) Wheel.getCart((int) x);
						double x1 = Wheel.getCenter().getX();
						double z1 = Wheel.getCenter().getZ();
						System.out.println(x);
						double totalDif = x * 360 / Wheel.getCartsAmount() * (Math.PI / 180);
						double dif = Wheel.getRadius() * Math.cos(theta + totalDif);
						//System.out.println(dif);
						if(Wheel.getAxis().equalsIgnoreCase("X"))
							x1 += dif;
						else
							z1 += dif;
						double y1 = Wheel.getCenter().getY() + Wheel.getRadius() * Math.sin(theta + totalDif);
						m.getHandle().setPosition(x1, y1, z1);
						Location l = new Location(Wheel.getCenter().getWorld(), x1, y1, z1);
						l.getWorld().spawnParticle(Particle.REDSTONE, l, 25);
					}
					if(Wheel.isInRange(current, 270 * Math.PI / 180, Wheel.getSpeedModifier() * Math.PI / 180)){
						revolutions++;
						if(revolutions == 2){
							revolutions = 0;
							for(Player p : Wheel.getPlayerBuffer()){
								
								Wheel.removeFromBuffer(p);
								
							}
							time++;
						}
					}
				}
				
			}
			current += Math.PI / 180 * Wheel.getSpeedModifier();
			if(current >= Math.PI * 2){
				current = 0.0;
			}
		}else{

			for(double x = 0; x != Wheel.getCarts().size(); x++){
				CraftMinecart m = (CraftMinecart) Wheel.getCart((int) x);
				double x1 = Wheel.getCenter().getX();
				double z1 = Wheel.getCenter().getZ();
				System.out.println(x);
				double totalDif = x * 360 / Wheel.getCartsAmount() * (Math.PI / 180);
				double dif = Wheel.getRadius() * Math.cos(270 * Math.PI / 180 + totalDif);
				if(Wheel.getAxis().equalsIgnoreCase("X"))
					x1 += dif;
				else
					z1 += dif;
				double y1 = Wheel.getCenter().getY() + Wheel.getRadius() * Math.sin(270 * Math.PI / 180 + totalDif);
				m.getHandle().setPosition(x1, y1, z1);
				Location l = new Location(Wheel.getCenter().getWorld(), x1, y1, z1);
				l.getWorld().spawnParticle(Particle.REDSTONE, l, 25);
			}
			time++;
			if(time == 20 * 5){
				time = 0;
			}
		}
	}

}
