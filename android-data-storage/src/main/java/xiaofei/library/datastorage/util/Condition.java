package xiaofei.library.datastorage.util;

/**
 * Created by Xiaofei on 15/11/11.
 *
 * Indicates whether an object satisfies a condition.
 *
 * In some cases, programmers want to load some objects satisfying a specified condition from the
 * data storage. They may provide the loading function with an argument of this interface to indicate
 * which kind of objects they want to load. The data storage loads the objects and pass each object
 * as an argument into the function satisfy(T) of the Condition interface to test whether the object
 * satisfies the condition. If the function satisfy(T) returns true, the object is added into a list
 * which will be returned as the result of the loading function after the testing of each object.
 *
 * See the IDataStorage interface for the more details of the usage of this interface.
 *
 */
public interface Condition<T> {
    boolean satisfy(T o);
}
