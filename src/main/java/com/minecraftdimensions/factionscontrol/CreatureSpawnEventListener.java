package com.minecraftdimensions.factionscontrol;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

public class CreatureSpawnEventListener implements Listener {

	@EventHandler
	public void spawnerPlace(BlockPlaceEvent e){
		Block b = e.getBlockPlaced();
		if(!b.getType().equals(Material.MOB_SPAWNER)){
			return;
		}
		
		if(b.getWorld().getEnvironment().equals(World.Environment.NETHER)){
			Player p = e.getPlayer();
			int itemId = p.getInventory().getHeldItemSlot();
			ItemStack iStack = p.getInventory().getItem(itemId);
			short durability = iStack.getDurability();
			if(durability== 51){
				e.getPlayer().sendMessage( ChatColor.RED+ "You are unable to place skeleton spawners in the nether");
				e.setCancelled(true);
			}
			
		}
	}
	
	@EventHandler
	public void spawnerPlace(CreatureSpawnEvent e){
		if(!e.getSpawnReason().equals(SpawnReason.SPAWNER)){
			return;
		}
		
		if(!e.getLocation().getWorld().getEnvironment().equals(World.Environment.NETHER)){
			return;
		}
		
		if(e.getEntityType().equals(EntityType.SKELETON)){
			e.setCancelled(true);
		}
		
	}
	
	
	@EventHandler
	public void spawnerPlace(EntityDamageEvent e){
		if(!e.getCause().equals(DamageCause.FALL)){
			return;
		}
		if(!(e.getEntity() instanceof LivingEntity)){
			return;
		}
		
		if(e.getEntity() instanceof Player){
			return;
		}
		
		LivingEntity l = (LivingEntity) e.getEntity();
		if(l.getHealth()-e.getDamage()<=0){
			e.setCancelled(true);
		}
		
	}
	
	
}
