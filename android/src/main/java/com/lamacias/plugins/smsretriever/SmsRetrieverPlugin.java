package com.lamacias.plugins.smsretriever;

import android.util.Log;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CapacitorPlugin(name = "SmsRetriever")
public class SmsRetrieverPlugin extends Plugin {

    private SmsRetrieverHandler smsRetrieverHandler;

    @Override
    public void load() {
        super.load();
        smsRetrieverHandler = new SmsRetrieverHandler(getActivity());
    }

    @PluginMethod
    public void startWatch(PluginCall call) {
        startSMSListener();
        smsRetrieverHandler.startBroadcastReceiver();

        smsRetrieverHandler.setOtpReceivedCallback(new OtpReceivedInterface() {
            @Override
            public void onOtpReceived(String message) {
                Log.v("onOtpReceived", message);
                getActivity().runOnUiThread(() -> {
                    String codeReceived = getOTPCode(message);
                    JSObject ret = new JSObject();
                    ret.put("message", message);
                    ret.put("code", codeReceived);
                    call.resolve(ret);
                });
            }

            @Override
            public void onOtpTimeout() {
                call.reject("SMS Retriever timed out");
            }
        });
    }

    @PluginMethod
    public void getAppHash(PluginCall call) {
        try {
            AppSignatureHelper appSignatureHelper = new AppSignatureHelper(getActivity());
            if (appSignatureHelper.getAppSignatures() != null && !appSignatureHelper.getAppSignatures().isEmpty()) {
                String hashKey = appSignatureHelper.getAppSignatures().get(0);
                JSObject ret = new JSObject();
                ret.put("hash", hashKey);
                call.resolve(ret);
            } else {
                call.reject("No se pudo obtener el App Hash");
            }
        } catch (Exception e) {
            call.reject("Error al generar hash: " + e.getMessage());
        }
    }

    private void startSMSListener() {
        SmsRetrieverClient mClient = SmsRetriever.getClient(getActivity());
        Task<Void> mTask = mClient.startSmsRetriever();

        mTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.v("onSuccess", "Éxito al iniciar SMS Retriever Listener");
            }
        });

        mTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.v("onFailure", "Error al iniciar listener: " + e.getMessage());
            }
        });
    }

    private String getOTPCode(String message) {
        if (message == null) return "";
        Pattern p = Pattern.compile("\\b\\d{6}\\b");
        Matcher m = p.matcher(message);
        String code = "";
        while (m.find()) {
            code = m.group(0);
        }
        return code;
    }
}