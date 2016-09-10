// Generated code from Butter Knife. Do not modify!
package com.ticcorp.ticsong;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MainActivity$$ViewBinder<T extends com.ticcorp.ticsong.MainActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427445, "field 'mainJukeBox' and method 'mainJokeBoxClicked'");
    target.mainJukeBox = finder.castView(view, 2131427445, "field 'mainJukeBox'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.mainJokeBoxClicked();
        }
      });
    view = finder.findRequiredView(source, 2131427479, "field 'btn_ranking' and method 'rankingClicked'");
    target.btn_ranking = finder.castView(view, 2131427479, "field 'btn_ranking'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.rankingClicked();
        }
      });
    view = finder.findRequiredView(source, 2131427480, "field 'btn_setting'");
    target.btn_setting = finder.castView(view, 2131427480, "field 'btn_setting'");
    view = finder.findRequiredView(source, 2131427482, "field 'img_juke_bold'");
    target.img_juke_bold = finder.castView(view, 2131427482, "field 'img_juke_bold'");
    view = finder.findRequiredView(source, 2131427481, "field 'img_juke_sport'");
    target.img_juke_sport = finder.castView(view, 2131427481, "field 'img_juke_sport'");
    view = finder.findRequiredView(source, 2131427451, "field 'item1_cnt'");
    target.item1_cnt = finder.castView(view, 2131427451, "field 'item1_cnt'");
    view = finder.findRequiredView(source, 2131427453, "field 'item2_cnt'");
    target.item2_cnt = finder.castView(view, 2131427453, "field 'item2_cnt'");
    view = finder.findRequiredView(source, 2131427455, "field 'item3_cnt'");
    target.item3_cnt = finder.castView(view, 2131427455, "field 'item3_cnt'");
    view = finder.findRequiredView(source, 2131427457, "field 'item4_cnt'");
    target.item4_cnt = finder.castView(view, 2131427457, "field 'item4_cnt'");
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
  }
}
