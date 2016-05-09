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
import android.database.sqlite.SQLiteFullException;
import android.support.v4.util.Pair;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import xiaofei.library.datastorage.annotation.AnnotationProcessor;
import xiaofei.library.datastorage.util.Condition;

/**
 * Created by Xiaofei on 15/11/9.
 *
 * This class is used as the database cache. It is thread-safe.
 *
 * In this framework, the DatabaseStorage class is an implementation of the IDataStorage interface.
 *
 * In the DatabaseStorage class, the storage has two level, a high level which is the memory cache
 * and a low level which is the database file. A loading operation first searches the high level
 * for the specified data. If the search is successful, the loading operation succeeds. Otherwise,
 * the low level loads the specified data from the database file (if it exists), and the high level
 * stores it in the memory cache and return it as the search result. Any following loading operation
 * of the same data obtains the data from the memory cache and thus avoids the heavy-overhead
 * operations of database.
 *
 * Each operation is performed both in the high level and the low level. The memory cache has an
 * excellent performance, so the effect of the operation is immediate. The overhead of database
 * operation is heavy, so the operation is performed asynchronously. To avoiding any exception
 * when operating the database, this class uses a single-thread executor to perform database
 * operations sequentially.
 *
 * Before any operation, a sync operation is performed to check whether the high level has the data
 * of the specified class, and if it does not, the low level simply load all the data of the specified
 * class from the database.
 *
 */
public class DbCache implements IDbOperation {

    private static DbCache sInstance = null;

    private final ExecutorService mExecutorService;

    private DbService mDatabase;

    private AnnotationProcessor mAnnotationProcessor;

    private final Map<String, Map<String, Object>> mCache;

    private DbCache(Context context) {
        mExecutorService = Executors.newSingleThreadExecutor();
        mCache = new HashMap<String, Map<String, Object>>();
        try {
            mDatabase = DbService.getInstance(context);
        } catch (RuntimeException e) {
            e.printStackTrace();
            mDatabase = null;
        }
        mAnnotationProcessor = AnnotationProcessor.getInstance();
    }

