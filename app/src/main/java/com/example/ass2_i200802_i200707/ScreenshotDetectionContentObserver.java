package com.example.ass2_i200802_i200707;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ScreenshotDetectionContentObserver  extends ContentObserver {

    private Context context;

    public ScreenshotDetectionContentObserver(Handler handler, Context context) {
        super(handler);
        this.context = context;
        Log.d("ScreenshotDetection", "Screenshot CHECKING CONSTRUCTOR");

    }
    public boolean deliverSelfNotifications() {
        return super.deliverSelfNotifications();
    }

    @Override
    public void onChange(boolean selfChange) {
        onChange(selfChange, null);
    }
    public void onChange(boolean selfChange, Uri uri) {
        // This method is called when a screenshot is taken
        // You can perform any actions you need here, such as showing a notification
        // or sending a broadcast to your activity
        // For example:
        // sendBroadcast(new Intent("screenshot_taken"));
        Toast.makeText(context, "Screenshot taken", Toast.LENGTH_SHORT).show();
        Log.d("ScreenshotDetection", "Screenshot taken");

        sendPushNotification();


        // Check if the URI contains the screenshot
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference reference = storage.getReference("Screenshots").child(System.currentTimeMillis() + "screenshot.png");
//        reference.putFile(uri);


    }


    private void sendPushNotification() {
        String ONESIGNAL_APP_ID = "aef5540c-ea1e-446d-b87e-967088abcc40"; // Replace with your OneSignal App ID
        String REST_API_KEY = "MTI4ZjEzOWYtMGE3ZS00OTUwLTkwOTItNmU5MmEyNzU2ZDMw"; // Replace with your OneSignal REST API Key

        // Define custom data to be sent with the push notification
        JSONObject customData = new JSONObject();
        try {
            customData.put("screenshot_detected", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Create the push notification payload
        JSONObject notificationPayload = new JSONObject();
        try {
            notificationPayload.put("app_id", ONESIGNAL_APP_ID);
            notificationPayload.put("contents", new JSONObject().put("en", "A screenshot was taken."));
            notificationPayload.put("included_segments", new JSONArray().put("All"));
            notificationPayload.put("data", customData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Send the push notification to OneSignal using OkHttp
        OkHttpClient client = new OkHttpClient();
        String url = "https://onesignal.com/api/v1/notifications";
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), notificationPayload.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Authorization", "Basic " + REST_API_KEY)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.e("ScreenshotDetection", "Error sending push notification");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.d("ScreenshotDetection", "Push notification sent successfully");
                } else {
                    Log.e("ScreenshotDetection", "Error sending push notification");
                }
            }
        });

    }
}
