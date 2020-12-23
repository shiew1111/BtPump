package com.ionicframework.BtPumpPlugin;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;


import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.ionicframework.BtPumpPlugin.display.Menu;
import com.ionicframework.BtPumpPlugin.display.MenuAttribute;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static com.ionicframework.BtPumpPlugin.Ruffy.serviceBinder;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Service {




    public static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool( 3 );

    public static boolean mServiceBound = false;
    public static IRuffyService mBoundService;

    public MainFragment() {
    }

    public static ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceBound = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBoundService = IRuffyService.Stub.asInterface(service);
            mServiceBound = true;

            try {
                mBoundService.setHandler(handler);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    public static void rtStarted() {
    }

    public void onDestroy() {

        super.onDestroy();
        if (mServiceBound) {

            try {
                appendLog("Try to disconnect before exit");
                mBoundService.doRTDisconnect();
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            Plugin.bridge.getActivity().unbindService(mServiceConnection);
            mServiceBound = false;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return (IBinder) mServiceConnection;
    }

    public static int upRunning = 0;
    public static int downRunning = 0;

    public static Runnable upThread = new Runnable()
    {
        @Override
        public void run() {
            while(upRunning >0)
            {
                if(upRunning==1) {
                    upRunning++;
                    rtSendKey(Ruffy.Key.UP,true);
                } else {
                    rtSendKey(Ruffy.Key.UP,false);
                }
                try{
                    sleep(200);
                    upRunning = 0;
                }catch(Exception e){}
            }
            rtSendKey(Ruffy.Key.NO_KEY,true);
        }
    };

    public static Runnable downThread = new Runnable()
    {
        @Override
        public void run() {
            while(downRunning >0)
            {
                if(downRunning==1) {
                    downRunning++;
                    rtSendKey(Ruffy.Key.DOWN,true);
                }
                else
                {
                    rtSendKey(Ruffy.Key.DOWN,false);
                }
                try{
                    sleep(200);
                    downRunning = 0;
                }catch(Exception e){}
            }
            rtSendKey(Ruffy.Key.NO_KEY,true);
        }
    };

    public static void sleep(long millis)
    {
        try{
            Thread.sleep(millis);}catch(Exception e){/*ignore*/}
    }

    public static void rtSendKey(byte keyCode, boolean changed)  {
        try {
            if (mBoundService.isConnected()) {
                try {
                    mBoundService.rtSendKey(keyCode, changed);
                } catch (RemoteException re) {
                    re.printStackTrace();
                    appendLog("failed keySend: " + re.getMessage());
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void resetPairing()
    {
        try {
            mBoundService.resetPairing();
        }catch(RemoteException re)
        {
            re.printStackTrace();
            appendLog("failed keySend: "+re.getMessage());
        }
    }

    public static IRTHandler.Stub handler = new IRTHandler.Stub() {
        @Override
        public void log(String message) throws RemoteException {
            appendLog(message);
        }

        @Override
        public void fail(String message) throws RemoteException {
            appendLog("fail: "+message);
        /*getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                connect.setText("Try Connect again!");
                connect.setEnabled(true);
            }
        });*/
        }

        @Override
        public void requestBluetooth() throws RemoteException {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            Plugin.bridge.getActivity().startActivityForResult(enableBtIntent, 1);
        }

        public void rtStarted()
        {
            Plugin.bridge.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mBoundService = IRuffyService.Stub.asInterface(serviceBinder);
                    try {
                        mBoundService.setHandler(MainFragment.handler);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (mBoundService.isConnected()) {
                           Log.d("PUMP","Connect" ) ;
                        } else {
                            Log.d("PUMP","Connect");
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void rtClearDisplay() throws RemoteException {
            //displayView.clear();
        }

        //TODO just for debug marker byte[][] display = new byte[4][];
        @Override
        public void rtUpdateDisplay(byte[] quarter, int which) throws RemoteException {

            //displayView.update(quarter,which);

            //TODO just for debug marker display[which] = quarter;
            //if (connectLog.getVisibility() != View.GONE)
                Plugin.bridge.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // connectLog.setVisibility(View.GONE);
                        Log.d("PUMP", "Update rtDisplay");
                    }
                });
        }



        @Override
        public void rtDisplayHandleMenu(final Menu menu) throws RemoteException {
            Plugin.bridge.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String s = "";
                    JSObject ret = new JSObject();
                    for(MenuAttribute ma : menu.attributes())
                    {

                        String AttrName= ""+ma;

                        ret.put(AttrName, menu.getAttribute(ma));

                    }
//Ustawienie jakie menu jest wiświetlane
                    //  frameCounter.setText("display found: "+menu.getType()+s);
                    // Nazwa ekranu
                    ret.put("MenuType", menu.getType());
                    BtPumpPlugin.PumpDataForExport(ret);

                    //String menu = frameCounter.getText().toString();
                }
            });
        }

        @Override
        public void rtDisplayHandleNoMenu() throws RemoteException {
            Plugin.bridge.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // frameCounter.setText("no display found");
/*                    DisplayParser.findMenu(display, new DisplayParserHandler() {
                        @Override
                        public void menuFound(Menu menu) {

                        }

                        @Override
                        public void noMenuFound() {

                        }
                    });*///TODO just for debug marker
                }
            });
        }

        public void rtStopped()
        {
            Plugin.bridge. getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //connectLog.setVisibility(View.VISIBLE);
                }
            });
        }
    };

