package com.example.ass2_i200802_i200707;

import androidx.annotation.NonNull;

import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchError;


public class SinchClientListener implements com.sinch.android.rtc.SinchClientListener {

    @Override
    public void onPushTokenRegistered() {

    }

    @Override
    public void onPushTokenRegistrationFailed(@NonNull SinchError sinchError) {

    }

    @Override    public void onPushTokenUnregistered() {

    }

    @Override
    public void onPushTokenUnregistrationFailed(@NonNull SinchError sinchError) {

    }

    @Override
    public void onClientFailed(@NonNull SinchClient sinchClient, @NonNull SinchError sinchError) {

    }

    @Override
    public void onClientStarted(@NonNull SinchClient sinchClient) {

    }

    @Override
    public void onLogMessage(int i, @NonNull String s, @NonNull String s1) {

    }



    @Override
    public void onCredentialsRequired(@NonNull ClientRegistration clientRegistration) {



    }

    @Override
    public void onUserRegistered() {

    }

    @Override
    public void onUserRegistrationFailed(@NonNull SinchError sinchError) {

    }


}
