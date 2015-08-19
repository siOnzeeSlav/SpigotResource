/*
 * [SpigotResource]
 * Created by sionzee.
 * All rights reserved to siOnzee.cz 2015
 * Repository: https://github.com/siOnzeeSlav/SpigotResource
 */

package cz.sionzee.spigotresource.utils;

public class Array {

    /**
     * Create new array and set index zero {index} of {array}
     * @param array Source
     * @param clazz Array Type? .class
     * @param index From which it start?
     * @param <T> Array Type?
     * @return new Array
     */
    public static <T> T[] startFrom(T[] array, Class<T> clazz, int index) {
        if (index >= array.length)
            try {
                throw new Exception("Index cannot be higher or equal to size of array");
            } catch (Exception e) {
                e.printStackTrace();
            }

        int size = array.length - index;
        T[] result = (T[]) java.lang.reflect.Array.newInstance(clazz, size);
        for (int i = 0; i < size; i++) {
            result[i] = array[index + i];
        }
        return result;
    }

    /**
     * Join array values by {join} (ignore last index)
     * @param array [The, Word]
     * @param join " ,.- "
     * @return (The ,.- Word)
     */
    public static String join(String[] array, String join) {
        String result = "";
        for(int i = 0; i < array.length; i++) {
            result += array[i] + (i == array.length? "": join);
        }
        return result;
    }
}
