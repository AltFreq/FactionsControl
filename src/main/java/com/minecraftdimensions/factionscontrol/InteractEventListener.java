package com.minecraftdimensions.factionscontrol;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractEventListener implements Listener {
	
	@EventHandler
	public void blockbreak(PlayerInteractEvent e) {
		if(e.isCancelled()){
			return;
		}
		if((e.getAction().equals(Action.LEFT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK))){
			if(!FactionManager.canPlayerInteractHere(e.getPlayer(), e.getClickedBlock())){
				e.setCancelled(true);
				return;
			}
			return;
		}
		
	}

}
