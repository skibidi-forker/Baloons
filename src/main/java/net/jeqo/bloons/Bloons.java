package net.jeqo.bloons;

import net.jeqo.bloons.data.BalloonCommand;
import net.jeqo.bloons.data.BalloonOwner;
import net.jeqo.bloons.data.BalloonTab;
import net.jeqo.bloons.data.Utils;
import net.jeqo.bloons.listeners.MenuHandlers;
import net.jeqo.bloons.listeners.PlayerQuit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public final class Bloons extends JavaPlugin {

    public static HashMap<UUID, BalloonOwner> playerBalloons = new HashMap<>();
    private static Bloons instance;

    @Override
    public void onEnable() {
        Utils.log("|---[ BLOONS ]-------------------------------------------------------|");
        Utils.log("|                           Plugin loaded.                           |");
        Utils.log("|-------------------------------------------------[ MADE BY JEQO ]---|");

        instance = this;
        loadCommands();
        loadListeners();

        getConfig().options().copyDefaults();
        saveDefaultConfig();
    }


    @Override
    public void onDisable() {

        Utils.log("|---[ BLOONS ]-------------------------------------------------------|");
        Utils.log("|                          Shutting down...                          |");
        Utils.log("|-------------------------------------------------[ MADE BY JEQO ]---|");

        for (BalloonOwner owner : playerBalloons.values()) {
            owner.cancel();
        }

        HandlerList.unregisterAll((Plugin)this);
    }

















    private void loadListeners() {
        getServer().getPluginManager().registerEvents((Listener)new PlayerQuit(), (Plugin)this);
        getServer().getPluginManager().registerEvents((Listener)new MenuHandlers(), (Plugin)this);
    }

    private void loadCommands() {
        Objects.requireNonNull(getCommand("bloons")).setExecutor(new BalloonCommand());
        TabCompleter tc = new BalloonTab();
        Objects.requireNonNull(this.getCommand("bloons")).setTabCompleter(tc);
    }

    public static String getMessage(String id, String arg) {
        return Utils.hex(String.format(getInstance().getConfig().getString("messages." + id, ""), arg));
    }

    public static String getMessage(String id) {
        return Utils.hex(getInstance().getConfig().getString("messages." + id, ""));
    }

    public static Bloons getInstance() {
        return instance;
    }

    public static String getString(String path) {
        return getInstance().getConfig().getString(path);
    }


    public static Integer getInt(String path) {
        return getInstance().getConfig().getInt(path);
    }
}