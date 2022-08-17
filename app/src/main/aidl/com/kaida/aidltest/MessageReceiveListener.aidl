// MessageReceiveListener.aidl
package com.kaida.aidltest;
import com.kaida.aidltest.entity.MessageBean;
// Declare any non-default types here with import statements

interface MessageReceiveListener {

   void onMessageReceiver(inout MessageBean messageBean);

}