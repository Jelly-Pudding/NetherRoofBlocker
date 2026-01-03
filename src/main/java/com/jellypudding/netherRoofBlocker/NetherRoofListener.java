package com.jellypudding.netherRoofBlocker;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;

public class NetherRoofListener extends BukkitRunnable {

    private static final int NETHER_ROOF_Y = 128;
    private static final int SEARCH_START_Y = 120;
    private static final int MAX_HORIZONTAL_SEARCH = 5;

    private final Set<Material> unsafeBlocks;
    private final NetherRoofBlocker plugin;

    public NetherRoofListener(Set<Material> unsafeBlocks, NetherRoofBlocker plugin) {
        this.unsafeBlocks = unsafeBlocks;
        this.plugin = plugin;
    }

    public void start(Plugin plugin) {
        this.runTaskTimer(plugin, 20L, 100L);
    }

    @Override
    public void run() {
        if (!plugin.isBlockingActive()) {
            return;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            World world = player.getWorld();

            if (world.getEnvironment() != World.Environment.NETHER) {
                continue;
            }

            if (player.getLocation().getY() < NETHER_ROOF_Y) {
                continue;
            }

            Location safeLocation = findSafeLocation(player.getLocation());
            if (safeLocation != null) {
                safeLocation.setYaw(player.getLocation().getYaw());
                safeLocation.setPitch(player.getLocation().getPitch());
                player.teleport(safeLocation);
                player.sendMessage("§cThe nether roof is disabled.");
            }
        }
    }

    private Location findSafeLocation(Location origin) {
        World world = origin.getWorld();
        int originX = origin.getBlockX();
        int originZ = origin.getBlockZ();

        for (int dx = 0; dx <= MAX_HORIZONTAL_SEARCH; dx++) {
            for (int dz = 0; dz <= MAX_HORIZONTAL_SEARCH; dz++) {
                int[] xOffsets = (dx == 0) ? new int[]{0} : new int[]{dx, -dx};
                int[] zOffsets = (dz == 0) ? new int[]{0} : new int[]{dz, -dz};

                for (int xOff : xOffsets) {
                    for (int zOff : zOffsets) {
                        Location loc = findSafeY(world, originX + xOff, originZ + zOff);
                        if (loc != null) {
                            return loc;
                        }
                    }
                }
            }
        }

        return world.getSpawnLocation();
    }

    private Location findSafeY(World world, int x, int z) {
        for (int y = SEARCH_START_Y; y > world.getMinHeight(); y--) {
            Block feet = world.getBlockAt(x, y, z);
            Block head = world.getBlockAt(x, y + 1, z);
            Block ground = world.getBlockAt(x, y - 1, z);

            if (isPassable(feet) && isPassable(head) && isSafeToStandOn(ground)) {
                return new Location(world, x + 0.5, y, z + 0.5);
            }
        }
        return null;
    }

    private boolean isPassable(Block block) {
        Material type = block.getType();
        if (type.isAir()) {
            return true;
        }
        return !type.isSolid() && !unsafeBlocks.contains(type);
    }

    private boolean isSafeToStandOn(Block block) {
        Material type = block.getType();
        return type.isSolid() && !unsafeBlocks.contains(type);
    }
}
