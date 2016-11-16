package com.architjn.myapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.architjn.myapp.R;
import com.architjn.myapp.model.Conversation;
import com.architjn.myapp.utils.Constants;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by HP on 21-05-2016.
 */

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Conversation> items;
    private OnItemSelected callback = null;
    private final int SPACE = 0;
    private final int SENT_CHAT = 1;
    private final int RECEIVED_CHAT = 2;

    public ConversationAdapter(Context context, ArrayList<Conversation> items) {
        this.context = context;
        this.items = items;
    }

    public void setOnClickListener(OnItemSelected callback) {
        this.callback = callback;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int viewId = R.layout.list_item_chat_bubble;
        if (viewType == SPACE)
            viewId = R.layout.space_view;
        if (viewType == SENT_CHAT)
            viewId = R.layout.list_item_chat_bubble_right;
        View v = LayoutInflater.from(parent.getContext()).inflate(viewId, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (position == items.size())
            return;
        holder.userImg.setVisibility(View.INVISIBLE);
        setBackground(holder, position);
        final Conversation item = items.get(position);
        holder.text.setText(item.getMessage());
        if (items.get(position).isSent()) {
            holder.userImg.setImageDrawable(null);
            Picasso.with(context).load(new File(Constants.getProfileThumbFolder(context)
                    + File.separator
                    + item.getSenderId() + ".jpg"))
                    .placeholder(R.drawable.ic_account_black_56dp)
                    .into(holder.userImg);
        }
        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback != null) callback.itemSelected(item);
            }
        });
    }

    private void setBackground(ViewHolder holder, int position) {
        if (items.get(position).isSent()) {
            if (position == 0) {
                if (items.size() - 1 != position && items.get(position + 1).isSent()) {
                    holder.bgHolder.setBackgroundResource(R.drawable.chat_bubble_left_top);
                    holder.userImg.setVisibility(View.VISIBLE);
                    holder.userImg.setImageResource(R.drawable.ic_account_black_56dp);
                } else {
                    holder.bgHolder.setBackgroundResource(R.drawable.chat_bubble_left);
                    holder.userImg.setVisibility(View.VISIBLE);
                    holder.userImg.setImageResource(R.drawable.ic_account_black_56dp);
                }
            } else if (items.get(position - 1).isSent()) {
                if (items.size() - 1 != position && items.get(position + 1).isSent())
                    holder.bgHolder.setBackgroundResource(R.drawable.chat_bubble_left_middle);
                else holder.bgHolder.setBackgroundResource(R.drawable.chat_bubble_left_bottom);
            } else {
                if (items.size() - 1 != position && items.get(position + 1).isSent()) {
                    holder.bgHolder.setBackgroundResource(R.drawable.chat_bubble_left_top);
                    holder.userImg.setVisibility(View.VISIBLE);
                    holder.userImg.setImageResource(R.drawable.ic_account_black_56dp);
                } else {
                    holder.bgHolder.setBackgroundResource(R.drawable.chat_bubble_left);
                    holder.userImg.setVisibility(View.VISIBLE);
                    holder.userImg.setImageResource(R.drawable.ic_account_black_56dp);
                }
            }
        } else {
            if (position == 0) {
                if (position + 1 != items.size() && items.get(position + 1).isSent())
                    holder.bgHolder.setBackgroundResource(R.drawable.chat_bubble_right);
                else if (position + 1 == items.size())
                    holder.bgHolder.setBackgroundResource(R.drawable.chat_bubble_right);
                else holder.bgHolder.setBackgroundResource(R.drawable.chat_bubble_right_top);
            } else if (!items.get(position - 1).isSent()) {
                if (position + 1 != items.size() && !items.get(position + 1).isSent())
                    holder.bgHolder.setBackgroundResource(R.drawable.chat_bubble_right_middle);
                else holder.bgHolder.setBackgroundResource(R.drawable.chat_bubble_right_bottom);
            } else if (position + 1 == items.size()) {
                holder.bgHolder.setBackgroundResource(R.drawable.chat_bubble_right);
            } else {
                if (position + 1 != items.size() && items.get(position + 1).isSent())
                    holder.bgHolder.setBackgroundResource(R.drawable.chat_bubble_right);
                else holder.bgHolder.setBackgroundResource(R.drawable.chat_bubble_right_top);
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size() + 1;//Plus one for extra spacing
    }

    public void updateItems(ArrayList<Conversation> contacts) {
        this.items = contacts;
        notifyDataSetChanged();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView text;
        private final View mainView, bgHolder;
        private final CircleImageView userImg;

        ViewHolder(View itemView) {
            super(itemView);
            mainView = itemView;
            text = (TextView) itemView.findViewById(R.id.text);
            bgHolder = itemView.findViewById(R.id.bg_holder);
            userImg = (CircleImageView) itemView.findViewById(R.id.user_img);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (items.size() == position)
            return SPACE;
        if (items.get(position).isSent())
            return RECEIVED_CHAT;
        return SENT_CHAT;
    }

    interface OnItemSelected {
        void itemSelected(Conversation conv);
    }

}

