package xiaofei.library.datastorage.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DbOpenHelper extends android.database.sqlite.SQLiteOpenHelper {


    public DbOpenHelper(Context context) {
        super(context.getApplicationContext(), DbConst.TABLE_NAME, null, DbConst.VERSION);

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
