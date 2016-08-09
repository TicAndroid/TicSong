package eu.jpark.ticsong;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.jpark.ticsong.core.ItemController;
import eu.jpark.ticsong.core.LoginController;
import eu.jpark.ticsong.core.MyScoreController;
import eu.jpark.ticsong.core.RegisterController;

/**
 * Created by Daesub Kim on 2016-08-09.
 */
public class ServerTestActivity extends AppCompatActivity {

    @Bind(R.id.test_btn)
    Button testBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_test);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.test_btn)
    void onTestBtnClicked() {

        // Register Test  OK.
        //registerTest("1111", "Daesub", 1);

        // Login Test  OK.
        //loginTest("1111", "Daesub", 1);

        // Insert MyScore Test  OK.
        //insertMyScoreTest("1111", 100, 1);

        // Update MyScore Test  OK.
        //updateMyScoreTest("1111", 500, 2);

        // Retrieve MyScore Test  OK.
        //retrieveMyScoreTest("1111");

        // Insert Item Test  OK.
        //insertItemTest("1111", 1, 2, 3, 4);

        // Update Item Test  OK.
        //updateItemTest("1111", 10, 20, 30, 40);

        // Retrieve Item Test  OK.
        //retrieveItemTest("1111");

    }

    private void registerTest(String userId, String name, int platform) {
        RegisterController regCon = RegisterController.getInstance();
        regCon.register(this, userId, name, ""+platform);
    }
    private void loginTest(String userId, String name, int platform) {
        LoginController loginCon = LoginController.getInstance();
        loginCon.requestLogin(this, userId, name, ""+platform);
    }
    private void insertMyScoreTest(String userId, int exp, int userLevel) {
        MyScoreController scoreCon = MyScoreController.getInstance();
        scoreCon.insertMyScore(this, userId, exp, userLevel);
    }
    private void updateMyScoreTest(String userId, int exp, int userLevel) {
        MyScoreController scoreCon = MyScoreController.getInstance();
        scoreCon.updateMyScore(this, userId, exp, userLevel);
    }
    private void retrieveMyScoreTest(String userId) {
        MyScoreController scoreCon = MyScoreController.getInstance();
        scoreCon.getMyScore(this, userId);
    }
    private void insertItemTest(String userId, int item1Cnt, int item2Cnt, int item3Cnt, int item4Cnt) {
        ItemController itemCon = ItemController.getInstance();
        itemCon.insertItem(this, userId, item1Cnt, item2Cnt, item3Cnt, item4Cnt);
    }
    private void updateItemTest(String userId, int item1Cnt, int item2Cnt, int item3Cnt, int item4Cnt) {
        ItemController itemCon = ItemController.getInstance();
        itemCon.updateItem(this, userId, item1Cnt, item2Cnt, item3Cnt, item4Cnt);
    }
    private void retrieveItemTest(String userId) {
        ItemController itemCon = ItemController.getInstance();
        itemCon.getItem(this, userId);
    }
}
