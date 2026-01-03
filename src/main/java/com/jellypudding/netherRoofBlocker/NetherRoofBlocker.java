package com.jellypudding.netherRoofBlocker;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class NetherRoofBlocker extends JavaPlugin {

    private final Set<Material> unsafeBlocks = new HashSet<>();
    private NetherRoofListener roofChecker;
    private boolean blockingActive;

    private void loadConfigValues() {
        FileConfiguration config = getConfig();

        blockingActive = config.getBoolean("enabled", true);

        unsafeBlocks.clear();
        List<String> unsafeBlocksList = config.getStringList("unsafe-blocks");
        for (String blockName : unsafeBlocksList) {
            try {
                Material material = Material.valueOf(blockName.toUpperCase());
                unsafeBlocks.add(material);
            } catch (IllegalArgumentException e) {
                getLogger().warning("Invalid material name in config: " + blockName);
            }
        }
    }

    @Override
    public void onEnable() {
        // bstats
        int pluginId = 28683;
        new Metrics(this, pluginId);

        saveDefaultConfig();
        loadConfigValues();

        roofChecker = new NetherRoofListener(unsafeBlocks, this);
        roofChecker.start(this);

        getLogger().info("NetherRoofBlocker enabled. Ensure nether-ceiling-void-damage-height is set to 'disabled' in config/paper-world-defaults.yml");
    }

    @Override
    public void onDisable() {
        if (roofChecker != null) {
            roofChecker.cancel();
        }
        getLogger().info("NetherRoofBlocker disabled.");
    }

    public boolean isBlockingActive() {
        return blockingActive;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {
        if (!command.getName().equalsIgnoreCase("netherroofblocker")) {
            return false;
        }

        if (args.length == 0) {
            sender.sendMessage("Usage: /netherroofblocker <reload|toggle>");
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            reloadConfig();
            loadConfigValues();
            sender.sendMessage("§aNetherRoofBlocker configuration reloaded.");
            return true;
        }

        if (args[0].equalsIgnoreCase("toggle")) {
            blockingActive = !blockingActive;
            String status = blockingActive ? "§aenabled" : "§cdisabled";
            sender.sendMessage("§7NetherRoofBlocker is now " + status + "§7.");
            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String @NotNull [] args) {
        if (command.getName().equalsIgnoreCase("netherroofblocker") && args.length == 1) {
            List<String> completions = new ArrayList<>();
            String input = args[0].toLowerCase();
            if ("reload".startsWith(input)) {
                completions.add("reload");
            }
            if ("toggle".startsWith(input)) {
                completions.add("toggle");
            }
            return completions;
        }
        return new ArrayList<>();
    }
}
