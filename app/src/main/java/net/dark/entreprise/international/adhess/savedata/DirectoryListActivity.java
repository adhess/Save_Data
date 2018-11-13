package net.dark.entreprise.international.adhess.savedata;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import net.dark.entreprise.international.adhess.savedata.DataBase.Directory;
import net.dark.entreprise.international.adhess.savedata.DataBase.DirectoryDAO;
import net.dark.entreprise.international.adhess.savedata.RecyclerViewAdapter.DirectoryAdapter;

import java.util.ArrayList;
import java.util.List;

public class DirectoryListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory_list);

        DirectoryDAO directoryDao = new DirectoryDAO(this);
        List<Directory> list_dir = new ArrayList<>();

        Cursor cursor = directoryDao.selectALL();
        if (cursor.moveToFirst()) {
            do {
                String url = cursor.getString(cursor.getColumnIndex("url")).substring(20);
                String id = "" + cursor.getString(cursor.getColumnIndex("id"));
                list_dir.add(new Directory(Long.parseLong(id),url));
            } while (cursor.moveToNext());
        }
        directoryDao.close();




        RecyclerView rc = findViewById(R.id.rc_dir);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        DirectoryAdapter adapter = new DirectoryAdapter(this,list_dir);

        rc.setAdapter(adapter);
        rc.setLayoutManager(layoutManager);
        rc.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
    }
}
