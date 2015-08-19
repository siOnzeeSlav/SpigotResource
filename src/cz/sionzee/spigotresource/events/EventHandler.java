/*
 * [SpigotResource]
 * Created by sionzee.
 * All rights reserved to siOnzee.cz 2015
 * Repository: https://github.com/siOnzeeSlav/SpigotResource
 */

package cz.sionzee.spigotresource.events;

import cz.sionzee.spigotresource.SpigotPlugin;
import cz.sionzee.spigotresource.utils.Utils;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.util.HashMap;

public class EventHandler {

    /**
     * Bukkit listened events
     * Listener cant be get from classInstance by casting
     *
     * @return HashMap<String, EventEntity> (getEvent() == null)
     */
    public static HashMap<String, EventEntity> getAllListeners(SpigotPlugin spigotPlugin) {
        HashMap<String, EventEntity> listeners = new HashMap<>();

        for(Class<?> clazz : Utils.getClassesFromNames(Utils.getClassNames())) {
            if (Listener.class.isAssignableFrom(clazz)) {
                Object instance = null;
                if (clazz.getDeclaredConstructors().length > 0)
                    for (Constructor c : clazz.getDeclaredConstructors())
                        if (c.getParameterTypes().length > 0)
                            if (JavaPlugin.class.isAssignableFrom(c.getParameterTypes()[0]))
                                try {
                                    instance = c.newInstance(spigotPlugin);
                                    break;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                try {
                    if (instance == null)
                        instance = clazz.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                listeners.put(clazz.getSimpleName(), new EventEntity(instance, null));
            }
        }
        return listeners;
    }

    /**
     * Custom registered events
     *
     * @return HashMap<String, EventEntity>
     */
    public static HashMap<String, EventEntity> getAllEvents(SpigotPlugin spigotPlugin) {
        HashMap<String, EventEntity> events = new HashMap<>();

        for(Class<?> clazz : Utils.getClassesFromNames(Utils.getClassNames())) {
            if(Event.class.isAssignableFrom(clazz)) {
                Object instance = null;
                if(clazz.getDeclaredConstructors().length > 0)
                    for (Constructor c : clazz.getDeclaredConstructors())
                        if (c.getParameterTypes().length > 0)
                            if (JavaPlugin.class.isAssignableFrom(c.getParameterTypes()[0]))
                                try {
                                    instance = c.newInstance(spigotPlugin);
                                    break;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                try {
                    if (instance == null)
                        instance = clazz.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                events.put(clazz.getSimpleName(), new EventEntity(instance, (Event) instance));
            }
        }

        return events;
    }
}
