// Generated code from Butter Knife. Do not modify!
package com.ticcorp.ticsong;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class GameActivity$$ViewBinder<T extends com.ticcorp.ticsong.GameActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558518, "field 'txt_msg'");
    target.txt_msg = finder.castView(view, 2131558518, "field 'txt_msg'");
    view = finder.findRequiredView(source, 2131558534, "field 'edit_ans'");
    target.edit_ans = finder.castView(view, 2131558534, "field 'edit_ans'");
    view = finder.findRequiredView(source, 2131558541, "field 'listen'");
    target.listen = finder.castView(view, 2131558541, "field 'listen'");
    view = finder.findRequiredView(source, 2131558543, "field 'mic'");
    target.mic = finder.castView(view, 2131558543, "field 'mic'");
    view = finder.findRequiredView(source, 2131558519, "field 'btn_play' and method 'playClick'");
    target.btn_play = finder.castView(view, 2131558519, "field 'btn_play'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.playClick();
        }
      });
    view = finder.findRequiredView(source, 2131558537, "field 'img_life1'");
    target.img_life1 = finder.castView(view, 2131558537, "field 'img_life1'");
    view = finder.findRequiredView(source, 2131558538, "field 'img_life2'");
    target.img_life2 = finder.castView(view, 2131558538, "field 'img_life2'");
    view = finder.findRequiredView(source, 2131558539, "field 'img_life3'");
    target.img_life3 = finder.castView(view, 2131558539, "field 'img_life3'");
    view = finder.findRequiredView(source, 2131558533, "field 'frame_ans'");
    target.frame_ans = finder.castView(view, 2131558533, "field 'frame_ans'");
    view = finder.findRequiredView(source, 2131558522, "field 'space'");
    target.space = finder.castView(view, 2131558522, "field 'space'");
    view = finder.findRequiredView(source, 2131558517, "field 'btn_pass' and method 'passOkClick'");
    target.btn_pass = finder.castView(view, 2131558517, "field 'btn_pass'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.passOkClick();
        }
      });
    view = finder.findRequiredView(source, 2131558536, "field 'btn_send' and method 'sendClick'");
    target.btn_send = finder.castView(view, 2131558536, "field 'btn_send'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.sendClick();
        }
      });
    view = finder.findRequiredView(source, 2131558535, "field 'btn_voice' and method 'voiceClick'");
    target.btn_voice = finder.castView(view, 2131558535, "field 'btn_voice'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.voiceClick();
        }
      });
    view = finder.findRequiredView(source, 2131558540, "field 'frame_voice' and method 'frameVoiceClick'");
    target.frame_voice = finder.castView(view, 2131558540, "field 'frame_voice'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.frameVoiceClick();
        }
      });
    view = finder.findRequiredView(source, 2131558542, "field 'txt_voice_system'");
    target.txt_voice_system = finder.castView(view, 2131558542, "field 'txt_voice_system'");
    view = finder.findRequiredView(source, 2131558525, "method 'onButtonClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onButtonClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558527, "method 'onButtonClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onButtonClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558529, "method 'onButtonClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onButtonClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558531, "method 'onButtonClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onButtonClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558511, "method 'exitOkClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.exitOkClick();
        }
      });
  }

  @Override public void unbind(T target) {
    target.txt_msg = null;
    target.edit_ans = null;
    target.listen = null;
    target.mic = null;
    target.btn_play = null;
    target.img_life1 = null;
    target.img_life2 = null;
    target.img_life3 = null;
    target.frame_ans = null;
    target.space = null;
    target.btn_pass = null;
    target.btn_send = null;
    target.btn_voice = null;
    target.frame_voice = null;
    target.txt_voice_system = null;
  }
}
