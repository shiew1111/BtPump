package com.ionicframework.BtPumpPlugin;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.Objects;

class PairingRequest extends BroadcastReceiver {
    private final Activity activity;
    private final BTHandler handler;

    public PairingRequest(final Activity activity, final BTHandler handler)
        {
            super();
            this.activity = activity;
            this.handler = handler;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Objects.equals(intent.getAction(), "android.bluetooth.device.action.PAIRING_REQUEST")) {
                try {
                    final BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try
                            {
                                byte[] pinBytes;
                                pinBytes = ("}gZ='GD?gj2r|B}>").getBytes("UTF-8");

                                Log.d("PUMP", "Try to set the PIN");
                                Method m = device.getClass().getMethod("setPin", byte[].class);
                                m.invoke(device, pinBytes);
                                Log.d("PUMP","Success to add the PIN.");

                                try {
                                    m = device.getClass().getMethod("createBond");
                                    m.invoke(device);
                                    Log.d("PUMP","Success to start bond.");
                                } catch (Exception e) {
                                    Log.e("PUMP","Failure to start bond: " + e.getMessage() );
                                    e.printStackTrace();
                                }
                                try {
                                    // java.lang.SecurityException: Need BLUETOOTH PRIVILEGED permission: Neither user 10094 nor current process has android.permission.BLUETOOTH_PRIVILEGED.
                                    // above perm is only granted to system apps, not third party apps ...
                                    device.getClass().getMethod("setPairingConfirmation", boolean.class).invoke(device, true);
                                    Log.d("PUMP", "Success to setPairingConfirmation.");
                                } catch (Exception e) {
                                    Log.e("PUMP","Failure running setPairingConfirmation: " + e.getMessage());
                                    e.printStackTrace();
                                }
                            }catch(Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }