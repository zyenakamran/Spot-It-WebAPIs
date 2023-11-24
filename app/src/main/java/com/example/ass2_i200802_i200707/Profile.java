package com.example.ass2_i200802_i200707;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {

    FirebaseAuth mAuth;
    String userId;
    String userName, userEmail, userContact, userCountry;
    TextView nameText, countryText;
    ImageView profilePic, coverPic;
    private final int EDIT_PROF_CODE = 100;
    private static final int DP_REQUEST_CODE = 110; // Request code for image picker
    private static final int COVER_REQUEST_CODE = 200; // Request code for image picker
    //User user = new User();
    User user = CurrentUserDataHolder.getInstance().getUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nameText = findViewById(R.id.nameText);
        profilePic = findViewById(R.id.profilePic);
        coverPic = findViewById(R.id.coverPic);
        countryText = findViewById(R.id.country);

        // Get from Intent
        //user = getIntent().getParcelableExtra("User");

        nameText.setText(user.getName());
        if (!user.getDisplayUrl().equals(""))
            Picasso.get().load(user.getDisplayUrl()).into(profilePic);

        if (!user.getCoverUrl().equals(""))
            Picasso.get().load(user.getCoverUrl()).into(coverPic);


        // Bottom Nav Bar
        Button home = findViewById(R.id.homeButton);
        home.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, Home.class);
                //intent.putExtra("User", (Parcelable) user);
                startActivity(intent);
            }
        });

        Button search = findViewById(R.id.searchButton);
        search.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, Search.class);
                //intent.putExtra("User", (Parcelable) user);
                startActivity(intent);
            }
        });

        Button addItem = findViewById(R.id.addButton);
        addItem.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, PostItem.class);
                //intent.putExtra("User", (Parcelable) user);
                startActivity(intent);
            }
        });

        Button chat = findViewById(R.id.chatButton);
        chat.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, Chat.class);
                //intent.putExtra("User", (Parcelable) user);
                startActivity(intent);
            }
        });

        Button profile = findViewById(R.id.profileButton);
        profile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, Profile.class);
                //intent.putExtra("User", (Parcelable) user);
                startActivity(intent);
            }
        });


        Button editProf = findViewById(R.id.editProfileButton);
        editProf.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, EditProfile.class);
                //intent.putExtra("User", (Parcelable) user);
                startActivityForResult(intent, EDIT_PROF_CODE);

            }
        });

        // photo from gallery
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, DP_REQUEST_CODE);
            }
        });
        coverPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, COVER_REQUEST_CODE);
            }
        });
    }


    protected  void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_PROF_CODE && resultCode == RESULT_OK && data != null) {

            userName = data.getStringExtra("userName");
            userEmail = data.getStringExtra("userEmail");
            userContact = data.getStringExtra("userContact");

            nameText.setText(userName);

            updateUserDataPostRequest(userName, userEmail, userContact, user.getDisplayUrl(), user.getCoverUrl(), user.getUserId(), new RegistrationCallback() {
                @Override
                public void onRegistrationComplete(boolean success) {
                    user.setName(userName);
                    user.setEmail(userEmail);
                    user.setContact(userContact);
                }
            });




        }

        if (requestCode == DP_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            String photoUrl = selectedImage.toString();

            //set dp
            Picasso.get().load(photoUrl).into(profilePic);

            //save to db
            updateUserDataPostRequest(user.getName(), user.getEmail(), user.getContact(), photoUrl, user.getCoverUrl(), user.getUserId(), new RegistrationCallback() {
                @Override
                public void onRegistrationComplete(boolean success) {
                    user.setDisplayUrl(photoUrl);
                }
            });


        }

        if (requestCode == COVER_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri selectedImg = data.getData();

            String photoUrl = selectedImg.toString();

            //set on img in xml
            Picasso.get().load(photoUrl).into(coverPic);

            updateUserDataPostRequest(user.getName(), user.getEmail(), user.getContact(), user.getDisplayUrl(), photoUrl, user.getUserId(), new RegistrationCallback() {
                @Override
                public void onRegistrationComplete(boolean success) {
                    user.setCoverUrl(photoUrl);
                }
            });

        }


    }


    private interface RegistrationCallback {
        void onRegistrationComplete(boolean success);
    }

    private void updateUserDataPostRequest(String name, String email, String contact, String displayUrl, String coverUrl, int userId, Profile.RegistrationCallback callback) {
        Log.d("Registration", "Initiating post req");

        String webApiUrl = "http://192.168.1.14/smd_a3/update_user.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, webApiUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Log the full response
                        Log.d("Profile", "Response: " + response);

                        try {
                            JSONObject object = new JSONObject(response);

                            if (object.getInt("Status") == 1) {
                                Log.d("Profile", "Post success");
                                Toast.makeText(Profile.this, response, Toast.LENGTH_SHORT).show();
                                callback.onRegistrationComplete(true);
                            } else {
                                // Handle other status values if needed
                                Log.d("Profile", "Post failed with status: " + object.getInt("status"));
                                callback.onRegistrationComplete(false);
                            }

                        } catch (JSONException e) {
                            // Handle the case where the response is not a JSON object
                            Log.d("Profile", "JSONException: " + e.getMessage());
                            e.printStackTrace(); // Add this line to log the stack trace
                            callback.onRegistrationComplete(false);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        // Handle error
                        Toast.makeText(Profile.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("Profile", "Post fail: " + e.getMessage());
                        callback.onRegistrationComplete(false);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("email", email);
                params.put("contact", contact);
                params.put("displayUrl", displayUrl);
                params.put("coverUrl", coverUrl);
                params.put("userId", String.valueOf(userId));
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(Profile.this);
        queue.add(stringRequest);
    }

}