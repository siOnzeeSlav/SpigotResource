/*
 * [SpigotResource]
 * Created by sionzee.
 * All rights reserved to siOnzee.cz 2015
 * Repository: https://github.com/siOnzeeSlav/SpigotResource
 */

package cz.sionzee.spigotresource.autoloader;

import java.lang.reflect.Field;

public class InjectEntry {
    private Field field;
    private Object value;
    private Object instance;

    public InjectEntry(Field field, Object value, Object instance) {
        this.field = field;
        this.value = value;
        this.instance = instance;
    }

    public Object getValue() {
        return value;
    }

    public Object getInstance() {
        return instance;
    }

    public Field getField() {
        return field;
    }
}
