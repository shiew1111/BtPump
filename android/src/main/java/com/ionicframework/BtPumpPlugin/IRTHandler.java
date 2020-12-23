package com.ionicframework.BtPumpPlugin;

public interface IRTHandler  extends android.os.IInterface{
    public static abstract class Stub extends android.os.Binder implements IRTHandler
    {
        private static final String DESCRIPTOR = "com.ionicframework.BtPumpPlugin.IRTHandler";
        /** Construct the stub at attach it to the interface. */
        public Stub()
        {
            this.attachInterface(this, DESCRIPTOR);
        }
        /**
         * Cast an IBinder object into an com.ionicframework.BtPumpPlugin.IRTHandler interface,
         * generating a proxy if needed.
         */
        public static IRTHandler asInterface(android.os.IBinder obj)
        {
            if ((obj==null)) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (((iin!=null)&&(iin instanceof IRTHandler))) {
                return ((IRTHandler)iin);
            }
            return new Proxy(obj);
        }
        @Override public android.os.IBinder asBinder()
        {
            return this;
        }
        @Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
        {
            String descriptor = DESCRIPTOR;
            switch (code)
            {
                case INTERFACE_TRANSACTION:
                {
                    reply.writeString(descriptor);
                    return true;
                }
                case TRANSACTION_log:
                {
                    data.enforceInterface(descriptor);
                    String _arg0;
                    _arg0 = data.readString();
                    this.log(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case TRANSACTION_fail:
                {
                    data.enforceInterface(descriptor);
                    String _arg0;
                    _arg0 = data.readString();
                    this.fail(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case TRANSACTION_requestBluetooth:
                {
                    data.enforceInterface(descriptor);
                    this.requestBluetooth();
                    reply.writeNoException();
                    return true;
                }
                case TRANSACTION_rtStopped:
                {
                    data.enforceInterface(descriptor);
                    this.rtStopped();
                    reply.writeNoException();
                    return true;
                }
                case TRANSACTION_rtStarted:
                {
                    data.enforceInterface(descriptor);
                    this.rtStarted();
                    reply.writeNoException();
                    return true;
                }
                case TRANSACTION_rtClearDisplay:
                {
                    data.enforceInterface(descriptor);
                    this.rtClearDisplay();
                    reply.writeNoException();
                    return true;
                }
                case TRANSACTION_rtUpdateDisplay:
                {
                    data.enforceInterface(descriptor);
                    byte[] _arg0;
                    _arg0 = data.createByteArray();
                    int _arg1;
                    _arg1 = data.readInt();
                    this.rtUpdateDisplay(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                }
                case TRANSACTION_rtDisplayHandleMenu:
                {
                    data.enforceInterface(descriptor);
                    com.ionicframework.BtPumpPlugin.display.Menu _arg0;
                    if ((0!=data.readInt())) {
                        _arg0 = com.ionicframework.BtPumpPlugin.display.Menu.CREATOR.createFromParcel(data);
                    }
                    else {
                        _arg0 = null;
                    }
                    this.rtDisplayHandleMenu(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case TRANSACTION_rtDisplayHandleNoMenu:
                {
                    data.enforceInterface(descriptor);
                    this.rtDisplayHandleNoMenu();
                    reply.writeNoException();
                    return true;
                }
                default:
                {
                    return super.onTransact(code, data, reply, flags);
                }
            }
        }
        private static class Proxy implements IRTHandler
        {
            private android.os.IBinder mRemote;
            Proxy(android.os.IBinder remote)
            {
                mRemote = remote;
            }
            @Override public android.os.IBinder asBinder()
            {
                return mRemote;
            }
            public String getInterfaceDescriptor()
            {
                return DESCRIPTOR;
            }
            @Override public void log(String message) throws android.os.RemoteException
            {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(message);
                    mRemote.transact(Stub.TRANSACTION_log, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            @Override public void fail(String message) throws android.os.RemoteException
            {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(message);
                    mRemote.transact(Stub.TRANSACTION_fail, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            @Override public void requestBluetooth() throws android.os.RemoteException
            {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    mRemote.transact(Stub.TRANSACTION_requestBluetooth, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            @Override public void rtStopped() throws android.os.RemoteException
            {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    mRemote.transact(Stub.TRANSACTION_rtStopped, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            @Override public void rtStarted() throws android.os.RemoteException
            {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    mRemote.transact(Stub.TRANSACTION_rtStarted, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            @Override public void rtClearDisplay() throws android.os.RemoteException
            {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    mRemote.transact(Stub.TRANSACTION_rtClearDisplay, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            @Override public void rtUpdateDisplay(byte[] quarter, int which) throws android.os.RemoteException
            {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeByteArray(quarter);
                    _data.writeInt(which);
                    mRemote.transact(Stub.TRANSACTION_rtUpdateDisplay, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            @Override public void rtDisplayHandleMenu(com.ionicframework.BtPumpPlugin.display.Menu menu) throws android.os.RemoteException
            {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    if ((menu!=null)) {
                        _data.writeInt(1);
                        menu.writeToParcel(_data, 0);
                    }
                    else {
                        _data.writeInt(0);
                    }
                    mRemote.transact(Stub.TRANSACTION_rtDisplayHandleMenu, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            @Override public void rtDisplayHandleNoMenu() throws android.os.RemoteException
            {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    mRemote.transact(Stub.TRANSACTION_rtDisplayHandleNoMenu, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
        static final int TRANSACTION_log = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
        static final int TRANSACTION_fail = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
        static final int TRANSACTION_requestBluetooth = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
        static final int TRANSACTION_rtStopped = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
        static final int TRANSACTION_rtStarted = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
        static final int TRANSACTION_rtClearDisplay = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
        static final int TRANSACTION_rtUpdateDisplay = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
        static final int TRANSACTION_rtDisplayHandleMenu = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
        static final int TRANSACTION_rtDisplayHandleNoMenu = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
    }
    public void log(String message) throws android.os.RemoteException;
    public void fail(String message) throws android.os.RemoteException;
    public void requestBluetooth() throws android.os.RemoteException;
    public void rtStopped() throws android.os.RemoteException;
    public void rtStarted() throws android.os.RemoteException;
    public void rtClearDisplay() throws android.os.RemoteException;
    public void rtUpdateDisplay(byte[] quarter, int which) throws android.os.RemoteException;
    public void rtDisplayHandleMenu(com.ionicframework.BtPumpPlugin.display.Menu menu) throws android.os.RemoteException;
    public void rtDisplayHandleNoMenu() throws android.os.RemoteException;
}
