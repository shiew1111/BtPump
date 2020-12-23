package com.ionicframework.BtPumpPlugin;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.RemoteException;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;


import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import static com.ionicframework.BtPumpPlugin.MainFragment.rtSendKey;
import static com.ionicframework.BtPumpPlugin.MainFragment.sleep;
import static com.ionicframework.BtPumpPlugin.Ruffy.serviceBinder;


@NativePlugin
public class BtPumpPlugin extends Plugin  {
    private static JSObject PunpData;
    private BTConnection btConn;
    private int step = 0;
    private BluetoothDevice pairingDevice;
    private byte[] pin;
    final byte[] pairingKey = {16,9,2,0,-16};
    public static IRuffyService mBoundService;
    public static succesfulPluginMetod retSuccesConnect;


    public static void PumpDataForExport(JSObject PumpData){
         PunpData = PumpData;


    }










    @PluginMethod
    public void connect(PluginCall call) throws RemoteException {
        mBoundService = IRuffyService.Stub.asInterface(serviceBinder);
        mBoundService.setHandler(MainFragment.handler);
        serviceBinder.doRTConnect();
        retSuccesConnect = new succesfulPluginMetod(call);

    }


    @PluginMethod
    public void disconnect(PluginCall call) throws RemoteException {

        serviceBinder.doRTDisconnect();


        retSuccesConnect = new succesfulPluginMetod(call);
        retSuccesConnect.sendSuccess("isConnected","False");
    }
    @PluginMethod
    public void keyUp(PluginCall call) throws RemoteException {

        MainFragment.upRunning = 1;
        MainFragment.scheduler.execute(MainFragment.upThread);

        sleep(100);
        String value = "True";
        JSObject ret = new JSObject();
        ret.put("KeySend", value);
        call.success(ret);

    }
    @PluginMethod
    public void keyDown(PluginCall call) throws RemoteException {

        MainFragment.downRunning = 1;
        MainFragment.scheduler.execute(MainFragment.downThread);

        sleep(100);
        String value = "True";
        JSObject ret = new JSObject();
        ret.put("KeySend", value);
        call.success(ret);
    }
    @PluginMethod
    public void keyMenu(PluginCall call) throws RemoteException {

        rtSendKey(Ruffy.Key.MENU,true);
        sleep(100);
        rtSendKey(Ruffy.Key.NO_KEY,true);

        String value = "True";
        JSObject ret = new JSObject();
        ret.put("KeySend", value);
        call.success(ret);
    }
    @PluginMethod
    public void keyCheck(PluginCall call) throws RemoteException {

        rtSendKey(Ruffy.Key.CHECK,true);
        sleep(100);
        rtSendKey(Ruffy.Key.NO_KEY,true);

        String value = "True";
        JSObject ret = new JSObject();
        ret.put("KeySend", value);
        call.success(ret);
    }


    @PluginMethod
    public void GetPumpData(final PluginCall call) {





        call.success(PunpData);

    }









    @PluginMethod()
    public void pairing(final PluginCall call) {


//Connect
        btConn = new BTConnection(new BTHandler() {
            BluetoothDevice device;

            @Override
            public void deviceConnected() {

                pairingDevice = device;
                step = 1;
                btConn.writeCommand(pairingKey);
            }

            @Override
            public void log(String s) {

            }


            @Override
            public void fail(String s) {
                if(step == 1)//trying to connect
                {
                    btConn.connect(pairingDevice);
                }
            }

            @Override
            public void deviceFound(BluetoothDevice device) {
                if (this.device == null) {
                    this.device = device;
                } else if (this.device.getAddress().equals(device.getAddress())) {
                    pairingDevice = device;
                    Log.d("MACADDRESS", String.valueOf(pairingDevice));

                    btConn.connect(device);

                } else {
                    this.device = device;
                }
            }

            @Override
            public void handleRawData(byte[] buffer, int bytes) {
                handleData(buffer,bytes);
            }

            @Override
            public void requestBlueTooth() {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

                startActivityForResult(call,enableBtIntent,1);
            }
            @Override
            public void ReturnMacToIonic() {

                JSObject ret = new JSObject();
                ret.put("mac", pairingDevice);
                call.success(ret);
            }

        });
        btConn.makeDiscoverable(this.bridge.getActivity());












    }
    private void processAppResponse(byte[] payload, boolean reliable) {
        Log.d("PUMP","processing app response");
        ByteBuffer b = ByteBuffer.wrap(payload);
        b.order(ByteOrder.LITTLE_ENDIAN);

        b.get();
        byte servId = b.get();
        short commId = b.getShort();

        Log.d("PUMP","Service ID: " + String.format("%X", servId) + " Comm ID: " + String.format("%X", commId) + " reliable: " + reliable);

        short error = b.getShort();
        if (error != 0) {
            Log.e("PUMP","got error :(");
            return;
        }

        switch (commId) {
            case (short) 0xA055:
                Application.sendAppCommand(Application.Command.COMMANDS_SERVICES_VERSION,btConn);
                break;
            case (short) 0xA065:
                Application.sendAppCommand(Application.Command.BINDING,btConn);
                break;
            case (short) 0xA095:
                step+=100;
                Protocol.sendSyn(btConn);
                break;
        }
    }

