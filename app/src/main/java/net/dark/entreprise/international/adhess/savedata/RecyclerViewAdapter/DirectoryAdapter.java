package net.dark.entreprise.international.adhess.savedata.RecyclerViewAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import net.dark.entreprise.international.adhess.savedata.DataBase.Directory;
import net.dark.entreprise.international.adhess.savedata.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adhess on 09/06/2018.
 */

public class DirectoryAdapter extends RecyclerView.Adapter<DirectoryAdapter.ViewHolder> {
    Context context;
    List<Directory> list_dir = new ArrayList<>();

    public DirectoryAdapter(Context context, List<Directory> list_dir) {
        this.context = context;
        this.list_dir = list_dir;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_directory,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Directory directory = list_dir.get(position);
        holder.dir_url.setText(directory.getUrl());
    }

    @Override
    public int getItemCount() {
        return list_dir.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dir_url;
        public ViewHolder(View itemView) {
            super(itemView);
            dir_url = itemView.findViewById(R.id.dir_url);
        }
    }
}
