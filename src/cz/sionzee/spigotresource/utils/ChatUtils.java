/*
 * [SpigotResource]
 * Created by sionzee.
 * All rights reserved to siOnzee.cz 2015
 * Repository: https://github.com/siOnzeeSlav/SpigotResource
 */

package cz.sionzee.spigotresource.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

//TODO UNFINISHED CLASS
public class ChatUtils {

    public static final Pattern REGEX_URL = Pattern.compile("\\[url=(.+?)\\](.+?)\\[\\/url\\]");

    private static class UrlEntry {
        private String url;
        private String name;

        public UrlEntry(String url, String name) {
            this.url = url;
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public String getName() {
            return name;
        }

        public String getJson() {
            JsonObject object = new JsonObject();
            object.addProperty("text", getName());

            JsonObject event = new JsonObject();
            event.addProperty("action", "open_url");
            event.addProperty("value", getUrl());
            object.add("clickEvent", event);

            return new Gson().toJson(object);
        }
    }

    private static class TextEntry {
        private String text, color;
        private boolean bold, italic, underlined, strikethrough, obfuscated;

        public TextEntry(String text, String color, boolean bold, boolean italic, boolean underlined, boolean strikethrough, boolean obfuscated) {
            this.text = text;
            this.color = color;
            this.bold = bold;
            this.italic = italic;
            this.underlined = underlined;
            this.strikethrough = strikethrough;
            this.obfuscated = obfuscated;
        }

        public String getText() {
            return text;
        }

        public String getColor() {
            return color;
        }

        public boolean isBold() {
            return bold;
        }

        public boolean isItalic() {
            return italic;
        }

        public boolean isUnderlined() {
            return underlined;
        }

        public boolean isStrikethrough() {
            return strikethrough;
        }

        public boolean isObfuscated() {
            return obfuscated;
        }
    }

    public static void sendPlayerMessage(Player player, String message) {
        HashMap<String, UrlEntry> urls = new HashMap<>();

        //JsonObject component = new JsonObject();
        String result = message;
        while(Regex.isMatch(result, REGEX_URL)) {
            String[] groups = Regex.getGroups(message, REGEX_URL);
            String url = groups[0];
            String name = groups[1];
            urls.put("\\{URL_" + (urls.size() + 1) + "\\}", new UrlEntry(url, name));
            result = Regex.replace(result, REGEX_URL, "\\{URL_" + (urls.size() + 1) + "\\}");
        }



        for(Map.Entry<String, UrlEntry> entry : urls.entrySet()) {
            result = result.replace(entry.getKey(), entry.getValue().getJson());
        }

        //component.addProperty("text", "test");

        IChatBaseComponent comp = IChatBaseComponent.ChatSerializer.a(result);
        Bukkit.broadcastMessage("Sending: " + comp.getText());
        PacketPlayOutChat packet = new PacketPlayOutChat(comp, (byte) 0);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

}
