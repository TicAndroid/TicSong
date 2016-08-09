package eu.jpark.ticsong;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GameActivity extends Activity {

    public int MAX_QUIZ_NUM = 5; // 한 게임의 문제 개수
    public int MAX_LIFE = 3; // 한 문제의 정답 기회

    public int gameMode = 0; // 0 : 문제 대기 중, 1 : 문제 내는 중, 2 : 맞추는 중, 3 : 정답 확인 중
    public ArrayList<Integer> itemArray = new ArrayList<Integer>();
    // 아티스트 보여주기, 3초 듣기, 정답 1회 증가, 제목 한 글자 보여주기
    public int itemUsed = 0; // 한 노래에서 아이템은 한 번 사용 가능
    // 0 : 아이템 사용하지 않음, 1 : 아티스트 보여주기, 2 : 3초 듣기, 3 : 정답 1회 증가, 4 : 제목 한 글자 보여주기
    public int quizNum = 0; // 문제 번호
    public int life = MAX_LIFE; // 현재 정답 기회
    public int score = 0; // 누적 획득 점수
    public ArrayList<String> answerArray = new ArrayList<String>(); // 정답 리스트
    public ArrayList<String> artistArray = new ArrayList<String>(); // 아티스트 리스트
    public ArrayList<String> addressArray = new ArrayList<String>(); // 음원 주소 리스트
    public ArrayList<Integer> correctArray = new ArrayList<Integer>(); // 정답 여부 리스트, 맞출 때의 남은 기회 기록

    public MediaPlayer mPlayer;
    public TextWatcher textWatcher;

    @Bind(R.id.txt_msg)
    TextView txt_msg;
    @Bind(R.id.edit_ans)
    EditText edit_ans;
    @Bind(R.id.btn_play)
    ImageView btn_play;
    @Bind(R.id.img_life1) ImageView img_life1;
    @Bind(R.id.img_life2) ImageView img_life2;
    @Bind(R.id.img_life3) ImageView img_life3;
    @Bind(R.id.img_life4) ImageView img_life4;
    @Bind(R.id.frame_exit)
    FrameLayout frame_exit;
    @Bind(R.id.txt_exit) TextView txt_exit;
    @Bind(R.id.frame_pass) FrameLayout frame_pass;
    @Bind(R.id.txt_pass) TextView txt_pass;
    @Bind(R.id.btn_pass) ImageView btn_pass;
    @Bind(R.id.btn_item)
    Button btn_item;
    @Bind(R.id.btn_item_artist) Button btn_item_artist;
    @Bind(R.id.btn_item_3sec) Button btn_item_3sec;
    @Bind(R.id.btn_item_life) Button btn_item_life;
    @Bind(R.id.btn_item_name) Button btn_item_name;
    @Bind(R.id.btn_send) Button btn_send;
    @Bind(R.id.btn_voice) Button btn_voice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);

        quizSetting();
        textWatching();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        // 뒤로 가기 버튼 터치 시 지금 메인화면으로 돌아가면 경험치를 얻을 수 없다는 알림을 띄우고 다시 확인
        downKeyboard(this, edit_ans);
        txt_exit.setText("지금 게임에서 나가실 경우 경험치를 얻을 수 없습니다. 그래도 나가시겠습니까?");
        frame_exit.setVisibility(View.VISIBLE); // 팝업
    }

    @OnClick(R.id.btn_exit)
    void exitClick() {
        // 나가기 버튼 클릭 시 지금 메인화면으로 돌아가면 경험치를 얻을 수 없다는 알림을 띄우고 다시 확인
        downKeyboard(this, edit_ans);
        txt_exit.setText("지금 게임에서 나가실 경우 경험치를 얻을 수 없습니다. 그래도 나가시겠습니까?");
        frame_exit.setVisibility(View.VISIBLE); // 팝업
    }

    @OnClick (R.id.btn_exit_ok)
    void exitOkClick() {
        // 나가기 버튼 확인 시 게임 종료
        try {
            if (mPlayer != null) {// 음악 재생 중일 경우 음악 종료
                if (mPlayer.isPlaying()) {
                    mPlayer.stop();
                    mPlayer.release();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finish();
    }

    @OnClick (R.id.btn_exit_cancel)
    void exitCancelClick() {
        // 나가기 버튼 취소 시 팝업 닫고 게임 계속 진행
        frame_exit.setVisibility(View.GONE); // 팝업 제거
    }

    @OnClick (R.id.btn_pass)
    void passClick() {
        // 패스 버튼 클릭 시 확인 팝업을 띄움
        downKeyboard(this, edit_ans);
        txt_pass.setText("정말 이번 문제를 패스하시겠습니까?");
        frame_pass.setVisibility(View.VISIBLE); // 팝업
    }

    @OnClick (R.id.btn_pass_ok)
    void passOkClick() {
        // 패스 버튼 확인 시 오답 처리하고 정답 공개
        frame_pass.setVisibility(View.GONE); // 팝업 제거
        gameMode = 3;
        btn_pass.setVisibility(View.INVISIBLE); // 패스 버튼 숨기기
        txt_msg.setText("정답은 " + artistArray.get(quizNum-1) + "의 " + answerArray.get(quizNum - 1) + "였습니다!");
        correctArray.set((quizNum-1), 0); // 오답 문제 기록
        mPlayer = new MediaPlayer(); // 정답 음악 재생
        mPlayer = MediaPlayer.create(this, R.raw.sample); // 임시 셋팅
        mPlayer.start();
    }

    @OnClick (R.id.btn_pass_cancel)
    void passCancelClick() {
        // 패스 버튼 취소 시 팝업 닫고 게임 계속 진행
        frame_pass.setVisibility(View.GONE); // 팝업 제거
    }

    @OnClick ({R.id.frame_exit, R.id.frame_pass})
    void frameClick() {
        // 팝업이 띄워져 있을 시 게임화면 클릭 방지하기 위한 함수
    }

    @OnClick (R.id.btn_play)
    void playClick() {
        downKeyboard(this, edit_ans);
        switch (gameMode) {
            case 0:
                // 문제 1초 재생
                musicPlay(1000);
                break;
            case 1:
                // 문제 재생 중이므로 반응 없음
                break;
            case 2:
                // 문제 재생
                musicPlay(1000);
                break;
            case 3:
                // 다음 문제로
                nextQuiz();
                break;
        }

    }

    @OnClick (R.id.btn_item)
    void itemClick() {
        // 아이템 사용
        downKeyboard(this, edit_ans);
        if (gameMode == 0 | gameMode == 2) { // 맞추는 중에만 사용 가능
            switch (itemUsed) {
                case 0: // 아이템을 사용하지 않은 경우
                    // 아이템 fab
                    // 이하 임시 구현
                    if (btn_item_artist.getVisibility() == View.INVISIBLE) { // 아이템이 감춰져 있을 때
                        btn_item_artist.setVisibility(View.VISIBLE);
                        btn_item_3sec.setVisibility(View.VISIBLE);
                        btn_item_life.setVisibility(View.VISIBLE);
                        btn_item_name.setVisibility(View.VISIBLE);
                    } else {
                        btn_item_artist.setVisibility(View.INVISIBLE);
                        btn_item_3sec.setVisibility(View.INVISIBLE);
                        btn_item_life.setVisibility(View.INVISIBLE);
                        btn_item_name.setVisibility(View.INVISIBLE);
                    }
                    break;
                case 1: // 아티스트 보여주기 아이템을 사용한 경우
                    Toast.makeText(this, "아이템은 한 문제에 한 번만 사용 가능합니다!" +
                            "\n이 곡의 아티스트는 '" + artistArray.get(quizNum - 1) + "'입니다.", Toast.LENGTH_SHORT).show();
                    break;
                case 2: // 3초 듣기 아이템을 사용한 경우
                    Toast.makeText(this, "아이템은 한 문제에 한 번만 사용 가능합니다!" +
                            "\n3초 듣기 아이템을 이미 사용하셨습니다.", Toast.LENGTH_SHORT).show();
                    break;
                case 3: // 정답 1회 증가 아이템을 사용한 경우
                    Toast.makeText(this, "아이템은 한 문제에 한 번만 사용 가능합니다!" +
                            "\n정답 1회 증가 아이템을 이미 사용하셨습니다", Toast.LENGTH_SHORT).show();
                    break;
                case 4: // 제목 한 글자 보여주기 아이템을 사용한 경우
                    Toast.makeText(this, "아이템은 한 문제에 한 번만 사용 가능합니다!" +
                            "곡 제목의 첫 글자는 '" + textChanger(answerArray.get(quizNum - 1)).charAt(0) + "'입니다.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    @OnClick (R.id.btn_item_artist)
    void itemArtistClick() {
        // 아티스트 보여주기 아이템
        downKeyboard(this, edit_ans);
        if (gameMode == 0 | gameMode == 2) { // 맞추는 중에만 사용 가능
            itemUsed = 1;
            Toast.makeText(this, "이 곡의 아티스트는 '" + artistArray.get(quizNum - 1) + "'입니다.", Toast.LENGTH_SHORT).show();
            btn_item_artist.setVisibility(View.INVISIBLE);
            btn_item_3sec.setVisibility(View.INVISIBLE);
            btn_item_life.setVisibility(View.INVISIBLE);
            btn_item_name.setVisibility(View.INVISIBLE);
            btn_item.setText("USED");
        }
    }

    @OnClick (R.id.btn_item_3sec)
    void item3SecClick() {
        // 3초 듣기 아이템
        downKeyboard(this, edit_ans);
        if (gameMode == 0 | gameMode == 2) { // 맞추는 중에만 사용 가능
            itemUsed = 2;
            musicPlay(3000);
            btn_item_artist.setVisibility(View.INVISIBLE);
            btn_item_3sec.setVisibility(View.INVISIBLE);
            btn_item_life.setVisibility(View.INVISIBLE);
            btn_item_name.setVisibility(View.INVISIBLE);
            btn_item.setText("USED");
        }
    }

    @OnClick (R.id.btn_item_life)
    void itemLifeClick() {
        // 정답 1회 증가 아이템
        downKeyboard(this, edit_ans);
        if (gameMode == 0 | gameMode == 2) { // 맞추는 중에만 사용 가능
            itemUsed = 3;
            life++;
            lifeRefresh();
            btn_item_artist.setVisibility(View.INVISIBLE);
            btn_item_3sec.setVisibility(View.INVISIBLE);
            btn_item_life.setVisibility(View.INVISIBLE);
            btn_item_name.setVisibility(View.INVISIBLE);
            btn_item.setText("USED");
        }
    }

    @OnClick (R.id.btn_item_name)
    void itemNameClick() {
        // 제목 한 글자 보여주기 아이템
        downKeyboard(this, edit_ans);
        if (gameMode == 0 | gameMode == 2) { // 맞추는 중에만 사용 가능
            itemUsed = 4;
            Toast.makeText(this, "곡 제목의 첫 글자는 '" + textChanger(answerArray.get(quizNum - 1)).charAt(0) + "'입니다.", Toast.LENGTH_SHORT).show();
            btn_item_artist.setVisibility(View.INVISIBLE);
            btn_item_3sec.setVisibility(View.INVISIBLE);
            btn_item_life.setVisibility(View.INVISIBLE);
            btn_item_name.setVisibility(View.INVISIBLE);
            btn_item.setText("USED");
        }
    }

    @OnClick (R.id.btn_voice)
    void voiceClick() {
        // 음성 녹음
        downKeyboard(this, edit_ans);
        if (gameMode == 0 | gameMode == 2) { // 맞추는 중에만 사용 가능
            Toast.makeText(this, "음성인식 구현 중!", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick (R.id.btn_send)
    void sendClick() {
        // 정답 제출
        downKeyboard(this, edit_ans);
        if (gameMode == 0 | gameMode == 2) { // 맞추는 중에만 사용 가능
            if (textChanger(edit_ans.getText().toString()).equals(textChanger(answerArray.get(quizNum - 1)))) {
                // 정답 시
                gameMode = 3;
                btn_pass.setVisibility(View.INVISIBLE); // 패스 버튼 숨기기
                int nowScore = 0;
                switch (life) { // 남은 기회에 따른 점수 계산
                    case 3:
                        nowScore = 100;
                        break;
                    case 2:
                        nowScore = 60;
                        break;
                    case 1:
                        nowScore = 30;
                        break;
                    default:
                        nowScore = 0;
                        break;
                }
                txt_msg.setText(artistArray.get(quizNum - 1) + "의 " + answerArray.get(quizNum - 1) + " 정답입니다! " + nowScore + "점 획득!");
                score += nowScore; // 누적 점수에 이번 점수 추가
                correctArray.set((quizNum - 1), life); // 남은 라이프 기록
                mPlayer = new MediaPlayer(); // 정답 음악 재생
                mPlayer = MediaPlayer.create(this, R.raw.sample); // 임시 셋팅
                mPlayer.start();
            } else if (textChanger(edit_ans.getText().toString()).equals("")) {
                // 정답이 비어있을 때
                Toast.makeText(this, "정답을 입력해주세요.", Toast.LENGTH_SHORT).show();
            } else {
                // 오답 시
                life--;
                lifeRefresh();
                if (life > 0) { // 아직 기회가 남았을 경우
                    txt_msg.setText("틀렸습니다! " + life + "번 남았습니다!");
                    edit_ans.setText(""); // EditText 초기화
                } else { // 기회 모두 사용 시 오답 처리 후 정답 공개
                    gameMode = 3;
                    btn_pass.setVisibility(View.INVISIBLE); // 패스 버튼 숨기기
                    txt_msg.setText("틀렸습니다! 정답은 " + artistArray.get(quizNum - 1) + "의 " + answerArray.get(quizNum - 1) + "였습니다!");
                    correctArray.set((quizNum - 1), 0); // 오답 문제 기록
                    mPlayer = new MediaPlayer(); // 정답 음악 재생
                    mPlayer = MediaPlayer.create(this, R.raw.sample); // 임시 셋팅
                    mPlayer.start();
                }
            }
        }
    }

    public void quizSetting() {
        // 문제 준비
        for(int i = 0; i < MAX_QUIZ_NUM; i++) {
            answerArray.add(i, "너랑 나");
            artistArray.add(i, "아이유");
            addressArray.add(i, "url");
            correctArray.add(i, 0);
        }

        nextQuiz();
    }

    public void nextQuiz() {
        // 다음 문제 초기화
        try {
            if (mPlayer != null) {// 음악 재생 중일 경우 음악 종료
                if (mPlayer.isPlaying()) {
                    mPlayer.stop();
                    mPlayer.release();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        quizNum++; // 문제 번호 증가
        itemUsed = 0; // 아이템 사용 초기화
        btn_item.setText("ITEM");
        if(quizNum > MAX_QUIZ_NUM) {
            // 마지막 문제 완료 시 결과 화면으로 전환
            Toast.makeText(this, "게임 끝! " + score + "점 획득!", Toast.LENGTH_SHORT).show();
        } else {
            life = MAX_LIFE; // 라이프 초기화
            lifeRefresh();
            txt_msg.setText(quizNum + "번째 문제입니다!");
        }
        edit_ans.setText(""); // EditText 초기화
        btn_pass.setVisibility(View.INVISIBLE); // 패스 버튼 숨기기
        gameMode = 0; // 문제 대기 중 모드로 변경
    }

    public void musicPlay(int time) {
        // time ms만큼 음악 재생
        gameMode = 1; // 문제 내는 중 모드로 변경
        btn_play.setVisibility(View.INVISIBLE); // 임시로 보이지 않게 함 -> 나중에 프로그레시브 들어갈 부분
        btn_pass.setVisibility(View.INVISIBLE); // 패스 버튼 숨기기
        mPlayer = new MediaPlayer();
        mPlayer = MediaPlayer.create(this, R.raw.sample); // 임시 셋팅
        mPlayer.start();
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() { // time ms 후 음악 정지
            @Override
            public void run() {
                mPlayer.stop();
                mPlayer.release();
                gameMode = 2;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btn_play.setVisibility(View.VISIBLE); // 다시 나타나게 함
                        btn_pass.setVisibility(View.VISIBLE); // 패스 버튼 드러내기
                    }
                });
            }
        }, time);
    }

    public void textWatching() {
        // EditText의 값이 변하는 것을 감지하는 함수
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // 텍스트 입력 전 발생할 이벤트
                if(textChanger(edit_ans.getText().toString()) == "") {
                    // EditText가 비어있을 때 음성인식 버튼 보임
                    btn_voice.setVisibility(View.VISIBLE);
                    btn_send.setVisibility(View.INVISIBLE);
                } else {
                    // EditText에 입력되어 있을 때 보내기 버튼 보임
                    btn_send.setVisibility(View.VISIBLE);
                    btn_voice.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // 텍스트 입력 중 발생할 이벤트

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // 텍스트 입력 후 발생할 이벤트
                if(textChanger(edit_ans.getText().toString()) == "") {
                    // EditText가 비어있을 때 음성인식 버튼 보임
                    btn_voice.setVisibility(View.VISIBLE);
                    btn_send.setVisibility(View.INVISIBLE);
                } else {
                    // EditText에 입력되어 있을 때 보내기 버튼 보임
                    btn_send.setVisibility(View.VISIBLE);
                    btn_voice.setVisibility(View.INVISIBLE);
                }
            }
        };
        edit_ans.addTextChangedListener(textWatcher);
    }

    public void lifeRefresh() {
        // 남은 회수 UI 새로고침
        img_life1.setVisibility(View.INVISIBLE);
        img_life2.setVisibility(View.INVISIBLE);
        img_life3.setVisibility(View.INVISIBLE);
        img_life4.setVisibility(View.INVISIBLE);
        if (life >= 1) { img_life1.setVisibility(View.VISIBLE); }
        if (life >= 2) { img_life2.setVisibility(View.VISIBLE); }
        if (life >= 3) { img_life3.setVisibility(View.VISIBLE); }
        if (life >= 4) { img_life4.setVisibility(View.VISIBLE); }
    }

    public void downKeyboard(Context context, EditText editText) {
        //키보드 내리기
        InputMethodManager imm = (InputMethodManager) getSystemService(context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public String textChanger(String text) {
        // 텍스트 공백 및 특수문자 제거, 숫자 한글 변환
        String resultTxt = text;
        resultTxt = resultTxt.replaceAll("[^ㄱ-ㅎ가-힣0-9]", ""); // 한글이나 숫자가 아니면 전부 제거
        resultTxt = resultTxt.replaceAll("1", "일");
        resultTxt = resultTxt.replaceAll("2", "이");
        resultTxt = resultTxt.replaceAll("3", "삼");
        resultTxt = resultTxt.replaceAll("4", "사");
        resultTxt = resultTxt.replaceAll("5", "오");
        resultTxt = resultTxt.replaceAll("6", "육");
        resultTxt = resultTxt.replaceAll("7", "칠");
        resultTxt = resultTxt.replaceAll("8", "팔");
        resultTxt = resultTxt.replaceAll("9", "구");
        resultTxt = resultTxt.replaceAll("0", "영");
        return resultTxt;
    }

}
