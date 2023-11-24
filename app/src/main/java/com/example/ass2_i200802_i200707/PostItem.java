package com.example.ass2_i200802_i200707;

import static okhttp3.internal.http.HttpDate.format;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PostItem extends AppCompatActivity {
    private final int UPLOAD_IMG_CODE = 1000;
    private final int UPLOAD_VID_CODE = 1200;
    ImageView uploadPhoto;
    String photoUrl, itemId;
    Button postItemButton;
    EditText name, price, description;
    User user = CurrentUserDataHolder.getInstance().getUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_item);

        name = findViewById(R.id.nameTextBox);
        price = findViewById(R.id.hourlyRateTextBox);
        description = findViewById(R.id.descriptionBox);

        ImageView back = findViewById(R.id.crossIcon);
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        uploadPhoto = findViewById(R.id.uploadPhotoButton);
        uploadPhoto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, UPLOAD_IMG_CODE);
            }
        });

        ImageView vid = findViewById(R.id.uploadVidButton);
        vid.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("video/*");
                startActivityForResult(intent, UPLOAD_VID_CODE);
            }
        });

        postItemButton = findViewById(R.id.postItemButton);
        postItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = name.getText().toString();
                String itemPrice = price.getText().toString();
                String itemDescription = description.getText().toString();
                String userId = String.valueOf(user.getUserId());
                //get today's date
                Date today = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-LLL-yy", Locale.getDefault());
                String itemDate = dateFormat.format(today);

                //Add to db
                intitateItemDataPostRequest(itemName, photoUrl, itemPrice, itemDescription, userId, itemDate, new RegistrationCallback() {
                            @Override
                            public void onRegistrationComplete(boolean success) {
                                if (success) {

                                    // Send data to home
                                    Intent intent = new Intent();
                                    intent.putExtra("itemImg", photoUrl);
                                    intent.putExtra("itemName", itemName);
                                    intent.putExtra("itemPrice", itemPrice);
                                    intent.putExtra("itemDescription", itemDescription);
                                    intent.putExtra("userId", userId);
                                    intent.putExtra("itemId", itemId);
                                    intent.putExtra("itemDate", itemDate);

                                    setResult(RESULT_OK, intent);
                                    finish();

                                } else {
                                    Toast.makeText(PostItem.this, "Failed to Post Item", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == UPLOAD_IMG_CODE && data != null)
        {
            Uri selectedImg = data.getData();
            grantUriPermission(getPackageName(), selectedImg, Intent.FLAG_GRANT_READ_URI_PERMISSION);

            photoUrl = selectedImg.toString();
            Toast.makeText(PostItem.this, "Image uploaded successfully", Toast.LENGTH_LONG).show();
        }

        if(resultCode == RESULT_OK && requestCode == UPLOAD_VID_CODE && data != null)
        {
            Uri selectedImg = data.getData();
            grantUriPermission(getPackageName(), selectedImg, Intent.FLAG_GRANT_READ_URI_PERMISSION);

            photoUrl = selectedImg.toString();
            Toast.makeText(PostItem.this, "Video uploaded successfully", Toast.LENGTH_LONG).show();
        }
    }


    private interface RegistrationCallback {
        void onRegistrationComplete(boolean success);
    }

    private void intitateItemDataPostRequest(String itemName, String photoUrl, String itemPrice, String itemDescription, String userId, String itemDate, PostItem.RegistrationCallback callback) {
        Log.d("Registration", "Initiating post req");

        String webApiUrl = "http://192.168.1.14/smd_a3/insert_item.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, webApiUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Log the full response
                        Log.d("Registration", "Response: " + response);

                        try {
                            JSONObject object = new JSONObject(response);

                            if (object.getInt("Status") == 1) {
                                Log.d("Registration", "Post success");
                                //Toast.makeText(Registration.this, response, Toast.LENGTH_SHORT).show();

                                if (object.has("userId")) {
                                    itemId = String.valueOf(object.getInt("userId"));
                                }

                                callback.onRegistrationComplete(true);
                            } else {
                                // Handle other status values if needed
                                Log.d("Registration", "Post failed with status: " + object.getInt("status"));
                                callback.onRegistrationComplete(false);
                            }

                        } catch (JSONException e) {
                            // Handle the case where the response is not a JSON object
                            Log.d("Registration", "JSONException: " + e.getMessage());
                            e.printStackTrace(); // Add this line to log the stack trace
                            callback.onRegistrationComplete(false);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        // Handle error
                        Toast.makeText(PostItem.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("Registration", "Post fail: " + e.getMessage());
                        callback.onRegistrationComplete(false);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("imgUrl", photoUrl);
                params.put("itemName", itemName);
                params.put("itemPrice", itemPrice);
                params.put("itemDescription", itemDescription);
                params.put("userId", userId);
                params.put("itemDate", itemDate);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(PostItem.this);
        queue.add(stringRequest);
    }

}

