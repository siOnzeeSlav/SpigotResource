/*
 * [SpigotResource]
 * Created by sionzee.
 * All rights reserved to siOnzee.cz 2015
 * Repository: https://github.com/siOnzeeSlav/SpigotResource
 */

package cz.sionzee.spigotresource.events;

import org.bukkit.event.Event;

public class EventEntity {

    private Object classInstance;
    private Event event;

    public EventEntity(Object classInstance, Event event) {
        this.classInstance = classInstance;
        this.event = event;
    }

    public Object getClassInstance() {
        return classInstance;
    }

    public Event getEvent() {
        return event;
    }
}
