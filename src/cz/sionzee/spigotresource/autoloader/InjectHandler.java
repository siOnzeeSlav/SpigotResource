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

    public static Object getInstance(Class<?> clazz) {
        if(entryMap.containsKey(clazz))
            return entryMap.get(clazz);

        try {
            register(clazz.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entryMap.get(clazz);
    }

    public static void load() {
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

   /*

    public static Map<Class<?>, List<InjectEntry>> getEntries() {
        HashMap<Class<?>, List<InjectEntry>> map = new HashMap<>();
        try {
            for(Class<?> clazz : Utils.getClassesFromNames(Utils.getClassNames())) {
                Object clazzInstance = null;
                for(Field field : clazz.getDeclaredFields()) {
                    Inject[] injects = Annotations.getAnnotationsByType(field, Inject.class);
                    if(injects.length > 0) {
                        Inject inject = injects[0];
                        Object injectInstance = null;
                        if(hasClassInstance(inject.value())) {
                            injectInstance = getClassInstance(inject.value());
                        } else {
                            Utils.throwAFakeException("Missing registration for service \"" + inject.value().getSimpleName() + "\"");
                        }
                        List<InjectEntry> entries = new ArrayList<>();
                        if(map.containsKey(clazz)) {
                            entries = map.get(clazz);
                        }

                        if(clazzInstance == null) {
                            clazzInstance = clazz.newInstance();
                        }

                        entries.add(new InjectEntry(field, injectInstance, clazzInstance));
                        map.put(clazz, entries);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.unmodifiableMap(map);
    }*/

    public static void register(Object instance) {
        System.out.println("Registering service: " + instance.getClass());
        entryMap.put(instance.getClass(), instance);
    }
}
