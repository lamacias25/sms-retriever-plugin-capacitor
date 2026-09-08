package com.lamacias.plugins.smsretriever;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

/**
 * Created by pcamilo on 10/10/2019
 */
public class SmsRetrieverHandler {

    private Activity activity;
    private static String TAG = SmsRetrieverHandler.class.getSimpleName();
    protected OtpReceivedInterface onOtpReceived;
    private boolean isReceiverRegistered = false;

    public SmsRetrieverHandler(Activity activity) {
        TAG = this.getClass().getSimpleName();
        this.activity = activity;
    }

    public BroadcastReceiver getBroadcastReceiver() {
        return mSmsBroadcastReceiver;
    }

    public void setOtpReceivedCallback(OtpReceivedInterface callback) {
        this.onOtpReceived = callback;
    }

    public void startBroadcastReceiver() {
        if (isReceiverRegistered) return;

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            activity.registerReceiver(mSmsBroadcastReceiver, intentFilter, Context.RECEIVER_EXPORTED);
        } else {
            activity.registerReceiver(mSmsBroadcastReceiver, intentFilter);
        }
        isReceiverRegistered = true;
    }

    public void stopBroadcastReceiver() {
        if (isReceiverRegistered && activity != null) {
            try {
                activity.unregisterReceiver(mSmsBroadcastReceiver);
            } catch (Exception e) {
                Log.e(TAG, "Error al desregistrar receiver: " + e.getMessage());
            }
            isReceiverRegistered = false;
        }
    }

    private BroadcastReceiver mSmsBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: ");
            if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
                Bundle extras = intent.getExtras();
                if (extras == null) {
                    Log.e(TAG, "extras es null");
                    return;
                }
                Status mStatus = (Status) extras.get(SmsRetriever.EXTRA_STATUS);
                if (mStatus == null) {
                    Log.e(TAG, "Status es null");
                    return;
                }

                switch (mStatus.getStatusCode()) {
                    case CommonStatusCodes.SUCCESS:
                        String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                        if (message != null) {
                            Log.d(TAG, "SMS message: " + message);
                            if (onOtpReceived != null) {
                                try {
                                    String otpMessage = message.replace("<#> Your otp code is: ", "");
                                    String[] lines = otpMessage.split("\n");
                                    String otp = lines.length > 0 ? lines[0] : "";
                                    onOtpReceived.onOtpReceived(otp);
                                } catch (Exception e) {
                                    Log.e(TAG, "Error extracting OTP: " + e.getMessage());
                                    onOtpReceived.onOtpReceived("");
                                }
                            }
                        }
                        stopBroadcastReceiver();
                        break;

                    case CommonStatusCodes.TIMEOUT:
                        Log.d(TAG, "onReceive: failure / timeout");
                        if (onOtpReceived != null) {
                            onOtpReceived.onOtpTimeout();
                        }
                        stopBroadcastReceiver();
                        break;
                }
            }
        }
    };
}