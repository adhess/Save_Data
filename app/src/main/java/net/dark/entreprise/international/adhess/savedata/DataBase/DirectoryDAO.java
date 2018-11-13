package net.dark.entreprise.international.adhess.savedata.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by adhess on 31/05/2018.
 */

public class DirectoryDAO extends DAOBase {
    public static final String key = "id";
    public static final String url = "url";
    public static final String table_name = "Directory";
    public static final String table_create = "CREATE TABLE " + table_name + " (" +
            key + " INTEGER PRIMARY KEY AUTOINCREMENT, " + url + " TEXT );";
    public static final String table_drop = "DROP TABLE IF EXISTS " + table_name + ";";
    public SQLiteDatabase db;

    public DirectoryDAO(Context pContext) {
        super(pContext);
        db = super.open();
    }


    public void add(Directory m) {
        ContentValues value = new ContentValues();
        value.put(url, m.getUrl());
        mDb.insert(table_name,null,value);
    }

    public void delete(long id) {
        mDb.delete(table_name, key + " = ?", new String[]{String.valueOf(id)});
    }

    public Cursor selectALL(){
        return mDb.query(false,table_name,null,null,null,
                null,null,null,null);
    }

    public void deleteAll() {
        mDb.execSQL(table_drop);
        mDb.execSQL(table_create);
    }
}
