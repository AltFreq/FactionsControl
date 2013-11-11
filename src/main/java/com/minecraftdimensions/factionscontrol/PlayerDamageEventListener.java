package com.minecraftdimensions.factionscontrol;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.UPlayer;

public class PlayerDamageEventListener implements Listener {
	
	@EventHandler
	public void blockbreak(EntityDamageByEntityEvent e) {
		if(!(e.getDamager() instanceof Player)){
			return;
		}
		
		if(!(e.getEntity() instanceof Player)){
			return;
		}
		
		Player t = (Player) e.getEntity();
		if(t.getHealth()-e.getDamage()>0){
			return;
		}
		Player p = (Player) e.getDamager(); //attacker
		UPlayer ut = UPlayer.get(t);
		UPlayer up = UPlayer.get(p);
		Faction host = FactionManager.getFactionAt(p.getLocation().getBlock()); //attackers location
		if(host.equals(up.getFaction())){ //if location is the attackers faction
			return;
		}
		
		if(ut.getPower()<=1){
			damagePlayer(p);
			p.sendMessage(ChatColor.RED+"You have been hurt for camping a weak player outside of your territory");
			return;
		}
		
		
	}
	
	public static void damagePlayer(Player p){
		p.damage(4.0);
		for(PotionEffect pe:p.getActivePotionEffects()){
			if(pe.getType().equals(PotionEffectType.HEALTH_BOOST)){
				p.removePotionEffect(pe.getType());
			}else if(pe.getType().equals(PotionEffectType.INVISIBILITY)){
				p.removePotionEffect(pe.getType());
			}else if(pe.getType().equals(PotionEffectType.REGENERATION)){
				p.removePotionEffect(pe.getType());
			}else if(pe.getType().equals(PotionEffectType.ABSORPTION)){
				p.removePotionEffect(pe.getType());
			}
		}
	}

}
