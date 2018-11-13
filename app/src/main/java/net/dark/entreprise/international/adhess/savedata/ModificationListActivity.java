package net.dark.entreprise.international.adhess.savedata;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import net.dark.entreprise.international.adhess.savedata.DataBase.Modification;
import net.dark.entreprise.international.adhess.savedata.DataBase.ModificationDAO;
import net.dark.entreprise.international.adhess.savedata.RecyclerViewAdapter.ModificationAdapter;

import java.util.ArrayList;
import java.util.List;

public class ModificationListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modification_list);


        List<Modification> list_mod = new ArrayList<>();

        ModificationDAO dao = new ModificationDAO(this);


        Cursor cursor = dao.selectALL();
        if (cursor.moveToFirst()) {
            do {
                String url = cursor.getString(cursor.getColumnIndex("url")).substring(20);
                String id = "" + cursor.getString(cursor.getColumnIndex("id"));
                String last_modification = cursor.getString(cursor.getColumnIndex("last_modification"));
                list_mod.add(new Modification(Integer.parseInt(id), url, last_modification));

            } while (cursor.moveToNext());
        }
        dao.close();


        RecyclerView data_rc = findViewById(R.id.data_rc);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false);
        ModificationAdapter adapter = new ModificationAdapter(this, list_mod);
        data_rc.setLayoutManager(layoutManager);
        data_rc.setAdapter(adapter);
        data_rc.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

    }
}
