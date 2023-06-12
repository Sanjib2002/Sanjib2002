package com.example.yowhatsapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yowhatsapp.ChatDetailActivity;

import com.example.yowhatsapp.Models.MessageModel;
import com.example.yowhatsapp.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter {

    public ChatAdapter(ArrayList<MessageModel> messageModels, ChatDetailActivity chatDetailActivity) {
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class RecieverViewHolder extends RecyclerView.ViewHolder {
        ArrayList<MessageModel> MessageModels;
        Context context;
        int SENDER_VIEW_TYPE = 1;
        int RECIEVER_VIEW_TYPE = 2;

        public RecieverViewHolder(@NonNull View itemView, ArrayList<MessageModel> messageModels, Context context) {
            super(itemView);
            MessageModels = messageModels;
            this.context = context;
        }

        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == SENDER_VIEW_TYPE) {
                View view = LayoutInflater.from(context).inflate(R.layout.sample_sender, parent, false);
                return new senderViewHolder(view);
            } else {
                View view = LayoutInflater.from(context).inflate(R.layout.sample_reciever, parent, false);
                return new recieverViewHolder(view);
            }
        }

        public int getItemViewType(int position) {
            if (MessageModels.get(position).getuId().equals(FirebaseAuth.getInstance().getUid())) {
                return SENDER_VIEW_TYPE;
            } else {
                return RECIEVER_VIEW_TYPE;
            }
        }

        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            MessageModel messageModels = MessageModels.get(position);
            if (holder.getClass() == senderViewHolder.class) {
                ((senderViewHolder) holder).senderMsg.setText(messageModels.getMessage());
            }
            else {
                ((recieverViewHolder) holder).recieverMsg.setText(messageModels.getMessage());
            }


        }

        public int getItemCount() {
            return MessageModels.size();
        }

        public class recieverViewHolder extends RecyclerView.ViewHolder {
            TextView recieverMsg, recieverTime;

            public recieverViewHolder(@NonNull View itemView) {
                super(itemView);
                recieverMsg = itemView.findViewById(R.id.recieverText);
                recieverTime = itemView.findViewById(R.id.recieverTime);

            }
        }

        public class senderViewHolder extends RecyclerView.ViewHolder {
            TextView senderMsg, senderTime;

            public senderViewHolder(@NonNull View itemView) {
                super(itemView);
                senderMsg = itemView.findViewById(R.id.senderText);
                senderTime = itemView.findViewById(R.id.senderTime);


            }
        }
    }
}