    static synchronized DbCache getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DbCache(context);
        }
        return sInstance;
    }

    private void sync(Class clazz) {
        synchronized (mCache) {
            String className = mAnnotationProcessor.getClassId(clazz);
            if (!mCache.containsKey(className)) {
                //TODO 此处可能会出现数据库不同步，暂时先不考虑
                List<Pair<String, Object>> list = mDatabase == null ? new ArrayList<Pair<String, Object>>() : mDatabase.getAllObjects(clazz);
                Map<String, Object> map = new HashMap<String, Object>();
                for (Pair<String, Object> pair : list) {
                    map.put(pair.first, pair.second);
                }
                mCache.put(className, map);
            }
        }
    }

    @Override
    public void close() {
        operateDb(new Runnable() {
            @Override
            public void run() {
                mDatabase.close();
            }
        });
    }

    private void operateDb(Runnable runnable) {
        operateDb(runnable, true);
    }

    private void operateDb(final Runnable runnable, final boolean needHandleException) {
        if (mDatabase == null) {
            return;
        }
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } catch (SQLiteFullException exception) {
                    if (needHandleException) {
                        // If the database is full, simply delete all the data in the database
                        // and store the data in the memory cache into the database.
                        updateAllObjects();
                    }
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void deleteObject(final Class clazz, final String objectId) {
        if (clazz == null || TextUtils.isEmpty(objectId)) {
            throw new IllegalArgumentException();
        }
        sync(clazz);
        synchronized (mCache) {
            Map<String, Object> map = mCache.get(mAnnotationProcessor.getClassId(clazz));
            if (map.containsKey(objectId)) {
                map.remove(objectId);
                operateDb(new Runnable() {
                    @Override
                    public void run() {
                        mDatabase.deleteObject(clazz, objectId);
                    }
                });
            }
        }
    }

    @Override
    public <T> void deleteAllObjects(final Class<T> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException();
        }
        sync(clazz);
        synchronized (mCache) {
            mCache.remove(mAnnotationProcessor.getClassId(clazz));
            operateDb(new Runnable() {
                @Override
                public void run() {
                    mDatabase.deleteAllObjects(clazz);
                }
            });
        }
    }

    @Override
    public <T> void deleteObjects(final Class<T> clazz, final List<String> objectIds) {
        if (clazz == null || objectIds == null) {
            throw new IllegalArgumentException();
        }
        sync(clazz);
        synchronized (mCache) {
            Map<String, Object> map = mCache.get(mAnnotationProcessor.getClassId(clazz));
            for (String id : objectIds) {
                if (id != null) {
                    map.remove(id);
                }
            }
            operateDb(new Runnable() {
                @Override
                public void run() {
                    mDatabase.deleteObjects(clazz, objectIds);
                }
            });
        }
    }

    public <T> void deleteObjects(Class<T> clazz, Condition<T> condition) {
        if (clazz == null) {
            throw new IllegalArgumentException();
        }
        sync(clazz);
        synchronized (mCache) {
            Map<String, Object> map = mCache.get(mAnnotationProcessor.getClassId(clazz));
            Collection<Object> objects = map.values();
            List<String> ids = new ArrayList<>();
            for (Object object : objects) {
                if (object == null) {
                    continue;
                }
                if (condition == null || condition.satisfy((T) object)) {
                    ids.add(mAnnotationProcessor.getObjectId(object));
                }
            }
            if (!ids.isEmpty()) {
                deleteObjects(clazz, ids);
            }
        }
    }

    @Override
    public boolean containsObject(Class clazz, String objectId) {
        if (clazz == null || TextUtils.isEmpty(objectId)) {
            throw new IllegalArgumentException();
        }
        sync(clazz);
        synchronized (mCache) {
            Map<String, Object> map = mCache.get(mAnnotationProcessor.getClassId(clazz));
            return map.containsKey(objectId);
        }
    }

    @Override
    public <T> void insertObject(final T object, final String objectId) {
        if (object == null || TextUtils.isEmpty(objectId)) {
            throw new IllegalArgumentException();
        }
        sync(object.getClass());
        synchronized (mCache) {
            mCache.get(mAnnotationProcessor.getClassId(object.getClass())).put(objectId, object);
            operateDb(new Runnable() {
                @Override
                public void run() {
                    mDatabase.insertObject(object, objectId);
                }
            });
        }
    }

    @Override
    public <T> void insertObjects(final List<T> objects, final List<String> objectIds) {
        if (objects == null || objectIds == null || objects.size() != objectIds.size()) {
            throw new IllegalArgumentException();
        }
        if (objects.isEmpty()) {
            return;
        }
        //TODO
        Class<T> clazz = (Class<T>) objects.get(0).getClass();
        sync(clazz);
        synchronized (mCache) {
            String className = mAnnotationProcessor.getClassId(clazz);
            Map<String, Object> map = mCache.get(className);
            int size = objects.size();
            for (int i = 0; i < size; ++i) {
                String id = objectIds.get(i);
                T value = objects.get(i);
                if (value == null || TextUtils.isEmpty(id)) {
                    throw new IllegalArgumentException();
                }
                if (value.getClass() != clazz) {
                    throw new IllegalArgumentException();
                }
                map.put(id, value);
            }
            operateDb(new Runnable() {
                @Override
                public void run() {
                    mDatabase.insertObjects(objects, objectIds);
                }
            });
        }
    }

    @Override
    public <T> List<Pair<String, T>> getAllObjects(Class<T> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException();
        }
        sync(clazz);
        synchronized (mCache) {
            String className = mAnnotationProcessor.getClassId(clazz);
            Map<String, Object> map = mCache.get(className);
            List<Pair<String, T>> result = new LinkedList<Pair<String, T>>();
            Set<Map.Entry<String, Object>> entries = map.entrySet();
            for (Map.Entry<String, Object> entry : entries) {
                result.add(new Pair<String, T>(entry.getKey(), (T) entry.getValue()));
            }
            return result;
        }
    }

    @Override
    public <T> T getObject(Class<T> clazz, String objectIds) {
        if (clazz == null || TextUtils.isEmpty(objectIds)) {
            throw new IllegalArgumentException();
        }
        sync(clazz);
        synchronized (mCache) {
            String className = mAnnotationProcessor.getClassId(clazz);
            Map<String, Object> map = mCache.get(className);
            return (T) map.get(objectIds);
        }
    }

    @Override
    public <T> List<Pair<String, T>> getObjects(Class<T> clazz, Condition<T> condition) {
        if (clazz == null) {
            throw new IllegalArgumentException();
        }
        sync(clazz);
        synchronized (mCache) {
            Map<String, Object> map = mCache.get(mAnnotationProcessor.getClassId(clazz));
            List<Pair<String, T>> result = new LinkedList<Pair<String, T>>();
            Set<Map.Entry<String, Object>> entries = map.entrySet();
            for (Map.Entry<String, Object> entry : entries) {
                if (entry == null) {
                    continue;
                }
                T value = (T) entry.getValue();
                if (value != null && (condition == null || condition != null && condition.satisfy(value))) {
                    result.add(new Pair<String, T>(entry.getKey(), value));
                }
            }
            return result;
        }
    }

    @Override
    public <T> void replaceObjects(final Class<T> clazz, final List<String> oldObjectIds,
                                   final List<T> newObjects, final List<String> newObjectIds) {
        if (clazz == null || oldObjectIds == null || newObjects == null || newObjectIds == null) {
            throw new IllegalArgumentException();
        }
        if (newObjects.size() != newObjectIds.size()) {
            throw new IllegalArgumentException();
        }
        sync(clazz);
        synchronized (mCache) {
            Map<String, Object> map = mCache.get(mAnnotationProcessor.getClassId(clazz));
            for (String id : oldObjectIds) {
                if (id == null) {
                    throw new IllegalArgumentException();
                }
                map.remove(id);
            }
            int size = newObjects.size();
            for (int i = 0; i < size; ++i) {
                String id = newObjectIds.get(i);
                T value = newObjects.get(i);
                if (id == null || value == null) {
                    throw new IllegalArgumentException();
                }
                map.put(id, value);
            }
            operateDb(new Runnable() {
                @Override
                public void run() {
                    mDatabase.replaceObjects(clazz, oldObjectIds, newObjects, newObjectIds);
                }
            });
        }
    }

    @Override
    public void clearTable() {
        mCache.clear();
        operateDb(new Runnable() {
            @Override
            public void run() {
                mDatabase.clearTable();
            }
        });

    }

    public void updateAllObjects() {
        operateDb(new Runnable() {
            @Override
            public void run() {
                synchronized (mCache) {
                    mDatabase.clearTable();
                    for (Map<String, Object> map : mCache.values()) {
                        if (map == null) {
                            continue;
                        }
                        Set<Map.Entry<String, Object>> entries = map.entrySet();
                        if (entries.isEmpty()) {
                            continue;
                        }
                        List<String> ids = new ArrayList<String>();
                        List<Object> objects = new ArrayList<Object>();
                        for (Map.Entry<String, Object> entry : entries) {
                            String id = entry.getKey();
                            Object object = entry.getValue();
                            if (id != null && object != null) {
                                ids.add(id);
                                objects.add(object);
                            }
                        }
                        mDatabase.insertObjects(objects, ids);
                    }
                }
            }
        }, false);
    }
}
