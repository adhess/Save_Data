package net.dark.entreprise.international.adhess.savedata.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by adhess on 31/05/2018.
 */

public class DbHandler extends SQLiteOpenHelper {

    public static final String key = "id";
    public static final String url = "url";
    public static final String table_name_d = "Directory";
    public static final String table_name_m = "Modification";
    public static final String last_modificationod = "last_modification";
    public static final String table_create_d = "CREATE TABLE " + table_name_d + " (" +
            key + " INTEGER PRIMARY KEY AUTOINCREMENT, " + url + " TEXT );";
    public static final String table_create_m = "CREATE TABLE " + table_name_m + " (" +
            key + " INTEGER PRIMARY KEY AUTOINCREMENT, " + url + " TEXT," +
            last_modificationod + " TEXT );";
    public static final String table_drop_d = "DROP TABLE IF EXISTS " + table_name_d + ";";
    public static final String table_drop_m = "DROP TABLE IF EXISTS " + table_name_m + ";";

    public DbHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(table_create_d);
        sqLiteDatabase.execSQL(table_create_m);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(table_drop_d);
        sqLiteDatabase.execSQL(table_drop_m);
        onCreate(sqLiteDatabase);
    }
}
