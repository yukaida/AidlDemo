package com.kaida.aidltest

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.RemoteCallbackList
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LifecycleCoroutineScope
import com.kaida.aidltest.entity.MessageBean
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

private const val TAG = "RemoteService"

class RemoteService : Service() {

    //连接状态
    var connectionState = false

    val handler = Handler(Looper.getMainLooper())

    val remoteCallbackList = RemoteCallbackList<MessageReceiveListener>()

    //RemoteService代理
    private val iConnectionService = object : IConnection.Stub() {
        override fun connect() {
            connectionState = true

            try {
                Thread.sleep(3000)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            handler.post {
                Toast.makeText(this@RemoteService, "connect", Toast.LENGTH_SHORT).show()
            }

            Log.d(TAG, "connect: ")
        }

        override fun disconnect() {
            connectionState = false
            Log.d(TAG, "disconnect: ")
        }

        override fun getConnectState(): Boolean {
            Log.d(TAG, "getConnectState: ")
            return connectionState
        }

    }

    //MessageService代理
    private val messageService =  object :MessageService.Stub() {
        override fun sendMessage(messageBean: MessageBean?) {
            val callBackSize=remoteCallbackList.beginBroadcast()
            for (index in 0 until callBackSize) {
                val currentReceiverListener = remoteCallbackList.getBroadcastItem(index)
                currentReceiverListener.onMessageReceiver(messageBean)
            }
            remoteCallbackList.finishBroadcast()

        }

        override fun registerMessageReceiveListener(messageReceiverListener: MessageReceiveListener) {
            remoteCallbackList.register(messageReceiverListener)
        }

        override fun unRegisterMessageReceiveListener(messageReceiverListener: MessageReceiveListener) {
            remoteCallbackList.unregister(messageReceiverListener)
        }

    }


    //serviceManager 管理类
    private val serviceManger=object :ServiceManger.Stub(){
        override fun getServiceByName(name: String): IBinder {
           return when(name){
               "MessageService"-> {
                   messageService
               }
               "IConnection"->{
                   iConnectionService
               }
               else -> {
                   iConnectionService
               }
           }
        }

    }

    override fun onBind(intent: Intent): IBinder {
        return serviceManger.asBinder()
    }


    override fun onCreate() {
        super.onCreate()

        thread {
            while (true) {
                Thread.sleep(3000)
                Log.d(TAG, "create message and send: ")
                messageService.sendMessage(MessageBean("this is a message create from RemoteService",true))
            }
        }
    }
}