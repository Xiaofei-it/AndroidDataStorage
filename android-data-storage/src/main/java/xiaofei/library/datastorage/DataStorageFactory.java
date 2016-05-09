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

import android.content.Context;

import xiaofei.library.datastorage.database.DatabaseStorage;

/**
 * Created by Xiaofei on 16/3/24.
 */
public class DataStorageFactory {

    public static final int TYPE_DATABASE = 0;

    public static final int TYPE_FILE = TYPE_DATABASE + 1;

    private DataStorageFactory() {

    }

    public static IDataStorage getInstance(Context context, int type) {
        switch (type) {
            case TYPE_DATABASE:
                return DatabaseStorage.getInstance(context);
            case TYPE_FILE:
            default:
                throw new IllegalArgumentException("Type " + type + " is not supported.");
        }
    }

}
