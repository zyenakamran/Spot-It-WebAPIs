package com.example.ass2_i200802_i200707;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<MessageModel> ls;
    String displayUrl;
    private static final int VIEW_TYPE_TEXT = 15;
    private static final int VIEW_TYPE_IMAGE = 25;
    private static final int VIEW_TYPE_AUDIO = 35;


    public MessageAdapter(Context context, List<MessageModel> ls) {
        this.context=context;
        this.ls=ls;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == VIEW_TYPE_TEXT) {
            View itemView = inflater.inflate(R.layout.text_msg_view, parent, false);
            return new TextMessageViewHolder(itemView);
        } else if (viewType == VIEW_TYPE_IMAGE) {
            View itemView = inflater.inflate(R.layout.img_msg_view, parent, false);
            return new ImageMessageViewHolder(itemView);
        }
        else if (viewType == VIEW_TYPE_AUDIO) {
            View itemView = inflater.inflate(R.layout.voice_msg_view, parent, false);
            return new AudioMessageViewHolder(itemView);
        }

        return null;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        MessageModel message = ls.get(position);

        if (holder instanceof TextMessageViewHolder) {
            TextMessageViewHolder textHolder = (TextMessageViewHolder) holder;
            textHolder.msg.setText(message.getMsg());
            textHolder.timestamp.setText(message.getTimestamp());

            // if exists load display pic
            if (message.getDisplayUrl() != null && !message.getDisplayUrl().isEmpty()) {
                Picasso.get().load(message.getDisplayUrl()).into(textHolder.dp);
            }
            textHolder.edit_msg.setTag(position); // pos is a tag at edit button
            textHolder.edit_msg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //get pos from tag
                    int position = (int) v.getTag();

                    openEditMessageDialog(message);
                }

                private void openEditMessageDialog(MessageModel message) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Edit Message");

                    final EditText editMessageInput = new EditText(context);
                    editMessageInput.setText(message.getMsg());
                    builder.setView(editMessageInput);

                    builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String editedMessage = editMessageInput.getText().toString();

                            //Update xml
                            message.setMsg(editedMessage);
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                            String timestamp= sdf.format(new Date());
                            message.setTimestamp(timestamp);
                            notifyItemChanged(position);


                            // msg update in db
                            textHolder.msg.setText(editedMessage);
                            textHolder.timestamp.setText(timestamp);
                            String messageId = ls.get(position).getMessageId();

                            DatabaseReference messages = FirebaseDatabase.getInstance().getReference("Messages").child(messageId);
                            messages.child("msg").setValue(editedMessage);

                            Toast.makeText(context, "Message edited successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }
            });


        } else if (holder instanceof ImageMessageViewHolder) {
            ImageMessageViewHolder imageHolder = (ImageMessageViewHolder) holder;

            String imageUrl = message.getMsg();
            Picasso.get().load(imageUrl).into(imageHolder.img_msg);

            imageHolder.timestamp.setText(message.getTimestamp());

        }
        else if (holder instanceof AudioMessageViewHolder) {
            // audio msg viewholder binded with vm
            AudioMessageViewHolder audioHolder = (AudioMessageViewHolder) holder;

            audioHolder.bindData(message.getMsg()); // Set audio URL

            audioHolder.timestamp.setText(message.getTimestamp());
            // senders dp if req
            if (message.getDisplayUrl() != null && !message.getDisplayUrl().isEmpty()) {
                Picasso.get().load(message.getDisplayUrl()).into(audioHolder.dp);
            }
        }

    }

    @Override
    public int getItemCount() {
        return ls.size();
    }


    @Override
    public int getItemViewType(int position) {
        MessageModel message = ls.get(position);

        if (message != null) {
            switch (message.getMessageType()) {
                case "TEXT":
                    return VIEW_TYPE_TEXT;
                case "IMAGE":
                    return VIEW_TYPE_IMAGE;
                case "AUDIO":
                    return VIEW_TYPE_AUDIO;
            }
        }
        return VIEW_TYPE_TEXT;
    }


    private class TextMessageViewHolder extends RecyclerView.ViewHolder {
        TextView msg,timestamp;
        CircleImageView dp;
        ImageView edit_msg, deleteButton;
        public TextMessageViewHolder(View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.msg);
            timestamp = itemView.findViewById(R.id.timestamp);
            dp = itemView.findViewById(R.id.displayPic);
            edit_msg =itemView.findViewById(R.id.editMsg);
            deleteButton = itemView.findViewById(R.id.deleteMsg); // Initialize the deleteButton

            // Set an OnClickListener for the delete button
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        deleteMessage(position);
                    }
                }
            });
        }
    }

    private void deleteMessage(int position) {
        MessageModel message = ls.get(position);
        String messageId = message.getMessageId();

        DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference("Messages").child(messageId);
        messagesRef.removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Remove the message from the list
                            ls.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, ls.size());
                            Toast.makeText(context, "Message deleted successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            // Handle the error, e.g., show a toast
                            Toast.makeText(context, "Failed to delete the message", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



    private class ImageMessageViewHolder extends RecyclerView.ViewHolder {
        ImageView img_msg;
        TextView timestamp;

        public ImageMessageViewHolder(View itemView) {
            super(itemView);
            img_msg = itemView.findViewById(R.id.img);
            timestamp = itemView.findViewById(R.id.timestamp);
        }
    }

    private class AudioMessageViewHolder extends RecyclerView.ViewHolder {
        TextView timestamp;
        CircleImageView dp;
        private MediaPlayer mediaPlayer;
        private String audioUrl;

        public AudioMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            timestamp = itemView.findViewById(R.id.timestamp);
            dp = itemView.findViewById(R.id.displayPic);
/*
            int position = getAdapterPosition();
            MessageModel message = ls.get(position);
            String messageId = message.getMessageId();
            DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference("Messages").child(messageId);

            messagesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        MessageModel newItem = snapshot.getValue(MessageModel.class);

                        if(newItem.getMessageId().equals(message.getMessageId()))
                        {
                            audioUrl = newItem.getMsg();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }


            });

 */

            ImageView playButton = itemView.findViewById(R.id.playButton);

            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mediaPlayer == null) {
                        Log.d("TAG", "onClick: PLAY BUTTON");
                        startAudioPlayback();
                        playButton.setImageResource(R.drawable.pause);
                    } else {
                        stopAudioPlayback();
                        playButton.setImageResource(R.drawable.play);
                    }
                }
            });
        }



        private void startAudioPlayback() {
            mediaPlayer = new MediaPlayer();
            try {

                Log.d("audioURL", audioUrl );
                mediaPlayer.setDataSource(audioUrl);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void stopAudioPlayback() {
            if (mediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer = null;
            }
        }

        public void bindData(String audioUrl) {
            this.audioUrl = audioUrl;
        }
    }
}
