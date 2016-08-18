package com.ticcorp.ticsong.core;

import android.app.Activity;
import android.util.Log;

import com.ticcorp.ticsong.DTO.FriendsScoreView;
import com.ticcorp.ticsong.DTO.MyScoreDTO;

import com.ticcorp.ticsong.DTO.ScoreView;
import com.ticcorp.ticsong.StaticInfo;
import com.ticcorp.ticsong.model.CustomPreference;
import com.ticcorp.ticsong.module.ServerAccessModule;
import com.ticcorp.ticsong.retrofit2Interface.MyScoreInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Daesub Kim on 2016-07-31.
 */
public class MyScoreController {

    private static MyScoreController myScoreController;

    private Retrofit retrofit = null;
    /**a
     * 성공 했는지 아닌지
     */
    private boolean isSuccess=false;

    public boolean getIsSuccess(){return this.isSuccess;}

    static {
        myScoreController = new MyScoreController();
    }

    public static MyScoreController getInstance() {
        if(myScoreController==null)
            myScoreController= new MyScoreController();
        return myScoreController;
    }


    public boolean getMyScore(final Activity activity, String userId) {
        /*
        원래코드

        아래는 바뀐코드이다.
        Retrofit 생성하는 부분을 랩해놓은 부분. 근데 없어도 될듯??조금더 생각해보자.*/
        //retrofit = RetrofitCreator.getRetrofit(LOGIN_URL);
        retrofit = new Retrofit.Builder().baseUrl(StaticInfo.TICSONG_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        /*요청보낼 interface 객체 생성.*/
        MyScoreInterface myScoreInterface = retrofit.create(MyScoreInterface.class);

        /*서버로 요청을 보낼 객체생성.*/
        //Call<MyScoreDTO> call = myScoreInterface.getMyScore("get", userId);
        Call<MyScoreDTO> call = myScoreInterface.retrieveMyScore("get", userId);

        /*Call은 동기화 클래스이다.
        * 한번 요청을 보낸 다음, 재 요청을 보낼 경우 에러가 발생한다.
        * 그렇기 때문에 값싼 clone()메소드를 호출하여 복사하고,
        * 복사한 Call객체로 요청큐에 넣는다. */
        call.clone().enqueue(new Callback<MyScoreDTO>() {
            @Override
            public void onResponse(Call<MyScoreDTO> call, Response<MyScoreDTO> response) {
                /* 응답코드가 200번대가 아니라면*/
                if(!response.isSuccess()) {
                    Log.d("로그인 코드_",response.body().getResultCode()+"");
                    return ; // 아무 코드를 실행하지 않고 리턴.
                }
                Log.d("로그인_성공코드 -", response.code() + ""); // 디버깅용

                MyScoreDTO myScoreDTO = response.body();
                if(myScoreDTO.getResultCode().equals("0")) {
                    Log.e("Score Get 실패 ", response.code() + "");
                    return;
                }

                Log.e("MyScore Get 성공 ", myScoreDTO.getResultCode() + "");

                CustomPreference customPreference = CustomPreference.getInstance(activity);

                customPreference.put("userId",myScoreDTO.getUserId());
                customPreference.put("exp",myScoreDTO.getExp());
                customPreference.put("userLevel",myScoreDTO.getUserLevel());

                Log.e("login User ID", customPreference.getValue("userId", myScoreDTO.getUserId()));
                Log.e("User exp", customPreference.getValue("Exp", ""+myScoreDTO.getExp()));
                Log.e("User userLevel", customPreference.getValue("UserLevel", ""+myScoreDTO.getUserLevel()));

                /*CustomPreference customPreference = CustomPreference.getInstance(activity);

                customPreference.put("userId",userDTO.getUserId());
                customPreference.put("name",userDTO.getName());
                customPreference.put("login",true);
                Log.e("login_login","true");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                activity.startActivity(new Intent(activity, MainActivity.class));
                            }
                        });
                    }
                }).start();*/
            }

            @Override
            public void onFailure(Call<MyScoreDTO> call, Throwable t) {
                isSuccess = false;
                Log.d("로그인_실패코드-",call.toString()+"__"+t.getMessage());
                Log.d("로그인_왜실패?",t.toString());
            }
        });
        return isSuccess;
    }


    public boolean insertMyScore(final Activity activity, String userId, int exp, int userLevel) {
        /*
        원래코드

        아래는 바뀐코드이다.
        Retrofit 생성하는 부분을 랩해놓은 부분. 근데 없어도 될듯??조금더 생각해보자.*/
        //retrofit = RetrofitCreator.getRetrofit(LOGIN_URL);
        retrofit = new Retrofit.Builder().baseUrl(StaticInfo.TICSONG_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        /*요청보낼 interface 객체 생성.*/
        MyScoreInterface myScoreInterface = retrofit.create(MyScoreInterface.class);

        /*서버로 요청을 보낼 객체생성.*/
        Call<MyScoreDTO> call = myScoreInterface.insertMyScore("insert", userId, exp, userLevel);

        /*Call은 동기화 클래스이다.
        * 한번 요청을 보낸 다음, 재 요청을 보낼 경우 에러가 발생한다.
        * 그렇기 때문에 값싼 clone()메소드를 호출하여 복사하고,
        * 복사한 Call객체로 요청큐에 넣는다. */
        call.clone().enqueue(new Callback<MyScoreDTO>() {
            @Override
            public void onResponse(Call<MyScoreDTO> call, Response<MyScoreDTO> response) {
                /* 응답코드가 200번대가 아니라면*/
                if(!response.isSuccess()) {
                    Log.d("MyScore Insert 코드_",response.body().getResultCode()+"");
                    return ; // 아무 코드를 실행하지 않고 리턴.
                }
                Log.d("MyScore Insert 성공 -", response.code() + ""); // 디버깅용

                MyScoreDTO myScoreDTO = response.body();
                if(myScoreDTO.getResultCode().equals("0")) {

                    /* 존재하지 않는 아이디 */

                    return;
                }

                Log.e("MyScore Insert 성공 ", myScoreDTO.getResultCode() + "");


                /*CustomPreference customPreference = CustomPreference.getInstance(activity);

                customPreference.put("userId",scoreDTO.getUserId());
                customPreference.put("score",scoreDTO.());
                customPreference.put("Register",true);
                Log.e("Register","true");*/
                /*new Thread(new Runnable() {
                    @Override
                    public void run() {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                activity.startActivity(new Intent(activity, MainActivity.class));
                            }
                        });
                    }
                }).start();*/
            }

            @Override
            public void onFailure(Call<MyScoreDTO> call, Throwable t) {
                isSuccess = false;
                Log.d("MyScore Insert_실패코드-",call.toString()+"__"+t.getMessage());
                Log.d("MyScore Insert_왜실패?",t.toString());
            }
        });
        return isSuccess;
    }


    public boolean updateMyScore(String userId, int score, int userLevel) {

        retrofit = new Retrofit.Builder().baseUrl(StaticInfo.TICSONG_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        /*요청보낼 interface 객체 생성.*/
        MyScoreInterface myScoreInterface = retrofit.create(MyScoreInterface.class);
        /*서버로 요청을 보낼 객체생성.*/
        Call<MyScoreDTO> call = myScoreInterface.updateMyScore("update", userId, score, userLevel);
        call.clone().enqueue(new Callback<MyScoreDTO>() {
            @Override
            public void onResponse(Call<MyScoreDTO> call, Response<MyScoreDTO> response) {
                /* 응답코드가 200번대가 아니라면*/
                if(!response.isSuccess()) {
                    Log.d("MyScore Update 코드_",response.body().getResultCode()+"");
                    return ; // 아무 코드를 실행하지 않고 리턴.
                }
                Log.d("MyScore Update_성공코드 -", response.code() + ""); // 디버깅용

                MyScoreDTO myScoreDTO = response.body();
                if(myScoreDTO.getResultCode().equals("0")) {

                    return;
                }
                Log.e("MyScore Update 성공 ", myScoreDTO.getResultCode() + "");
            }

            @Override
            public void onFailure(Call<MyScoreDTO> call, Throwable t) {
                isSuccess = false;
                Log.d("MyScore Update_실패코드-",call.toString()+"__"+t.getMessage());
                Log.d("MyScore Update_왜실패?",t.toString());
            }
        });
        return isSuccess;
    }



    public boolean updateMyScore(final Activity activity, String userId, int score, int userLevel) {
        /*
        원래코드

        아래는 바뀐코드이다.
        Retrofit 생성하는 부분을 랩해놓은 부분. 근데 없어도 될듯??조금더 생각해보자.*/
        //retrofit = RetrofitCreator.getRetrofit(LOGIN_URL);
        retrofit = new Retrofit.Builder().baseUrl(StaticInfo.TICSONG_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        /*요청보낼 interface 객체 생성.*/
        MyScoreInterface myScoreInterface = retrofit.create(MyScoreInterface.class);

        /*서버로 요청을 보낼 객체생성.*/
        Call<MyScoreDTO> call = myScoreInterface.updateMyScore("update", userId, score, userLevel);

        /*Call은 동기화 클래스이다.
        * 한번 요청을 보낸 다음, 재 요청을 보낼 경우 에러가 발생한다.
        * 그렇기 때문에 값싼 clone()메소드를 호출하여 복사하고,
        * 복사한 Call객체로 요청큐에 넣는다. */
        call.clone().enqueue(new Callback<MyScoreDTO>() {
            @Override
            public void onResponse(Call<MyScoreDTO> call, Response<MyScoreDTO> response) {
                /* 응답코드가 200번대가 아니라면*/
                if(!response.isSuccess()) {
                    Log.d("MyScore Update 코드_",response.body().getResultCode()+"");
                    return ; // 아무 코드를 실행하지 않고 리턴.
                }
                Log.d("MyScore Update_성공코드 -", response.code() + ""); // 디버깅용

                MyScoreDTO myScoreDTO = response.body();
                if(myScoreDTO.getResultCode().equals("0")) {

                    return;
                }

                Log.e("MyScore Update 성공 ", myScoreDTO.getResultCode() + "");

                /*CustomPreference customPreference = CustomPreference.getInstance(activity);
                customPreference.put("userId",scoreDTO.getUserId());
                customPreference.put("score",scoreDTO.());
                customPreference.put("Register",true);
                Log.e("Register","true");*/
                /*new Thread(new Runnable() {
                    @Override
                    public void run() {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                activity.startActivity(new Intent(activity, MainActivity.class));
                            }
                        });
                    }
                }).start();*/

            }

            @Override
            public void onFailure(Call<MyScoreDTO> call, Throwable t) {
                isSuccess = false;
                Log.d("MyScore Update_실패코드-",call.toString()+"__"+t.getMessage());
                Log.d("MyScore Update_왜실패?",t.toString());
            }
        });
        return isSuccess;
    }

    public boolean getScores(String userId) {
        retrofit = new Retrofit.Builder().baseUrl(StaticInfo.TICSONG_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        /*요청보낼 interface 객체 생성.*/
        MyScoreInterface myScoreInterface = retrofit.create(MyScoreInterface.class);
        /*서버로 요청을 보낼 객체생성.*/
        Call<List<ScoreView>> call = myScoreInterface.retrieveScores("list", userId);
        call.clone().enqueue(new Callback<List<ScoreView>>() {
            @Override
            public void onResponse(Call<List<ScoreView>> call, Response<List<ScoreView>> response) {
                /* 응답코드가 200번대가 아니라면*/
                if(!response.isSuccess()) {
                    Log.d("로그인 코드_","실패");
                    return ; // 아무 코드를 실행하지 않고 리턴.
                }
                Log.d("로그인_성공코드 -", response.code() + ""); // 디버깅용

                List<ScoreView> scoreList = response.body();
                ServerAccessModule.TOP_RANKER_LIST = null;
                if(scoreList != null) {
                    ServerAccessModule.TOP_RANKER_LIST = scoreList;

                    Log.e("Name",scoreList.toString());
                    for(ScoreView score : scoreList) {
                        if(score!=null) {
                            Log.e("Name", score.getName());
                            Log.e("EXP", ""+score.getExp());
                            Log.e("UserLevel", ""+score.getUserLevel());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ScoreView>> call, Throwable t) {
                isSuccess = false;
                Log.d("로그인_실패코드-",call.toString()+"__"+t.getMessage());
                Log.d("로그인_왜실패?",t.toString());
            }
        });
        return isSuccess;
    }


    public boolean getScores(final Activity activity, String userId) {
        retrofit = new Retrofit.Builder().baseUrl(StaticInfo.TICSONG_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        /*요청보낼 interface 객체 생성.*/
        MyScoreInterface myScoreInterface = retrofit.create(MyScoreInterface.class);

        /*서버로 요청을 보낼 객체생성.*/
        Call<List<ScoreView>> call = myScoreInterface.retrieveScores("list", userId);

        /*Call은 동기화 클래스이다.
        * 한번 요청을 보낸 다음, 재 요청을 보낼 경우 에러가 발생한다.
        * 그렇기 때문에 값싼 clone()메소드를 호출하여 복사하고,
        * 복사한 Call객체로 요청큐에 넣는다. */
        call.clone().enqueue(new Callback<List<ScoreView>>() {
            @Override
            public void onResponse(Call<List<ScoreView>> call, Response<List<ScoreView>> response) {
                /* 응답코드가 200번대가 아니라면*/
                if(!response.isSuccess()) {
                    Log.d("로그인 코드_","실패");
                    return ; // 아무 코드를 실행하지 않고 리턴.
                }
                Log.d("로그인_성공코드 -", response.code() + ""); // 디버깅용

                List<ScoreView> scoreList = response.body();
                ServerAccessModule.getInstance().TOP_RANKER_LIST = null;
                if(scoreList != null) {
                    ServerAccessModule.getInstance().TOP_RANKER_LIST = scoreList;


                    Log.e("Name",scoreList.toString());
                    for(ScoreView score : scoreList) {
                        if(score!=null) {
                            Log.e("Name", score.getName());
                            Log.e("EXP", ""+score.getExp());
                            Log.e("UserLevel", ""+score.getUserLevel());
                        }
                    }
                }

               /* CustomPreference customPreference = CustomPreference.getInstance(activity);

                customPreference.put("userId",myScoreDTO.getUserId());
                customPreference.put("exp",myScoreDTO.getExp());
                customPreference.put("userLevel",myScoreDTO.getUserLevel());*/

            }

            @Override
            public void onFailure(Call<List<ScoreView>> call, Throwable t) {
                isSuccess = false;
                Log.d("로그인_실패코드-",call.toString()+"__"+t.getMessage());
                Log.d("로그인_왜실패?",t.toString());
            }
        });
        return isSuccess;
    }

    public boolean getFriendsScore(String userId, List<String> friendIdList) {

        retrofit = new Retrofit.Builder().baseUrl(StaticInfo.TICSONG_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        /*요청보낼 interface 객체 생성.*/
        MyScoreInterface myScoreInterface = retrofit.create(MyScoreInterface.class);

        Call<List<FriendsScoreView>> call = null;
        JSONArray friends = convertFriendListToJSONArray(friendIdList);
        if(friends != null) {
            /*서버로 요청을 보낼 객체생성.*/
            call = myScoreInterface.retrieveFriendsScores("friends", userId, friends.toString());
        }
        call.clone().enqueue(new Callback<List<FriendsScoreView>>() {
            @Override
            public void onResponse(Call<List<FriendsScoreView>> call, Response<List<FriendsScoreView>> response) {
                /* 응답코드가 200번대가 아니라면*/
                if(!response.isSuccess()) {
                    Log.d("로그인 코드_","실패");
                    return ; // 아무 코드를 실행하지 않고 리턴.
                }
                Log.d("로그인_성공코드 -", response.code() + ""); // 디버깅용

                List<FriendsScoreView> scoreList = response.body();
                ServerAccessModule.FRIEND_LIST = null;
                if(scoreList != null) {
                    ServerAccessModule.FRIEND_LIST = scoreList;

                    Collections.sort(ServerAccessModule.FRIEND_LIST, new Comparator<FriendsScoreView>() {
                        public int compare(FriendsScoreView o1, FriendsScoreView o2) {
                            return o1.getExp() > o2.getExp() ? -1 : o1.getExp() == o2.getExp() ? 0 : 1;
                        }
                    });

                    Log.e("Name",ServerAccessModule.FRIEND_LIST.toString());
                    for(FriendsScoreView score : ServerAccessModule.FRIEND_LIST) {
                        if(score!=null) {
                            Log.e("User ID", score.getUserId());
                            Log.e("Name", score.getName());
                            Log.e("EXP", ""+score.getExp());
                            Log.e("UserLevel", ""+score.getUserLevel());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<FriendsScoreView>> call, Throwable t) {
                isSuccess = false;
                Log.d("로그인_실패코드-",call.toString()+"__"+t.getMessage());
                Log.d("로그인_왜실패?",t.toString());
            }
        });
        return isSuccess;
    }

/*
    public boolean getFriendsScore(final Activity activity, String userId) {

        retrofit = new Retrofit.Builder().baseUrl(StaticInfo.TICSONG_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        *//*요청보낼 interface 객체 생성.*//*
        MyScoreInterface myScoreInterface = retrofit.create(MyScoreInterface.class);

        Call<List<FriendsScoreView>> call = null;
        if(genFriendList() != null) {
            *//*서버로 요청을 보낼 객체생성.*//*
            call = myScoreInterface.retrieveFriendsScores("friends", userId, genFriendList().toString());
            //call = myScoreInterface.retrieveFriendsScore("friends", userId, genFriendList());

        }

        *//*Call은 동기화 클래스이다.
        * 한번 요청을 보낸 다음, 재 요청을 보낼 경우 에러가 발생한다.
        * 그렇기 때문에 값싼 clone()메소드를 호출하여 복사하고,
        * 복사한 Call객체로 요청큐에 넣는다. *//*
        call.clone().enqueue(new Callback<List<FriendsScoreView>>() {
            @Override
            public void onResponse(Call<List<FriendsScoreView>> call, Response<List<FriendsScoreView>> response) {
                *//* 응답코드가 200번대가 아니라면*//*
                if(!response.isSuccess()) {
                    Log.d("로그인 코드_","실패");
                    return ; // 아무 코드를 실행하지 않고 리턴.
                }
                Log.d("로그인_성공코드 -", response.code() + ""); // 디버깅용

                List<FriendsScoreView> scoreList = response.body();
                if(scoreList != null) {


                    Log.e("Name",scoreList.toString());
                    for(FriendsScoreView score : scoreList) {
                        if(score!=null) {
                            Log.e("User ID", score.getUserId());
                            Log.e("Name", score.getName());
                            Log.e("EXP", ""+score.getExp());
                            Log.e("UserLevel", ""+score.getUserLevel());
                        }
                    }


                }

               *//* CustomPreference customPreference = CustomPreference.getInstance(activity);

                customPreference.put("userId",myScoreDTO.getUserId());
                customPreference.put("exp",myScoreDTO.getExp());
                customPreference.put("userLevel",myScoreDTO.getUserLevel());*//*

            }

            @Override
            public void onFailure(Call<List<FriendsScoreView>> call, Throwable t) {
                isSuccess = false;
                Log.d("로그인_실패코드-",call.toString()+"__"+t.getMessage());
                Log.d("로그인_왜실패?",t.toString());
            }
        });
        return isSuccess;
    }*/

    /**
     * 친구ID List를 JSONArray로 Convert
     * @param friendIdList
     * @return
     */
    private JSONArray convertFriendListToJSONArray(List<String> friendIdList) {
        JSONArray friends = null;
        try {
            friends = new JSONArray();
            for(String fId : friendIdList) {
                friends.put(new JSONObject().put("friendId", fId));
            }
        } catch (JSONException je) {
            je.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return friends;
    }
}
