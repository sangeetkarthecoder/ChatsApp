package com.saurav.chatsapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.pgreze.reactions.ReactionPopup;
import com.github.pgreze.reactions.ReactionsConfig;
import com.github.pgreze.reactions.ReactionsConfigBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.saurav.chatsapp.ModuleClass.Message;
import com.saurav.chatsapp.R;
import com.saurav.chatsapp.databinding.ItemReceiveBinding;
import com.saurav.chatsapp.databinding.ItemSendBinding;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter {

    final int ITEM_SENT = 1;
    final int ITEM_RECEIVED = 2;

    String SenderRoom;
    String ReceiverRoom;

    Context context;
    ArrayList<Message> messages;

    public MessagesAdapter(Context context, ArrayList<Message> messages, String SenderRoom, String ReceiverRoom) {
        this.context = context;
        this.SenderRoom = SenderRoom;
        this.ReceiverRoom = ReceiverRoom;
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

        int [] reactions = new int[]{
                R.drawable.ic_fb_like,
                R.drawable.ic_fb_love,
                R.drawable.ic_fb_laugh,
                R.drawable.ic_fb_wow,
                R.drawable.ic_fb_sad,
                R.drawable.ic_fb_angry
        };

        ReactionsConfig config = new ReactionsConfigBuilder(context)
                .withReactions(reactions)
                .build();

        ReactionPopup popup = new ReactionPopup(context, config, (pos) -> {

            if(holder.getClass()==SentViewHolder.class) {
                SentViewHolder viewHolder = (SentViewHolder) holder;
                viewHolder.binding.feeling.setImageResource(reactions[pos]);
                viewHolder.binding.feeling.setVisibility(View.VISIBLE);
            } else {
                ReceiveViewHolder viewHolder = (ReceiveViewHolder) holder;
                viewHolder.binding.feeling.setImageResource(reactions[pos]);
                viewHolder.binding.feeling.setVisibility(View.VISIBLE);
            }

            message.setFeeling(pos);

            FirebaseDatabase.getInstance().getReference().child("chats")
                    .child(SenderRoom)
                    .child("messages")
                    .child(message.getMsg_id()).setValue(message);

            FirebaseDatabase.getInstance().getReference().child("chats")
                    .child(ReceiverRoom)
                    .child("messages")
                    .child(message.getMsg_id()).setValue(message);


            return true; // true is closing popup, false is requesting a new selection
        });


        if(holder.getClass()==SentViewHolder.class) {
            SentViewHolder viewHolder = (SentViewHolder) holder;
            viewHolder.binding.msg.setText(message.getMsg());


            if (message.getFeeling()>=0) {
                viewHolder.binding.feeling.setImageResource(reactions[(int) message.getFeeling()]);
                viewHolder.binding.feeling.setVisibility(View.VISIBLE);
            }
            else{
                viewHolder.binding.feeling.setVisibility(View.GONE);
            }


            viewHolder.binding.msg.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    popup.onTouch(v, event);
                    return false;
                }
            });
        }
        else {
            ReceiveViewHolder viewHolder = (ReceiveViewHolder) holder;
            viewHolder.binding.msg.setText(message.getMsg());

            if (message.getFeeling()>=0) {
                viewHolder.binding.feeling.setImageResource(reactions[(int) message.getFeeling()]);
                viewHolder.binding.feeling.setVisibility(View.VISIBLE);
            }
            else{
                viewHolder.binding.feeling.setVisibility(View.GONE);
            }

            viewHolder.binding.msg.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    popup.onTouch(v, event);
                    return false;
                }
            });
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
