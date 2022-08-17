// ServiceManger.aidl
package com.kaida.aidltest;

// Declare any non-default types here with import statements

interface ServiceManger {

   IBinder getServiceByName(String name);

}