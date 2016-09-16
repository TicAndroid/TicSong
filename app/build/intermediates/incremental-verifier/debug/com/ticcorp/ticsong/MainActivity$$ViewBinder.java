// Generated code from Butter Knife. Do not modify!
package com.ticcorp.ticsong;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MainActivity$$ViewBinder<T extends com.ticcorp.ticsong.MainActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131492982, "field 'mainJukeBox' and method 'mainJokeBoxClicked'");
    target.mainJukeBox = finder.castView(view, 2131492982, "field 'mainJukeBox'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.mainJokeBoxClicked();
        }
      });
    view = finder.findRequiredView(source, 2131493017, "field 'btn_ranking' and method 'rankingClicked'");
    target.btn_ranking = finder.castView(view, 2131493017, "field 'btn_ranking'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.rankingClicked();
        }
      });
    view = finder.findRequiredView(source, 2131493018, "field 'btn_setting'");
    target.btn_setting = finder.castView(view, 2131493018, "field 'btn_setting'");
    view = finder.findRequiredView(source, 2131493020, "field 'img_juke_bold'");
    target.img_juke_bold = finder.castView(view, 2131493020, "field 'img_juke_bold'");
    view = finder.findRequiredView(source, 2131493019, "field 'img_juke_sport'");
    target.img_juke_sport = finder.castView(view, 2131493019, "field 'img_juke_sport'");
    view = finder.findRequiredView(source, 2131492989, "field 'item1_cnt'");
    target.item1_cnt = finder.castView(view, 2131492989, "field 'item1_cnt'");
    view = finder.findRequiredView(source, 2131492991, "field 'item2_cnt'");
    target.item2_cnt = finder.castView(view, 2131492991, "field 'item2_cnt'");
    view = finder.findRequiredView(source, 2131492993, "field 'item3_cnt'");
    target.item3_cnt = finder.castView(view, 2131492993, "field 'item3_cnt'");
    view = finder.findRequiredView(source, 2131492995, "field 'item4_cnt'");
    target.item4_cnt = finder.castView(view, 2131492995, "field 'item4_cnt'");
    view = finder.findRequiredView(source, 2131493016, "field 'exp_bar'");
    target.exp_bar = finder.castView(view, 2131493016, "field 'exp_bar'");
    view = finder.findRequiredView(source, 2131493012, "field 'profile_img'");
    target.profile_img = finder.castView(view, 2131493012, "field 'profile_img'");
  }

  @Override public void unbind(T target) {
    target.mainJukeBox = null;
    target.btn_ranking = null;
    target.btn_setting = null;
    target.img_juke_bold = null;
    target.img_juke_sport = null;
    target.item1_cnt = null;
    target.item2_cnt = null;
    target.item3_cnt = null;
    target.item4_cnt = null;
    target.exp_bar = null;
    target.profile_img = null;
  }
}
