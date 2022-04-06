package fr.caranouga.imagicblasters;

import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.List;

public final class Imagicblasters extends JavaPlugin implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e){
        if(e.getAction() == Action.RIGHT_CLICK_AIR){
            if(e.getItem().getType() == Material.NETHERITE_HOE) {
                Location pLoc = e.getPlayer().getLocation();
                Location loc = pLoc.add(0, 1, 0);
                ArmorStand as = (ArmorStand) e.getPlayer().getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
                as.setVisible(false);
                as.setGravity(false);
                as.setArms(false);
                as.setSmall(true);
                as.setBasePlate(false);
                as.setInvulnerable(true);
                as.setCustomName("ray");
                as.setMetadata("ply", new FixedMetadataValue(this, e.getPlayer().getName()));
            }
        }
    }

    @Override
    public void onEnable() {
        Bukkit.getLogger().info(ChatColor.GREEN + "Enabled " + this.getName());
        getServer().getPluginManager().registerEvents(this, this);
        // Plugin startup logic
        BukkitScheduler scheduler_ = getServer().getScheduler();
        scheduler_.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < getServer().getWorlds().size(); i++){
                    World world = getServer().getWorlds().get(i);
                    for(int j = 0; j < getServer().getWorld(world.getUID()).getEntities().size(); j++){
                        Entity entity = getServer().getWorld(world.getUID()).getEntities().get(j);
                        if(entity instanceof ArmorStand) {
                            ArmorStand as = (ArmorStand) entity;
                            if (as.getCustomName() != null && as.getCustomName().equals("ray")) {
                                Location asLoc = as.getLocation();
                                Location loc = asLoc.add(as.getEyeLocation().getDirection().getX(), as.getEyeLocation().getDirection().getY(), as.getEyeLocation().getDirection().getZ());
                                as.teleport(loc);
                                as.getWorld().spawnParticle(Particle.CRIT, as.getLocation(), 5, 0.1, 0.1, 0.1);

                                if (as.getTicksLived() >= 50) {
                                    as.remove();
                                }

                                List<Entity> entities = as.getNearbyEntities( .1,.1,.1);

                                for (Entity ent : entities) {
                                    if (ent instanceof ArmorStand) {
                                        ArmorStand as_ = (ArmorStand) ent;
                                        if (as_.getCustomName() != null && as_.getCustomName().equals("target")) {
                                            Player ply = Bukkit.getServer().getPlayer(as.getMetadata("ply").get(0).asString());
                                            ply.sendMessage("Touché");
                                            as.remove();
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }, 0L, 1L);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getLogger().info(ChatColor.RED + "Disabled " + this.getName());
    }
}
