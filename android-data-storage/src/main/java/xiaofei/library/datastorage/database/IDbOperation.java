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

import android.support.v4.util.Pair;

import java.util.List;

import xiaofei.library.datastorage.util.Condition;

/**
 * Created by Xiaofei on 15/11/9.
 *
 * This interface defines basic methods of database operations.
 *
 */
interface IDbOperation {

    /**
     * Return true if the table contains the object of the specified class and
     * the specified object id.
     *
     * Return false otherwise.
     *
     * @param clazz
     * @param objectId
     * @param <T>
     * @return
     */
    <T> boolean containsObject(Class<T> clazz, String objectId);

    /**
     * Insert the object of the specified object id.
     *
     * If the table contains an object of the same id, it replace the old one with the new one.
     *
     * @param object
     * @param objectId
     * @param <T>
     */
    <T> void insertObject(T object, String objectId);

    /**
     * Insert the objects of the specified object ids.
     *
     * If the table contains an object of the same id, it replace the old one with the new one.
     *
     * @param objects
     * @param objectIds
     * @param <T>
     */
    <T> void insertObjects(List<T> objects, List<String> objectIds);

    /**
     * Return all the objects of the specified class.
     *
     * The result is a list of pairs, whose first element is the object id and the second element
     * is the corresponding object.
     *
     * @param clazz
     * @param <T>
     * @return
     */
    <T> List<Pair<String, T>> getAllObjects(Class<T> clazz);

    /**
     * Return the objects of the specified class and the specified object ids.
     *
     * @param clazz
     * @param objectId
     * @param <T>
     * @return
     */
    <T> T getObject(Class<T> clazz, String objectId);

    /**
     * Return the objects of the specified class, which satisfy the condition.
     *
     * @param clazz
     * @param condition
     * @param <T>
     * @return
     */
    <T> List<Pair<String, T>> getObjects(Class<T> clazz, Condition<T> condition);

    /**
     * Delete the object of the specified class and the specified object id.
     *
     * @param clazz
     * @param objectId
     * @param <T>
     */
    <T> void deleteObject(Class<T> clazz, String objectId);

    /**
     * Delete the objects of the specified class and the specified object ids.
     *
     * @param clazz
     * @param objectIds
     * @param <T>
     */
    <T> void deleteObjects(Class<T> clazz, List<String> objectIds);

    /**
     * Delete all the object of the specified class.
     *
     * @param clazz
     * @param <T>
     */
    <T> void deleteAllObjects(Class<T> clazz);

    /**
     * Close the database.
     */
    void close();

    /**
     * Delete all the objects in the table.
     */
    void clearTable();
}
