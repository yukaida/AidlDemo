package com.kaida.aidltest

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log
import kotlin.concurrent.thread

private const val TAG = "RemoteMessageService"

class RemoteMessageService : Service() {


    private val handler = Handler(
        Looper.getMainLooper()
    ) {
        Log.d(TAG, "handler message ${it.data.get("content")}")
        messengerMainProxy = it.replyTo

        thread {
            Thread.sleep(3000)
            messengerMainProxy?.send(Message.obtain().apply {
                data = Bundle().apply {
                    putString("content", "this is a message send from RemoteMessageService")
                }
            })
        }

        true
    }

    private val messenger = Messenger(handler)

    private var messengerMainProxy: Messenger? = null

    override fun onBind(intent: Intent): IBinder {
        return messenger.binder
    }


}