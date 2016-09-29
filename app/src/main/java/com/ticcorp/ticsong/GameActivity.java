package com.ticcorp.ticsong;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.naver.speech.clientapi.SpeechConfig;
import com.ticcorp.ticsong.model.CustomPreference;
import com.ticcorp.ticsong.model.DBManager;
import com.ticcorp.ticsong.model.StaticSQLite;
import com.ticcorp.ticsong.module.SQLiteAccessModule;
import com.ticcorp.ticsong.module.ServerAccessModule;
import com.ticcorp.ticsong.utils.AudioWriterPCM;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class GameActivity extends Activity {

    ApplicationClass appClass;

    public int MAX_QUIZ_NUM = 5; // 한 게임의 문제 개수
    public int MAX_LIFE = 3; // 한 문제의 정답 기회
    public int MAX_TRACK_COUNT = 250; // 트랙 개수

    public String userId;
    public int userLevel;
    public int userExp;

    public int gameMode = 0; // 0 : 문제 대기 중, 1 : 문제 내는 중, 2 : 맞추는 중, 3 : 정답 확인 중
    public ArrayList<Integer> itemArray = new ArrayList<Integer>(); // 아이템 개수 리스트
    // 아티스트 보여주기, 3초 듣기, 정답 1회 증가, 제목 한 글자 보여주기
    public int itemUsed = 0; // 한 노래에서 아이템은 한 번 사용 가능
    // 0 : 아이템 사용하지 않음, 1 : 아티스트 보여주기, 2 : 3초 듣기, 3 : 정답 1회 증가, 4 : 제목 한 글자 보여주기
    public int quizNum = 0; // 문제 번호
    public int life = MAX_LIFE; // 현재 정답 기회
    public int score = 0; // 누적 획득 점수

    public CustomPreference pref;
    public ArrayList<String> answerArray = new ArrayList<String>(); // 정답 리스트
    public ArrayList<String> artistArray = new ArrayList<String>(); // 아티스트 리스트
    public ArrayList<String> addressArray = new ArrayList<String>(); // 트랙 주소 리스트
    public ArrayList<Integer> timeArray = new ArrayList<Integer>(); // 트랙 시작 지점(Millisec) 리스트
    public ArrayList<MediaPlayer> playerArray = new ArrayList<MediaPlayer>(); // MediaPlayer 리스트
    //public ArrayList<Integer> correctArray = new ArrayList<Integer>(); // 정답 여부 리스트, 맞출 때의 남은 기회 기록(pref로 전환)

    public MediaPlayer mPlayer;
    public TextWatcher textWatcher;

    public ScrollView scrollView;
    public ImageView recordImage;
    public Animation btn_click;
    public Animation start_click;
    public Animation tic_click;
    public Animation start_click_third;
    public Animation tic_click_third;
    public Animation start_click_infinite;
    public Animation tic_click_infinite;

    public ImageButton item1, item2, item3, item4;

    public ImageView tictac;
    public ImageView rotate;

    public int item_selected = 0; // 0 = 일반상태, 1 = 아티스트, 2 = 생명회복, 3 = 한 글자, 4 = 3초 듣기

    private static final String item1_tag = "아티스트 공개";
    private static final String item2_tag = "생명력 회복";
    private static final String item3_tag = "한 글자 공개";
    private static final String item4_tag = "3초 듣기";

    public TextView item1_cnt;
    public TextView item2_cnt;
    public TextView item3_cnt;
    public TextView item4_cnt;

    public ImageButton btn_exit;

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
    @Bind(R.id.listen)
    LinearLayout listen;
    @Bind(R.id.mic)
    ImageView mic;
    @Bind(R.id.btn_play)
    ImageView btn_play;
    @Bind(R.id.life1)
    ImageView img_life1;
    @Bind(R.id.life2)
    ImageView img_life2;
    @Bind(R.id.life3)
    ImageView img_life3;
    @Bind(R.id.frame_ans)
    LinearLayout frame_ans;
    @Bind(R.id.space)
    Space space;
    @Bind(R.id.btn_pass)
    ImageButton btn_pass;
    @Bind(R.id.btn_send)
    Button btn_send;
    @Bind(R.id.btn_voice)
    ImageButton btn_voice;
    @Bind(R.id.frame_voice)
    LinearLayout frame_voice;
    @Bind(R.id.txt_voice_system)
    TextView txt_voice_system;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);

        appClass = (ApplicationClass) getApplication();

        Animation rotate_back = AnimationUtils.loadAnimation(this, R.anim.base_rotate_anim);
        ImageView star_back = (ImageView) findViewById(R.id.background_star);

        star_back.startAnimation(rotate_back); //별 윙윙 도는거
        //버튼클릭 애니메이션
        btn_click = AnimationUtils.loadAnimation(this, R.anim.button_click_animation);

        btn_exit = (ImageButton) findViewById(R.id.btn_exit); //나가기 버튼

        item1 = (ImageButton) findViewById(R.id.item1); //아티스트 공개
        item2 = (ImageButton) findViewById(R.id.item2); //생명증가
        item3 = (ImageButton) findViewById(R.id.item3); //한글자
        item4 = (ImageButton) findViewById(R.id.item4); //3초듣기

        item1.setTag(item1_tag);
        item2.setTag(item2_tag);
        item3.setTag(item3_tag);
        item4.setTag(item4_tag);

        item1_cnt = (TextView) findViewById(R.id.item1_cnt);
        item2_cnt = (TextView) findViewById(R.id.item2_cnt);
        item3_cnt = (TextView) findViewById(R.id.item3_cnt);
        item4_cnt = (TextView) findViewById(R.id.item4_cnt);

        item1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(GameActivity.this, "가수 이름 보여주기 아이템", Toast.LENGTH_SHORT).show();
                return true;
            }
        });


        item2.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(GameActivity.this, "생명력 증가 아이템", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        item3.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(GameActivity.this, "한 글자 보여주기 아이템", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        item4.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(GameActivity.this, "3초 듣기", Toast.LENGTH_SHORT).show();
                return true;
            }
        });


        tictac = (ImageView) findViewById(R.id.tictac);
        rotate = (ImageView) findViewById(R.id.rotate);

        //시계방향 rotate animation
        start_click = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anim);
        start_click_third = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anim_thirdsec);
        start_click_infinite = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anim_infinte);
        //시계 반대방향 rotate animation
        tic_click = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_tic_anim);
        tic_click_third = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_tic_anim_thirdsec);
        tic_click_infinite = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_tic_anim_infinite);

        //음성인식
        handler = new RecognitionHandler(this);
        naverRecognizer = new NaverRecognizer(this, handler, CLIENT_ID, SPEECH_CONFIG);

        pref = pref.getInstance(this.getApplicationContext());

        Handler hd = new Handler();
        hd.postDelayed(new quizSettingHandler(), 100);
        textWatching();
    }

    //아이템 사용
    @OnClick({R.id.item1, R.id.item2, R.id.item3, R.id.item4})
    public void onButtonClick(View view){
        switch (view.getId()) {
            case R.id.item1:
                if (pref.getValue("item1Cnt", 0) > 0) {
                    pref.put("item1Cnt", pref.getValue("item1Cnt", 0) - 1);
                    ServerAccessModule.getInstance().gameFinished(userId, userExp, userLevel,
                            pref.getValue("item1Cnt", 0), pref.getValue("item2Cnt", 0),
                            pref.getValue("item3Cnt", 0), pref.getValue("item4Cnt", 0));
                    SQLiteAccessModule.getInstance(GameActivity.this.getApplicationContext()).gameFinished(userId, userExp, userLevel,
                            pref.getValue("item1Cnt", 0), pref.getValue("item2Cnt", 0),
                            pref.getValue("item3Cnt", 0), pref.getValue("item4Cnt", 0));
                    item1_cnt.setText(pref.getValue("item1Cnt", 0) + "");

                    Toast.makeText(view.getContext(), "이 곡의 아티스트는 '" + artistArray.get(quizNum - 1)
                            + "'입니다.", Toast.LENGTH_SHORT).show();
                    fxPlay(R.raw.wind_chimes);
                    itemUsed = 1;
                    edit_ans.setHint("이 곡의 아티스트는 '" + artistArray.get(quizNum - 1)
                            + "'입니다.");
                    item1.setEnabled(false);
                    item2.setEnabled(false);
                    item3.setEnabled(false);
                    item4.setEnabled(false);
                    item1.setBackgroundResource(R.drawable.item_artist);
                    item2.setBackgroundResource(R.drawable.item_onemore_no);
                    item3.setBackgroundResource(R.drawable.item_onechar_no);
                    item4.setBackgroundResource(R.drawable.item_thirdsecond_no);
                }break;
            case R.id.item2:
                if (pref.getValue("item2Cnt", 0) > 0) {
                    if (life >= 3) {
                        fxPlay(R.raw.btn_touch);
                        Toast.makeText(view.getContext(), "현재 생명력 만땅입니다!", Toast.LENGTH_SHORT).show();
                    } else {
                        pref.put("item2Cnt", pref.getValue("item2Cnt", 0) - 1);
                        ServerAccessModule.getInstance().gameFinished(userId, userExp, userLevel,
                                pref.getValue("item1Cnt", 0), pref.getValue("item2Cnt", 0),
                                pref.getValue("item3Cnt", 0), pref.getValue("item4Cnt", 0));
                        SQLiteAccessModule.getInstance(GameActivity.this.getApplicationContext()).gameFinished(userId, userExp, userLevel,
                                pref.getValue("item1Cnt", 0), pref.getValue("item2Cnt", 0),
                                pref.getValue("item3Cnt", 0), pref.getValue("item4Cnt", 0));
                        item2_cnt.setText(pref.getValue("item2Cnt", 0) + "");
                        fxPlay(R.raw.wind_chimes);
                        itemUsed = 2;
                        life++;
                        lifeRefresh();
                        item1.setEnabled(false);
                        item2.setEnabled(false);
                        item3.setEnabled(false);
                        item4.setEnabled(false);
                        item1.setBackgroundResource(R.drawable.item_artist_no);
                        item2.setBackgroundResource(R.drawable.item_onemore);
                        item3.setBackgroundResource(R.drawable.item_onechar_no);
                        item4.setBackgroundResource(R.drawable.item_thirdsecond_no);
                    }
                } else {
                    fxPlay(R.raw.btn_touch);
                    Toast.makeText(GameActivity.this, "아이템을 가지고 있지 않습니다!", Toast.LENGTH_SHORT).show();
                } break;
            case R.id.item3:
                if (pref.getValue("item3Cnt", 0) > 0) {
                    pref.put("item3Cnt", pref.getValue("item3Cnt", 0) - 1);
                    ServerAccessModule.getInstance().gameFinished(userId, userExp, userLevel,
                            pref.getValue("item1Cnt", 0), pref.getValue("item2Cnt", 0),
                            pref.getValue("item3Cnt", 0), pref.getValue("item4Cnt", 0));
                    SQLiteAccessModule.getInstance(GameActivity.this.getApplicationContext()).gameFinished(userId, userExp, userLevel,
                            pref.getValue("item1Cnt", 0), pref.getValue("item2Cnt", 0),
                            pref.getValue("item3Cnt", 0), pref.getValue("item4Cnt", 0));
                    item3_cnt.setText(pref.getValue("item3Cnt", 0) + "");
                    fxPlay(R.raw.wind_chimes);
                    itemUsed = 3;
                    Toast.makeText(view.getContext(), "곡 제목의 첫 글자는 '" +
                            textChanger(answerArray.get(quizNum - 1)).charAt(0) + "'입니다.", Toast.LENGTH_SHORT).show();
                    edit_ans.setHint("곡 제목의 첫 글자는 '" +
                            textChanger(answerArray.get(quizNum - 1)).charAt(0) + "'입니다.");

                    item1.setEnabled(false);
                    item2.setEnabled(false);
                    item3.setEnabled(false);
                    item4.setEnabled(false);
                    item1.setBackgroundResource(R.drawable.item_artist_no);
                    item2.setBackgroundResource(R.drawable.item_onemore_no);
                    item3.setBackgroundResource(R.drawable.item_onechar);
                    item4.setBackgroundResource(R.drawable.item_thirdsecond_no);

                } else {
                    fxPlay(R.raw.btn_touch);
                    Toast.makeText(GameActivity.this, "아이템을 가지고 있지 않습니다!", Toast.LENGTH_SHORT).show();
                } break;
            case R.id.item4:
                if (pref.getValue("item4Cnt", 0) > 0) {
                    pref.put("item4Cnt", pref.getValue("item4Cnt", 0) - 1);
                    ServerAccessModule.getInstance().gameFinished(userId, userExp, userLevel,
                            pref.getValue("item1Cnt", 0), pref.getValue("item2Cnt", 0),
                            pref.getValue("item3Cnt", 0), pref.getValue("item4Cnt", 0));
                    SQLiteAccessModule.getInstance(GameActivity.this.getApplicationContext()).gameFinished(userId, userExp, userLevel,
                            pref.getValue("item1Cnt", 0), pref.getValue("item2Cnt", 0),
                            pref.getValue("item3Cnt", 0), pref.getValue("item4Cnt", 0));
                    item4_cnt.setText(pref.getValue("item4Cnt", 0) + "");
                    Toast.makeText(view.getContext(), "3초 재생 적용!", Toast.LENGTH_SHORT).show();
                    rotate.startAnimation(start_click_third);
                    tictac.startAnimation(tic_click_third);
                    musicPlay(3550); // 2016/09/29/ by jeon 2-2. 3초아이템 쓰면 3550으로 재생
                    //fxPlay(R.raw.wind_chimes); 2016/09/29/ by jeon 4. 3초아이템사용시 효과음삭제함
                    //itemUsed = 4; 태호형이 지우래여
                    item1.setEnabled(false);
                    item2.setEnabled(false);
                    item3.setEnabled(false);
                    item4.setEnabled(false);
                    item1.setBackgroundResource(R.drawable.item_artist_no);
                    item2.setBackgroundResource(R.drawable.item_onemore_no);
                    item3.setBackgroundResource(R.drawable.item_onechar_no);
                    item4.setBackgroundResource(R.drawable.item_thirdsecond);

                } else {
                    fxPlay(R.raw.btn_touch);
                    Toast.makeText(GameActivity.this, "아이템을 가지고 있지 않습니다!", Toast.LENGTH_SHORT).show();
                } break;
            default:
        }
    }

    // 폰트 적용
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        // 뒤로 가기 버튼 터치 시 지금 메인화면으로 돌아가면 경험치를 얻을 수 없다는 알림을 띄우고 다시 확인
        fxPlay(R.raw.btn_touch);
        downKeyboard(this, edit_ans);
        exitOkClick();
    }

    @OnClick(R.id.btn_exit)
    void exitOkClick() {
        // 나가기 버튼 확인 시 게임 종료
        btn_exit.startAnimation(btn_click);
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("메인화면으로 나가시겠습니까?")
                .setContentText("현재까지 획득한 점수를 모두 잃습니다!")
                .setCancelText("아니요, 계속 할래요!")
                .setConfirmText("나가요..")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        fxPlay(R.raw.btn_touch);
                        sDialog.cancel();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        try {
                            /*if (mPlayer != null) {// 음악 재생 중일 경우 음악 종료
                                if (mPlayer.isPlaying()) {
                                    mPlayer.stop();
                                    mPlayer.release();
                                }
                            }*/
                            for(int i = 0; i < MAX_QUIZ_NUM; i++) {
                                if (playerArray.get(i).isPlaying()) { // 음악 재생 중일 경우 음악 종료
                                    playerArray.get(i).stop();
                                    playerArray.get(i).release();
                                }
                            }
                            playerArray.clear();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        fxPlay(R.raw.btn_touch);
                        Intent intent = new Intent(getApplication(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                })
                .show();


    }

    @OnClick(R.id.btn_pass)
    void passOkClick() {
        // 패스 버튼 확인 시 오답 처리하고 정답 공개
        btn_pass.startAnimation(btn_click);
        fxPlay(R.raw.btn_touch);
        gameMode = 3;
        txt_msg.setText("정답은 " + artistArray.get(quizNum - 1) + "의 " + answerArray.get(quizNum - 1) + "입니다!");
        //correctArray.add((quizNum - 1), 0); // 오답 문제 기록
        pref.put("correct" + quizNum, 0); // 오답 문제 기록
        musicPlay(-1);
    }

    @OnClick(R.id.btn_play)
    void playClick() {
        downKeyboard(this, edit_ans);
        switch (gameMode) {
            case 0:
                rotate.startAnimation(start_click);
                tictac.startAnimation(tic_click);
                // 문제 1초 재생
                musicPlay(1550); // 2016/09/29/ by jeon 2-1. 기본 노래재생 1550으로 수정
                item1.setBackgroundResource(R.drawable.item_artist);
                item2.setBackgroundResource(R.drawable.item_onemore);
                item3.setBackgroundResource(R.drawable.item_onechar);
                item4.setBackgroundResource(R.drawable.item_thirdsecond);
                item1.setEnabled(true);
                item2.setEnabled(true);
                item3.setEnabled(true);
                item4.setEnabled(true);
                break;
            case 1:
                // 문제 재생 중이므로 반응 없음
                break;
            case 2:
                // 문제 재생
                if (itemUsed == 2) {
                    rotate.startAnimation(start_click_third);
                    tictac.startAnimation(tic_click_third);
                    musicPlay(1550);
                } else { //2016/09/29/ by jeon 날려주세요~
                    rotate.startAnimation(start_click);
                    tictac.startAnimation(tic_click);
                    musicPlay(1550);
                }
                break;
            case 3:
                // 다음 문제로
                fxPlay(R.raw.btn_touch);
                nextQuiz();
                break;
        }

    }

    @OnClick(R.id.btn_voice)
    void voiceClick() {
        // 음성인식
        btn_voice.startAnimation(btn_click);
        downKeyboard(this, edit_ans);
        if (gameMode == 0 | gameMode == 2) { // 맞추는 중에만 사용 가능
            downKeyboard(this, edit_ans);
            if (!voiceRunning) {
                // 음성 팝업 창을 띄움
                txt_voice_system.setText("연결 중입니다. 아직 말하지 마세요.");
                frame_voice.setVisibility(View.VISIBLE); // 팝업
                voiceRunning = true;
                mic.setBackgroundResource(R.drawable.mic_listen_no);
                listen.setBackgroundResource(0);
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
        frame_voice.setVisibility(View.GONE); //팝업 제거
        voiceRunning = false;
    }

    @OnClick(R.id.btn_send)
    void sendClick() {
        // 정답 제출
        btn_send.startAnimation(btn_click);
        downKeyboard(this, edit_ans);
        if (gameMode == 0 | gameMode == 2) { // 맞추는 중에만 사용 가능
            if (textChanger(edit_ans.getText().toString()).equals(textChanger(answerArray.get(quizNum - 1)))) {
                // 정답 시
                fxPlay(R.raw.right_answer);
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
                //correctArray.add((quizNum - 1), life); // 남은 라이프 기록
                pref.put("correct" + quizNum, life); // 남은 라이프 기록
                musicPlay(-1);
            } else if (textChanger(edit_ans.getText().toString()).equals("")) {
                // 정답이 비어있을 때
                fxPlay(R.raw.btn_touch);
                Toast.makeText(this, "정답을 입력해주세요.", Toast.LENGTH_SHORT).show();
            } else {
                // 오답 시
                life--;
                lifeRefresh();
                fxPlay(R.raw.wrong_answer);
                if (life > 0) { // 아직 기회가 남았을 경우
                    txt_msg.setText("틀렸습니다! " + life + "번 남았습니다!");
                    edit_ans.setText(""); // EditText 초기화
                } else { // 기회 모두 사용 시 오답 처리 후 정답 공개
                    gameMode = 3;
                    txt_msg.setText("틀렸습니다! 정답은 " + artistArray.get(quizNum - 1) + "의 " + answerArray.get(quizNum - 1) + "입니다!");
                    //correctArray.set((quizNum - 1), 0); // 오답 문제 기록
                    pref.put("correct" + quizNum, 0); // 오답 문제 기록
                    musicPlay(-1);
                }
            }
        }
    }

    public class quizSettingHandler implements Runnable {
        public void run () {
            // 문제 준비 및 유저 정보 받아오기
            for (int i = 0; i < MAX_QUIZ_NUM; i++) { // 문제와 답 정해진 개수만큼 설정
                boolean isNumUsed = false; // 번호 중복 확인 여부

                final String CLIENT_ID = "59eb0488cc28a2c558ecbf47ed19f787";
                int soundNum = 1 + (int) (Math.random() * MAX_TRACK_COUNT);

                String[] track_data = getResources().getStringArray(getResources().getIdentifier("track" + soundNum, "array", GameActivity.this.getPackageName()));

                String track_id = track_data[0];

                String soundUrl = "https://api.soundcloud.com/tracks/" + track_id + "/stream?client_id=" + CLIENT_ID;

                for (int j = 0; j < i; j++) {
                    if (soundUrl.equals(addressArray.get(j))) {
                        // 문제 중복
                        isNumUsed = true;
                    }
                }

                if (isNumUsed) { // 문제 중복 시 다시 받아오게 함
                    Log.i("ticlog", "quizNum reset");
                    i--;
                } else {
                    addressArray.add(soundUrl);

                    answerArray.add(track_data[1]);
                    artistArray.add(track_data[2]);
                    timeArray.add(Integer.parseInt(track_data[3]));
                    playerArray.add(new MediaPlayer());
                    playerArray.get(i).setAudioStreamType(AudioManager.STREAM_MUSIC);
                    try {
                        playerArray.get(i).setDataSource(addressArray.get(i));
                        try {
                            playerArray.get(i).prepare();
                            playerArray.get(i).seekTo(timeArray.get(i));
                            //prepare 하는데 시간이 많이 걸림
                            Log.i("ticlog", "prepare success / soundNum : " + soundNum + " / Time : " + playerArray.get(i).getDuration());
                        } catch (Exception e) { // prepare가 안되면 삭제된 파일이므로 이번 Array를 삭제하고 다시 받아오게 함
                            e.printStackTrace();
                            Log.i("ticlog", "prepare catch, removed trackNum : " + soundNum);
                            answerArray.remove(i);
                            artistArray.remove(i);
                            addressArray.remove(i);
                            timeArray.remove(i);
                            playerArray.remove(i);
                            i--;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.i("ticlog", "setDataSource catch, removed trackNum : " + soundNum);
                        answerArray.remove(i);
                        artistArray.remove(i);
                        addressArray.remove(i);
                        timeArray.remove(i);
                        playerArray.remove(i);
                        i--;
                    }
                }
            }

            setUserData();
            nextQuiz();
        }
    }


    public void nextQuiz() {
        // 다음 문제 초기화
        /*try {
            if (mPlayer != null) {// 음악 재생 중일 경우 음악 종료
                if (mPlayer.isPlaying()) {
                    mPlayer.stop();
                    mPlayer.release();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        for(int i = 0; i < MAX_QUIZ_NUM; i++) {
            if (playerArray.get(i).isPlaying()) { // 음악 재생 중일 경우 음악 종료
                playerArray.get(i).stop();
                try {
                    playerArray.get(i).prepare();
                    playerArray.get(i).seekTo(timeArray.get(i));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        rotate.clearAnimation();
        tictac.clearAnimation();
        edit_ans.setHint("정답을 입력하세요!");
        quizNum++; // 문제 번호 증가
        itemUsed = 0; // 아이템 사용 초기화
        // btn_item.setText("ITEM");
        if (quizNum > MAX_QUIZ_NUM) {
            // 마지막 문제 완료 시 결과 화면으로 전환
            //Toast.makeText(this, "게임 끝! " + score + "점 획득!", Toast.LENGTH_SHORT).show();
            for(int i = 0; i < MAX_QUIZ_NUM; i++) {
                if (playerArray.get(i).isPlaying()) { // 음악 재생 중일 경우 음악 종료
                    playerArray.get(i).stop();
                    playerArray.get(i).release();
                }
            }
            playerArray.clear();
            pref.put("score", score);
            Intent intent = new Intent(this, ResultActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            life = MAX_LIFE; // 라이프 초기화
            lifeRefresh();
            txt_msg.setText(quizNum + "번째 문제입니다!\n문제를 먼저 들어주세요!");
        }
        edit_ans.setText(""); // EditText 초기화
        btn_pass.setVisibility(View.INVISIBLE); // 패스 버튼 숨기기
        frame_ans.setVisibility(View.GONE); // 정답창 숨기기
        space.setVisibility(View.VISIBLE);
        gameMode = 0; // 문제 대기 중 모드로 변경
    }

    public void setUserData() {
        // 유저 정보 받아오기
        /*DBManager db = new DBManager(this.getApplicationContext(), StaticSQLite.TICSONG_DB, null, 1);
        userId = pref.getValue("userId", "userId");
        Cursor cursor = null;
        cursor = db.retrieve(StaticSQLite.retrieveMyScoreSQL(userId));
        while (cursor.moveToNext()) {
            userExp = cursor.getInt(1);
            userLevel = cursor.getInt(2);
        }

        cursor = db.retrieve(StaticSQLite.retrieveItemSQL(userId));

        while (cursor.moveToNext()) {
            itemArray.add(0, cursor.getInt(1));
            itemArray.add(1, cursor.getInt(2));
            itemArray.add(2, cursor.getInt(3));
            itemArray.add(3, cursor.getInt(4));
        }
        cursor.close();
        db.close();

        item1_cnt.setText(itemArray.get(0) + "");
        item2_cnt.setText(itemArray.get(1) + "");
        item3_cnt.setText(itemArray.get(2) + "");
        item4_cnt.setText(itemArray.get(3) + ""); */

        pref.put("score", 0);

        item1_cnt.setText(pref.getValue("item1Cnt", 0) + " ");
        item2_cnt.setText(pref.getValue("item2Cnt", 0) + " ");
        item3_cnt.setText(pref.getValue("item3Cnt", 0) + " ");
        item4_cnt.setText(pref.getValue("item4Cnt", 0) + " ");
    }

    public void musicPlay(int time) {
        // time ms만큼 음악 재생, time = -1일 경우 계속 재생
        if (time >= 0) {
            gameMode = 1; // 문제 내는 중 모드로 변경
            btn_pass.setVisibility(View.INVISIBLE); // 패스 버튼 숨기기
            /*mPlayer = new MediaPlayer();
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
            }*/
            playerArray.get(quizNum - 1).start();

            Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() { // time ms 후 음악 정지
                @Override
                public void run() {
                    playerArray.get(quizNum - 1).stop();
                    try {
                        playerArray.get(quizNum - 1).prepare();
                        playerArray.get(quizNum - 1).seekTo(timeArray.get(quizNum - 1));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //프로그레스 바 초기화
                    gameMode = 2;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btn_pass.setVisibility(View.VISIBLE); // 패스 버튼 드러내기
                            frame_ans.setVisibility(View.VISIBLE); // 정답창 드러내기
                            space.setVisibility(View.GONE);
                            // 문제를 한 번 들어야 정답창이 드러나도록 함
                            txt_msg.setText(quizNum + "번째 문제입니다!\n곡명을 한글로 맞춰주세요!");
                        }
                    });
                }
            }, time);
        } else {
            btn_pass.setVisibility(View.INVISIBLE); // 패스 버튼 숨기기
            frame_ans.setVisibility(View.GONE); // 정답창 숨기기
            space.setVisibility(View.VISIBLE);
            item1.setBackgroundResource(R.drawable.item_artist_no);
            item2.setBackgroundResource(R.drawable.item_onemore_no);
            item3.setBackgroundResource(R.drawable.item_onechar_no);
            item4.setBackgroundResource(R.drawable.item_thirdsecond_no);
            item1.setEnabled(false);
            item2.setEnabled(false);
            item3.setEnabled(false);
            item4.setEnabled(false);
            rotate.startAnimation(start_click_infinite);
            tictac.startAnimation(tic_click_infinite);
            if (pref.getValue("setting_music", true)) {
                playerArray.get(quizNum - 1).start();
            }
        }
    }

    public void fxPlay(int target) {
        // 효과음 설정이 되어있을 경우 효과음 재생
        if (pref.getValue("setting_fx", true)) {
            MediaPlayer fxPlayer = new MediaPlayer();
            fxPlayer = MediaPlayer.create(GameActivity.this, target);
            fxPlayer.setVolume(0.7f,0.7f); // 2016/09/29/ by jeon 3. 70%로 소리 줄임 테스트 해봐야하고 안되면 숫자를 0.07f로 수정하거나 start()뒤로 보내거나 audiostream setvvolume을 사용해야함 구글링바람!
            fxPlayer.start();
            fxPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.release();
                }
            });
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
        // 추가 답 처리 부분
        resultTxt = resultTxt.replaceAll("forever", "포에버");
        // 추가 답 처리 부분 종료
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

    //이하 음성인식
    @Override
    protected void onResume() {
        super.onResume();
        naverRecognizer.getSpeechRecognizer().initialize();

        vResult = "";
        frame_voice.setVisibility(View.GONE); //팝업 제거
    }

    @Override
    protected void onPause() { // 화면이 가려졌을 때
        super.onPause();
        naverRecognizer.getSpeechRecognizer().stopImmediately();
        naverRecognizer.getSpeechRecognizer().release();
        vResult = "";
        frame_voice.setVisibility(View.GONE); //팝업 제거
        voiceRunning = false;
        //이상 음성인식 부분, 이하 음원 정지 부분
        if(!playerArray.isEmpty()) {
            for (int i = 0; i < MAX_QUIZ_NUM; i++) {
                if (playerArray.get(i).isPlaying()) { // 음악 재생 중일 경우 음악 종료
                    playerArray.get(i).stop();
                    try {
                        playerArray.get(i).prepare();
                        playerArray.get(i).seekTo(timeArray.get(i));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
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
                fxPlay(R.raw.btn_touch);
                txt_voice_system.setText("이야기 해주세요");
                listen.setBackgroundResource(R.drawable.listen_back);
                mic.setBackgroundResource(R.drawable.mic_listen_yes);
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
                txt_voice_system.setText(vResult);
                break;

            case R.id.finalResult:
                // Extract obj property typed with String array.
                // The first element is recognition result for speech.
                String[] results = (String[]) msg.obj;
                vResult = results[0];
                txt_voice_system.setText(vResult);
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
