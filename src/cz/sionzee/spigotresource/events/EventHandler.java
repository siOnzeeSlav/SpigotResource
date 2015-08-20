/*
 * [SpigotResource]
 * Created by sionzee.
 * All rights reserved to siOnzee.cz 2015
 * Repository: https://github.com/siOnzeeSlav/SpigotResource
 */

package cz.sionzee.spigotresource.events;

import cz.sionzee.spigotresource.SpigotPlugin;
import cz.sionzee.spigotresource.autoloader.Inject;
import cz.sionzee.spigotresource.autoloader.InjectHandler;
import cz.sionzee.spigotresource.utils.Utils;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class EventHandler {

    /**
     * Bukkit listened events
     * Listener cant be get from classInstance by casting
     *
     * @return HashMap<String, EventEntity> (getEvent() == null)
     */
    public static Map<String, EventEntity> getAllListeners(InjectHandler injectHandler) {
        HashMap<String, EventEntity> listeners = new HashMap<>();

        for(Class<?> clazz : Utils.getClassesFromNames(Utils.getClassNames())) {
            if (Listener.class.isAssignableFrom(clazz)) {
                Object instance = injectHandler.getInstance(clazz);
                listeners.put(clazz.getSimpleName(), new EventEntity(instance, null));
            }
        }
        return Collections.unmodifiableMap(listeners);
    }
}
