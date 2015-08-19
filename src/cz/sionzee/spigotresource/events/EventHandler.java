/*
 * [SpigotResource]
 * Created by sionzee.
 * All rights reserved to siOnzee.cz 2015
 * Repository: https://github.com/siOnzeeSlav/SpigotResource
 */

package cz.sionzee.spigotresource.events;

import cz.sionzee.spigotresource.utils.Utils;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;

import java.util.HashMap;

public class EventHandler {

    /**
     * Bukkit listened events
     * Listener cant be get from classInstance by casting
     *
     * @return HashMap<String, EventEntity> (getEvent() == null)
     */
    public static HashMap<String, EventEntity> getAllListeners() {
        HashMap<String, EventEntity> listeners = new HashMap<>();

        for(Class<?> clazz : Utils.getClassesFromNames(Utils.getClassNames())) {
            if(Listener.class.isAssignableFrom(clazz)) {
                try {
                    listeners.put(clazz.getSimpleName(), new EventEntity(clazz.newInstance(), null));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return listeners;
    }

    /**
     * Custom registered events
     *
     * @return HashMap<String, EventEntity>
     */
    public static HashMap<String, EventEntity> getAllEvents() {
        HashMap<String, EventEntity> events = new HashMap<>();

        for(Class<?> clazz : Utils.getClassesFromNames(Utils.getClassNames())) {
            if(Event.class.isAssignableFrom(clazz)) {
                try {
                    Object instance = clazz.newInstance();
                    events.put(clazz.getSimpleName(), new EventEntity(instance, (Event) instance));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return events;
    }
}
