package xiaofei.library.datastorage.database;

/**
 * Created by Eric on 15/11/11.
 */
public class DbConst {

    public static final String TABLE_NAME = "data_storage";

    public static final String ID = "id";

    public static final String CLASS_ID = "class_id";

    public static final String OBJECT_ID = "object_id";

    public static final String OBJECT_DATA = "object_dat";

    public static final int VERSION = 1;

    public static final String CREATE_TABLE_COMMAND =
            "CREATE TABLE IF NOT EXISTS " + DbConst.TABLE_NAME + " ("
                    + "`" + DbConst.ID + "` INTEGER PRIMARY KEY  AUTOINCREMENT NOT NULL, "
                    + "`" + DbConst.CLASS_ID + "` varchar(100) NOT NULL, "
                    + "`" + DbConst.OBJECT_ID + "` varchar(100) NOT NULL, "
                    + "`" + DbConst.OBJECT_DATA + "` text NOT NULL); "
                    + "CREATE UNIQUE INDEX id_object ON " + DbConst.TABLE_NAME + "(`" + DbConst.CLASS_ID
                    + "`, `" + DbConst.OBJECT_ID + "`);";

    public static final String DELETE_TABLE_COMMAND =
            "DROP TABLE IF EXISTS " + DbConst.TABLE_NAME;

    public static final String REPLACE_COMMAND = "REPLACE INTO " + DbConst.TABLE_NAME
            + " (`" + DbConst.CLASS_ID + "`, `" + DbConst.OBJECT_ID + "`, `" + DbConst.OBJECT_DATA + "`)\n"
            + "VALUES ";

    private DbConst() {

    }
}
