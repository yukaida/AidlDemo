package com.kaida.aidltest

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.drawable.Icon
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import com.kaida.aidltest.databinding.ActivityMainBinding
import com.kaida.aidltest.entity.MessageBean

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    var iConnectionProxy: IConnection? = null
    var messageService:MessageService?=null

    var serviceManger:ServiceManger?=null

    var messageReceiveListener=object : MessageReceiveListener.Stub() {

        override fun onMessageReceiver(messageBean: MessageBean?) {
            Log.d(TAG, "onMessageReceiver: $messageBean")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            //连接
            buttonConnect.setOnClickListener {
                bindService(
                    Intent(this@MainActivity, RemoteService::class.java),
                    object : ServiceConnection {
                        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                            serviceManger=ServiceManger.Stub.asInterface(service)

                            iConnectionProxy = IConnection.Stub.asInterface( serviceManger?.getServiceByName("IConnection"))
                            messageService=MessageService.Stub.asInterface( serviceManger?.getServiceByName("MessageService"))

                            iConnectionProxy?.let {
                                it.connect()
                                messageService?.registerMessageReceiveListener(messageReceiveListener)
                            }
                        }

                        override fun onServiceDisconnected(name: ComponentName?) {
                            iConnectionProxy?.disconnect()
                        }

                    },
                    Context.BIND_AUTO_CREATE
                )
            }

            //断开连接
            buttonDisconnect.setOnClickListener {
                messageService?.unRegisterMessageReceiveListener(messageReceiveListener)
                iConnectionProxy?.disconnect()

            }

            //获取连接状态
            buttonGetState.setOnClickListener {
                Log.d(TAG, "connectState: ${iConnectionProxy?.connectState}")

            }



        }


    }


}