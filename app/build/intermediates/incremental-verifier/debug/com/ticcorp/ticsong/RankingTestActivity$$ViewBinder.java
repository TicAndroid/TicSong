// Generated code from Butter Knife. Do not modify!
package com.ticcorp.ticsong;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class RankingTestActivity$$ViewBinder<T extends com.ticcorp.ticsong.RankingTestActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427482, "field 'txt_friend'");
    target.txt_friend = finder.castView(view, 2131427482, "field 'txt_friend'");
    view = finder.findRequiredView(source, 2131427483, "field 'txt_yourRank'");
    target.txt_yourRank = finder.castView(view, 2131427483, "field 'txt_yourRank'");
    view = finder.findRequiredView(source, 2131427484, "field 'img_profile'");
    target.img_profile = finder.castView(view, 2131427484, "field 'img_profile'");
    view = finder.findRequiredView(source, 2131427442, "method 'exitClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.exitClick();
        }
      });
    view = finder.findRequiredView(source, 2131427481, "method 'changeClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.changeClick();
        }
      });
  }

  @Override public void unbind(T target) {
    target.txt_friend = null;
    target.txt_yourRank = null;
    target.img_profile = null;
  }
}
