package com.ticcorp.ticsong;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.naver.speech.clientapi.SpeechConfig;
import com.ticcorp.ticsong.utils.AudioWriterPCM;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

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
    public ArrayList<Integer> itemNumArray = new ArrayList<Integer>(); // 아이템 개수 리스트

    public MediaPlayer mPlayer;
    public TextWatcher textWatcher;

    public LinearLayout gameLayout;

    //fab버튼 & 아이템
    public FloatingActionsMenu menuMultipleActions;
    public FloatingActionButton item1;
    public FloatingActionButton item2;
    public FloatingActionButton item3;
    public FloatingActionButton item4;
    public LinearLayout fabBackground;

    //음성인식
    private static final String CLIENT_ID = "6IBqNmtyh17oLX3VbNej"; // 네이버 API ID 확인
    private static final SpeechConfig SPEECH_CONFIG = SpeechConfig.OPENAPI_KR; // or SpeechConfig.OPENAPI_EN
    private RecognitionHandler handler;
    private NaverRecognizer naverRecognizer;
    private AudioWriterPCM writer;
    private boolean voiceRunning;
    private String vResult;

    @Bind(R.id.txt_msg)
    TextView txt_msg;
    @Bind(R.id.edit_ans)
    EditText edit_ans;
    @Bind(R.id.btn_play)
    ImageView btn_play;
    @Bind(R.id.btn_progress)
    CircularProgressButton btn_progress;
    @Bind(R.id.img_life1)
    ImageView img_life1;
    @Bind(R.id.img_life2)
    ImageView img_life2;
    @Bind(R.id.img_life3)
    ImageView img_life3;
    @Bind(R.id.frame_ans)
    LinearLayout frame_ans;
    @Bind(R.id.btn_pass)
    ImageButton btn_pass;
    @Bind(R.id.btn_send)
    ImageButton btn_send;
    @Bind(R.id.btn_voice)
    ImageButton btn_voice;
    @Bind(R.id.frame_voice)
    FrameLayout frame_voice;
    @Bind(R.id.txt_voice_result)
    TextView txt_voice_result;
    @Bind(R.id.txt_voice_system)
    TextView txt_voice_system;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);

        quizSetting();
        textWatching();

        /////////////
        //Fab 버튼
        FloatingActionButton actionC = new FloatingActionButton(getBaseContext());
        actionC.setTitle("Item 1");

        fabBackground = (LinearLayout) findViewById(R.id.fab_background);

        menuMultipleActions = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        fabClick();

        item1 = (FloatingActionButton) findViewById(R.id.action_a);  //아티스트 보여주기
        item2 = (FloatingActionButton) findViewById(R.id.action_b);  //3초 듣기
        item3 = (FloatingActionButton) findViewById(R.id.action_c);  //정답 1회 증가
        item4 = (FloatingActionButton) findViewById(R.id.action_d);  //제목 한 글자 보여주기

        item1.setOnClickListener(new View.OnClickListener() {
            @Override   //아티스트 공개
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "이 곡의 아티스트는 '" + artistArray.get(quizNum - 1)
                        + "'입니다.", Toast.LENGTH_SHORT).show();
                itemUsed = 1;
                edit_ans.setHint("이 곡의 아티스트는 '" + artistArray.get(quizNum - 1)
                        + "'입니다.");
                menuMultipleActions.collapse();
            }

        });

        item2.setOnClickListener(new View.OnClickListener() {
            @Override   //3초 들려주기
            public void onClick(View view) {
                musicPlay(3000);
                itemUsed = 2;
                menuMultipleActions.collapse();
            }
        });

        item3.setOnClickListener(new View.OnClickListener() {
            @Override   //생명력 증가
            public void onClick(View view) {
                if (life >= 3)
                    Toast.makeText(view.getContext(), "현재 생명력 만땅입니다!", Toast.LENGTH_SHORT).show();
                else {
                    itemUsed = 3;
                    life++;
                    lifeRefresh();
                    menuMultipleActions.collapse();
                }
            }
        });

        item4.setOnClickListener(new View.OnClickListener() {
            @Override   //제목 한글자
            public void onClick(View view) {
                itemUsed = 4;
                Toast.makeText(view.getContext(), "곡 제목의 첫 글자는 '" +
                        textChanger(answerArray.get(quizNum - 1)).charAt(0) + "'입니다.", Toast.LENGTH_SHORT).show();
                edit_ans.setHint("곡 제목의 첫 글자는 '" +
                        textChanger(answerArray.get(quizNum - 1)).charAt(0) + "'입니다.");
                menuMultipleActions.collapse();
            }
        });
        /////////////

        //음성인식
        handler = new RecognitionHandler(this);
        naverRecognizer = new NaverRecognizer(this, handler, CLIENT_ID, SPEECH_CONFIG);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        // 뒤로 가기 버튼 터치 시 지금 메인화면으로 돌아가면 경험치를 얻을 수 없다는 알림을 띄우고 다시 확인
        downKeyboard(this, edit_ans);
        exitOkClick();
    }

    void fabClick(){
        //Fab 버튼 클릭시 화면 검/투명
        menuMultipleActions.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                if(gameMode!=2){
                    item1.setVisibility(View.INVISIBLE);
                    item2.setVisibility(View.INVISIBLE);
                    item3.setVisibility(View.INVISIBLE);
                    item4.setVisibility(View.INVISIBLE);
                    item1.setEnabled(false);
                    item2.setEnabled(false);
                    item3.setEnabled(false);
                    item4.setEnabled(false);
                    menuMultipleActions.collapse();
                    Toast.makeText(getBaseContext(),"문제를 받고 눌러주세요!",Toast.LENGTH_SHORT).show();
                }else if(itemUsed!=0){
                    item1.setVisibility(View.INVISIBLE);
                    item2.setVisibility(View.INVISIBLE);
                    item3.setVisibility(View.INVISIBLE);
                    item4.setVisibility(View.INVISIBLE);
                    item1.setEnabled(false);
                    item2.setEnabled(false);
                    item3.setEnabled(false);
                    item4.setEnabled(false);
                    //menuMultipleActions.collapse();
                    Toast.makeText(getBaseContext(),"아이템이 이미 사용되었습니다!",Toast.LENGTH_SHORT).show();
                }else{
                    fabBackground.setBackgroundColor(getResources().getColor(R.color.black_semi_transparent));
                    item1.setEnabled(true);
                    item2.setEnabled(true);
                    item3.setEnabled(true);
                    item4.setEnabled(true);
                    item1.setVisibility(View.VISIBLE);
                    item2.setVisibility(View.VISIBLE);
                    item3.setVisibility(View.VISIBLE);
                    item4.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onMenuCollapsed() {
                fabBackground.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            }
        });
    }


    @OnClick(R.id.btn_exit)
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

    @OnClick(R.id.btn_pass)
    void passOkClick() {
        // 패스 버튼 확인 시 오답 처리하고 정답 공개
        gameMode = 3;
        txt_msg.setText("정답은 " + artistArray.get(quizNum - 1) + "의 " + answerArray.get(quizNum - 1) + "입니다!");
        correctArray.add((quizNum - 1), 0); // 오답 문제 기록
        musicPlay(-1);
    }

    @OnClick(R.id.btn_play)
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
                if(itemUsed==2) musicPlay(3000);
                else musicPlay(1000);
                break;
            case 3:
                // 다음 문제로
                nextQuiz();
                break;
        }

    }

    @OnClick(R.id.btn_voice)
    void voiceClick() {
        // 음성인식
        downKeyboard(this, edit_ans);
        if (gameMode == 0 | gameMode == 2) { // 맞추는 중에만 사용 가능
            downKeyboard(this, edit_ans);
            if (!voiceRunning) {
                // 음성 팝업 창을 띄움
                txt_voice_result.setText("연결 중...");
                txt_voice_system.setText("연결 중입니다. 아직 말하지 마세요.");
                frame_voice.setVisibility(View.VISIBLE); // 팝업
                voiceRunning = true;

                naverRecognizer.recognize();
            } else {
                naverRecognizer.getSpeechRecognizer().stop();
            }
        }
    }

    @OnClick(R.id.frame_voice)
    void frameVoiceClick() {
        // 음성인식 취소하고 화면으로 되돌아감
        naverRecognizer.getSpeechRecognizer().stopImmediately();
        naverRecognizer.getSpeechRecognizer().release();
        vResult = "";
        txt_voice_result.setText("");
        frame_voice.setVisibility(View.GONE); //팝업 제거
        voiceRunning = false;
    }

    @OnClick(R.id.btn_send)
    void sendClick() {
        // 정답 제출
        downKeyboard(this, edit_ans);
        if (gameMode == 0 | gameMode == 2) { // 맞추는 중에만 사용 가능
            if (textChanger(edit_ans.getText().toString()).equals(textChanger(answerArray.get(quizNum - 1)))) {
                // 정답 시
                gameMode = 3;
                int nowScore = 0;
                switch (life) { // 남은 기회에 따른 점수 계산
                    case 4: // 기회 추가 아이템 사용 시
                        nowScore = 100;
                        break;
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
                correctArray.add((quizNum - 1), life); // 남은 라이프 기록
                musicPlay(-1);
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
                    txt_msg.setText("틀렸습니다! 정답은 " + artistArray.get(quizNum - 1) + "의 " + answerArray.get(quizNum - 1) + "입니다!");
                    correctArray.add((quizNum - 1), 0); // 오답 문제 기록
                    musicPlay(-1);
                }
            }
        }
    }

    public void quizSetting() {
        // 문제 준비 및 유저 정보 받아오기
        for (int i = 0; i < MAX_QUIZ_NUM; i++) { // 문제와 답 5개씩 설정
            int soundNum = 10 + ((int) (Math.random() * 46));
            switch (soundNum) {
                case 10:
                    answerArray.add("스토커");
                    artistArray.add("10cm");
                    break;
                case 11:
                    answerArray.add("위아래");
                    artistArray.add("EXID");
                    break;
                case 12:
                    answerArray.add("니가알던내가아냐");
                    artistArray.add("AOMG");
                    break;
                case 13:
                    answerArray.add("버스안에서");
                    artistArray.add("자자");
                    break;
                case 14:
                    answerArray.add("꺼내먹어요");
                    artistArray.add("자이언티");
                    break;
                case 15:
                    answerArray.add("양화대교");
                    artistArray.add("자이언티");
                    break;
                case 16:
                    answerArray.add("쿵");
                    artistArray.add("자이언티");
                    break;
                case 17:
                    answerArray.add("핑계");
                    artistArray.add("김건모");
                    break;
                case 18:
                    answerArray.add("잘못된만남");
                    artistArray.add("김건모");
                    break;
                case 19:
                    answerArray.add("허니");
                    artistArray.add("박진영");
                    break;
                case 20:
                    answerArray.add("야생화");
                    artistArray.add("박효신");
                    break;
                case 21:
                    answerArray.add("바보");
                    artistArray.add("박효신");
                    break;
                case 22:
                    answerArray.add("쏘쏘");
                    artistArray.add("백아연");
                    break;
                case 23:
                    answerArray.add("이럴거면그러지말지");
                    artistArray.add("백아연");
                    break;
                case 24:
                    answerArray.add("우주를건너");
                    artistArray.add("백예린");
                    break;
                case 25:
                    answerArray.add("벚꽃엔딩");
                    artistArray.add("버스커버스커");
                    break;
                case 26:
                    answerArray.add("점점");
                    artistArray.add("브라운아이드소울");
                    break;
                case 27:
                    answerArray.add("한");
                    artistArray.add("샤크라");
                    break;
                case 28:
                    answerArray.add("썸");
                    artistArray.add("소유&정기고");
                    break;
                case 29:
                    answerArray.add("이밤의끝을잡고");
                    artistArray.add("솔리드");
                    break;
                case 30:
                    answerArray.add("겁");
                    artistArray.add("송민호");
                    break;
                case 31:
                    answerArray.add("챔피언");
                    artistArray.add("싸이");
                    break;
                case 32:
                    answerArray.add("금요일에만나요");
                    artistArray.add("아이유");
                    break;
                case 33:
                    answerArray.add("스물셋");
                    artistArray.add("아이유");
                    break;
                case 34:
                    answerArray.add("너의의미");
                    artistArray.add("아이유");
                    break;
                case 35:
                    answerArray.add("널사랑하지않아");
                    artistArray.add("어반자카파");
                    break;
                case 36:
                    answerArray.add("시간을달려서");
                    artistArray.add("여자친구");
                    break;
                case 37:
                    answerArray.add("오늘부터우리는");
                    artistArray.add("여자친구");
                    break;
                case 38:
                    answerArray.add("너그리고나");
                    artistArray.add("여자친구");
                    break;
                case 39:
                    answerArray.add("파도");
                    artistArray.add("UN");
                    break;
                case 40:
                    answerArray.add("와");
                    artistArray.add("이정현");
                    break;
                case 41:
                    answerArray.add("한숨");
                    artistArray.add("이하이");
                    break;
                case 42:
                    answerArray.add("여름아부탁해");
                    artistArray.add("인디고");
                    break;
                case 43:
                    answerArray.add("또다시사랑");
                    artistArray.add("임창정");
                    break;
                case 44:
                    answerArray.add("기억의습작");
                    artistArray.add("전람회");
                    break;
                case 45:
                    answerArray.add("실연");
                    artistArray.add("코요태");
                    break;
                case 46:
                    answerArray.add("파란");
                    artistArray.add("코요태");
                    break;
                case 47:
                    answerArray.add("순정");
                    artistArray.add("코요태");
                    break;
                case 48:
                    answerArray.add("잊어버리지마");
                    artistArray.add("크러쉬");
                    break;
                case 49:
                    answerArray.add("오아시스");
                    artistArray.add("크러쉬");
                    break;
                case 50:
                    answerArray.add("우아해");
                    artistArray.add("크러쉬");
                    break;
                case 51:
                    answerArray.add("꿍따리샤바라");
                    artistArray.add("클론");
                    break;
                case 52:
                    answerArray.add("초련");
                    artistArray.add("클론");
                    break;
                case 53:
                    answerArray.add("영원한사랑");
                    artistArray.add("핑클");
                    break;
                case 54:
                    answerArray.add("위잉위잉");
                    artistArray.add("혁오");
                    break;
                case 55:
                    answerArray.add("흔들린우정");
                    artistArray.add("홍경민");
                    break;
            }//http://52.78.10.41/unithon/seven/sound/21.mp3
            String soundUrl = "http://52.78.10.41/unithon/seven/sound/" + soundNum + ".mp3";
            addressArray.add(soundUrl);
        }

        setUserData();
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
        edit_ans.setHint("정답을 입력하세요!");
        quizNum++; // 문제 번호 증가
        itemUsed = 0; // 아이템 사용 초기화
        // btn_item.setText("ITEM");
        if (quizNum > MAX_QUIZ_NUM) {
            // 마지막 문제 완료 시 결과 화면으로 전환
            Toast.makeText(this, "게임 끝! " + score + "점 획득!", Toast.LENGTH_SHORT).show();
        } else {
            life = MAX_LIFE; // 라이프 초기화
            lifeRefresh();
            txt_msg.setText(quizNum + "번째 문제입니다! 문제를 들어주세요!");
        }
        edit_ans.setText(""); // EditText 초기화
        btn_pass.setVisibility(View.INVISIBLE); // 패스 버튼 숨기기
        frame_ans.setVisibility(View.INVISIBLE); // 정답창 숨기기
        gameMode = 0; // 문제 대기 중 모드로 변경
    }

    public void setUserData() {
        // 유저 정보 받아오기
    }

    public void musicPlay(int time) {
        // time ms만큼 음악 재생, time = -1일 경우 계속 재생
        if (time >= 0) {
            gameMode = 1; // 문제 내는 중 모드로 변경
            btn_progress.setVisibility(View.VISIBLE);
            btn_play.setVisibility(View.INVISIBLE); // 플레이 버튼 숨기고 프로그레스 버튼 진행
            btn_pass.setVisibility(View.INVISIBLE); // 패스 버튼 숨기기
            mPlayer = new MediaPlayer();
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mPlayer.setDataSource(addressArray.get(quizNum - 1));
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                mPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mPlayer.start();
            simulateSuccessProgress(btn_progress, time);
            Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() { // time ms 후 음악 정지
                @Override
                public void run() {
                    mPlayer.stop();
                    mPlayer.release();
                    //프로그레스 바 초기화
                    gameMode = 2;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btn_play.setVisibility(View.VISIBLE); // 프로그레스 버튼 숨기고 플레이 버튼 드러내기
                            btn_progress.setVisibility(View.INVISIBLE);
                            btn_progress.setProgress(0); // 프로그레스 초기화
                            btn_pass.setVisibility(View.VISIBLE); // 패스 버튼 드러내기
                            frame_ans.setVisibility(View.VISIBLE); // 정답창 드러내기
                            // 문제를 한 번 들어야 정답창이 드러나도록 함
                            txt_msg.setText(quizNum + "번째 문제입니다! 곡명을 한글로 맞춰주세요!");
                        }
                    });
                }
            }, time);
        } else {
            btn_pass.setVisibility(View.INVISIBLE); // 패스 버튼 숨기기
            frame_ans.setVisibility(View.INVISIBLE); // 정답창 숨기기
            mPlayer = new MediaPlayer();
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mPlayer.setDataSource(addressArray.get(quizNum - 1));
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                mPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mPlayer.start();
        }
    }

    public void textWatching() {
        // EditText의 값이 변하는 것을 감지하는 함수
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // 텍스트 입력 전 발생할 이벤트
                if (textChanger(edit_ans.getText().toString()) == "") {
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
                if (textChanger(edit_ans.getText().toString()) == "") {
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

        if (life >= 1) {
            img_life1.setVisibility(View.VISIBLE);
        }
        if (life >= 2) {
            img_life2.setVisibility(View.VISIBLE);
        }
        if (life >= 3) {
            img_life3.setVisibility(View.VISIBLE);
        }
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

    // 프로그레스 바
    private void simulateSuccessProgress(final CircularProgressButton button, int duration) {
        ValueAnimator widthAnimation = ValueAnimator.ofInt(1, 100);
        widthAnimation.setDuration(duration);
        widthAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        widthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                button.setProgress(value);
            }
        });
        widthAnimation.start();
    }

    //이하 음성인식
    @Override
    protected void onResume() {
        super.onResume();
        naverRecognizer.getSpeechRecognizer().initialize();

        vResult = "";
        txt_voice_result.setText("");
        frame_voice.setVisibility(View.GONE); //팝업 제거
    }

    @Override
    protected void onPause() {
        super.onPause();
        naverRecognizer.getSpeechRecognizer().stopImmediately();
        naverRecognizer.getSpeechRecognizer().release();
        vResult = "";
        txt_voice_result.setText("");
        frame_voice.setVisibility(View.GONE); //팝업 제거
        voiceRunning = false;
    }

    static class RecognitionHandler extends Handler {
        private final WeakReference<GameActivity> mActivity;

        RecognitionHandler(GameActivity activity) {
            mActivity = new WeakReference<GameActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            GameActivity activity = mActivity.get();
            if (activity != null) {
                activity.handleMessage(msg);
            }
        }
    }

    private void handleMessage(Message msg) {
        switch (msg.what) {
            case R.id.clientReady:
                // 지금부터 음성을 받음
                txt_voice_result.setText("");
                txt_voice_system.setText("지금 말해주세요!");
                writer = new AudioWriterPCM(
                        Environment.getExternalStorageDirectory().getAbsolutePath() + "/NaverSpeechTest");
                writer.open("Test");
                break;

            case R.id.audioRecording:
                writer.write((short[]) msg.obj);
                break;

            case R.id.partialResult:
                // Extract obj property typed with String.
                vResult = (String) (msg.obj);
                txt_voice_result.setText(vResult);
                break;

            case R.id.finalResult:
                // Extract obj property typed with String array.
                // The first element is recognition result for speech.
                String[] results = (String[]) msg.obj;
                vResult = results[0];
                txt_voice_result.setText(vResult);
                // EditText에 반영
                edit_ans.setText(vResult);
                break;

            case R.id.recognitionError:
                if (writer != null) {
                    writer.close();
                }

                vResult = "Error code : " + msg.obj.toString();
                // 녹음 창 닫히고 Toast로 띄움
                Toast.makeText(this, "NAVER 음성인식 오류\n" + vResult, Toast.LENGTH_LONG).show();
                frame_voice.setVisibility(View.GONE);
                voiceRunning = false;
                break;

            case R.id.clientInactive:
                if (writer != null) {
                    writer.close();
                }
                // 녹음 창 닫힘
                frame_voice.setVisibility(View.GONE);
                voiceRunning = false;
                break;
        }
    }
}
