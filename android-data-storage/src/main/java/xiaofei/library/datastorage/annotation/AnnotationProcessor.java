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
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Xiaofei on 16/3/25.
 *
 * Annotation processor.
 */
public class AnnotationProcessor {

    private Map<Class<?>, String> mClassIdMap;

    private Map<Class<?>, Member> mObjectIdMap;

    private static AnnotationProcessor sInstance = null;

    private AnnotationProcessor() {
        mClassIdMap = new HashMap<Class<?>, String>();
        mObjectIdMap = new HashMap<Class<?>, Member>();
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
