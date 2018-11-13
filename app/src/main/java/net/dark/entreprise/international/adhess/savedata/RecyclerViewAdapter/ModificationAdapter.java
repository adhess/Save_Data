package net.dark.entreprise.international.adhess.savedata.RecyclerViewAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import net.dark.entreprise.international.adhess.savedata.DataBase.Modification;
import net.dark.entreprise.international.adhess.savedata.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by adhess on 09/06/2018.
 */

public class ModificationAdapter extends RecyclerView.Adapter <ModificationAdapter.ViewHolder>{
    Context context;
    List<Modification> list_mod = new ArrayList<>();

    public ModificationAdapter(Context context, List<Modification> list_mod) {
        this.context = context;
        this.list_mod = list_mod;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_modification,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Modification mod = list_mod.get(position);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        holder.mod_date.setText(dateFormat.format(Long.parseLong(mod.getLast_modification())));
        holder.mod_url.setText(mod.getUrl());
    }

    @Override
    public int getItemCount() {
        return list_mod.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mod_url, mod_date;
        public ViewHolder(View itemView) {
            super(itemView);

            mod_url = itemView.findViewById(R.id.mod_url);
            mod_date = itemView.findViewById(R.id.mod_date);
        }
    }
}