//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
////
//         View v = inflater.inflate(R.layout.empty , container, false);
////
//        try {
//            versionNameView = (TextView) v.findViewById(R.id.versionName);
//            versionNameView.setText(getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0).versionName);
//        } catch (PackageManager.NameNotFoundException e) {}
//
//        connect = (Button) v.findViewById(R.id.main_connect);
//        connect.setOnClickListener(this);
//
//        Button reset = (Button) v.findViewById(R.id.main_reset);
//        reset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new AlertDialog.Builder(getContext()).setTitle("remove bonding?").setMessage("Really delete bonding informations with pump?").setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        resetPairing();
//                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,new SetupFragment()).addToBackStack("Start").commit();
//                    }
//                }).setNegativeButton("NO",null).show();
//            }
//        });
//
//        connectLog = (TextView) v.findViewById(R.id.main_log);
//        connectLog.setMovementMethod(new ScrollingMovementMethod());
//        connectLog.setTextIsSelectable(true);
//
//        displayLayout= (LinearLayout) v.findViewById(R.id.pumpPanel);
//        displayView = (PumpDisplayView) displayLayout.findViewById(R.id.pumpView);
//
//        frameCounter = (TextView) v.findViewById(R.id.frameCounter);
//        Button menu = (Button) displayLayout.findViewById(R.id.pumpMenu);
//        menu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                rtSendKey(Ruffy.Key.MENU,true);
//                sleep(100);
//                rtSendKey(Ruffy.Key.NO_KEY,true);
//            }
//        });
//        Button check = (Button) displayLayout.findViewById(R.id.pumpCheck);
//        check.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                rtSendKey(Ruffy.Key.CHECK,true);
//                sleep(100);
//                rtSendKey(Ruffy.Key.NO_KEY,true);
//            }
//        });
//        Button up = (Button) displayLayout.findViewById(R.id.pumpUp);
//        up.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch(event.getAction())
//                {
//                    case MotionEvent.ACTION_DOWN:
//                        if(upRunning==0) {
//                            upRunning = 1;
//                            scheduler.execute(upThread);
//                        }
//                    break;
//
//                    case MotionEvent.ACTION_UP:
//                        upRunning=0;
//                        break;
//                }
//
//                return false;
//            }
//        });
//        Button down= (Button) displayLayout.findViewById(R.id.pumpDown);
//        down.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch(event.getAction())
//                {
//                    case MotionEvent.ACTION_DOWN:
//                        if(downRunning==0) {
//                            downRunning = 1;
//                            scheduler.execute(downThread);
//                        }
//                        break;
//
//                    case MotionEvent.ACTION_UP:
//                        downRunning=0;
//                        break;
//                }
//
//                return false;
//            }
//        });
//        Intent intent = new Intent(Plugin.bridge.getActivity(), Ruffy.class);
//        ComponentName name = Plugin.bridge.getActivity().startService(intent);
//        if(name != null)
//        {
//            if(Plugin.bridge.getActivity().bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE))
//            {
//             //   Log.v("Start","bound it");
//            }
//        }
//        return v;
//
//    }

    public static void WokePump() {


        try {
            mBoundService.doRTConnect();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private static void appendLog(final String message) {
        String currentDateTime = "NO_DATE";
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSS");
            currentDateTime = dateFormat.format(new Date()); // Find todays date
        } catch (Exception e) {
            e.printStackTrace();
        }

        final String message_time = currentDateTime + " - " + message;
//        Log.v("RUFFY_LOG", message);

        /*if(connectLog.getVisibility()!= View.GONE) {
            Plugin.bridge.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (connectLog.getLineCount() < 1000) {
                        connectLog.append("\n" + message_time);
                    } else {
                        connectLog.setText("");
                    }
                    // TODO not sure how or why, but I kept getting NPEs with the getLayout call
                    // here when doing the initial pairing
                    if (connectLog.getLayout() != null) {
                        final int scrollAmount = connectLog.getLayout().getLineTop(connectLog.getLineCount()) - connectLog.getHeight();
                        if (scrollAmount > 0)
                            connectLog.scrollTo(0, scrollAmount);
                        else
                            connectLog.scrollTo(0, 0);
                    }
                }
            });
        }*/
    }
}
