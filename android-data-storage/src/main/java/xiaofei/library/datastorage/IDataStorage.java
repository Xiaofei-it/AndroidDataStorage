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

package xiaofei.library.datastorage;

import java.util.Comparator;
import java.util.List;

import xiaofei.library.datastorage.util.Condition;
/**
 * Created by Xiaofei on 16/3/24.
 *
 * This interface defines basic methods of the data storage.
 *
 * Developers use the implementation of this interface to use the storage.
 *
 */
public interface IDataStorage {

    /**
     * Return true if the storage contains the element of the specified class
     * and the specified id, false otherwise.
     * @param clazz
     * @param id
     * @param <T>
     * @return
     */
    <T> boolean contains(Class<T> clazz, String id);

    /**
     * Return true if the storage contains the element, false otherwise.
     *
     * The element should have one and only one field of the String type on which
     * the ObjectId annotation is present.
     *
     * @param element
     * @param <T>
     * @return
     */
    <T> boolean contains(T element);

    /**
     * Store or update an element of the specified id.
     *
     * If the storage previously contained one element of the same id, the old one is
     * replaced by the new one.
     *
     * Otherwise, the storage stores the element.
     *
     * @param element
     * @param id
     * @param <T>
     */
    <T> void storeOrUpdate(T element, String id);

    /**
     * Store or update an element.
     *
     * The element should have one and only one field of the String type on which
     * the ObjectId annotation is present.
     *
     * If the storage previously contained one element of the same id, the old one
     * is replaced by the new one.
     *
     * Otherwise, the storage stores the element.
     *
     * @param element
     * @param <T>
     */
    <T> void storeOrUpdate(T element);

    /**
     * Store or update a list of elements of the same type.
     *
     * Each element has a corresponding id at the corresponding position in the list ids.
     *
     * The two lists should be of the same size.
     * @param list
     * @param ids
     * @param <T>
     */
    <T> void storeOrUpdate(List<T> list, List<String> ids);

    /**
     * Store or update a list of elements of the same type.
     *
     * The elements should have one and only one field of the String type on which
     * the ObjectId annotation is present.
     *
     * @param list
     * @param <T>
     */
    <T> void storeOrUpdate(List<T> list);

    /**
     * Return the element of the specified class and the specified id.
     *
     * @param clazz
     * @param id
     * @param <T>
     * @return
     */
    <T> T load(Class<T> clazz, String id);

    /**
     * Return a list of all the elements of the specified class.
     *
     * @param clazz
     * @param <T>
     * @return
     */
    <T> List<T> loadAll(Class<T> clazz);


    /**
     * Return a list of all the elements of the specified class.
     *
     * The list is sorted according to the comparator.
     *
     * @param clazz
     * @param <T>
     * @return
     */
    <T> List<T> loadAll(Class<T> clazz, Comparator<T> comparator);

    /**
     * Return a list of elements of the specified class and the specified ids.
     *
     * Each element has an id at the corresponding position in the List ids.
     *
     * @param clazz
     * @param ids
     * @param <T>
     * @return
     */
    <T> List<T> load(Class<T> clazz, List<String> ids);

    /**
     * Return a list of elements of the specified class and the specified ids.
     *
     * Each element has an id at the corresponding position in the List ids.
     *
     * The returned list is sorted according to the comparator.
     *
     * @param clazz
     * @param ids
     * @param <T>
     * @return
     */
    <T> List<T> load(Class<T> clazz, List<String> ids, Comparator<T> comparator);

    /**
     * Return a list of elements which satisfying the condition.
     *
     * @param clazz
     * @param condition
     * @param <T>
     * @return
     */
    <T> List<T> load(Class<T> clazz, Condition<T> condition);

    /**
     * Return a list of elements which satisfying the condition.
     *
     * The returned list is sorted according to the comparator.
     *
     * @param clazz
     * @param condition
     * @param <T>
     * @return
     */
    <T> List<T> load(Class<T> clazz, Condition<T> condition, Comparator<T> comparator);

    /**
     * Delete the element.
     *
     * The element should have one and only one field of the String type on which
     * the ObjectId annotation is present.
     *
     * @param element
     * @param <T>
     */
    <T> void delete(T element);

    /**
     * Delete the element of the specified class and the specified id.
     *
     * @param clazz
     * @param id
     * @param <T>
     */
    <T> void delete(Class<T> clazz, String id);

    /**
     * Delete all the elements of the specified class;
     */
    <T> void deleteAll(Class<T> clazz);

    /**
     * Delete the elements of the specified class and the id in the list ids.
     *
     * @param clazz
     * @param ids
     * @param <T>
     */
    <T> void delete(Class<T> clazz, List<String> ids);

    /**
     * Delete the elements in the list.
     *
     * The elements should have one and only one field of the String type on which
     * the ObjectId annotation is present.
     *
     * @param list
     * @param <T>
     */
    <T> void delete(List<T> list);

    /**
     * Delete the elements which are of the specified class and satisfy the condition.
     *
     */
    <T> void delete(Class<T> clazz, Condition<T> condition);

    /**
     * Clear all the data in the storage.
     */
    void clear();
}
