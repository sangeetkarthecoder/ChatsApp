package com.saurav.chatsapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.saurav.chatsapp.ModuleClass.Message;
import com.saurav.chatsapp.R;
import com.saurav.chatsapp.databinding.ItemReceiveBinding;
import com.saurav.chatsapp.databinding.ItemSendBinding;

import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter {

    final int ITEM_SENT = 1;
    final int ITEM_RECEIVED = 2;

    Context context;
    ArrayList<Message> messages;

    public MessagesAdapter(Context context, ArrayList<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == ITEM_SENT) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_send, parent, false);
            return new SentViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(context).inflate(R.layout.item_receive, parent, false);
            return new ReceiveViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if(FirebaseAuth.getInstance().getUid().equals(message.getSender_id())) {
            return ITEM_SENT;
        }
        else{
            return ITEM_RECEIVED;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);
        if(holder.getClass()==SentViewHolder.class) {
            SentViewHolder viewHolder = (SentViewHolder) holder;
            viewHolder.binding.msg.setText(message.getMsg());
        }
        else {
            ReceiveViewHolder viewHolder = (ReceiveViewHolder) holder;
            viewHolder.binding.msg.setText(message.getMsg());
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class SentViewHolder extends RecyclerView.ViewHolder {

        ItemSendBinding binding;
        public SentViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemSendBinding.bind(itemView);
        }
    }

    public class ReceiveViewHolder extends RecyclerView.ViewHolder {

        ItemReceiveBinding binding;
        public ReceiveViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemReceiveBinding.bind(itemView);
        }
    }

}
