package net.dark.entreprise.international.adhess.savedata;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.provider.DocumentFile;
import android.support.v7.app.AlertDialog;
import android.util.Pair;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import net.dark.entreprise.international.adhess.savedata.DataBase.Directory;
import net.dark.entreprise.international.adhess.savedata.DataBase.DirectoryDAO;
import net.dark.entreprise.international.adhess.savedata.DataBase.Modification;
import net.dark.entreprise.international.adhess.savedata.DataBase.ModificationDAO;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_addDirectory) {
            addDirectory();
        } else if (id == R.id.nav_showAllFiles) {
            showAllFiles();
        } else if (id == R.id.nav_showAllDirectories) {
            showAllDirectories();
        } else if (id == R.id.nav_deleteAllFiles) {
            deleteAllFiles();
        } else if (id == R.id.nav_deleteAllDirectories) {
            deleteAllDirectories();
        } else if (id == R.id.download_server) {
            /* https://drive.google.com/open?id=1qalAjQdR-qAJ9JegP5r4eTp7ssTplLCU */
            download_server();
        } else if (id == R.id.send_feedback) {
            feedback();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void feedback() {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "m.save.data@gmail.com" });
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Save Data");
        startActivity(Intent.createChooser(emailIntent, Intent.EXTRA_TITLE));
    }

    private void download_server() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Télécharger l'application puis copier sur votre machine, enfin, exécuté !");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com/open?id=1qalAjQdR-qAJ9JegP5r4eTp7ssTplLCU"));
                startActivity(intent);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void showAllFiles() {
        Intent intent = new Intent(this, ModificationListActivity.class);
        startActivity(intent);
    }

    public void showAllDirectories() {
        Intent intent = new Intent(this, DirectoryListActivity.class);
        startActivity(intent);
    }

    public void show() {
        try {

            ModificationDAO dao = new ModificationDAO(this);
            TextView tv = findViewById(R.id.tv_y);
            String ans = "";

            Cursor cursor = dao.selectALL();
            if (cursor.moveToFirst()) {
                do {
                    String url = cursor.getString(cursor.getColumnIndex("url"));
                    String id = "" + cursor.getString(cursor.getColumnIndex("id"));
                    String last_modification = cursor.getString(cursor.getColumnIndex("last_modification"));
                    Long date = Long.parseLong(last_modification, 10);
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    ans += id + " | " + url + " | " + dateFormat.format(date) + "\n";

                } while (cursor.moveToNext());
            }
            tv.setText(ans);
            dao.close();
        } catch (Exception e) {
            TextView tv = findViewById(R.id.tv_y);
            tv.setText("erreur: " + e.toString());
        }
    }

    public void add(View v) {
        try {

            ModificationDAO dao = new ModificationDAO(this);
            dao.add(new Modification(1, "url" + (int) (Math.random() * 100), "" + new Date().getTime()));

            dao.close();
        } catch (Exception e) {
            TextView tv = (TextView) findViewById(R.id.tv_y);
            tv.setText("erreur: " + e.toString());
        }
    }

    public void deleteAllFiles() {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage("Voulez-vous vraiment supprimez l'historique des fichiers?")
                .setTitle("Supprimer tous les fichiers");
        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ModificationDAO dao = new ModificationDAO(getApplicationContext());
                dao.deleteAll();
                Toast.makeText(getApplicationContext(), "c'est bon", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void modify(View v) {
        try {

            ModificationDAO dao = new ModificationDAO(this);
            dao.modify(2);

            TextView tv = (TextView) findViewById(R.id.tv_y);
            tv.setText("");
        } catch (Exception e) {
            TextView tv = (TextView) findViewById(R.id.tv_y);
            tv.setText("erreur: " + e.toString());
        }
    }

    public void delete(View v) {
        try {

            ModificationDAO dao = new ModificationDAO(this);
            dao.delete(1);

            TextView tv = (TextView) findViewById(R.id.tv_y);
            tv.setText("");
        } catch (Exception e) {
            TextView tv = (TextView) findViewById(R.id.tv_y);
            tv.setText("erreur: " + e.toString());
        }
    }


    public void Dshow() {
        try {

            DirectoryDAO directoryDao = new DirectoryDAO(this);
            TextView tv = findViewById(R.id.tv_y);
            String ans = "";

            Cursor cursor = directoryDao.selectALL();
            if (cursor.moveToFirst()) {
                do {
                    String url = cursor.getString(cursor.getColumnIndex("url"));
                    String id = "" + cursor.getString(cursor.getColumnIndex("id"));
                    ans += id + " | " + url + "\n";

                } while (cursor.moveToNext());
            }
            tv.setText(ans);
            directoryDao.close();
        } catch (Exception e) {
            TextView tv = findViewById(R.id.tv_y);
            tv.setText("erreur: " + e.toString());
        }
    }

    public void Dadd(View v) {
        try {

            DirectoryDAO directoryDao = new DirectoryDAO(this);
            directoryDao.add(new Directory(1, "url" + (int) (Math.random() * 100)));
            directoryDao.close();
        } catch (Exception e) {
            TextView tv = (TextView) findViewById(R.id.tv_y);
            tv.setText("erreur: " + e.toString());
        }
    }

    public void deleteAllDirectories() {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage("Voulez-vous vraiment supprimez l'historique des dossiers?")
                .setTitle("Supprimer tous les dossiers");
        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                DirectoryDAO directoryDao = new DirectoryDAO(getApplicationContext());
                directoryDao.deleteAll();
                Toast.makeText(getApplicationContext(), "c'est bon", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();

    }


    public void Ddelete(View v) {
        try {
            DirectoryDAO directoryDao = new DirectoryDAO(this);
            directoryDao.delete(1);
        } catch (Exception e) {
            TextView tv = (TextView) findViewById(R.id.tv_y);
            tv.setText("erreur: " + e.toString());
        }
    }


    public void addDirectory() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        startActivityForResult(intent, 1998);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1998 && resultCode == RESULT_OK) {
            addDirectoryToDB(data);
        }
    }


    private void addDirectoryToDB(Intent data) {
        String dir = DocumentFile.fromTreeUri(getApplicationContext(), (Uri) data.getData()).getUri().getPath();
        dir = "/storage/emulated/0/" + dir.substring(dir.lastIndexOf(":") + 1, dir.length());
        DirectoryDAO directoryDao = new DirectoryDAO(this);
        directoryDao.add(new Directory(1, dir));
    }

    private void addFilesToDB(File file, String FilesAlreadySent) {
        try {

            if (file.isFile() && !FilesAlreadySent.contains(file.getPath())) {
                ModificationDAO dao = new ModificationDAO(this);
                dao.add(new Modification(1, file.getPath(), -1 + ""));
            } else if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File f : files) {
                    addFilesToDB(f, FilesAlreadySent);
                }
            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "enable storage Settings->Apps->Your app->Permissions", Toast.LENGTH_LONG).show();
        }

    }

    public void synchronize(View v) {
        try {
            String FilesAlreadySent = getAllFilesFromDB();
            // check if there is new file to send later
            addNewFilesToDB(FilesAlreadySent);
            // look into Modification table for files that was modified and haven't sent
            List<Pair<Integer, String>> p = getFilesToSend();

            // send All new Files
            sendAllNewFiles(p);
        } catch (Exception e) {
            TextView tv = (TextView) findViewById(R.id.tv_y);
            tv.setText("erreur: " + e.toString());
        }
    }


    public String getAllFilesFromDB() {
        ModificationDAO dao = new ModificationDAO(this);
        String ans = "";
        Cursor cursor = dao.selectALL();
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndex("id"));
                String url = cursor.getString(cursor.getColumnIndex("url"));
                String last_modification = cursor.getString(cursor.getColumnIndex("last_modification"));
                ans += id + " " + url + " " + last_modification + "\n";
            } while (cursor.moveToNext());
        }
        return ans;
    }

    private void addNewFilesToDB(String filesAlreadySent) {
        DirectoryDAO directoryDao = new DirectoryDAO(this);

        Cursor cursor = directoryDao.selectALL();
        if (cursor.moveToFirst()) {
            do {
                String url = cursor.getString(cursor.getColumnIndex("url"));
                addFilesToDB(new File(url), filesAlreadySent);
            } while (cursor.moveToNext());
        }
    }

    public List<Pair<Integer, String>> getFilesToSend() {
        List<Pair<Integer, String>> p = new ArrayList<>();
        ModificationDAO dao = new ModificationDAO(this);

        Cursor cursor = dao.selectALL();
        if (cursor.moveToFirst()) {
            do {
                String url = cursor.getString(cursor.getColumnIndex("url"));
                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                String last_modification = cursor.getString(cursor.getColumnIndex("last_modification"));
                Long date = Long.parseLong(last_modification, 10);
                Long lm = new File(url).lastModified();
                if (date < lm) p.add(new Pair<>(id, url));
            } while (cursor.moveToNext());

        }
        return p;
    }

    private void sendAllNewFiles(List<Pair<Integer, String>> listPair) {
        new SendFileTask().execute(listPair);
    }


    class SendFileTask extends AsyncTask<List<Pair<Integer, String>>, Integer, Void> {
        ProgressBar bar;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            bar = findViewById(R.id.progressBar);
            bar.setMax(100);
            bar.setProgress(0);
            bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            bar.setProgress(values[0]);
        }

        @Override
        protected Void doInBackground(List<Pair<Integer, String>>[] lists) {
            int progress = 1;

            for (Pair<Integer, String> pair : lists[0]) {
                sendFiles(pair.first, pair.second);
                progress++;
                publishProgress(((progress * 100) / lists[0].size()));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            bar.setVisibility(View.INVISIBLE);


            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            builder.setMessage("Terminé");
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }

        private void sendFiles(int id, String url) {
            Socket s;
            PrintWriter writer;
            byte[] bytearray = new byte[1024 * 16];
            FileInputStream fis = null;
            String addr = ((EditText) findViewById(R.id.editText)).getText().toString();
            try {
                s = new Socket(addr, 6900);
                if (s != null && s.isConnected()) {

                    //String nameFile = url.substring(url.lastIndexOf("/") + 1);
                    PrintWriter pw = new PrintWriter(s.getOutputStream());
                    pw.write(url);
                    pw.close();
                    s.close();

                    s = new Socket(addr, 6900);
                    if (s != null && s.isConnected()) {

                        fis = new FileInputStream(url);
                        OutputStream output = s.getOutputStream();
                        BufferedInputStream bis = new BufferedInputStream(fis);

                        int readLength;
                        while ((readLength = bis.read(bytearray)) > 0) {
                            output.write(bytearray, 0, readLength);
                        }
                        bis.close();
                        output.close();
                        s.close();
                        ModificationDAO dao = new ModificationDAO(getApplicationContext());
                        dao.modify(id);
                    }
                }
            } catch (Exception ex) {

            }

        }
    }

}
