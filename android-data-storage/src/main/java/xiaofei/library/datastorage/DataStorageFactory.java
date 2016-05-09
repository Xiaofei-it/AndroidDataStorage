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
