package com.architjn.myapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.architjn.myapp.R;
import com.architjn.myapp.model.Contact;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by HP on 21-05-2016.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Contact> items;

    public ContactAdapter(Context context, ArrayList<Contact> items) {
        this.context = context;
        this.items = items;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_contact, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Contact item = items.get(position);
        holder.name.setText(item.getName());
        holder.status.setText(item.getStatus());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateItems(ArrayList<Contact> contacts) {
        this.items = contacts;
        notifyDataSetChanged();
    }


    protected static class ViewHolder extends RecyclerView.ViewHolder {
        private final CircleImageView photo;
        private final TextView name, status;
        private final View mainView;

        public ViewHolder(View itemView) {
            super(itemView);
            mainView = itemView;
            photo = (CircleImageView) itemView.findViewById(R.id.photo);
            name = (TextView) itemView.findViewById(R.id.contact_name);
            status = (TextView) itemView.findViewById(R.id.contact_status);
        }
    }

}

