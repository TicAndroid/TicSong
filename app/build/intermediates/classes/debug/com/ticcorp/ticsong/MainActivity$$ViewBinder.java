// Generated code from Butter Knife. Do not modify!
package com.ticcorp.ticsong;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MainActivity$$ViewBinder<T extends com.ticcorp.ticsong.MainActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427469, "field 'mainJukeBox' and method 'mainJokeBoxClicked'");
    target.mainJukeBox = finder.castView(view, 2131427469, "field 'mainJukeBox'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.mainJokeBoxClicked();
        }
      });
<<<<<<< HEAD
    view = finder.findRequiredView(source, 2131427478, "field 'btn_ranking'");
    target.btn_ranking = finder.castView(view, 2131427478, "field 'btn_ranking'");
    view = finder.findRequiredView(source, 2131427479, "field 'btn_setting'");
    target.btn_setting = finder.castView(view, 2131427479, "field 'btn_setting'");
=======
    view = finder.findRequiredView(source, 2131427478, "method 'rankingClicked'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.rankingClicked();
        }
      });
>>>>>>> 28269a3eac0d687413000f7ae329dbdc22e66454
  }

  @Override public void unbind(T target) {
    target.mainJukeBox = null;
    target.btn_ranking = null;
    target.btn_setting = null;
  }
}
