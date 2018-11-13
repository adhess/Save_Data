package net.dark.entreprise.international.adhess.savedata.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;

/**
 * Created by adhess on 31/05/2018.
 */

public class ModificationDAO extends DAOBase {
    public static final String key = "id";
    public static final String url = "url";
    public static final String last_modificationod = "last_modification";
    public static final String table_name = "Modification";
    public static final String table_create = "CREATE TABLE " + table_name + " (" +
            key + " INTEGER PRIMARY KEY AUTOINCREMENT, " + url + " TEXT," +
            last_modificationod + " TEXT );";
    public static final String table_drop = "DROP TABLE IF EXISTS " + table_name + ";";
    public SQLiteDatabase db;

    public ModificationDAO(Context pContext) {
        super(pContext);
        db = super.open();
    }


    public void add(Modification m) {
        ContentValues value = new ContentValues();
        value.put(last_modificationod, m.getLast_modification());
        value.put(url, m.getUrl());
        mDb.insert(table_name,null,value);
    }

    public void delete(long id) {
        mDb.delete(table_name, key + " = ?", new String[]{String.valueOf(id)});
    }

    public void modify(int id) {
        ContentValues value = new ContentValues();
        value.put(last_modificationod, new Date().getTime());
        mDb.update(table_name, value, key + " = ?", new String[]{String.valueOf(id)});
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
