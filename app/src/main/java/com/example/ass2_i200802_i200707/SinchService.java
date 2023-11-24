package com.example.ass2_i200802_i200707;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.activity.result.IntentSenderRequest;
import androidx.annotation.Nullable;

import com.sinch.android.rtc.PushConfiguration;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.CallControllerListener;

import java.io.IOException;

public class SinchService extends Service {

    private static final String APP_KEY = "932d891a-1818-4fac-ae8e-103a7c7c2fcd";
    private static final String APP_SECRET = "sgc0Fl85okiuXXmy1Fquzg==";
    private static final String ENVIRONMENT = "clientapi.sinch.com";
    private static final String APP_FCM_SENDER_ID = "337615081538";
    private SinchClient sinchClient;

    private void createClient(String username) throws IOException {
        assert getFcmRegistrationToken(this) != null;
        sinchClient = SinchClient.builder()
                .context(getApplicationContext())
                .userId(username)
                .applicationKey(APP_KEY)
                .environmentHost(ENVIRONMENT)
                .pushConfiguration(
                        PushConfiguration.fcmPushConfigurationBuilder()
                                .senderID(APP_FCM_SENDER_ID)
                                .registrationToken((String) getFcmRegistrationToken(this))
                                        .build()
                )
                .pushNotificationDisplayName("User " + username)
                .build();

        sinchClient.addSinchClientListener(new SinchClientListener());
        sinchClient.getCallController().addCallControllerListener((CallControllerListener) new SinchClientListener());
    }

    private Object getFcmRegistrationToken(SinchService sinchService) {
        return null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class Builder {
        public IntentSenderRequest.Builder pushProvider(FcmPushProvider fcmPushProvider) {
            return null;
        }
    }

    private class FcmPushProvider {
        public FcmPushProvider(String s) {
        }
    }
}
