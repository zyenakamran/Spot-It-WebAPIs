package com.example.ass2_i200802_i200707;

import android.app.Application;
import android.content.Context;

import com.onesignal.Continue;
import com.onesignal.OneSignal;
import com.onesignal.debug.LogLevel;
import com.sinch.android.rtc.SinchClient;


import java.io.IOException;

public class myapp extends Application {

        Context context;

        // NOTE: Replace the below with your own ONESIGNAL_APP_ID
        private static final String ONESIGNAL_APP_ID = "aef5540c-ea1e-446d-b87e-967088abcc40";

        @Override
        public void onCreate() {
            super.onCreate();

            // Verbose Logging set to help debug issues, remove before releasing your app.
            OneSignal.getDebug().setLogLevel(LogLevel.VERBOSE);

            // OneSignal Initialization
            OneSignal.initWithContext(this, ONESIGNAL_APP_ID);

            // requestPermission will show the native Android notification permission prompt.
            // NOTE: It's recommended to use a OneSignal In-App Message to prompt instead.
            OneSignal.getNotifications().requestPermission(true, Continue.with(r -> {
                if (r.isSuccess()) {
                    if (r.getData()) {
                        // `requestPermission` completed successfully and the user has accepted permission
                    } else {
                        // `requestPermission` completed successfully but the user has rejected permission
                    }
                } else {
                    // `requestPermission` completed unsuccessfully, check `r.getThrowable()` for more info on the failure reason
                }
            }));

            // Initialize Sinch
            try {
                SinchClient sinchClient = SinchClient.builder()
                        .context(this)
                        .userId("ONL2310_4A8hkn")
                        .applicationKey("932d891a-1818-4fac-ae8e-103a7c7c2fcd")
                        //                    .applicationSecret("HgB7QgktNky7cH9ep1qkwQ==")
                        .environmentHost("clientapi.sinch.com")
                        .build();
                // Add SinchClient listener to handle events
                sinchClient.addSinchClientListener(new SinchClientListener());
                sinchClient.start();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
}
