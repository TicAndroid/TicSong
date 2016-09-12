// Generated code from Butter Knife. Do not modify!
package com.ticcorp.ticsong;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ServerTestActivity$$ViewBinder<T extends com.ticcorp.ticsong.ServerTestActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131493041, "field 'loginBtn' and method 'loginBtn'");
    target.loginBtn = finder.castView(view, 2131493041, "field 'loginBtn'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.loginBtn();
        }
      });
    view = finder.findRequiredView(source, 2131493042, "field 'logoutBtn' and method 'logoutBtn'");
    target.logoutBtn = finder.castView(view, 2131493042, "field 'logoutBtn'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.logoutBtn();
        }
      });
    view = finder.findRequiredView(source, 2131493046, "field 'selectBtn' and method 'deleteUserBtnClicked'");
    target.selectBtn = finder.castView(view, 2131493046, "field 'selectBtn'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.deleteUserBtnClicked();
        }
      });
    view = finder.findRequiredView(source, 2131493043, "field 'gameoverBtn' and method 'gameoverBtn'");
    target.gameoverBtn = finder.castView(view, 2131493043, "field 'gameoverBtn'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.gameoverBtn();
        }
      });
    view = finder.findRequiredView(source, 2131493044, "field 'btnGetTopRank' and method 'btnGetTopRankClicked'");
    target.btnGetTopRank = finder.castView(view, 2131493044, "field 'btnGetTopRank'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.btnGetTopRankClicked();
        }
      });
    view = finder.findRequiredView(source, 2131493045, "field 'friendBtn' and method 'friendBtnClicked'");
    target.friendBtn = finder.castView(view, 2131493045, "field 'friendBtn'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.friendBtnClicked();
        }
      });
  }

  @Override public void unbind(T target) {
    target.loginBtn = null;
    target.logoutBtn = null;
    target.selectBtn = null;
    target.gameoverBtn = null;
    target.btnGetTopRank = null;
    target.friendBtn = null;
  }
}
