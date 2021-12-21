package me.ikevoodoo.antihack;

import me.ikevoodoo.antihack.listeners.PlayerListener;
import net.md_5.bungee.api.plugin.Plugin;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.message.Message;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public final class Main extends Plugin {

    public static Main INSTANCE;

    private static final List<UUID> banned = new ArrayList<>();
    private static File bannedStorage;

    @Override
    public void onEnable() {
        addFilter();
        INSTANCE = this;
        getProxy().getPluginManager().registerListener(this, new PlayerListener());
        bannedStorage = new File("./antihack/bannedStorage");
        if(!bannedStorage.exists()) {
            try {
                bannedStorage.mkdirs();
                bannedStorage.delete();
                bannedStorage.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else readBans();
    }

    private static void writeBan(String s) {
        try(PrintWriter pw = new PrintWriter(new FileWriter(bannedStorage, true))) {
            pw.println(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void readBans() {
        readUUID(bannedStorage, banned);
    }

    public static void ban(UUID id) {
        writeBan(id.toString());
        banned.add(id);
    }

    public static boolean isBanned(UUID id) {
        return banned.contains(id);
    }

    private static UUID getUUID(String input) throws IOException {
        try {
            return UUID.fromString(input);
        } catch (Exception ignored) {}
        URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + input);
        try(BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
            String line;
            while((line = br.readLine()) != null) {
                if(line.isBlank()) continue;
                try {
                    line = line.replaceFirst(".*\"id\":\"(.*)\".*", "$1");
                    return UUID.fromString(line);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private static void readUUID(File input, List<UUID> storage) {
        try(BufferedReader br = new BufferedReader(new FileReader(input))) {
            String line;
            while((line = br.readLine()) != null) {
                if(line.isBlank()) continue;
                UUID id = getUUID(line);
                if(id != null) storage.add(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addFilter() {
        ((org.apache.logging.log4j.core.Logger) LogManager.getRootLogger()).addFilter(new Filter() {
            @Override
            public Result getOnMismatch() {
                return Result.NEUTRAL;
            }

            @Override
            public Result getOnMatch() {
                return Result.NEUTRAL;
            }

            @Override
            public Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
                return shouldDeny(msg) ? Result.DENY : Result.NEUTRAL;
            }

            @Override
            public Result filter(Logger logger, Level level, Marker marker, String message, Object p0) {
                return filter(logger, level, marker, message, p0, null, null, null, null, null, null, null, null, null, null);
            }

            @Override
            public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1) {
                return filter(logger, level, marker, message, p0, p1, null, null, null, null, null, null, null, null, null);
            }

            @Override
            public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2) {
                return filter(logger, level, marker, message, p0, p1, p2, null, null, null, null, null, null, null, null);
            }

            @Override
            public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
                return filter(logger, level, marker, message, p0, p1, p2, p3, null, null, null, null, null, null, null);
            }

            @Override
            public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
                return filter(logger, level, marker, message, p0, p1, p2, p3, p4, null, null, null, null, null, null, null, null);
            }

            @Override
            public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
                return filter(logger, level, marker, message, p0, p1, p2, p3, p4, p5, null, null, null, null, null);
            }

            @Override
            public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
                return filter(logger, level, marker, message, p0, p1, p2, p3, p4, p5, p6, null, null, null, null, null);
            }

            @Override
            public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
                return filter(logger, level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, null, null, null);
            }

            @Override
            public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
                return filter(logger, level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, null, null);
            }

            @Override
            public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
                return filter(logger, level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, null);
            }

            @Override
            public Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
                return filter(logger, level, marker, String.valueOf(msg));
            }

            @Override
            public Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
                return filter(logger, level, marker, String.valueOf(msg));
            }

            @Override
            public Result filter(LogEvent event) {
                return filter(null, event.getLevel(), event.getMarker(), event.getMessage().getFormattedMessage());
            }

            @Override
            public State getState() {
                return State.STARTED;
            }

            @Override
            public void initialize() {

            }

            @Override
            public void start() {

            }

            @Override
            public void stop() {

            }

            @Override
            public boolean isStarted() {
                return true;
            }

            @Override
            public boolean isStopped() {
                return false;
            }
        });
    }

    public static boolean shouldDeny(String message) {
        message = new String(message.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        message = URLDecoder.decode(message, StandardCharsets.UTF_8).toLowerCase(Locale.ROOT);
        message = message.toLowerCase().stripLeading().stripTrailing().stripIndent()
                .replaceAll("\s*", "")
                .replaceAll("\n", "")
                .replace("${}", "");
        return
                message.contains("jndi") || message.contains("jnd") || (
                        (message.contains("${")
                                && message.contains("}") || message.contains("${::-")));
    }
}
