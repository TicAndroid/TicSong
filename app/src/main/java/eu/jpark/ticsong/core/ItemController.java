package eu.jpark.ticsong.core;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import eu.jpark.ticsong.DTO.ItemDTO;

import eu.jpark.ticsong.StaticInfo;
import eu.jpark.ticsong.model.CustomPreference;
import eu.jpark.ticsong.retrofit2Interface.ItemInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Daesub Kim on 2016-07-31.
 */
public class ItemController {

    private static ItemController myPageController;

    private Retrofit retrofit = null;
    /**a
     * 성공 했는지 아닌지
     */
    private boolean isSuccess=false;

    public boolean getIsSuccess(){return this.isSuccess;}

    static {
        myPageController = new ItemController();
    }

    public static ItemController getInstance() {
        if(myPageController==null)
            myPageController= new ItemController();
        return myPageController;
    }


    public boolean getItem(final AppCompatActivity activity, String userId) {
        /*
        원래코드

        아래는 바뀐코드이다.
        Retrofit 생성하는 부분을 랩해놓은 부분. 근데 없어도 될듯??조금더 생각해보자.*/
        //retrofit = RetrofitCreator.getRetrofit(LOGIN_URL);
        retrofit = new Retrofit.Builder().baseUrl(StaticInfo.TICSONG_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        /*요청보낼 interface 객체 생성.*/
        ItemInterface itemInterface = retrofit.create(ItemInterface.class);

        /*서버로 요청을 보낼 객체생성.*/
        Call<ItemDTO> call = itemInterface.getItem("get", userId);

        /*Call은 동기화 클래스이다.
        * 한번 요청을 보낸 다음, 재 요청을 보낼 경우 에러가 발생한다.
        * 그렇기 때문에 값싼 clone()메소드를 호출하여 복사하고,
        * 복사한 Call객체로 요청큐에 넣는다. */
        call.clone().enqueue(new Callback<ItemDTO>() {
            @Override
            public void onResponse(Call<ItemDTO> call, Response<ItemDTO> response) {
                /* 응답코드가 200번대가 아니라면*/
                if(!response.isSuccess()) {
                    Log.d("Get Item 코드_",response.body().getResultCode()+"");
                    return ; // 아무 코드를 실행하지 않고 리턴.
                }
                Log.d("Get Item_성공코드 -", response.code() + ""); // 디버깅용

                ItemDTO itemDTO = response.body();
                if(itemDTO.getResultCode().equals("0")) {
                    Log.e("Item Get 실패 ", response.code() + "");
                    return;
                }

                Log.e("Item Get 성공 ", itemDTO.getResultCode() + "");


                CustomPreference customPreference = CustomPreference.getInstance(activity);

                customPreference.put("userId",itemDTO.getUserId());
                customPreference.put("item1Cnt",itemDTO.getItem1Cnt());
                customPreference.put("item2Cnt",itemDTO.getItem2Cnt());
                customPreference.put("item3Cnt",itemDTO.getItem3Cnt());
                customPreference.put("item4Cnt",itemDTO.getItem4Cnt());

                /*Log.e("login User ID", customPreference.getValue("userId", itemDTO.getUserId()));
                Log.e("User Item1", customPreference.getValue("item1", ""+itemDTO.getItem1Cnt()));
                Log.e("User Item1", customPreference.getValue("item2", ""+itemDTO.getItem2Cnt()));
                Log.e("User Item1", customPreference.getValue("item3", ""+itemDTO.getItem3Cnt()));
                Log.e("User Item1", customPreference.getValue("item4", ""+itemDTO.getItem4Cnt()));
*/
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
            public void onFailure(Call<ItemDTO> call, Throwable t) {
                isSuccess = false;
                Log.d("Item Get_실패코드-",call.toString()+"__"+t.getMessage());
                Log.d("Item Get_왜실패?",t.toString());
            }
        });
        return isSuccess;
    }


    public boolean insertItem(final AppCompatActivity activity, String userId, int item1Cnt, int item2Cnt, int item3Cnt,int item4Cnt) {
        /*
        원래코드

        아래는 바뀐코드이다.
        Retrofit 생성하는 부분을 랩해놓은 부분. 근데 없어도 될듯??조금더 생각해보자.*/
        //retrofit = RetrofitCreator.getRetrofit(LOGIN_URL);
        retrofit = new Retrofit.Builder().baseUrl(StaticInfo.TICSONG_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        /*요청보낼 interface 객체 생성.*/
        ItemInterface itemInterface = retrofit.create(ItemInterface.class);

        /*서버로 요청을 보낼 객체생성.*/
        Call<ItemDTO> call = itemInterface.insertItem("insert", userId, item1Cnt,item2Cnt,item3Cnt,item4Cnt);

        /*Call은 동기화 클래스이다.
        * 한번 요청을 보낸 다음, 재 요청을 보낼 경우 에러가 발생한다.
        * 그렇기 때문에 값싼 clone()메소드를 호출하여 복사하고,
        * 복사한 Call객체로 요청큐에 넣는다. */
        call.clone().enqueue(new Callback<ItemDTO>() {
            @Override
            public void onResponse(Call<ItemDTO> call, Response<ItemDTO> response) {
                /* 응답코드가 200번대가 아니라면*/
                if(!response.isSuccess()) {
                    Log.d("Item Insert 코드_",response.body().getResultCode()+"");
                    return ; // 아무 코드를 실행하지 않고 리턴.
                }
                Log.d("Item Insert_성공코드 -", response.code() + ""); // 디버깅용

                ItemDTO itemDTO = response.body();
                if(itemDTO.getResultCode().equals("0")) {

                    // 이미 가입한 회원.
                    /*LoginController loginController = LoginController.getInstance();
                    loginController.requestLogin(activity, "123123", "Daesub");*/

                    return;
                }

                Log.e("ItemDTO Insert 성공 ", itemDTO.getResultCode() + "");


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
            public void onFailure(Call<ItemDTO> call, Throwable t) {
                isSuccess = false;
                Log.d("Item Insert_실패코드-",call.toString()+"__"+t.getMessage());
                Log.d("Item Insert_왜실패?",t.toString());
            }
        });
        return isSuccess;
    }


    public boolean updateItem(final AppCompatActivity activity, String userId, int item1Cnt, int item2Cnt, int item3Cnt,int item4Cnt) {
        /*
        원래코드

        아래는 바뀐코드이다.
        Retrofit 생성하는 부분을 랩해놓은 부분. 근데 없어도 될듯??조금더 생각해보자.*/
        //retrofit = RetrofitCreator.getRetrofit(LOGIN_URL);
        retrofit = new Retrofit.Builder().baseUrl(StaticInfo.TICSONG_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        /*요청보낼 interface 객체 생성.*/
        ItemInterface itemInterface = retrofit.create(ItemInterface.class);

        /*서버로 요청을 보낼 객체생성.*/
        Call<ItemDTO> call = itemInterface.updateItem("update", userId, item1Cnt,item2Cnt,item3Cnt,item4Cnt);

        /*Call은 동기화 클래스이다.
        * 한번 요청을 보낸 다음, 재 요청을 보낼 경우 에러가 발생한다.
        * 그렇기 때문에 값싼 clone()메소드를 호출하여 복사하고,
        * 복사한 Call객체로 요청큐에 넣는다. */
        call.clone().enqueue(new Callback<ItemDTO>() {
            @Override
            public void onResponse(Call<ItemDTO> call, Response<ItemDTO> response) {
                /* 응답코드가 200번대가 아니라면*/
                if(!response.isSuccess()) {
                    Log.d("Item Update 코드_",response.body().getResultCode()+"");
                    return ; // 아무 코드를 실행하지 않고 리턴.
                }
                Log.d("Item Update_성공코드 -", response.code() + ""); // 디버깅용

                ItemDTO itemDTO = response.body();
                if(itemDTO.getResultCode().equals("0")) {



                    return;
                }

                Log.e("ItemDTO Update 성공 ", itemDTO.getResultCode() + "");


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
            public void onFailure(Call<ItemDTO> call, Throwable t) {
                isSuccess = false;
                Log.d("Item Update_실패코드-",call.toString()+"__"+t.getMessage());
                Log.d("Item Update_왜실패?",t.toString());
            }
        });
        return isSuccess;
    }


}
