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
 * Indicates the class id of the annotated class.
 *
 * The data storage stores the objects according to their class ids and object ids.
 *
 * Always, the class name can be used as the unique id of each class. However, before
 * an app is released, its source code is always be obfuscated, so the class name is
 * therefore also obfuscated and it may not be the same every time the app is released.
 * Objects thus cannot be stored and restored.
 *
 * Therefore, an new technique should be applied to guarantee the objects stored in
 * earlier version can be restored in later version. This annotation indicates the class
 * id of the annotated class.
 *
 * Distinct classes should have distinct class ids. If not, the behavior is unspecified.
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ClassId {

    String value();

}
