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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Xiaofei on 16/3/25.
 *
 * Indicates the object id of an object.
 *
 * The data storage stores the objects according to their class id and object id.
 *
 * If an object has a field of the String type annotated by the ObjectId annotation, the value of
 * the field is used as the object id of the object.
 *
 * If an object do not have such an annotated field or the annotated filed is not of the String type,
 * a specified id should be provided when the data storage stores the object.
 *
 * Distinct objects of the same class should have distinct object ids. If not, the behavior is unspecified.
 */

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ObjectId {

}
