package com.ionicframework.BtPumpPlugin;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;


import com.getcapacitor.Plugin;
import com.ionicframework.BtPumpPlugin.display.DisplayParser;
import com.ionicframework.BtPumpPlugin.display.DisplayParserHandler;
import com.ionicframework.BtPumpPlugin.display.Menu;

import java.nio.ByteBuffer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static com.ionicframework.BtPumpPlugin.BtPumpPlugin.retSuccesConnect;


/**
 * Created by fishermen21 on 25.05.17.
 */

public class Ruffy extends Service {



    public static class Key {
        public static byte NO_KEY				=(byte)0x00;
        public static byte MENU					=(byte)0x03;
        public static byte CHECK				=(byte)0x0C;
        public static byte UP					=(byte)0x30;
        public static byte DOWN					=(byte)0xC0;
    }

    private static IRTHandler rtHandler = null;
    private static BTConnection btConn;
    private static PumpData pumpData;

    public static boolean rtModeRunning = false;
    private static long lastRtMessageSent = 0;

    private static final Object rtSequenceSemaphore = new Object();
    private static short rtSequence = 0;

    private static int modeErrorCount = 0;
    private static int step = 0;

    private static boolean synRun=false;//With set to false, write process is started at first time
    private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool( 3 );

    private static Display display;

