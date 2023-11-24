package com.example.ass2_i200802_i200707;

import static com.android.volley.Request.Method.POST;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity
{
    Button signUp;
    TextView login;
    EditText emailTextBox, passwordTextBox, nameTextBox;
    FirebaseAuth mAuth;
    String webApiUrl;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        emailTextBox =  findViewById(R.id.emailTextBox);
        passwordTextBox =  findViewById(R.id.passwordTextBox);
        nameTextBox =  findViewById(R.id.nameTextBox);
        signUp = findViewById(R.id.signUpButton);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                Log.d("Registration", "signup clicked");
                intitateUserDataPostRequest(nameTextBox.getText().toString(), emailTextBox.getText().toString(), "", "", "", passwordTextBox.getText().toString(), new RegistrationCallback() {
                            @Override
                            public void onRegistrationComplete(boolean success) {
                                Log.d("Registration", "success recvd: " + success);
                                // The success variable now holds the result of the registration
                                if (success) {
                                    // Registration was successful, proceed with the next steps
                                    Toast.makeText(Registration.this, "Sign Up Successful", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(Registration.this, Profile.class);

                                    User user = new User(nameTextBox.getText().toString(), emailTextBox.getText().toString(), "", "", "");
                                    user.setUserId(userId);

                                    // To set a new user object
                                    CurrentUserDataHolder.getInstance().setUser(user);

                                    //intent.putExtra("User", (Parcelable) user);

                                    startActivity(intent);
                                    finish();
                                } else {
                                    // Registration failed, handle the
                                    Toast.makeText(Registration.this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });


        login = findViewById(R.id.loginBottomText);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Registration.this, Login.class);
                startActivity(intent);
            }
        });
    }
    private interface RegistrationCallback {
        void onRegistrationComplete(boolean success);
    }

    private void intitateUserDataPostRequest(String name, String email, String contact, String displayUrl, String coverUrl, String password, RegistrationCallback callback) {
        Log.d("Registration", "Initiating post req");

        webApiUrl = "http://192.168.1.14/smd_a3/insert_user.php";
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
                                    userId = object.getInt("userId");
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
                        Toast.makeText(Registration.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("Registration", "Post fail: " + e.getMessage());
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
                params.put("password", password);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(Registration.this);
        queue.add(stringRequest);
    }

}