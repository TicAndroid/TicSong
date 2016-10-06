// Generated code from Butter Knife. Do not modify!
package com.ticcorp.ticsong;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class LoginTestActivity$$ViewBinder<T extends com.ticcorp.ticsong.LoginTestActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558546, "field 'kakaoLogin' and method 'kakaoBtnClicked'");
    target.kakaoLogin = finder.castView(view, 2131558546, "field 'kakaoLogin'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.kakaoBtnClicked();
        }
      });
    view = finder.findRequiredView(source, 2131558547, "field 'fbLogin' and method 'fbBtnClicked'");
    target.fbLogin = finder.castView(view, 2131558547, "field 'fbLogin'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.fbBtnClicked();
        }
      });
  }

  @Override public void unbind(T target) {
    target.kakaoLogin = null;
    target.fbLogin = null;
  }
}
