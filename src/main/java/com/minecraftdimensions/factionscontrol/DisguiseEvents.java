package com.minecraftdimensions.factionscontrol;


import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.events.DisguiseEvent;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DisguiseEvents implements Listener {
	
	@EventHandler
	public void damgageEvent(EntityDamageEvent e) {
		if(e.isCancelled()){
			return;
		}
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (DisguiseAPI.isDisguised(e.getEntity())) {
				DisguiseAPI.undisguiseToAll(e.getEntity());
				
				p.sendMessage(ChatColor.RED + "You have been undisguised!");
			}
		}
	}

	@EventHandler
	public void interactEvent(PlayerInteractEvent e) {
		if (e.getAction().equals(Action.RIGHT_CLICK_AIR)
				|| e.getAction().equals(Action.LEFT_CLICK_AIR)
				|| e.getAction().equals(Action.RIGHT_CLICK_BLOCK)
				|| e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
			if (DisguiseAPI.isDisguised(e.getPlayer())) {
				e.setCancelled(true);
			}
		}
	}
	@EventHandler
	public void disguiseEvent(DisguiseEvent e) {
		if(e.getEntity() instanceof Player){
			Player p = (Player) e.getEntity();
			for(PotionEffect po: p.getActivePotionEffects()){
				p.removePotionEffect(po.getType());
			}
		}
	}
	
	@EventHandler
	public void pvpEvent(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player){
			if (DisguiseAPI.isDisguised(e.getDamager())) {
				DisguiseAPI.undisguiseToAll(e.getDamager());
				((Player)e.getDamager()).sendMessage(ChatColor.RED + "You have been undisguised!");
			}
		}
	}

	@EventHandler
	public void potiondDrinkEvent(PlayerItemConsumeEvent e) {
		if (DisguiseAPI.isDisguised(e.getPlayer())) {
			e.getPlayer().sendMessage(
					ChatColor.RED + "Unable to use items while disguised");
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void potionSplashEvent(PotionSplashEvent e) {
		for (LivingEntity mob : e.getAffectedEntities()) {
			if (mob instanceof Player) {
				if (DisguiseAPI.isDisguised(mob)) {
					for (PotionEffect pe : e.getEntity().getEffects()) {
						if (pe.getType().equals(PotionEffectType.INVISIBILITY)) {
							e.setCancelled(true);
							return;
						}
					}
				}
			}
		}
	}

}
