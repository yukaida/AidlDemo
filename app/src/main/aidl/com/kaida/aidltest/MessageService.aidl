// MessageService.aidl
package com.kaida.aidltest;
import com.kaida.aidltest.entity.MessageBean;
import com.kaida.aidltest.MessageReceiveListener;
// Declare any non-default types here with import statements

interface MessageService {

   void sendMessage(out MessageBean messageBean);

   void registerMessageReceiveListener(MessageReceiveListener messageReceiverListener);
   void unRegisterMessageReceiveListener(MessageReceiveListener messageReceiverListener);

}