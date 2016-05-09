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

package xiaofei.library.datastorage.util;

/**
 * Created by Xiaofei on 15/11/11.
 *
 * Indicates whether an object satisfies a condition.
 *
 * In some cases, developers want to load some objects satisfying a specified condition from the
 * data storage. They can provide the loading method with an argument of this interface to indicate
 * which kind of objects they want to load. The data storage loads the objects and pass each object
 * as an argument into the method satisfy(T) of the Condition interface to test whether the object
 * satisfies the condition. If the method satisfy(T) returns true, the object is added into a list
 * which will be returned as the result of the loading method after the testing of each object.
 *
 * See the IDataStorage interface for the more details of the usage of this interface.
 *
 */
public interface Condition<T> {
    boolean satisfy(T o);
}
