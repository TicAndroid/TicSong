// Generated code from Butter Knife. Do not modify!
package com.ticcorp.ticsong;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SettingActivity$$ViewBinder<T extends com.ticcorp.ticsong.SettingActivity> implements ViewBinder<T> {
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
    view = finder.findRequiredView(source, 2131558588, "field 'setting_music' and method 'settingMusicClick'");
    target.setting_music = finder.castView(view, 2131558588, "field 'setting_music'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.settingMusicClick();
        }
      });
    view = finder.findRequiredView(source, 2131558589, "field 'setting_fx' and method 'settingFxClick'");
    target.setting_fx = finder.castView(view, 2131558589, "field 'setting_fx'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.settingFxClick();
        }
      });
    view = finder.findRequiredView(source, 2131558590, "method 'tutorialClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.tutorialClick();
        }
      });
    view = finder.findRequiredView(source, 2131558591, "method 'infoClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.infoClick();
        }
      });
    view = finder.findRequiredView(source, 2131558592, "method 'askClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.askClick();
        }
      });
    view = finder.findRequiredView(source, 2131558593, "method 'aboutUsClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.aboutUsClick();
        }
      });
    view = finder.findRequiredView(source, 2131558594, "method 'openSourceClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.openSourceClick();
        }
      });
    view = finder.findRequiredView(source, 2131558583, "method 'logoutClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.logoutClick();
        }
      });
    view = finder.findRequiredView(source, 2131558595, "method 'withdrawClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.withdrawClick();
        }
      });
  }

  @Override public void unbind(T target) {
    target.btn_exit = null;
    target.setting_music = null;
    target.setting_fx = null;
  }
}
