package com.ticcorp.ticsong;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.tsengvn.typekit.Typekit;
import com.tsengvn.typekit.TypekitContextWrapper;


public class ApplicationClass extends Application {

    //어플리케이션이 시작할 때 실행
    @Override
    public void onCreate() {
        super.onCreate();
        Typekit.getInstance().addNormal(Typekit.createFromAsset(this, "NanumBarunGothicBold.otf"))
                .addNormal(Typekit.createFromAsset(this, "Exo-BlackItalic.otf"))
                .addBoldItalic(Typekit.createFromAsset(this, "Exo-BoldItalic.otf"))
                .addItalic(Typekit.createFromAsset(this, "Exo-SemiBoldItalic.otf"));
    }

    //화면이 바뀌면 실행
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

}