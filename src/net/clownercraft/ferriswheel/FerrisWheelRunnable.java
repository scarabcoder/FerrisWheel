package net.clownercraft.ferriswheel;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftMinecart;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Minecart;
import org.bukkit.scheduler.BukkitRunnable;

public class FerrisWheelRunnable extends BukkitRunnable {
	
	private double current = 0;
	
	public FerrisWheelRunnable(){
		Location center = Wheel.getCenter().clone();
		
		center.setY(Math.cos(270 * Math.PI / 180));
		if(Wheel.getAxis().equalsIgnoreCase("X"))
			center.setX(Math.sin(270 * Math.PI / 180));
		else
			center.setZ(Math.sin(270 * Math.PI / 180));
		
		
		for(int x = 0; x <= Wheel.getCartsAmount(); x++){
			Minecart m = (Minecart) center.getWorld().spawnEntity(center, EntityType.MINECART);
			m.setGravity(false);
			Wheel.addMinecart(m);
		}
		
		
	}

	@Override
	public void run() {
		
		
		for(double theta = 0; theta < Math.PI * 2; theta += Math.PI / 180 * Wheel.getSpeedModifier()){
			
			if(theta == current){
				for(int x = 0; x != Wheel.getCarts().size(); x++){
					CraftMinecart m = (CraftMinecart) Wheel.getCarts().get(x);
					double x1 = Wheel.getCenter().getX();
					double z1 = Wheel.getCenter().getZ();
					
					double dif = Wheel.getRadius() * Math.sin(theta + (x * (Wheel.getCartsAmount() / 360) * Math.PI / 180));
					
					if(Wheel.getAxis().equalsIgnoreCase("X"))
						x1 += dif;
					else
						z1 += dif;
					
					m.getHandle().setPosition(x1, Wheel.getCenter().getY() + Wheel.getRadius() * Math.cos(theta), z1);
				}
			}
			
		}

		current += Math.PI / 180 * Wheel.getSpeedModifier();
	}

}
