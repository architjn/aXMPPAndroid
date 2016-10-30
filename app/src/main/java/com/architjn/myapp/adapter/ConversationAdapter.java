package com.architjn.myapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.architjn.myapp.R;
import com.architjn.myapp.model.Contact;
import com.architjn.myapp.model.Conversation;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by HP on 21-05-2016.
 */

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Conversation> items;
    private OnItemSelected callback = null;
    private final int SENT_CHAT = 0;
    private final int RECEIVED_CHAT = 1;

    public ConversationAdapter(Context context, ArrayList<Conversation> items) {
        this.context = context;
        this.items = items;
    }

    public void setOnClickListener(OnItemSelected callback) {
        this.callback = callback;
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position).isReceived())
            return RECEIVED_CHAT;
        return SENT_CHAT;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int viewId = R.layout.list_item_chat_bubble;
        if (viewType == SENT_CHAT)
            viewId = R.layout.list_item_chat_bubble_right;
        View v = LayoutInflater.from(parent.getContext()).inflate(viewId, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Conversation item = items.get(position);
        holder.text.setText(item.getMessage());
        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback != null) callback.itemSelected(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateItems(ArrayList<Conversation> contacts) {
        this.items = contacts;
        notifyDataSetChanged();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView text;
        private final View mainView;

        ViewHolder(View itemView) {
            super(itemView);
            mainView = itemView;
            text = (TextView) itemView.findViewById(R.id.text);
        }
    }

    interface OnItemSelected {
        void itemSelected(Conversation conv);
    }

}

