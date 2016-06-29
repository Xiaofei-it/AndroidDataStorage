/**
 *
 * Copyright 2015-2016 Xiaofei
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package xiaofei.library.datastorage.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Xiaofei on 16/3/25.
 *
 * Annotation processor.
 */
public class AnnotationProcessor {

    private final ConcurrentHashMap<Class<?>, String> mClassIdMap;

    private final ConcurrentHashMap<Class<?>, Member> mObjectIdMap;

    private static volatile AnnotationProcessor sInstance = null;

    private AnnotationProcessor() {
        mClassIdMap = new ConcurrentHashMap<Class<?>, String>();
        mObjectIdMap = new ConcurrentHashMap<Class<?>, Member>();
    }

    public static AnnotationProcessor getInstance() {
        if (sInstance == null) {
            synchronized (AnnotationProcessor.class) {
                if (sInstance == null) {
                    sInstance = new AnnotationProcessor();
                }
            }
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
            throw new IllegalArgumentException("Class cannot be null.");
        }
        String className = mClassIdMap.get(clazz);
        if (className != null) {
            return className;
        }
        ClassId classIdAnnotation = clazz.getAnnotation(ClassId.class);
        className = classIdAnnotation == null ? clazz.getName() : classIdAnnotation.value();
        mClassIdMap.put(clazz, className);
        return className;
    }

    public String getObjectId(Object object) {
        Class<?> clazz = object.getClass();
        Member member = mObjectIdMap.get(clazz);
        if (member != null) {
            return (String) member.getValue(object);
        } else {
            member = getObjectIdMember(clazz);
            if (member == null) {
                throw new IllegalArgumentException("Object does not have an object id. "
                        + "Please specify an object id when invoking method. "
                        + "Otherwise you can add @ObjectId on the field or the non-arg method which provides the object id.");
            }
            mObjectIdMap.put(clazz, member);
            return (String) member.getValue(object);
        }
    }

    private Member getObjectIdMember(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ObjectId.class) && field.getType() == String.class) {
                return new FieldMember(field);
            }
        }
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(ObjectId.class)
                    && method.getParameterTypes().length == 0
                    && method.getReturnType() == String.class) {
                return new MethodMember(method);
            }
        }
        return null;
    }
}
