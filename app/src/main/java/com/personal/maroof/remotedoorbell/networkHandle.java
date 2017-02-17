package com.personal.maroof.remotedoorbell;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Build;
import android.util.Log;

import java.net.InetAddress;

import static android.support.v7.widget.StaggeredGridLayoutManager.TAG;

/**
 * Created by Maroof on 11/8/2016.
 */

public class NetworkHandle {


    Context mContext;

    NsdManager mNsdManager;
    NsdManager.ResolveListener mResolveListener;
    NsdManager.DiscoveryListener mDiscoveryListener;
    NsdManager.RegistrationListener mRegistrationListener;


    public static final String SERVICE_TYPE = "_http._tcp.";

    public static final String TAG = "NsdHelper";
    public String mServiceName = "NsdChat";

    NsdServiceInfo mService;







    public NetworkHandle(Context context) {
        mContext = context;
        mNsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
    }

    public void initializeNsd() {
        initializeResolveListener();
        initializeDiscoveryListener();
        initializeRegistrationListener();

        //mNsdManager.init(mContext.getMainLooper(), this);

    }
    public void registerService(int port) {
        // Create the NsdServiceInfo object, and populate it.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            NsdServiceInfo serviceInfo = new NsdServiceInfo();

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
                    // Un-registration failed.  Put debugging code here to determine why.
                }
            };
        }


    }


    public void initializeDiscoveryListener() {




                mDiscoveryListener = new NsdManager.DiscoveryListener() {

                    //  Called as soon as service discovery begins.
                    @Override
                    public void onDiscoveryStarted(String regType) {
                    }

                    @Override
                    public void onServiceFound(NsdServiceInfo service) {
                        // A service was found!  Do something with it.
                        if (!service.getServiceType().equals(SERVICE_TYPE)) {
                            // Service type is the string containing the protocol and
                            // transport layer for this service.
                        } else if (service.getServiceName().equals(mServiceName)) {
                            // The name of the service tells the user what they'd be
                            // connecting to. It could be "Bob's Chat App".
                        } else if (service.getServiceName().contains("NsdChat")) {
                            mNsdManager.resolveService(service, mResolveListener);
                        }
                    }

                    @Override
                    public void onServiceLost(NsdServiceInfo service) {
                        // When the network service is no longer available.
                        // Internal bookkeeping code goes here.
                    }

                    @Override
                    public void onDiscoveryStopped(String serviceType) {
                    }

                    @Override
                    public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            mNsdManager.stopServiceDiscovery(this);
                        }
                    }

                    @Override
                    public void onStopDiscoveryFailed(String serviceType, int errorCode){
                            mNsdManager.stopServiceDiscovery(this);
                        }

                };


        }


    public void initializeResolveListener() {
        mResolveListener = new NsdManager.ResolveListener() {

            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                // Called when the resolve fails.  Use the error code to debug.

            }

            @Override
            public void onServiceResolved(NsdServiceInfo serviceInfo) {


                if (serviceInfo.getServiceName().equals(mServiceName)) {

                    return;
                }
                mService = serviceInfo;
                int port = mService.getPort();
                InetAddress host = mService.getHost();
            }
        };
    }


    public void discoverServices() {
        mNsdManager.discoverServices(
                SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);
    }

    public void stopDiscovery() {
        mNsdManager.stopServiceDiscovery(mDiscoveryListener);
    }

    public NsdServiceInfo getChosenServiceInfo() {
        return mService;
    }

    public void tearDown() {
        mNsdManager.unregisterService(mRegistrationListener);
    }


}
