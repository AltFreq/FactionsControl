package com.minecraftdimensions.factionscontrol;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlaceBlockEventListener implements Listener {

	@EventHandler
	public void blockbreak(BlockPlaceEvent e) {
		if(e.isCancelled()){
			return;
		}
		if(!FactionManager.canPlayerPlaceHere(e.getPlayer(), e.getBlock())){
			e.setCancelled(true);
		}
	}
	
}
