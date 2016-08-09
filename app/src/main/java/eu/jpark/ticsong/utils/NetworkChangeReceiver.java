package eu.jpark.ticsong.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by heyhe on 2016-04-25.
 */
public class NetworkChangeReceiver  extends BroadcastReceiver {

    //네트워크 변경 감지 유틸
    @Override
    public void onReceive(final Context context, final Intent intent) {

        String status = NetworkUtil.getConnectivityStatusString(context);

        Toast.makeText(context, status, Toast.LENGTH_LONG).show();
    }
}