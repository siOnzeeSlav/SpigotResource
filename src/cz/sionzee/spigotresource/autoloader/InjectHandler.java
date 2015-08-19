/*
 * [SpigotResource]
 * Created by sionzee.
 * All rights reserved to siOnzee.cz 2015
 * Repository: https://github.com/siOnzeeSlav/SpigotResource
 */

package cz.sionzee.spigotresource.autoloader;

import cz.sionzee.spigotresource.utils.Annotations;
import cz.sionzee.spigotresource.utils.Utils;

import java.lang.reflect.Field;
import java.util.*;

public class InjectHandler {
    private static HashMap<Class<?>, Object> entryMap = new HashMap<>();

    public Object getInstance(Class<?> clazz) {
        if(entryMap.containsKey(clazz))
            return entryMap.get(clazz);

        try {
            register(clazz.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entryMap.get(clazz);
    }

    public void load() {
        for(Class<?> clazz : Utils.getClassesFromNames(Utils.getClassNames())) {
            for (Field field : clazz.getDeclaredFields()) {
                Inject[] injects = Annotations.getAnnotationsByType(field, Inject.class);
                if(injects.length > 0) {
                    if(!entryMap.containsKey(clazz))
                        try {
                            register(clazz.newInstance());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    Inject inject = injects[0];
                    try {
                        field.setAccessible(true);
                        field.set(entryMap.get(clazz), entryMap.get(inject.value()));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void register(Object instance) {
        entryMap.put(instance.getClass(), instance);
    }
}