    public static final IRuffyService.Stub serviceBinder = new IRuffyService.Stub(){

        @Override
        public void setHandler(IRTHandler handler) throws RemoteException {
            rtHandler = handler;
        }

        @Override
        public int doRTConnect() throws RemoteException {
            if(isConnected() && rtModeRunning)
            {
                rtHandler.rtStarted();
                return 0;
            }
            step= 0;
//            if(rtHandler==null)
//            {
//                throw new IllegalStateException("XXX");
//
////                return -2;//FIXME make errors
//            }
            if(!isConnected()) {
                if (pumpData == null) {
                    pumpData = PumpData.loadPump(Plugin.bridge.getActivity(), rtHandler);
                }
                if (pumpData != null) {
                    inShutDown=false;
                    btConn = new BTConnection(rtBTHandler);
                    rtModeRunning = true;
                    btConn.connect(pumpData, 10);
                    return 0;
                }
            } else {
                inShutDown=false;
                rtModeRunning = true;
                Application.sendAppCommand(Application.Command.COMMAND_DEACTIVATE,btConn);
            }
            return -1;
        }

        public void doRTDisconnect()
        {
            step = 200;
            stopRT();
        }

        public void rtSendKey(byte keyCode, boolean changed)
        {
            //FIXME
            lastRtMessageSent = System.currentTimeMillis();
            synchronized (rtSequenceSemaphore) {
                rtSequence = Application.rtSendKey(keyCode, changed, rtSequence, btConn);
            }
        }

        public void resetPairing()
        {
//            SharedPreferences prefs = com.ionicframework.BtPumpPlugin.getSharedPreferences("pumpdata", Activity.MODE_PRIVATE);
//            prefs.edit().putBoolean("paired",false).apply();

            /*
            String bondedDeviceId = prefs.getString("device", null);
            if (bondedDeviceId != null) {
                BluetoothDevice boundedPump = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(bondedDeviceId);
                // TODO I know!
                try {
                    Method removeBound = boundedPump.getClass().getMethod("removeBond", (Class<?>[]) null);
                    removeBound.invoke(boundedPump, (Object[]) null);
                } catch (ReflectiveOperationException e) {
                    // it's not going better here either
                }
            }
*/

            synRun=false;
            rtModeRunning =false;
        }

        public boolean isConnected() {
            return btConn != null && btConn.isConnected();
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return serviceBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private static BTHandler rtBTHandler = new BTHandler() {
        @Override
        public void deviceConnected() {
            inShutDown=false;
            log("connected to pump");
            if(synRun==false && !inShutDown) {
                synRun = true;
                log("start synThread");
                scheduler.execute(synThread);
            }
        }

        @Override
        public void log(String s) {
            Ruffy.log(s);
            if(s.equals("got error in read") && step < 200 && !inShutDown)
            {
                synRun=false;
                btConn.connect(pumpData,4);
            }
            Log.v("RuffyService",s);
        }

        @Override
        public void fail(String s) {
            log("failed: "+s);
            synRun=false;
            if(step < 200)
                btConn.connect(pumpData,4);
            else
                Ruffy.fail(s);

        }

        @Override
        public void deviceFound(BluetoothDevice bd) {
            log("not be here!?!");
        }

        @Override
        public void handleRawData(byte[] buffer, int bytes) {
            log("got data from pump");
            synRun=false;
            step=0;
            Packet.handleRawData(buffer,bytes, rtPacketHandler);
        }

        @Override
        public void requestBlueTooth() {
            try {
                rtHandler.requestBluetooth();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void ReturnMacToIonic() {

        }
    };

    private static void stopRT()
    {
        step=200;
        rtModeRunning = false;
        // wait for the keep alive thread to detect rtModeRunning has become false
        // so it won't trigger during disconnect
        SystemClock.sleep(500 + 250);
        Application.sendAppCommand(Application.Command.DEACTIVATE_ALL,btConn);
    }

    private static void startRT() {
        Log.w("PUMP","starting RT keepAlive");
        retSuccesConnect.sendSuccess("isConnected","True");
        new Thread(){
            @Override
            public void run() {
                rtModeRunning = true;
                rtSequence = 0;
                lastRtMessageSent = System.currentTimeMillis();
                rtModeRunning = true;
                try {
                    display = new Display(new DisplayUpdater() {
                        @Override
                        public void clear() {
                            try {
                                rtHandler.rtClearDisplay();
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void update(byte[] quarter, int which) {
                            try {
                                rtHandler.rtUpdateDisplay(quarter,which);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    display.setCompletDisplayHandler(new CompleteDisplayHandler() {
                        @Override
                        public void handleCompleteFrame(byte[][] pixels) {
                            DisplayParser.findMenu(pixels, new DisplayParserHandler() {
                                @Override
                                public void menuFound(Menu menu) {
                                    try {
                                        rtHandler.rtDisplayHandleMenu(menu);
                                    } catch (RemoteException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void noMenuFound() {
                                    try {
                                        rtHandler.rtDisplayHandleNoMenu();
                                    } catch (RemoteException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                        }
                    });

                    rtHandler.rtStarted();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                while(rtModeRunning)
                {
                    try {
                        if (System.currentTimeMillis() > lastRtMessageSent + 1000L) {
                            log("sending keep alive");
                            synchronized (rtSequenceSemaphore) {
                                rtSequence = Application.sendRTKeepAlive(rtSequence, btConn);
                                lastRtMessageSent = System.currentTimeMillis();
                            }
                        }
                    } catch (Exception e) {
                        if (rtModeRunning) {
                            fail("Error sending keep alive while rtModeRunning is still true: " + e);
                        } else {
                            fail("Error sending keep alive. rtModeRunning is false, so this is most likely a race condition during disconnect: " + e);
                        }
                    }
                    try{
                        Thread.sleep(500);}catch(Exception e){/*ignore*/}
                }
                try {
                    rtHandler.rtStopped();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    private static Runnable synThread = new Runnable(){
        @Override
        public void run() {
            while(synRun && rtModeRunning)
            {
                Protocol.sendSyn(btConn);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private static AppHandler rtAppHandler = new AppHandler() {
        @Override
        public void log(String s) {
            Ruffy.log(s);
        }

        @Override
        public void connected() {
            Application.sendAppCommand(Application.Command.RT_MODE, btConn);
        }

        @Override
        public void rtModeActivated() {
            startRT();
        }

        @Override
        public void cmdModeActivated() {
            // not there yet
        }

        @Override
        public void rtModeDeactivated() {
            rtSequence =0;

            if(rtHandler!=null)
                try {rtHandler.rtStopped();} catch (RemoteException e){};
                if(!inShutDown) {
                    inShutDown = true;
                    Application.sendAppDisconnect(btConn);
                    btConn.disconnect();
                }
        }

        @Override
        public void cmdModeDeactivated() {
        }

        @Override
        public void modeDeactivated() {
            rtModeRunning = false;
            rtSequence =0;
            if(rtHandler!=null)
                try {rtHandler.rtStopped();} catch (RemoteException e){};
            if(!inShutDown) {
                inShutDown = true;
                Application.sendAppDisconnect(btConn);
                btConn.disconnect();
            }
        }

        @Override
        public void addDisplayFrame(ByteBuffer b) {
            display.addDisplayFrame(b);
        }

        @Override
        public void modeError() {
            modeErrorCount++;

            if (modeErrorCount > Application.MODE_ERROR_TRESHHOLD) {
                stopRT();
                log("wrong mode, deactivate");

                modeErrorCount = 0;
                Application.sendAppCommand(Application.Command.DEACTIVATE_ALL, btConn);
            }
        }

        @Override
        public void sequenceError() {
            Application.sendAppCommand(Application.Command.APP_DISCONNECT, btConn);
        }

        @Override
        public void error(short error, String desc) {
            switch (error)
            {
                case (short) 0xF056:
                    PumpData d = btConn.getPumpData();
                    btConn.disconnect();
                    btConn.connect(d,4);
                    break;
                default:
                    log(desc);
            }
        }
    };

    public static void log(String s) {
        try{
            rtHandler.log(s);
        }catch(Exception e){e.printStackTrace();}
    }

    public static void fail(String s) {
        try{
            rtHandler.fail(s);
        }catch(Exception e){e.printStackTrace();}
    }

    private static boolean inShutDown = false;
    private static PacketHandler rtPacketHandler = new PacketHandler(){
        @Override
        public void sendImidiateAcknowledge(byte sequenceNumber) {
            if(!inShutDown)
                Protocol.sendAck(sequenceNumber,btConn);
        }

        @Override
        public void log(String s) {
            Ruffy.log(s);
        }

        @Override
        public void handleResponse(Packet.Response response, boolean reliableFlagged, byte[] payload) {
            switch (response)
            {
                case ID:
                    Protocol.sendSyn(btConn);
                    break;
                case SYNC:
                    btConn.seqNo = 0x00;

                    if(step<100)
                        Application.sendAppConnect(btConn);
                    else
                    {
                        Application.sendAppDisconnect(btConn);
                        step = 200;
                    }
                    break;
                case UNRELIABLE_DATA:
                case RELIABLE_DATA:
                    Application.processAppResponse(payload, reliableFlagged, rtAppHandler);
                    break;
            }
        }

        @Override
        public void handleErrorResponse(byte errorCode, String errDecoded, boolean reliableFlagged, byte[] payload) {
            log(errDecoded);
        }

        @Override
        public Object getToDeviceKey() {
            return pumpData.getToDeviceKey();
        }
    };
}
