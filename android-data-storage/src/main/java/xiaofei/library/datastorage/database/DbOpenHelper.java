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
import android.database.sqlite.SQLiteDatabase;

public class DbOpenHelper extends android.database.sqlite.SQLiteOpenHelper {


    public DbOpenHelper(Context context) {
        super(context.getApplicationContext(), DbConst.TABLE_NAME, null, DbConfig.getVersion());

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbConst.CREATE_TABLE_COMMAND);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DbConst.DELETE_TABLE_COMMAND);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DbConst.DELETE_TABLE_COMMAND);
        onCreate(db);
    }
}
