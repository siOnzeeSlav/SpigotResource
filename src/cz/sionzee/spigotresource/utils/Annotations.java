/*
 * [SpigotResource]
 * Created by sionzee.
 * All rights reserved to siOnzee.cz 2015
 * Repository: https://github.com/siOnzeeSlav/SpigotResource
 */

package cz.sionzee.spigotresource.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;

public class Annotations {

    private static <Type> Type[] getAnnotationsByType(Annotation[] annotations, Class<?> annotationType) {
        int size = 0;
        for(Annotation annotation : annotations)
            if(annotation.annotationType() == annotationType)
                size++;

        Type[] result = (Type[]) java.lang.reflect.Array.newInstance(annotationType, size);

        int n = 0;
        for(Annotation annotation : annotations)
            if(annotation.annotationType() == annotationType)
                result[n++] = (Type) annotation;

        return result;
    }

    /**
     * Return [] of annotationType in class
     * @param clazz Where filter annotation
     * @param annotationType Which annotation
     * @return [] filtered of annotation
     */
    public static <Type> Type[] getAnnotationsByType(Class<?> clazz, Class<?> annotationType) {
        return getAnnotationsByType(clazz.getDeclaredAnnotations(), annotationType);
    }

    /**
     * Return [] of annotationType in class
     * @param method Where filter annotation
     * @param annotationType Which annotation
     * @return [] filtered of annotation
     */
    public static <Type> Type[] getAnnotationsByType(Method method, Class<?> annotationType) {
        return getAnnotationsByType(method.getDeclaredAnnotations(), annotationType);
    }

    /**
     * Return [] of annotationType in class
     * @param field Where filter annotation
     * @param annotationType Which annotation
     * @return [] filtered of annotation
     */
    public static <Type> Type[] getAnnotationsByType(Field field, Class<?> annotationType) {
        return getAnnotationsByType(field.getDeclaredAnnotations(), annotationType);
    }

}
