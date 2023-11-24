package com.example.ass2_i200802_i200707;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class Item extends AppCompatActivity {
    TextView itemName, itemPrice, itemDate, userName, descriptionText;
    ImageView itemImg, deleteItem, chatImg;
    String userId, itemId, imgUrl;
    Button rentButton;

    User user = CurrentUserDataHolder.getInstance().getUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        // fetch data from view
        itemName = findViewById(R.id.itemName);
        itemPrice = findViewById(R.id.itemPrice);
        itemImg = findViewById(R.id.itemImg);
        itemDate = findViewById(R.id.dateText);
        userName = findViewById(R.id.nameText);
        deleteItem = findViewById(R.id.deleteItem);
        descriptionText = findViewById(R.id.descriptionText);
        chatImg = findViewById(R.id.img3);

        //extract from intent
        itemName.setText(getIntent().getStringExtra("itemName"));
        String price = getIntent().getStringExtra("itemPrice");
        itemPrice.setText(String.format("$%s", price));
        itemDate.setText(getIntent().getStringExtra("itemDate"));
        imgUrl = getIntent().getStringExtra("itemImg");
        Picasso.get().load(imgUrl).into(itemImg);
        userId = getIntent().getStringExtra("userId");
        itemId = getIntent().getStringExtra("itemId");
        descriptionText.setText(getIntent().getStringExtra("itemDescription"));


        String cUser = String.valueOf(user.getUserId());

        deleteItem.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                //Can not delete someone else's item
                if (cUser.equals(userId)) {
                    deleteItemRequest(Integer.parseInt(itemId), user.getUserId(), new DeleteItemCallback() {
                        @Override
                        public void onDeleteItemComplete(boolean success) {
                            if (success) {
                                Toast.makeText(Item.this, "Item deleted successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Item.this, Home.class);

                                // Start the Home activity
                                startActivity(intent);
                                finish();

                            }
                            else {
                                Toast.makeText(Item.this, "Item could not be deleted", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
                else {
                    Toast.makeText(Item.this, "User can not delete an item not owned by them", Toast.LENGTH_LONG).show();

                }


            }
        });

        chatImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Item.this, ChatMessages.class);
                startActivity(intent);
            }
        });

        rentButton = findViewById(R.id.rentButton);
        rentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to next activity and send data
                Intent intent = new Intent(Item.this, Rent.class);
                int price_int = 0;

                try {
                    price_int = Integer.parseInt(price);

                } catch (NumberFormatException e) {

                }

                //send to next act
                intent.putExtra("itemImg", imgUrl);
                intent.putExtra("userId", userId);
                intent.putExtra("itemId", itemId);
                intent.putExtra("itemPrice", price_int);
                startActivity(intent);
            }
        });

        ImageView back = findViewById(R.id.backArrowIcon);
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ImageView chat = findViewById(R.id.chatButtonBg);
        chat.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Item.this, ChatMessages.class);
                startActivity(intent);
            }
        });

        TextView report = findViewById(R.id.reportButton);
        report.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Item.this, Report.class);
                intent.putExtra("userId", userId);
                intent.putExtra("itemId", itemId);
                startActivity(intent);

            }
        });
    }

    private interface DeleteItemCallback {
        void onDeleteItemComplete(boolean success);
    }

    private void deleteItemRequest(int itemId, int userId, Item.DeleteItemCallback callback) {
        Log.d("DeleteItem", "Initiating delete request");

        String webApiUrl = "http://192.168.1.14/smd_a3/delete_item.php?itemId=" + itemId + "&userId=" + userId;
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, webApiUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Log the full response
                        Log.d("DeleteItem", "Response: " + response);

                        try {
                            JSONObject object = new JSONObject(response);

                            if (object.getInt("Status") == 1) {
                                Log.d("DeleteItem", "Delete success");
                                callback.onDeleteItemComplete(true);
                            } else {
                                // Handle other status values if needed
                                Log.d("DeleteItem", "Delete failed with status: " + object.getInt("status"));
                                callback.onDeleteItemComplete(false);
                            }

                        } catch (JSONException e) {
                            // Handle the case where the response is not a JSON object
                            Log.d("DeleteItem", "JSONException: " + e.getMessage());
                            e.printStackTrace(); // Add this line to log the stack trace
                            callback.onDeleteItemComplete(false);
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        // Handle error
                        Toast.makeText(Item.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("DeleteItem", "Delete fail: " + e.getMessage());
                        callback.onDeleteItemComplete(false);
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(Item.this);
        queue.add(stringRequest);
    }

}