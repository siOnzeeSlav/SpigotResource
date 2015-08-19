/*
 * [SpigotResource]
 * Created by sionzee.
 * All rights reserved to siOnzee.cz 2015
 * Repository: https://github.com/siOnzeeSlav/SpigotResource
 */

package cz.sionzee.spigotresource.utils;

import java.util.List;

public class Lists {

    public static <T> T[] toArray(List<T> list, Class<T> clazz) {
        T[] array = (T[]) java.lang.reflect.Array.newInstance(clazz, list.size());
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }

}
