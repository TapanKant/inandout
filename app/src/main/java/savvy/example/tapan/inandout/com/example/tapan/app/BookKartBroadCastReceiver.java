package savvy.example.tapan.inandout.com.example.tapan.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class BookKartBroadCastReceiver extends BroadcastReceiver {
    public static MyBroadCastReceiverListener myBroadCastReceiverListener;

    public BookKartBroadCastReceiver() {
        super();
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();
        if (myBroadCastReceiverListener != null){
            myBroadCastReceiverListener.onNetworkConnectionChanged(isConnected);
        }
    }

    public static boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) MainApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }



    public interface MyBroadCastReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }

}
