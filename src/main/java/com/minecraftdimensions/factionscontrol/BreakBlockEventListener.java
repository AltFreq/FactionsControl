package com.minecraftdimensions.factionscontrol;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakBlockEventListener implements Listener {
	

	@EventHandler
	public void blockbreak(BlockBreakEvent e) {
		if(e.isCancelled()){
			return;
		}
		if(!FactionManager.canPlayerBreakHere(e.getPlayer(), e.getBlock())){
			e.setCancelled(true);
		}
	}
	
}