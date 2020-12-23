
/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package com.ionicframework.BtPumpPlugin;
public interface IRuffyService extends android.os.IInterface
{
    /** Local-side IPC implementation stub class. */
    public static abstract class Stub extends android.os.Binder implements IRuffyService
    {
        private static final String DESCRIPTOR = "com.ionicframework.BtPumpPlugin.IRuffyService";
        /** Construct the stub at attach it to the interface. */
        public Stub()
        {
            this.attachInterface(this, DESCRIPTOR);
        }
        /**
         * Cast an IBinder object into an com.ionicframework.BtPumpPlugin.IRuffyService interface,
         * generating a proxy if needed.
         */
        public static IRuffyService asInterface(android.os.IBinder obj)
        {
            if ((obj==null)) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (((iin!=null)&&(iin instanceof IRuffyService))) {
                return ((IRuffyService)iin);
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
                case TRANSACTION_setHandler:
                {
                    data.enforceInterface(descriptor);
                    IRTHandler _arg0;
                    _arg0 = IRTHandler.Stub.asInterface(data.readStrongBinder());
                    this.setHandler(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case TRANSACTION_doRTConnect:
                {
                    data.enforceInterface(descriptor);
                    int _result = this.doRTConnect();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                }
                case TRANSACTION_doRTDisconnect:
                {
                    data.enforceInterface(descriptor);
                    this.doRTDisconnect();
                    reply.writeNoException();
                    return true;
                }
                case TRANSACTION_rtSendKey:
                {
                    data.enforceInterface(descriptor);
                    byte _arg0;
                    _arg0 = data.readByte();
                    boolean _arg1;
                    _arg1 = (0!=data.readInt());
                    this.rtSendKey(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                }
                case TRANSACTION_resetPairing:
                {
                    data.enforceInterface(descriptor);
                    this.resetPairing();
                    reply.writeNoException();
                    return true;
                }
                case TRANSACTION_isConnected:
                {
                    data.enforceInterface(descriptor);
                    boolean _result = this.isConnected();
                    reply.writeNoException();
                    reply.writeInt(((_result)?(1):(0)));
                    return true;
                }
                default:
                {
                    return super.onTransact(code, data, reply, flags);
                }
            }
        }
        private static class Proxy implements IRuffyService
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
            @Override public void setHandler(IRTHandler handler) throws android.os.RemoteException
            {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongBinder((((handler!=null))?(handler.asBinder()):(null)));
                    mRemote.transact(Stub.TRANSACTION_setHandler, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            @Override public int doRTConnect() throws android.os.RemoteException
            {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                int _result;
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    mRemote.transact(Stub.TRANSACTION_doRTConnect, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readInt();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
            /** Disconnect from the pump */
            @Override public void doRTDisconnect() throws android.os.RemoteException
            {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    mRemote.transact(Stub.TRANSACTION_doRTDisconnect, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
// TODO what's the meaning of 'changed'?

            @Override public void rtSendKey(byte keyCode, boolean changed) throws android.os.RemoteException
            {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeByte(keyCode);
                    _data.writeInt(((changed)?(1):(0)));
                    mRemote.transact(Stub.TRANSACTION_rtSendKey, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            @Override public void resetPairing() throws android.os.RemoteException
            {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    mRemote.transact(Stub.TRANSACTION_resetPairing, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            @Override public boolean isConnected() throws android.os.RemoteException
            {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                boolean _result;
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    mRemote.transact(Stub.TRANSACTION_isConnected, _data, _reply, 0);
                    _reply.readException();
                    _result = (0!=_reply.readInt());
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
        }
        static final int TRANSACTION_setHandler = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
        static final int TRANSACTION_doRTConnect = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
        static final int TRANSACTION_doRTDisconnect = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
        static final int TRANSACTION_rtSendKey = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
        static final int TRANSACTION_resetPairing = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
        static final int TRANSACTION_isConnected = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
    }
    public void setHandler(IRTHandler handler) throws android.os.RemoteException;
    public int doRTConnect() throws android.os.RemoteException;
    /** Disconnect from the pump */
    public void doRTDisconnect() throws android.os.RemoteException;
// TODO what's the meaning of 'changed'?

    public void rtSendKey(byte keyCode, boolean changed) throws android.os.RemoteException;
    public void resetPairing() throws android.os.RemoteException;
    public boolean isConnected() throws android.os.RemoteException;
}
