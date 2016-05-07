package xiaofei.library.datastorage.annotation;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Xiaofei on 16/3/25.
 *
 * Annotation processor.
 */
public class AnnotationProcessor {

    private Map<Class<?>, String> mClassIdMap;

    private Map<Class<?>, Field> mObjectIdMap;

    private static AnnotationProcessor sInstance = null;

    private AnnotationProcessor() {
        mClassIdMap = new HashMap<Class<?>, String>();
        mObjectIdMap = new HashMap<Class<?>, Field>();
    }

    public static synchronized AnnotationProcessor getInstance() {
        if (sInstance == null) {
            sInstance = new AnnotationProcessor();
        }
        return sInstance;
    }

    /**
     * Return the class id of the specified class.
     * If an annotation ClassId is present on the class, the value of the annotation is returned.
     * Otherwise, the class name is returned.
     * @param clazz
     * @return
     */
    public String getClassId(Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException();
        }
        synchronized (mClassIdMap) {
            String className = mClassIdMap.get(clazz);
            if (className != null) {
                return className;
            }
            ClassId classIdAnnotation = clazz.getAnnotation(ClassId.class);
            className = classIdAnnotation == null ? clazz.getName() : classIdAnnotation.value();
            mClassIdMap.put(clazz, className);
            return className;
        }
    }

    public String getObjectId(Object object) {
        if (object == null) {
            throw new IllegalArgumentException();
        }
        Class<?> clazz = object.getClass();
        synchronized (mObjectIdMap) {
            Field field = mObjectIdMap.get(clazz);
            if (field != null) {
                try {
                    return (String) field.get(object);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                field = getObjectIdField(clazz);
                if (field == null) {
                    throw new RuntimeException("Object does not have an object id.");
                }
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                mObjectIdMap.put(clazz, field);
                try {
                    return (String) field.get(object);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        throw new RuntimeException("Object does not have an object id.");
    }

    private Field getObjectIdField(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ObjectId.class) && field.getType() == String.class) {
                return field;
            }
        }
        return null;
    }
}
