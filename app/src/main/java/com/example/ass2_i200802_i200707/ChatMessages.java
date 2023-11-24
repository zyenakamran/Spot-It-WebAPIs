package com.example.ass2_i200802_i200707;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallController;
import com.sinch.android.rtc.calling.CallControllerListener;
import com.sinch.android.rtc.calling.CallListener;
import com.sinch.android.rtc.calling.MediaConstraints;
import com.sinch.android.rtc.internal.natives.jni.CallClient;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatMessages extends AppCompatActivity {
    private static final int CAPTURED_CODE = 334;
    String count;
    RecyclerView chatMsgRV;
    MessageAdapter adapter;
    List<MessageModel> ls;
    EditText textReply;
    TextView sendButton;
    ImageView galleryIcon, audioIcon, voiceCall;
    private SinchClient sinchClient = null;

    private static final int CAPTURE_CODE = 333;
    private static final int GALLERY_REQUEST_CODE = 200; // Request code for image
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 202;

    private static final int VOICE_RECORD_REQUEST_CODE = 201; // Request code for voice recording
    private MediaRecorder mediaRecorder;
    private String voiceNoteFileName;

    Uri capture_image;

    String dpurl = "https://tinyurl.com/4xz7w35b";
    SqliteHelper sqliteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_messages);

        textReply = findViewById(R.id.textReply);
        sendButton = findViewById(R.id.sendButton);
        galleryIcon = findViewById(R.id.galleryIcon);
        audioIcon = findViewById(R.id.audioIcon);

        chatMsgRV = findViewById(R.id.chatMsgRV);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ChatMessages.this);

        ls = new ArrayList<>();
        adapter = new MessageAdapter(ChatMessages.this, ls);
        chatMsgRV.setAdapter(adapter);
        chatMsgRV.setLayoutManager(layoutManager);

        // VOICE CALL
        voiceCall = findViewById(R.id.voiceCall); // Assuming you have a call button in your layout
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String id= currentUser.getUid();
        voiceCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ChatMessages.this, "Voice Call Triggered", Toast.LENGTH_SHORT).show();
                // Code to initiate the call
                try {
                    initiateCall();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            private void initiateCall() throws IOException {
                Toast.makeText(ChatMessages.this, "Voice Call Inititating", Toast.LENGTH_SHORT).show();

                Toast.makeText(ChatMessages.this, "sinchClient" + sinchClient, Toast.LENGTH_SHORT).show();
                if (sinchClient == null) {

                    sinchClient = SinchClient.builder()
                            .context(ChatMessages.this)
                            .userId(id)
                            .applicationKey("932d891a-1818-4fac-ae8e-103a7c7c2fcd")
                            .environmentHost("sandbox.sinch.com")
//                           .environmentHost("clientapi.sinch.com")
                            .build();

                    Toast.makeText(ChatMessages.this, "sinchClient" + sinchClient, Toast.LENGTH_SHORT).show();
                }
                sinchClient.addSinchClientListener(new SinchClientListener());

                // Start the SinchClient
                sinchClient.start();
                if (!sinchClient.isStarted()) {
                    sinchClient.start();
                }
                Log.d("sinch Client ", "Call Initiated :  " + sinchClient);

                String caller_id = "+447520650944"; // Test number for your product


                CallController callController = sinchClient.getCallController();
                Call call = (Call) callController.callUser("ONL2310_4A8hkn", new MediaConstraints(false));

               call.addCallListener(new CallListener() {
                   @Override
                   public void onCallProgressing(@NonNull Call call) {
                       Toast.makeText(ChatMessages.this, "calling . . .", Toast.LENGTH_SHORT).show();
                   }

                   @Override
                   public void onCallEstablished(@NonNull Call call) {

                       Toast.makeText(ChatMessages.this, "call established . . .", Toast.LENGTH_SHORT).show();
                   }

                   @Override
                   public void onCallEnded(@NonNull Call call) {



                   }
               });

               /* callController.addCallControllerListener(new CallControllerListener() {

                    @Override
                    public void onIncomingCall(@NonNull CallController callController, @NonNull com.sinch.android.rtc.calling.Call call) {

                        call.addCallListener(new CallListener() {
                            @Override
                            public void onCallProgressing(@NonNull com.sinch.android.rtc.calling.Call call) {
                                Toast.makeText(ChatMessages.this, "Calling . . .", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCallEstablished(@NonNull com.sinch.android.rtc.calling.Call call) {
                                Toast.makeText(ChatMessages.this, "Call Established", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCallEnded(@NonNull com.sinch.android.rtc.calling.Call call) {
                                Toast.makeText(ChatMessages.this, "Call Ended", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
*/

            }


        });

        // SCREENSHOT DETECTION
        ScreenshotDetectionContentObserver contentObserver;
        contentObserver = new ScreenshotDetectionContentObserver(new Handler(), ChatMessages.this);
        getContentResolver().registerContentObserver(Uri.parse("content://media/external_primary/images/media"), true, contentObserver);

        chatMsgRV = findViewById(R.id.chatMsgRV);
        layoutManager = new LinearLayoutManager(ChatMessages.this);

        ls = new ArrayList<>();
        adapter = new MessageAdapter(ChatMessages.this, ls);
        chatMsgRV.setAdapter(adapter);
        chatMsgRV.setLayoutManager(layoutManager);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference messages = database.getReference("Messages");


        audioIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaRecorder == null) {
                    startRecording();
                    audioIcon.setImageResource(R.drawable.stop);
                } else {
                    stopRecording();
                    audioIcon.setImageResource(R.drawable.audio);
                }
            }

            private void startRecording() {

                // Check for audio recording permission at runtime
                @NonNull String permission = "Manifest.permission.RECORD_AUDIO";

                if (ContextCompat.checkSelfPermission(ChatMessages.this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ChatMessages.this, new String[]{android.Manifest.permission.RECORD_AUDIO}, VOICE_RECORD_REQUEST_CODE);
                    //return;

               }
                if (ContextCompat.checkSelfPermission(ChatMessages.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ChatMessages.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                }


                Log.d("C", "permission done");

                mediaRecorder = new MediaRecorder();
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

                // Use a timestamp to create a unique filename
                voiceNoteFileName = "voice_" + System.currentTimeMillis() + ".3gp";
                mediaRecorder.setOutputFile(voiceNoteFileName);

                try {
                    mediaRecorder.prepare();
                    mediaRecorder.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            private void stopRecording() {
                if (mediaRecorder != null) {
                    mediaRecorder.reset();
                    mediaRecorder.release();
                    mediaRecorder = null;

                    // Save in db
                    if (voiceNoteFileName != null) {

                        DatabaseReference messages = FirebaseDatabase.getInstance().getReference("Messages");
                        String msg_id = messages.push().getKey();

                        MessageModel voice_note_msg = new MessageModel(voiceNoteFileName, dpurl, getCurrentTimestamp(), "AUDIO", id);
                        voice_note_msg.setMessageId(msg_id);
                        messages.push().setValue(voice_note_msg);

                    }
                }
            }
        });

        // SEND MSG
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the message text
                String messageText = textReply.getText().toString().trim();

                if (!messageText.isEmpty()) {
                    count = String.valueOf(System.currentTimeMillis());

                    String msg_id = "message_" + count;

                    DatabaseReference messageRef = messages.child(msg_id);

                    MessageModel message = new MessageModel(messageText, dpurl, getCurrentTimestamp(), "TEXT", id);
                    message.setMessageId(msg_id);
                    messageRef.setValue(message);

                    //empty the edit text
                    textReply.setText("");
                }
            }

        });

        //SEND IMG MSG
        galleryIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, GALLERY_REQUEST_CODE);
            }
        });


        //msgs
        messages.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ls.add(snapshot.getValue(MessageModel.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });


        ImageView back = findViewById(R.id.backArrow);
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        ImageView camera = findViewById(R.id.cameraIcon);
        camera.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        String[] permission = {android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission, CAPTURE_CODE);


                    }
                    else {
                        openCamera();
                    }
                }
                else {
                    openCamera();
                }


                //Intent intent = new Intent(ChatMessages.this, Photo.class);
                //startActivity(intent);
            }

        });


        ImageView videoCall = findViewById(R.id.videoCall);
        videoCall.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatMessages.this, VideoCall.class);
                startActivity(intent);
            }
        });


    }

    private void openCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Capture");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "FROM CAMERA");
        capture_image = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, capture_image);
        startActivityForResult(cameraIntent, CAPTURED_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAPTURE_CODE: {
                if (grantResults.length > 0 && grantResults[0] ==  PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                }
                else {
                    Toast.makeText(ChatMessages.this, "Permission denied", Toast.LENGTH_SHORT).show();
                }

            }

        }

    }

    private String getCurrentTimestamp() {
        // Helper method to get the current timestamp as a string
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(new Date());
    }

    private void loadPreviousChat() {
        ls.clear();
        List<MessageModel> previousMessages = sqliteHelper.getAllMessages();
        ls.addAll(previousMessages);
        adapter.notifyDataSetChanged();
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String id= currentUser.getUid();

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            grantUriPermission(getPackageName(), selectedImageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            String imageUrl = selectedImageUri.toString();

            MessageModel imageMessage = new MessageModel(imageUrl, dpurl, getCurrentTimestamp(), "IMAGE", id);

            DatabaseReference messages = FirebaseDatabase.getInstance().getReference("Messages");
            messages.push().setValue(imageMessage);
        }

        if (requestCode == CAPTURED_CODE && resultCode == RESULT_OK ) {
            Uri selectedImageUri = data.getData();
            grantUriPermission(getPackageName(), selectedImageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            String imageUrl = selectedImageUri.toString();

            MessageModel imageMessage = new MessageModel(imageUrl, dpurl, getCurrentTimestamp(), "IMAGE", id);

            DatabaseReference messages = FirebaseDatabase.getInstance().getReference("Messages");
            messages.push().setValue(imageMessage);
        }
    }
}