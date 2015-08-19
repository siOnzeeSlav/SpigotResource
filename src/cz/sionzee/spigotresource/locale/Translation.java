/*
 * [SpigotResource]
 * Created by sionzee.
 * All rights reserved to siOnzee.cz 2015
 * Repository: https://github.com/siOnzeeSlav/SpigotResource
 */

package cz.sionzee.spigotresource.locale;

import cz.sionzee.spigotresource.utils.Formater;

import java.util.HashMap;

//TODO UNFINISHED CLASS
public class Translation {

    private static HashMap<String, String> map = new HashMap<>();

    public static String get(Object instance, String key, Object... args) {
        return Formater.format(instance, map.get(key), args);
    }

}
