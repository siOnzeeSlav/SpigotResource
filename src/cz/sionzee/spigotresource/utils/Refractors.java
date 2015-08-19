/*
 * [SpigotResource]
 * Created by sionzee.
 * All rights reserved to siOnzee.cz 2015
 * Repository: https://github.com/siOnzeeSlav/SpigotResource
 */

package cz.sionzee.spigotresource.utils;

public class Refractors {
    /**
     * Return correct StackTraceElement for Class instance
     *
     * @param instance Class Instance
     * @return StackTraceElement
     */
    public static StackTraceElement getStackTrace(StackTraceElement[] arr, Object instance) {
        if (arr == null)
            for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
                if (ste.getClassName().equals(instance.getClass().getName())) {
                    return ste;
                }
            }
        else
            for (StackTraceElement ste : arr) {
                if (ste.getClassName().equals(instance.getClass().getName())) {
                    return ste;
                }
            }
        return null;
    }

    public static String getCallerClassName() {
        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        for (int i = 1; i < stElements.length; i++) {
            StackTraceElement ste = stElements[i];
            if (ste.getClassName().indexOf("java.lang.Thread") != 0) {
                return ste.getClassName();
            }
        }
        return null;
    }
}
