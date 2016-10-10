// Generated code from Butter Knife. Do not modify!
package com.ticcorp.ticsong;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class KaKaoTalkActivity$$ViewBinder<T extends com.ticcorp.ticsong.KaKaoTalkActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558547, "field 'logout' and method 'logout'");
    target.logout = finder.castView(view, 2131558547, "field 'logout'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.logout();
        }
      });
  }

  @Override public void unbind(T target) {
    target.logout = null;
  }
}
