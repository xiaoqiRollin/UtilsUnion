package com.xiaoqi.utilslibrary;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * Created by yun.wang
 * Date :2017/6/30
 * Description: ***
 * Version: 1.0.0
 */
public final class NetworkManagerUtils {

    private static final String TAG = "NetworkManagerUtils";

    private Context context;
    private static NetworkManagerUtils instance;
    /**
     * GSM 3G
     */
    private static final int NETWORK_TYPE_UMTS = 3;

    /**
     * CDMA 3G
     */
    private static final int NETWORK_TYPE_EVDO_B = 12;

    private static final int TYPE_WIMAX = 6;

    public static NetworkManagerUtils instance(Context context) {
        if (instance == null) {
            instance = new NetworkManagerUtils(context);
        }
        return instance;
    }

    private NetworkManagerUtils(Context context) {
        this.context = context;
    }

    public boolean isDataUp() {
        return isDataWIFIUp() || isDataMobileUp();
        // return (isDataWIFIUp() != isDataMobileUp()) || isDataWiMAXUp();
    }

    public boolean isDataMobileUp() {
        ConnectivityManager connectivityManager = getConnectivityManager();
        NetworkInfo networkinfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return networkinfo != null && networkinfo.isAvailable() && networkinfo.isConnected();
    }

    public boolean isDataWIFIUp() {
        ConnectivityManager connectivityManager = getConnectivityManager();
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

    public boolean isData3GUp() {
        ConnectivityManager connectivityManager = getConnectivityManager();
        NetworkInfo networkinfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return networkinfo != null && networkinfo.isAvailable() && (networkinfo.getSubtype() == NETWORK_TYPE_UMTS || networkinfo.getSubtype() == NETWORK_TYPE_EVDO_B) && networkinfo.isConnected();
    }

    public boolean isDataWiMAXUp() {
        ConnectivityManager connectivityManager = getConnectivityManager();
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(TYPE_WIMAX);
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

    /**
     * current active network interface
     *
     * @return NetworkInterface
     */
    public NetworkInterface getNetworkInterface() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();

                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if (!address.isLoopbackAddress()) {
                        return networkInterface;
                    }
                }
            }
            return null;
        } catch (Throwable e) {
            return null;
        }
    }

    private WifiManager getWifiManager() {
        return (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    public String getMacAddress() {
        return getWifiManager().getConnectionInfo().getMacAddress();
    }

    private ConnectivityManager getConnectivityManager() {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    /**
     * 检查当前网络是否可用
     *
     * @return
     */

    public static boolean isNetworkAvailable(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    System.out.println(i + "===状态===" + networkInfo[i].getState());
                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * isCurrWifiAvailable
     *
     * @param context
     * @return
     */
    public static boolean isCurrWifiAvailable(Context context) {
        ConnectivityManager connectMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = connectMgr.getActiveNetworkInfo();
        if (info != null) {
            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}