    void handleData(byte buffer[], int bytes) {
        switch (step) {
            case 1: //we requested con, now we try to request auth
            {
                byte[] key = {16, 12, 2, 0, -16};

                btConn.writeCommand(key);

                if(this.bridge.getActivity()!=null) {
                    this.getBridge().getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final EditText pinIn = new EditText(getContext());
                            pinIn.setInputType(InputType.TYPE_CLASS_NUMBER);
                            pinIn.setHint("XXX XXX XXXX");
                            new AlertDialog.Builder(getContext())
                                    .setTitle("Enter Pin")
                                    .setMessage("Read the Pin-Code from pump and enter it")
                                    .setView(pinIn)
                                    .setPositiveButton("ENTER", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            String pin = pinIn.getText().toString();
                                            BtPumpPlugin.this.pin = Utils.generateKey(pin);
                                            step = 2;
                                            //sending key available:
                                            byte[] key = {16, 15, 2, 0, -16};
                                            btConn.writeCommand(key);

                                        }
                                    })
                                    .show();
                        }
                    });
                }

            }
            break;
            default: //we indicated that we have a key, now lets handle the handle to the handle with an handler
            {
                List<Byte> t = new ArrayList<>();
                for (int i = 0; i < bytes; i++)
                    t.add(buffer[i]);
                for (List<Byte> x : Frame.frameDeEscaping(t)) {
                    byte[] xx = new byte[x.size()];
                    for (int i = 0; i < x.size(); i++)
                        xx[i] = x.get(i);
                    boolean rel = false;
                    if ((x.get(1) & 0x20) == 0x20) {
                        rel = true;

                        byte seq = 0x00;
                        if ((x.get(1) & 0x80) == 0x80)
                            seq = (byte) 0x80;

                        btConn.getPumpData().incrementNonceTx();

                        List<Byte> packet = Packet.buildPacket(new byte[]{16, 5, 0, 0, 0}, null, true,btConn);

                        packet.set(1, (byte) (packet.get(1) | seq));                //OR the received sequence number

                        packet = Utils.ccmAuthenticate(packet, btConn.getPumpData().getToPumpKey(), btConn.getPumpData().getNonceTx());

                        List<Byte> temp = Frame.frameEscape(packet);
                        byte[] ro = new byte[temp.size()];
                        int i = 0;
                        for (byte b : temp)
                            ro[i++] = b;
                        try {
                            btConn.write(ro);

                        } catch (Exception e) {
                            e.printStackTrace();

                        }

                    } else {
                        rel = false;
                    }
                    handleRX(xx, x.size(), rel);
                }
            }
            break;
        }

    }
    public void handleRX(byte[] inBuf, int length, boolean rel) {

        ByteBuffer buffer = ByteBuffer.wrap(inBuf, 0, length);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        ByteBuffer pBuf;

        byte[] nonce, payload, umac, packetNoUmac;

        Byte command, addresses;
        buffer.get();
        command = (byte)(buffer.get() & 0x1F);

        short payloadlength = buffer.getShort();

        addresses = buffer.get();

        nonce = new byte[13];                            //Copy buffers for nonce
        buffer.get(nonce, 0, nonce.length);

        payload = new byte[payloadlength];                        //Payload
        buffer.get(payload, 0, payload.length);
        pBuf = ByteBuffer.wrap(payload);

        umac = new byte[8];                                //U-MAC
        buffer.get(umac, 0, umac.length);

        packetNoUmac = new byte[buffer.capacity() - umac.length];
        buffer.rewind();
        for (int i = 0; i < packetNoUmac.length; i++)
            packetNoUmac[i] = buffer.get();

        byte c = (byte)(command & 0x1F);
        switch (c) {
            case 0x11://key response?
                try {
                    Object tf = Twofish_Algorithm.makeKey(pin);
                    btConn.getPumpData().setAndSaveAddress((byte) ((addresses << 4) & 0xF0));        //Get the address and reverse it since source and destination are reversed from the RX packet

                    byte[] key_pd = new byte[16];                            //Get the bytes for the keys
                    byte[] key_dp = new byte[16];

                    pBuf.rewind();
                    pBuf.get(key_pd, 0, key_pd.length);
                    pBuf.get(key_dp, 0, key_dp.length);

                    String d = "";
                    for (byte b : key_pd)
                        d += String.format("%02X ", b);

                    d = "";
                    for (byte b : key_dp)
                        d += String.format("%02X ", b);


                    btConn.getPumpData().setAndSaveToDeviceKey(key_pd,tf);
                    btConn.getPumpData().setAndSaveToPumpKey(key_dp,tf);
                    btConn.getPumpData().setAndSavePumpMac(pairingDevice.getAddress());
                    Protocol.sendIDReq(btConn);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 20:
                if (Utils.ccmVerify(packetNoUmac, btConn.getPumpData().getToDeviceKey(), umac, nonce)) {
                    byte[] device = new byte[13];

                    pBuf.order(ByteOrder.LITTLE_ENDIAN);
                    int serverId = pBuf.getInt();
                    pBuf.get(device);
                    String deviceId = new String(device);


                    try {
                        Protocol.sendSyn(btConn);
                    }catch(Exception e) {
                        e.printStackTrace();
                    }

                }
                break;
            case 24:
                if (Utils.ccmVerify(packetNoUmac, btConn.getPumpData().getToDeviceKey(), umac, nonce)) {
                    btConn.seqNo = 0x00;



                    if(step<100)
                        Application.sendAppConnect(btConn);
                    else
                    {
                        Application.sendAppDisconnect(btConn);
                        step=200;
                    }
                }
                break;

            case 0x23: //recieved reliable data/
            case 0x03: //recieve unreliable data
                if (Utils.ccmVerify(packetNoUmac, btConn.getPumpData().getToDeviceKey(), umac, nonce)) {
                    BtPumpPlugin.this.processAppResponse(payload, rel);
                }
                break;

            case 0x05://ack response
                break;



        }
    }



}
