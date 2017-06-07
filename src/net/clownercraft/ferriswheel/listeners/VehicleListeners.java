package net.clownercraft.ferriswheel.listeners;

import net.clownercraft.ferriswheel.Wheel;

import org.bukkit.entity.Minecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

public class VehicleListeners implements Listener {
	
	@EventHandler
	public void onLeaveVehicle(VehicleExitEvent e){
		if(e.getVehicle() instanceof Minecart){
			Minecart m = (Minecart) e.getVehicle();
			if(Wheel.isVehicle(m) && Wheel.isInBuffer(e.getExited())) e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onVehicleDamage(VehicleDamageEvent e){
		if(e.getVehicle() instanceof Minecart && Wheel.isVehicle((Minecart)e.getVehicle())){
			e.setCancelled(true);
		}
	}
	
}
