package net.jeqo.bloons.listeners;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.data.BalloonOwner;
import net.jeqo.bloons.data.BalloonMenu;
import net.jeqo.bloons.data.Utils;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MenuHandlers implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onClick(InventoryClickEvent event){
        if(!(event.getWhoClicked() instanceof Player)) return;
        Player p = (Player) event.getWhoClicked();
        if(!BalloonMenu.users.containsKey(p.getUniqueId())) return;
        event.setCancelled(true);
        BalloonMenu inv = BalloonMenu.users.get(p.getUniqueId());
        if(event.getCurrentItem() == null) return;
        if(event.getCurrentItem().getItemMeta() == null) return;
        event.setCancelled(true);

        if (event.getSlot() < 45) {
            event.setCancelled(true);
            Utils.removeBalloon(p, (BalloonOwner) Bloons.playerBalloons.get(p.getUniqueId()));
            Player player = (Player) event.getWhoClicked();
            String balloon = event.getCurrentItem().getItemMeta().getLocalizedName();
            Utils.checkBalloonRemovalOrAdd(player, balloon);
            player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
            String balloonName = event.getCurrentItem().getItemMeta().getDisplayName();
            player.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("equipped", balloonName));
            player.closeInventory();
        }

        if(event.getCurrentItem().getItemMeta().getDisplayName().equals(BalloonMenu.nextPageName)) {
            event.setCancelled(true);
            if (inv.currpage >= inv.pages.size()-1) {
                ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.BLOCK_BONE_BLOCK_BREAK, 1, 1);
            } else {
                inv.currpage += 1;
                p.openInventory(inv.pages.get(inv.currpage));
            }


        } else if(event.getCurrentItem().getItemMeta().getDisplayName().equals(BalloonMenu.previousPageName)) {
            event.setCancelled(true);
            if (inv.currpage > 0) {
                inv.currpage -= 1;
                p.openInventory(inv.pages.get(inv.currpage));
            } else {
                ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.BLOCK_BONE_BLOCK_BREAK, 1, 1);
            }

        } else if(event.getCurrentItem().getItemMeta().getDisplayName().equals(BalloonMenu.removeName)) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();

            BalloonOwner balloonOwner1 = (BalloonOwner) Bloons.playerBalloons.get(player.getUniqueId());
            if (balloonOwner1 == null) {
                player.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("not-equipped"));
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 1, 1);
            } else {
                player.closeInventory();
                player.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("unequipped"));
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 1, 1);
            }
            Utils.removeBalloon(player, balloonOwner1);
        } else {
            event.setCancelled(true);
        }
    }
}