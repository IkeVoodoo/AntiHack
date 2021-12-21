package me.ikevoodoo.antihack.listeners;

import me.ikevoodoo.antihack.Main;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.Locale;

public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void on(ChatEvent event) {
        if(!(event.getSender() instanceof ProxiedPlayer)) return;
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();

        if(Main.shouldDeny(event.getMessage())) {
            event.setCancelled(true);
            event.setMessage("");
            Main.ban(player.getUniqueId());
            player.disconnect(TextComponent.fromLegacyText("&cYou have been banned for trying to hack the server."));
            for(ProxiedPlayer p : Main.INSTANCE.getProxy().getPlayers()) {
                if(p.hasPermission("antihack.notify")) {
                    p.sendMessage(TextComponent.fromLegacyText("&c" + player.getName() + " has been banned for trying to hack the server."));
                }
            }
        }
    }

    @EventHandler
    public void on(PostLoginEvent event) {
        if(Main.isBanned(event.getPlayer().getUniqueId())) {
            event.getPlayer().disconnect(TextComponent.fromLegacyText("&cYou have been banned for trying to hack the server."));
        }
    }

}
