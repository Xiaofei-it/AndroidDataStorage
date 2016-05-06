package xiaofei.library.datastorage.database;

import android.support.v4.util.Pair;

import java.util.List;

import xiaofei.library.datastorage.util.Condition;

/**
 * Created by Eric on 15/11/9.
 *
 * This interface defines basic functions of database operations.
 *
 */
public interface IDbOperation {

    /**
     * Return true if the database contains the object of the specified class and
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
     * If the database contains an object of the same id, it replace the old one with the new one.
     *
     * @param object
     * @param objectId
     * @param <T>
     */
    <T> void insertObject(T object, String objectId);

    /**
     * Insert the objects of the specified object ids.
     *
     * If the database contains an object of the same id, it replace the old one with the new one.
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
     * @param objectIds
     * @param <T>
     * @return
     */
    <T> T getObject(Class<T> clazz, String objectIds);

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
     * Replace the objects of the old object ids with the objects of the new object ids.
     *
     * @param clazz
     * @param oldObjectIds
     * @param newObjects
     * @param newObjectIds
     * @param <T>
     */
    <T> void replaceObjects(Class<T> clazz, List<String> oldObjectIds, List<T> newObjects, List<String> newObjectIds);

    /**
     * Close the database.
     */
    void close();

    /**
     * Delete all the objects in the table.
     */
    void clearTable();
}
