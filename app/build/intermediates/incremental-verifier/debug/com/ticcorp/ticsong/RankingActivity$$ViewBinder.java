// Generated code from Butter Knife. Do not modify!
package com.ticcorp.ticsong;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class RankingActivity$$ViewBinder<T extends com.ticcorp.ticsong.RankingActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131492979, "field 'btn_exit' and method 'exitClick'");
    target.btn_exit = finder.castView(view, 2131492979, "field 'btn_exit'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.exitClick();
        }
      });
<<<<<<< HEAD
    view = finder.findRequiredView(source, 2131493024, "field 'ranking_standby'");
    target.ranking_standby = finder.castView(view, 2131493024, "field 'ranking_standby'");
    view = finder.findRequiredView(source, 2131492979, "method 'exitClick'");
=======
    view = finder.findRequiredView(source, 2131493021, "field 'txt_friend'");
    target.txt_friend = finder.castView(view, 2131493021, "field 'txt_friend'");
    view = finder.findRequiredView(source, 2131493022, "field 'txt_yourRank'");
    target.txt_yourRank = finder.castView(view, 2131493022, "field 'txt_yourRank'");
    view = finder.findRequiredView(source, 2131493023, "field 'img_profile' and method 'changeClick'");
    target.img_profile = finder.castView(view, 2131493023, "field 'img_profile'");
>>>>>>> 20093bfc8eed7b2202a0942c5835a167dca9b048
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
    target.btn_exit = null;
    target.txt_friend = null;
    target.txt_yourRank = null;
    target.img_profile = null;
    target.ranking_standby = null;
  }
}
