package com.ionicframework.BtPumpPlugin;

import com.getcapacitor.JSObject;
import com.getcapacitor.PluginCall;

public class succesfulPluginMetod {
private final PluginCall caller;

    public succesfulPluginMetod(PluginCall call) {
        this.caller = call;

    }

    public void sendSuccess(String name, String value){


        JSObject ret = new JSObject();
        ret.put(name, value);
        caller.success(ret);


    }
}
