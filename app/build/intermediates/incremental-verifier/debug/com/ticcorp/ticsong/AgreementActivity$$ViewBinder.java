// Generated code from Butter Knife. Do not modify!
package com.ticcorp.ticsong;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class AgreementActivity$$ViewBinder<T extends com.ticcorp.ticsong.AgreementActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131492976, "field 'btn_agreement' and method 'agreementClick'");
    target.btn_agreement = finder.castView(view, 2131492976, "field 'btn_agreement'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.agreementClick();
        }
      });
    view = finder.findRequiredView(source, 2131492975, "field 'btn_exit'");
    target.btn_exit = finder.castView(view, 2131492975, "field 'btn_exit'");
  }

  @Override public void unbind(T target) {
    target.btn_agreement = null;
    target.btn_exit = null;
  }
}
