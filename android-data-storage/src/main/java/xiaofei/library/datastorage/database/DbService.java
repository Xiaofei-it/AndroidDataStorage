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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.util.Pair;

import java.util.LinkedList;
import java.util.List;

import xiaofei.library.datastorage.annotation.AnnotationProcessor;
import xiaofei.library.datastorage.util.Condition;
/**
 * Created by Xiaofei on 15/11/12.
 *
 * DbService performs database operations. It is not thread-safe.
 *
 */
class DbService implements IDbOperation {

    private Coder mCoder = null;

    private static final String EQUAL = "` = '";

    private static volatile DbService sInstance;

    private DbOpenHelper mDatabaseHelper;

    private AnnotationProcessor mAnnotationProcessor;

    private SQLiteDatabase mDb = null;

    private DbService(Context context) {
        mDatabaseHelper = new DbOpenHelper(context);
        //The following statement may throw an exception.
        mDb = mDatabaseHelper.getWritableDatabase();
        mCoder = new GsonObjectCoder(new GzipCoderHook());
        mAnnotationProcessor = AnnotationProcessor.getInstance();
    }

    static DbService getInstance(Context context) {
        if (sInstance == null) {
            synchronized (DbService.class) {
                if (sInstance == null) {
                    sInstance = new DbService(context);
                }
            }
        }
        return sInstance;
    }

    void setCoder(Coder coder) {
        mCoder = coder;
    }

    private static String generateEquation(String leftValue, String rightValue) {
        return "`" + leftValue + EQUAL + rightValue + "'";
    }

    private <T> String generateReplaceValue(T object, String id) {
        return "('" + mAnnotationProcessor.getClassId(object.getClass()) + "', '" + id + "', '" + mCoder.encode(object) + "')";
    }

    /**
     * 关闭数据库，程序退出时执行
     */
    public void close() {
        mDb.close();
    }

    @Override
    public <T> boolean containsObject(Class<T> clazz, String objectId) {
        Cursor c = mDb.query(DbConst.TABLE_NAME, null,
                generateEquation(DbConst.CLASS_ID, mAnnotationProcessor.getClassId(clazz)) + " and " + generateEquation(DbConst.OBJECT_ID, objectId),
                null, null, null, null, null);
        boolean result = c.getCount() != 0;
        c.close();
        return result;
    }

    @Override
    public <T> void insertObject(T object, String objectId) {
        String command = DbConst.REPLACE_COMMAND + generateReplaceValue(object, objectId) + ";";
        mDb.execSQL(command);
    }

    @Override
    public <T> void insertObjects(final List<T> objects, final List<String> objectIds) {
        if (objects.isEmpty()) {
            return;
        }
        executeInTransaction(new Runnable() {
            @Override
            public void run() {
                // To avoid the extremely long sql command, we insert objects one by one, instead of
                // insert all of them by a long sql command.
                // This is not the best practice. Future work should make this better.
                int size = objects.size();
                for (int i = 0; i < size; ++i) {
                    T object = objects.get(i);
                    String id = objectIds.get(i);
                    insertObject(object, id);
                }
            }
        });
    }

    @Override
    public <T> List<Pair<String, T>> getAllObjects(Class<T> clazz) {
        List<Pair<String, T>> list = new LinkedList<Pair<String, T>>();
        Cursor cursor = mDb.query(DbConst.TABLE_NAME, null,
                generateEquation(DbConst.CLASS_ID, mAnnotationProcessor.getClassId(clazz)),
                null, null, null, null);
        while (cursor.moveToNext()) {
            String data = cursor.getString(cursor.getColumnIndex(DbConst.OBJECT_DATA));
            String id = cursor.getString(cursor.getColumnIndex(DbConst.OBJECT_ID));
            T object = null;
            try {
                object = mCoder.decode(data, clazz);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
            if (object != null) {
                list.add(new Pair<String, T>(id, object));
            }
        }
        cursor.close();
        return list;
    }

    @Override
    public <T> T getObject(Class<T> clazz, String objectId) {
        T result = null;
        Cursor cursor = mDb.query(DbConst.TABLE_NAME, null,
                generateEquation(DbConst.CLASS_ID, mAnnotationProcessor.getClassId(clazz)) + " and " + generateEquation(DbConst.OBJECT_ID, objectId),
                null, null, null, null);
        if (cursor.moveToNext()) {
            String data = cursor.getString(cursor.getColumnIndex(DbConst.OBJECT_DATA));
            try {
                result = mCoder.decode(data, clazz);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        cursor.close();
        return result;
    }

    @Override
    public <T> List<Pair<String, T>> getObjects(Class<T> clazz, Condition<T> condition) {
        List<Pair<String, T>> result = new LinkedList<Pair<String, T>>();
        Cursor cursor = mDb.query(DbConst.TABLE_NAME, null,
                generateEquation(DbConst.CLASS_ID, mAnnotationProcessor.getClassId(clazz)),
                null, null, null, null);
        while (cursor.moveToNext()) {
            String data = cursor.getString(cursor.getColumnIndex(DbConst.OBJECT_DATA));
            T object = null;
            try {
                object = mCoder.decode(data, clazz);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
            if (object == null) {
                continue;
            }
            if (condition == null || condition.satisfy(object)) {
                String id = cursor.getString(cursor.getColumnIndex(DbConst.OBJECT_ID));
                result.add(new Pair<String, T>(id, object));
            }
        }
        cursor.close();
        return result;
    }

    @Override
    public void clearTable() {
        mDb.delete(DbConst.TABLE_NAME, null, null);
    }

    @Override
    public <T> void deleteObject(Class<T> clazz, String objectId) {
        String whereClause = generateEquation(DbConst.CLASS_ID, mAnnotationProcessor.getClassId(clazz)) + " and " + generateEquation(DbConst.OBJECT_ID, objectId);
        mDb.delete(DbConst.TABLE_NAME, whereClause, null);
    }

    @Override
    public <T> void deleteObjects(Class<T> clazz, List<String> objectIds) {
        if (objectIds.isEmpty()) {
            return;
        }
        StringBuilder whereClause = new StringBuilder(
                generateEquation(DbConst.CLASS_ID, mAnnotationProcessor.getClassId(clazz)) + " and `" + DbConst.OBJECT_ID + "` in ('" + objectIds.get(0) + "'");
        int size = objectIds.size();
        for (int i = 1; i < size; ++i) {
            whereClause.append(" ,'").append(objectIds.get(i)).append("'");
        }
        whereClause.append(")");
        mDb.delete(DbConst.TABLE_NAME, whereClause.toString(), null);
    }

    @Override
    public <T> void deleteAllObjects(Class<T> clazz) {
        String whereClause = generateEquation(DbConst.CLASS_ID, mAnnotationProcessor.getClassId(clazz));
        mDb.delete(DbConst.TABLE_NAME, whereClause, null);
    }

    public void executeInTransaction(Runnable runnable) {
        try {
            mDb.beginTransaction();
            try {
                runnable.run();
                mDb.setTransactionSuccessful();
            } finally {
                mDb.endTransaction();
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public interface Coder {
        String encode(Object object);
        <T> T decode(String string, Class<T> clazz);
    }
}
