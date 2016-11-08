package com.personal.maroof.remotedoorbell;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Build;

/**
 * Created by Maroof on 11/8/2016.
 */

public class networkHandle {


    Context mContext;

    NsdManager mNsdManager;
    NsdManager.ResolveListener mResolveListener;
    NsdManager.DiscoveryListener mDiscoveryListener;
    NsdManager.RegistrationListener mRegistrationListener;
    NsdServiceInfo mService;


    private String mServiceName;



    public networkHandle(Context context) {
        mContext = context;
        mNsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
    }



    public void registerService(int port) {
        // Create the NsdServiceInfo object, and populate it.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            NsdServiceInfo serviceInfo  = new NsdServiceInfo();

            // The name is subject to change based on conflicts
            // with other services advertised on the same network.
            serviceInfo.setServiceName("RingBell");
            serviceInfo.setServiceType("_http._tcp.");
            serviceInfo.setPort(port);

            mNsdManager.registerService(
                    serviceInfo, NsdManager.PROTOCOL_DNS_SD, mRegistrationListener);




        }

    }



    public void initializeRegistrationListener() {


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            mRegistrationListener = new NsdManager.RegistrationListener() {

                @Override
                public void onServiceRegistered(NsdServiceInfo NsdServiceInfo) {
                    // Save the service name.  Android may have changed it in order to
                    // resolve a conflict, so update the name you initially requested
                    // with the name Android actually used.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        mServiceName = NsdServiceInfo.getServiceName();
                    }
                }

                @Override
                public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                    // Registration failed!  Put debugging code here to determine why.
                }

                @Override
                public void onServiceUnregistered(NsdServiceInfo arg0) {
                    // Service has been unregistered.  This only happens when you call
                    // NsdManager.unregisterService() and pass in this listener.
                }

                @Override
                public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                    // Unregistration failed.  Put debugging code here to determine why.
                }
            };
        }


    }







}
