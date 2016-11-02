package com.architjn.myapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.architjn.myapp.R;
import com.architjn.myapp.database.ChatTable;
import com.architjn.myapp.database.ConversationTable;
import com.architjn.myapp.database.DbHelper;
import com.architjn.myapp.model.Chat;
import com.architjn.myapp.model.Contact;
import com.architjn.myapp.utils.Constants;
import com.architjn.myapp.utils.Utils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by HP on 21-05-2016.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Chat> items;
    private OnItemSelected callback = null;

    public ChatAdapter(Context context, ArrayList<Chat> items) {
        this.context = context;
        this.items = items;
    }

    public void setOnClickListener(OnItemSelected callback) {
        this.callback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_chat, parent, false);
        return new ViewHolder(v);
    }

    public void update(ArrayList<Chat> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Chat item = items.get(position);
        final Contact contact = DbHelper.getInstance(context)
                .getContact(item.getContactId());
        holder.name.setText(
                Utils.getContactName(context, contact.getPhoneNumber()));
        holder.lastMsg.setText(item.getLastMsg());
        holder.photo.setImageDrawable(null);
        Picasso.with(context).load(new File(Constants.getProfileThumbFolder(context)
                + File.separator
                + contact.getPhoneNumber() + ".jpg"))
                .placeholder(R.drawable.ic_account_black_56dp)
                .into(holder.photo);
        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback != null) callback.itemSelected(items.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        private final CircleImageView photo;
        private final TextView name, lastMsg, time;
        private final View mainView;

        public ViewHolder(View itemView) {
            super(itemView);
            mainView = itemView;
            photo = (CircleImageView) itemView.findViewById(R.id.img);
            name = (TextView) itemView.findViewById(R.id.name);
            lastMsg = (TextView) itemView.findViewById(R.id.last_text);
            time = (TextView) itemView.findViewById(R.id.time);
        }
    }

    public interface OnItemSelected {
        void itemSelected(Chat chat);
    }

}

