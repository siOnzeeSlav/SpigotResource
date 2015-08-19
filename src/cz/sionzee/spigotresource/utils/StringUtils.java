/*
 * [SpigotResource]
 * Created by sionzee.
 * All rights reserved to siOnzee.cz 2015
 * Repository: https://github.com/siOnzeeSlav/SpigotResource
 */

package cz.sionzee.spigotresource.utils;

public class StringUtils {

    public static int countChar(String a, char need) {
        int count = 0;
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) == need) {
                count++;
            }
        }
        return count;
    }
}
