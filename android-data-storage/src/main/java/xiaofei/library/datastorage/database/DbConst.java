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

/**
 * Created by Xiaofei on 15/11/11.
 */
class DbConst {

    public static final String TABLE_NAME = "data_storage";

    public static final String ID = "id";

    public static final String CLASS_ID = "class_id";

    public static final String OBJECT_ID = "object_id";

    public static final String OBJECT_DATA = "object_dat";

    public static final String CREATE_TABLE_COMMAND =
            "CREATE TABLE IF NOT EXISTS " + DbConst.TABLE_NAME + " ("
                    + "`" + DbConst.ID + "` INTEGER PRIMARY KEY  AUTOINCREMENT NOT NULL, "
                    + "`" + DbConst.CLASS_ID + "` varchar(100) NOT NULL, "
                    + "`" + DbConst.OBJECT_ID + "` varchar(100) NOT NULL, "
                    + "`" + DbConst.OBJECT_DATA + "` text NOT NULL, "
                    + "UNIQUE (`" + DbConst.CLASS_ID + "`, `" + DbConst.OBJECT_ID + "`) "
                    + "ON CONFLICT REPLACE);";

    public static final String DELETE_TABLE_COMMAND =
            "DROP TABLE IF EXISTS " + DbConst.TABLE_NAME;

    public static final String REPLACE_COMMAND = "REPLACE INTO " + DbConst.TABLE_NAME
            + " (`" + DbConst.CLASS_ID + "`, `" + DbConst.OBJECT_ID + "`, `" + DbConst.OBJECT_DATA + "`)\n"
            + "VALUES ";

    private DbConst() {

    }
}
