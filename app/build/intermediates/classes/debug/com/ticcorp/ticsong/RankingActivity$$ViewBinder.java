// Generated code from Butter Knife. Do not modify!
package com.ticcorp.ticsong;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class RankingActivity$$ViewBinder<T extends com.ticcorp.ticsong.RankingActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131493020, "field 'txt_friend'");
    target.txt_friend = finder.castView(view, 2131493020, "field 'txt_friend'");
    view = finder.findRequiredView(source, 2131493021, "field 'txt_yourRank'");
    target.txt_yourRank = finder.castView(view, 2131493021, "field 'txt_yourRank'");
    view = finder.findRequiredView(source, 2131493022, "field 'img_profile' and method 'changeClick'");
    target.img_profile = finder.castView(view, 2131493022, "field 'img_profile'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.changeClick();
        }
      });
    view = finder.findRequiredView(source, 2131492978, "method 'exitClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.exitClick();
        }
      });
  }

  @Override public void unbind(T target) {
    target.txt_friend = null;
    target.txt_yourRank = null;
    target.img_profile = null;
  }
}
