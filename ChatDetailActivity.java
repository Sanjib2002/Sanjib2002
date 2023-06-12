package com.example.yowhatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.yowhatsapp.Adapters.ChatAdapter;
import com.example.yowhatsapp.Models.MessageModel;
import com.example.yowhatsapp.databinding.ActivityChatDetailBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ChatDetailActivity extends AppCompatActivity {

    ActivityChatDetailBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        final String senderId = auth.getUid();
        String recieveId = getIntent().getStringExtra("userId");
        String username = getIntent().getStringExtra("userName");
        String profilepic = getIntent().getStringExtra("profilepic");

        binding.userName.setText(username);
        Picasso.get().load(profilepic).placeholder(R.drawable.avatar).into(binding.profileImage);
        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatDetailActivity.this , MainActivity.class);
                startActivity(intent);
            }
        });

        final ArrayList<MessageModel> MessageModels = new ArrayList<>();
        final ChatAdapter chatAdapter = new ChatAdapter(MessageModels,this);
        binding.chatRecyclarView.setAdapter(chatAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatRecyclarView.setLayoutManager(layoutManager);

        final String senderRoom = senderId + recieveId;
        final String recieverRoom = recieveId + senderId;

        database.getReference().child("chats")
                .child(senderRoom)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                MessageModels.clear();
                                for (DataSnapshot snapshot1 : snapshot.getChildren())
                                {
                                    MessageModel model =snapshot1.getValue(MessageModel.class);
                                    model.setMessageId(snapshot1.getKey());
                                    MessageModels.add(model);
                                }
                                    chatAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = binding.etMessage.getText().toString();
                final  MessageModel model = new MessageModel(senderId, message);
                model.setTimestamp(new Date().getTime());
                binding.etMessage.setText("");

                database.getReference().child("chats")
                        .child(senderRoom)
                        .push()
                        .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                database.getReference().child("chats")
                                        .child(recieverRoom)
                                        .push()
                                        .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                
                                            }
                                        });
                            }
                        });
            }
        });
    }
}