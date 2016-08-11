// Generated code from Butter Knife. Do not modify!
package com.ticcorp.ticsong;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class GameActivity$$ViewBinder<T extends com.ticcorp.ticsong.GameActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131296288, "field 'txt_msg'");
    target.txt_msg = finder.castView(view, 2131296288, "field 'txt_msg'");
    view = finder.findRequiredView(source, 2131296301, "field 'edit_ans'");
    target.edit_ans = finder.castView(view, 2131296301, "field 'edit_ans'");
    view = finder.findRequiredView(source, 2131296289, "field 'btn_play' and method 'playClick'");
    target.btn_play = finder.castView(view, 2131296289, "field 'btn_play'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.playClick();
        }
      });
    view = finder.findRequiredView(source, 2131296290, "field 'btn_progress'");
    target.btn_progress = finder.castView(view, 2131296290, "field 'btn_progress'");
    view = finder.findRequiredView(source, 2131296291, "field 'img_life1'");
    target.img_life1 = finder.castView(view, 2131296291, "field 'img_life1'");
    view = finder.findRequiredView(source, 2131296292, "field 'img_life2'");
    target.img_life2 = finder.castView(view, 2131296292, "field 'img_life2'");
    view = finder.findRequiredView(source, 2131296293, "field 'img_life3'");
    target.img_life3 = finder.castView(view, 2131296293, "field 'img_life3'");
    view = finder.findRequiredView(source, 2131296294, "field 'img_life4'");
    target.img_life4 = finder.castView(view, 2131296294, "field 'img_life4'");
    view = finder.findRequiredView(source, 2131296300, "field 'frame_ans'");
    target.frame_ans = finder.castView(view, 2131296300, "field 'frame_ans'");
    view = finder.findRequiredView(source, 2131296304, "field 'frame_exit' and method 'frameClick'");
    target.frame_exit = finder.castView(view, 2131296304, "field 'frame_exit'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.frameClick();
        }
      });
    view = finder.findRequiredView(source, 2131296305, "field 'txt_exit'");
    target.txt_exit = finder.castView(view, 2131296305, "field 'txt_exit'");
    view = finder.findRequiredView(source, 2131296308, "field 'frame_pass' and method 'frameClick'");
    target.frame_pass = finder.castView(view, 2131296308, "field 'frame_pass'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.frameClick();
        }
      });
    view = finder.findRequiredView(source, 2131296309, "field 'txt_pass'");
    target.txt_pass = finder.castView(view, 2131296309, "field 'txt_pass'");
    view = finder.findRequiredView(source, 2131296287, "field 'btn_pass' and method 'passClick'");
    target.btn_pass = finder.castView(view, 2131296287, "field 'btn_pass'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.passClick();
        }
      });
    view = finder.findRequiredView(source, 2131296295, "field 'btn_item' and method 'itemClick'");
    target.btn_item = finder.castView(view, 2131296295, "field 'btn_item'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.itemClick();
        }
      });
    view = finder.findRequiredView(source, 2131296296, "field 'btn_item_artist' and method 'itemArtistClick'");
    target.btn_item_artist = finder.castView(view, 2131296296, "field 'btn_item_artist'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.itemArtistClick();
        }
      });
    view = finder.findRequiredView(source, 2131296297, "field 'btn_item_3sec' and method 'item3SecClick'");
    target.btn_item_3sec = finder.castView(view, 2131296297, "field 'btn_item_3sec'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.item3SecClick();
        }
      });
    view = finder.findRequiredView(source, 2131296298, "field 'btn_item_life' and method 'itemLifeClick'");
    target.btn_item_life = finder.castView(view, 2131296298, "field 'btn_item_life'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.itemLifeClick();
        }
      });
    view = finder.findRequiredView(source, 2131296299, "field 'btn_item_name' and method 'itemNameClick'");
    target.btn_item_name = finder.castView(view, 2131296299, "field 'btn_item_name'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.itemNameClick();
        }
      });
    view = finder.findRequiredView(source, 2131296303, "field 'btn_send' and method 'sendClick'");
    target.btn_send = finder.castView(view, 2131296303, "field 'btn_send'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.sendClick();
        }
      });
    view = finder.findRequiredView(source, 2131296302, "field 'btn_voice' and method 'voiceClick'");
    target.btn_voice = finder.castView(view, 2131296302, "field 'btn_voice'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.voiceClick();
        }
      });
    view = finder.findRequiredView(source, 2131296312, "field 'frame_voice' and method 'frameVoiceClick'");
    target.frame_voice = finder.castView(view, 2131296312, "field 'frame_voice'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.frameVoiceClick();
        }
      });
    view = finder.findRequiredView(source, 2131296313, "field 'txt_voice_result'");
    target.txt_voice_result = finder.castView(view, 2131296313, "field 'txt_voice_result'");
    view = finder.findRequiredView(source, 2131296314, "field 'txt_voice_system'");
    target.txt_voice_system = finder.castView(view, 2131296314, "field 'txt_voice_system'");
    view = finder.findRequiredView(source, 2131296286, "method 'exitClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.exitClick();
        }
      });
    view = finder.findRequiredView(source, 2131296306, "method 'exitOkClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.exitOkClick();
        }
      });
    view = finder.findRequiredView(source, 2131296307, "method 'exitCancelClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.exitCancelClick();
        }
      });
    view = finder.findRequiredView(source, 2131296310, "method 'passOkClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.passOkClick();
        }
      });
    view = finder.findRequiredView(source, 2131296311, "method 'passCancelClick'");
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
    target.btn_item = null;
    target.btn_item_artist = null;
    target.btn_item_3sec = null;
    target.btn_item_life = null;
    target.btn_item_name = null;
    target.btn_send = null;
    target.btn_voice = null;
    target.frame_voice = null;
    target.txt_voice_result = null;
    target.txt_voice_system = null;
  }
}
