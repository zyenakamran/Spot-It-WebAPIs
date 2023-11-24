package com.example.ass2_i200802_i200707;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Login extends AppCompatActivity {

    EditText emailTextBox, passwordTextBox;
    List<User> userList = new ArrayList<>();
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        emailTextBox =  findViewById(R.id.emailTextBox);
        passwordTextBox =  findViewById(R.id.passwordTextBox);
        TextView forgotPassword = findViewById(R.id.forgotPassText);

        // Fetch all users data
        new UsersGetRequestAsyncTask().execute();

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, ForgotPassword.class);
                startActivity(intent);
            }
        });

        Button login = findViewById(R.id.loginButton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String enteredEmail = emailTextBox.getText().toString();
                String enteredPassword = passwordTextBox.getText().toString();

                // Iterate through the user list to find a match
                User signedInUser = null;
                for (User user : userList) {
                    if (user.getEmail().equals(enteredEmail) && user.getPassword().equals(enteredPassword)) {
                        signedInUser = user;
                        break;
                    }
                }

                // Display a toast message based on the result
                if (signedInUser != null) {
                    Toast.makeText(Login.this, "Sign In Successful", Toast.LENGTH_SHORT).show();

                    // Create an intent to start the Home activity
                    Intent intent = new Intent(Login.this, Home.class);

                    // Pass the signed-in user as a Parcelable extra
                    CurrentUserDataHolder.getInstance().setUser(signedInUser);
                    //intent.putExtra("User", signedInUser);

                    // Start the Home activity
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(Login.this, "Sign In Unsuccessful", Toast.LENGTH_SHORT).show();
                }

            }
        });

        TextView signUp = findViewById(R.id.SignupText);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Registration.class);
                startActivity(intent);
            }
        });
    }


    private class UsersGetRequestAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            initiateUsersGetRequest(new RequestCallback() {
                @Override
                public void onRequestComplete(boolean success) {
                    if (success) {
                        Log.d("Login", "Get Request success: " + success);
                    }
                    else {
                        Log.d("Login", "Get Request failed: " + success);
                    }
                }
            });
            return null;
        }

    }

    private void initiateUsersGetRequest(Login.RequestCallback callback) {
        Log.d("Registration", "Initiating post req");

        String webApiUrl = "http://192.168.1.14/smd_a3/get_users.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, webApiUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Log the full response
                        Log.d("Registration", "Response: " + response);

                        try {
                            JSONObject object = new JSONObject(response);

                            if (object.getInt("Status") == 1) {
                                JSONArray usersArray = object.getJSONArray("Users");

                                for (int i = 0; i < usersArray.length(); i++) {
                                    JSONObject userObject = usersArray.getJSONObject(i);

                                    int userId = userObject.getInt("userId");
                                    String name = userObject.getString("name");
                                    String email = userObject.getString("email");
                                    String contact = userObject.getString("contact");
                                    String displayUrl = userObject.getString("displayUrl");
                                    String coverUrl = userObject.getString("coverUrl");
                                    String password = userObject.getString("password");

                                    User user = new User(name, email, contact, displayUrl, coverUrl);
                                    user.setUserId(userId);
                                    user.setPassword(password);

                                    userList.add(user);
                                }

                                callback.onRequestComplete(true);
                                Toast.makeText(Login.this, response, Toast.LENGTH_SHORT).show();
                            } else {
                                // Handle other status values if needed
                                Log.d("Login", "Get failed with status: " + object.getInt("status"));
                                callback.onRequestComplete(false);
                            }

                        } catch (JSONException e) {
                            // Handle the case where the response is not a JSON object
                            callback.onRequestComplete(false);
                            Log.d("Login", "JSONException: " + e.getMessage());
                            e.printStackTrace(); // Add this line to log the stack trace
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        // Handle error
                        Toast.makeText(Login.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("Login", "Get fail: " + e.getMessage());
                        callback.onRequestComplete(false);
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(Login.this);
        queue.add(stringRequest);
    }


    private interface RequestCallback {
        void onRequestComplete(boolean success);
    }

}