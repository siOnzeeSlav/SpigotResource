/*
 * [SpigotResource]
 * Created by sionzee.
 * All rights reserved to siOnzee.cz 2015
 * Repository: https://github.com/siOnzeeSlav/SpigotResource
 */

package cz.sionzee.spigotresource.utils;

public class Array {

    public static <T> T[] startFrom(T[] array, Class<T> clazz, int index) throws Exception {
        if (index >= array.length)
            throw new Exception("Index cannot be higher or equal to size of array");

        int size = array.length - index;
        T[] result = (T[]) java.lang.reflect.Array.newInstance(clazz, size);
        for (int i = 0; i < size; i++) {
            result[i] = array[index + i];
            //System.out.println("[" + i + "] = " + result[i]);
        }
        return result;
    }
}
