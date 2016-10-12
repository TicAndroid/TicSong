// Generated code from Butter Knife. Do not modify!
package com.ticcorp.ticsong;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class RankingActivity$$ViewBinder<T extends com.ticcorp.ticsong.RankingActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558511, "field 'btn_exit' and method 'exitClick'");
    target.btn_exit = finder.castView(view, 2131558511, "field 'btn_exit'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.exitClick();
        }
      });
    view = finder.findRequiredView(source, 2131558561, "field 'txt_friend'");
    target.txt_friend = finder.castView(view, 2131558561, "field 'txt_friend'");
    view = finder.findRequiredView(source, 2131558562, "field 'txt_yourRank'");
    target.txt_yourRank = finder.castView(view, 2131558562, "field 'txt_yourRank'");
    view = finder.findRequiredView(source, 2131558563, "field 'img_profile'");
    target.img_profile = finder.castView(view, 2131558563, "field 'img_profile'");
    view = finder.findRequiredView(source, 2131558564, "field 'ranking_standby'");
    target.ranking_standby = finder.castView(view, 2131558564, "field 'ranking_standby'");
  }

  @Override public void unbind(T target) {
    target.btn_exit = null;
    target.txt_friend = null;
    target.txt_yourRank = null;
    target.img_profile = null;
    target.ranking_standby = null;
  }
}
