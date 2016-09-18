package com.ticcorp.ticsong;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

/**
 * Created by JPark on 2016-09-05.
 */
public class AboutUsDialog extends Dialog {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 다이얼로그 외부 흐리게 표현
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        setContentView(R.layout.activity_aboutus);

    }

    public AboutUsDialog(Context context) {
        super(context, R.style.Theme_Transparent);
    }
}
