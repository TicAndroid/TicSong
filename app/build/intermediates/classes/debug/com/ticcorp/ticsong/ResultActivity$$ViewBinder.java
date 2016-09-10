// Generated code from Butter Knife. Do not modify!
package com.ticcorp.ticsong;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ResultActivity$$ViewBinder<T extends com.ticcorp.ticsong.ResultActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427496, "field 'score'");
    target.score = finder.castView(view, 2131427496, "field 'score'");
    view = finder.findRequiredView(source, 2131427497, "field 'level'");
    target.level = finder.castView(view, 2131427497, "field 'level'");
    view = finder.findRequiredView(source, 2131427501, "method 'shareClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.shareClick();
        }
      });
    view = finder.findRequiredView(source, 2131427500, "method 'mainClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.mainClick();
        }
      });
  }

  @Override public void unbind(T target) {
    target.score = null;
    target.level = null;
  }
}
