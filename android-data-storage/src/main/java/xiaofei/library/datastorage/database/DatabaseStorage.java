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

package xiaofei.library.datastorage.database;

import android.content.Context;
import android.support.v4.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import xiaofei.library.datastorage.IDataStorage;
import xiaofei.library.datastorage.annotation.AnnotationProcessor;
import xiaofei.library.datastorage.util.Condition;

/**
 * Created by Xiaofei on 16/3/24.
 *
 * An implementation of the interface IDataStorage.
 *
 * DatabaseStorage stores data in an instance of the DbCache class. It is thread-safe, which is
 * guaranteed by the thread safety of DbCache.
 *
 * The methods within this class simply invoke the corresponding methods of the DbCache instance.
 * They seldom check the correctness of arguments, which will be checked in the invocations of the
 * corresponding methods.
 *
 * Developers should use DataStorageFactory to obtain DatabaseStorage.
 *
 */
public class DatabaseStorage implements IDataStorage {

    private static volatile DatabaseStorage sInstance = null;

    private DbCache mCache;

    private AnnotationProcessor mAnnotationProcessor;

    private DatabaseStorage(Context context) {
        mCache = DbCache.getInstance(context);
        mAnnotationProcessor = AnnotationProcessor.getInstance();
    }

    public static DatabaseStorage getInstance(Context context) {
        if (sInstance == null) {
            synchronized (DatabaseStorage.class) {
                if (sInstance == null) {
                    sInstance = new DatabaseStorage(context);
                }
            }
        }
        return sInstance;
    }

    @Override
    public <T> boolean contains(T element) {
        String id = mAnnotationProcessor.getObjectId(element);
        return contains(element.getClass(), id);
    }

    @Override
    public <T> void storeOrUpdate(T element) {
        String id = mAnnotationProcessor.getObjectId(element);
        storeOrUpdate(element, id);
    }

    @Override
    public <T> void storeOrUpdate(List<T> list, List<String> ids) {
        mCache.insertObjects(list, ids);
    }

    @Override
    public <T> List<T> load(Class<T> clazz, final List<String> ids) {
        return load(clazz, ids, null);
    }

    @Override
    public <T> List<T> load(Class<T> clazz, final List<String> ids, Comparator<T> comparator) {
        List<Pair<String, T>> list = mCache.getObjects(clazz, new Condition<T>() {
            @Override
            public boolean satisfy(T o) {
                String id = mAnnotationProcessor.getObjectId(o);
                return ids.contains(id);
            }
        });
        List<T> result = new ArrayList<T>();
        for (Pair<String, T> pair : list) {
            result.add(pair.second);
        }
        if (comparator != null) {
            Collections.sort(result, comparator);
        }
        return result;
    }

    @Override
    public <T> void storeOrUpdate(T element, String id) {
        mCache.insertObject(element, id);
    }

    @Override
    public <T> void storeOrUpdate(List<T> list) {
        List<String> ids = new ArrayList<String>();
        for (T element : list) {
            String id = mAnnotationProcessor.getObjectId(element);
            if (id == null) {
                throw new IllegalArgumentException("Element " + element + " has not initialized its ID.");
            }
            ids.add(id);
        }
        storeOrUpdate(list, ids);
    }

    @Override
    public <T> T load(Class<T> clazz, String id) {
        return mCache.getObject(clazz, id);
    }

    @Override
    public <T> List<T> loadAll(Class<T> clazz) {
        return loadAll(clazz, null);
    }

    @Override
    public <T> List<T> loadAll(Class<T> clazz, Comparator<T> comparator) {
        List<Pair<String, T>> list = mCache.getAllObjects(clazz);
        List<T> result = new ArrayList<T>();
        for (Pair<String, T> pair : list) {
            result.add(pair.second);
        }
        if (comparator != null) {
            Collections.sort(result, comparator);
        }
        return result;
    }

    @Override
    public <T> List<T> load(Class<T> clazz, Condition<T> condition) {
        return load(clazz, condition, null);
    }

    @Override
    public <T> List<T> load(Class<T> clazz, Condition<T> condition, Comparator<T> comparator) {
        List<Pair<String, T>> list = mCache.getObjects(clazz, condition);
        List<T> result = new ArrayList<T>();
        for (Pair<String, T> pair : list) {
            result.add(pair.second);
        }
        if (comparator != null) {
            Collections.sort(result, comparator);
        }
        return result;
    }

    @Override
    public <T> void delete(Class<T> clazz, String id) {
        mCache.deleteObject(clazz, id);
    }

    @Override
    public <T> void deleteAll(Class<T> clazz) {
        mCache.deleteAllObjects(clazz);
    }

    @Override
    public <T> boolean contains(Class<T> clazz, String id) {
        return mCache.containsObject(clazz, id);
    }

    @Override
    public <T> void delete(T element) {
        String id = mAnnotationProcessor.getObjectId(element);
        mCache.deleteObject(element.getClass(), id);
    }

    @Override
    public <T> void delete(Class<T> clazz, List<String> ids) {
        mCache.deleteObjects(clazz, ids);
    }

    @Override
    public <T> void delete(List<T> list) {
        if (list.isEmpty()) {
            return;
        }
        List<String> ids = new ArrayList<String>();
        Class<?> clazz = list.get(0).getClass();
        for (T element : list) {
            if (element == null || element.getClass() != clazz) {
                throw new IllegalArgumentException("Element " + element + " has the different type from others.");
            }
            ids.add(mAnnotationProcessor.getObjectId(element));
        }
        delete(clazz, ids);
    }

    @Override
    public <T> void delete(Class<T> clazz, Condition<T> condition) {
        mCache.deleteObjects(clazz, condition);
    }

    @Override
    public void clear() {
        mCache.clearTable();
    }
}
//TODO multi-process acess