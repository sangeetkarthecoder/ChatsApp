package com.saurav.chatsapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saurav.chatsapp.Adapters.MessagesAdapter;
import com.saurav.chatsapp.ModuleClass.Message;
import com.saurav.chatsapp.databinding.ActivityChatBinding;

import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding binding;
    MessagesAdapter adapter;
    ArrayList<Message> messages;

    String SenderRoom;
    String ReceiverRoom;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        messages = new ArrayList<>();
        adapter = new MessagesAdapter(this, messages, SenderRoom, ReceiverRoom);

        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String name = getIntent().getStringExtra("name");
        String uid = getIntent().getStringExtra("uid");
        String currentUID = FirebaseAuth.getInstance().getUid();

        SenderRoom = currentUID+uid;
        ReceiverRoom = uid+currentUID;
        database = FirebaseDatabase.getInstance();

        database.getReference().child("chats")
                        .child(SenderRoom)
                                .child("messages")
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                messages.clear();
                                                for(DataSnapshot snapshot1: snapshot.getChildren()) {
                                                    Message message = snapshot1.getValue(Message.class);
                                                    assert message != null;
                                                    message.setMsg_id(snapshot1.getKey());
                                                    messages.add(message);
                                                }
                                                adapter.notifyDataSetChanged();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

        binding.sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String msgTxt = binding.messagebox.getText().toString();

                if(msgTxt!="") {

                    Date date = new Date();

                    Message message = new Message(msgTxt, uid, date.getTime());
                    binding.messagebox.setText("");

                    String randomKey = database.getReference().push().getKey();

                    assert randomKey != null;
                    database.getReference().child("chats")
                            .child(SenderRoom)
                            .child("messages")
                            .child(randomKey)
                            .setValue(message)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                    database.getReference().child("chats")
                                            .child(ReceiverRoom)
                                            .child("messages")
                                            .child(randomKey)
                                            .setValue(message)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {

                                                }
                                            });

                                }
                            });
                } else{
                    binding.messagebox.setError("Please type a message");
                    return;
                }
            }
        });

        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}