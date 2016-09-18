package com.ticcorp.ticsong;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ticcorp.ticsong.model.CustomPreference;
import com.tsengvn.typekit.TypekitContextWrapper;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by JPark on 2016-09-05.
 */
public class AgreementActivity extends Activity {

    @Bind(R.id.btn_agreement)
    TextView btn_agreement;

    @Bind(R.id.btn_exit)
    ImageButton btn_exit;


    public Animation btn_click;

    CustomPreference pref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);
        ButterKnife.bind(this);

        pref = pref.getInstance(this.getApplicationContext());
        //btn_click = AnimationUtils.loadAnimation(this, R.anim.button_click_animation);

        btn_exit.setVisibility(View.GONE); // 뒤로 버튼 감춤
    }

    @OnClick (R.id.btn_agreement)
    void agreementClick() {
        //btn_agreement.startAnimation(btn_click);
        pref.put("tutorial", 1); // 약관 동의 함 확인(튜토리얼은 보지 않음, 임시로 프리퍼런스에 대입)
        startActivity(new Intent(AgreementActivity.this, TutorialActivity.class));
        AgreementActivity.this.finish();
    }

    // 폰트 적용
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}
