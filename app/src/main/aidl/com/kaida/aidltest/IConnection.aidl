// IConnection.aidl
package com.kaida.aidltest;

// Declare any non-default types here with import statements

interface IConnection {
   oneway void connect();
   void disconnect();
   boolean getConnectState();
}