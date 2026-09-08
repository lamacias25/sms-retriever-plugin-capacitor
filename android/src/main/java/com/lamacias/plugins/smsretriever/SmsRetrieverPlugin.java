package com.lamacias.plugins.smsretriever;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "SmsRetriever")
public class SmsRetrieverPlugin extends Plugin {

    @PluginMethod
    public void startWatch(PluginCall call) {
        // Tu lógica de SmsRetrieverHandler existente
        SmsRetrieverHandler handler = new SmsRetrieverHandler(getContext());
        handler.startSmsListener(new OtpReceivedInterface() {
            @Override
            public void onOtpReceived(String message) {
                JSObject ret = new JSObject();
                ret.put("message", message);
                call.resolve(ret);
            }

            @Override
            public void onOtpTimeout() {
                call.reject("SMS Retriever timed out");
            }
        });
    }

    @PluginMethod
    public void getAppHash(PluginCall call) {
        AppSignatureHelper helper = new AppSignatureHelper(getContext());
        String hash = helper.getAppSignatures().get(0);
        
        JSObject ret = new JSObject();
        ret.put("hash", hash);
        call.resolve(ret);
    }
}