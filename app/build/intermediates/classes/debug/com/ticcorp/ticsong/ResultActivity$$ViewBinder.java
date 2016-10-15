// Generated code from Butter Knife. Do not modify!
package com.ticcorp.ticsong;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ResultActivity$$ViewBinder<T extends com.ticcorp.ticsong.ResultActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558576, "field 'score'");
    target.score = finder.castView(view, 2131558576, "field 'score'");
    view = finder.findRequiredView(source, 2131558568, "field 'txt_result'");
    target.txt_result = finder.castView(view, 2131558568, "field 'txt_result'");
    view = finder.findRequiredView(source, 2131558577, "field 'level'");
    target.level = finder.castView(view, 2131558577, "field 'level'");
    view = finder.findRequiredView(source, 2131558557, "field 'txt_exp'");
    target.txt_exp = finder.castView(view, 2131558557, "field 'txt_exp'");
    view = finder.findRequiredView(source, 2131558581, "method 'shareClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.shareClick();
        }
      });
    view = finder.findRequiredView(source, 2131558580, "method 'mainClick'");
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
    target.txt_result = null;
    target.level = null;
    target.txt_exp = null;
  }
}
