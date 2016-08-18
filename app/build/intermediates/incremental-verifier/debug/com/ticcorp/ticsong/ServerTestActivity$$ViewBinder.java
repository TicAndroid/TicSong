// Generated code from Butter Knife. Do not modify!
package com.ticcorp.ticsong;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ServerTestActivity$$ViewBinder<T extends com.ticcorp.ticsong.ServerTestActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427485, "field 'testBtn' and method 'onTestBtnClicked'");
    target.testBtn = finder.castView(view, 2131427485, "field 'testBtn'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onTestBtnClicked();
        }
      });
  }

  @Override public void unbind(T target) {
    target.testBtn = null;
  }
}
