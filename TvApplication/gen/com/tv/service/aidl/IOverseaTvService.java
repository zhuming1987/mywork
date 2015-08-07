/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: E:\\My_Skyworth_Project\\MSD6A628\\2014-10-13\\628Project\\TvApplication\\src\\com\\tv\\service\\aidl\\IOverseaTvService.aidl
 */
package com.tv.service.aidl;
public interface IOverseaTvService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.tv.service.aidl.IOverseaTvService
{
private static final java.lang.String DESCRIPTOR = "com.tv.service.aidl.IOverseaTvService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.tv.service.aidl.IOverseaTvService interface,
 * generating a proxy if needed.
 */
public static com.tv.service.aidl.IOverseaTvService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.tv.service.aidl.IOverseaTvService))) {
return ((com.tv.service.aidl.IOverseaTvService)iin);
}
return new com.tv.service.aidl.IOverseaTvService.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_initSwitchSourceListener:
{
data.enforceInterface(DESCRIPTOR);
com.tv.service.aidl.ISwitchSourceListener _arg0;
_arg0 = com.tv.service.aidl.ISwitchSourceListener.Stub.asInterface(data.readStrongBinder());
this.initSwitchSourceListener(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_setSource:
{
data.enforceInterface(DESCRIPTOR);
this.setSource();
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.tv.service.aidl.IOverseaTvService
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
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void initSwitchSourceListener(com.tv.service.aidl.ISwitchSourceListener iSwitchSourceListener) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((iSwitchSourceListener!=null))?(iSwitchSourceListener.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_initSwitchSourceListener, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void setSource() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_setSource, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_initSwitchSourceListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_setSource = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
}
public void initSwitchSourceListener(com.tv.service.aidl.ISwitchSourceListener iSwitchSourceListener) throws android.os.RemoteException;
public void setSource() throws android.os.RemoteException;
}
