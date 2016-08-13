// Generated code from Butter Knife. Do not modify!
package com.ticcorp.ticsong;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class GameActivity$$ViewBinder<T extends com.ticcorp.ticsong.GameActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131296293, "field 'txt_msg'");
    target.txt_msg = finder.castView(view, 2131296293, "field 'txt_msg'");
    view = finder.findRequiredView(source, 2131296302, "field 'edit_ans'");
    target.edit_ans = finder.castView(view, 2131296302, "field 'edit_ans'");
    view = finder.findRequiredView(source, 2131296294, "field 'btn_play' and method 'playClick'");
    target.btn_play = finder.castView(view, 2131296294, "field 'btn_play'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.playClick();
        }
      });
    view = finder.findRequiredView(source, 2131296295, "field 'btn_progress'");
    target.btn_progress = finder.castView(view, 2131296295, "field 'btn_progress'");
    view = finder.findRequiredView(source, 2131296296, "field 'img_life1'");
    target.img_life1 = finder.castView(view, 2131296296, "field 'img_life1'");
    view = finder.findRequiredView(source, 2131296297, "field 'img_life2'");
    target.img_life2 = finder.castView(view, 2131296297, "field 'img_life2'");
    view = finder.findRequiredView(source, 2131296298, "field 'img_life3'");
    target.img_life3 = finder.castView(view, 2131296298, "field 'img_life3'");
    view = finder.findRequiredView(source, 2131296299, "field 'img_life4'");
    target.img_life4 = finder.castView(view, 2131296299, "field 'img_life4'");
    view = finder.findRequiredView(source, 2131296301, "field 'frame_ans'");
    target.frame_ans = finder.castView(view, 2131296301, "field 'frame_ans'");
    view = finder.findRequiredView(source, 2131296310, "field 'frame_exit' and method 'frameClick'");
    target.frame_exit = finder.castView(view, 2131296310, "field 'frame_exit'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.frameClick();
        }
      });
    view = finder.findRequiredView(source, 2131296311, "field 'txt_exit'");
    target.txt_exit = finder.castView(view, 2131296311, "field 'txt_exit'");
    view = finder.findRequiredView(source, 2131296314, "field 'frame_pass' and method 'frameClick'");
    target.frame_pass = finder.castView(view, 2131296314, "field 'frame_pass'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.frameClick();
        }
      });
    view = finder.findRequiredView(source, 2131296315, "field 'txt_pass'");
    target.txt_pass = finder.castView(view, 2131296315, "field 'txt_pass'");
    view = finder.findRequiredView(source, 2131296292, "field 'btn_pass' and method 'passClick'");
    target.btn_pass = finder.castView(view, 2131296292, "field 'btn_pass'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.passClick();
        }
      });
    view = finder.findRequiredView(source, 2131296304, "field 'btn_send' and method 'sendClick'");
    target.btn_send = finder.castView(view, 2131296304, "field 'btn_send'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.sendClick();
        }
      });
    view = finder.findRequiredView(source, 2131296303, "field 'btn_voice' and method 'voiceClick'");
    target.btn_voice = finder.castView(view, 2131296303, "field 'btn_voice'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.voiceClick();
        }
      });
    view = finder.findRequiredView(source, 2131296318, "field 'frame_voice' and method 'frameVoiceClick'");
    target.frame_voice = finder.castView(view, 2131296318, "field 'frame_voice'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.frameVoiceClick();
        }
      });
    view = finder.findRequiredView(source, 2131296319, "field 'txt_voice_result'");
    target.txt_voice_result = finder.castView(view, 2131296319, "field 'txt_voice_result'");
    view = finder.findRequiredView(source, 2131296320, "field 'txt_voice_system'");
    target.txt_voice_system = finder.castView(view, 2131296320, "field 'txt_voice_system'");
    view = finder.findRequiredView(source, 2131296291, "method 'exitClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.exitClick();
        }
      });
    view = finder.findRequiredView(source, 2131296312, "method 'exitOkClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.exitOkClick();
        }
      });
    view = finder.findRequiredView(source, 2131296313, "method 'exitCancelClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.exitCancelClick();
        }
      });
    view = finder.findRequiredView(source, 2131296316, "method 'passOkClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.passOkClick();
        }
      });
    view = finder.findRequiredView(source, 2131296317, "method 'passCancelClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.passCancelClick();
        }
      });
  }

  @Override public void unbind(T target) {
    target.txt_msg = null;
    target.edit_ans = null;
    target.btn_play = null;
    target.btn_progress = null;
    target.img_life1 = null;
    target.img_life2 = null;
    target.img_life3 = null;
    target.img_life4 = null;
    target.frame_ans = null;
    target.frame_exit = null;
    target.txt_exit = null;
    target.frame_pass = null;
    target.txt_pass = null;
    target.btn_pass = null;
    target.btn_send = null;
    target.btn_voice = null;
    target.frame_voice = null;
    target.txt_voice_result = null;
    target.txt_voice_system = null;
  }
}
