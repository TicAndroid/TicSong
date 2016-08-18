package com.ticcorp.ticsong.model;

/**
 * Created by Daesub Kim on 2016-08-15.
 */
public class StaticSQLite {

    public static String retrofittest_DB = "retrofittest.DB";
    public static String TICSONG_DB = "TICSONG.DB";


    public static String insertUserSQL(String userId, String name, int platform) {
        return "insert into USER values('" + userId + "', '" + name + "', " + platform + ");";
    }
    public static String dropUserSQL() {
        return "drop table USER;";
    }
    public static String retrieveUserSQL(String userId) {
        return "select * from USER where userid = '" + userId + "';";
    }

    public static String insertMyScoreSQL(String userId, int exp, int userLevel) {
        return "insert into MYSCORE values('" + userId + "', " + exp + ", " + userLevel + ");";
    }
    public static String updateMyScoreSQL(String userId, int exp, int userLevel) {
        return "update MYSCORE set exp = " + exp + ", userLevel = " + userLevel + " where userid = '" + userId + "';";
    }
    public static String dropMyScoreSQL() {
        return "drop table MYSCORE;";
    }
    public static String deleteMyScoreSQL() {
        return "delete from MYSCORE;";
    }
    public static String retrieveMyScoreSQL(String userId) {
        return "select * from MYSCORE where userid = '" + userId + "';";
    }
    public static String retrieveMyScoreListSQL() {
        return "select * from MYSCORE;";
    }


    public static String insertItemSQL(String userId, int item1Cnt, int item2Cnt, int item3Cnt, int item4Cnt) {
        return "insert into ITEM values('" + userId + "', " + item1Cnt + ", " + item2Cnt + ", " + item3Cnt + ", " + item4Cnt + ");";
    }
    public static String updateItemSQL(String userId, int item1Cnt, int item2Cnt, int item3Cnt, int item4Cnt) {
        return "update ITEM set item1Cnt = " + item1Cnt + ", item2Cnt = " + item2Cnt + ", item3Cnt = " + item3Cnt + ", item4Cnt = " + item4Cnt
                + " where userid = '" + userId + "';";
    }
    public static String dropItemSQL() {
        return "drop table ITEM;";
    }
    public static String deleteItemSQL() {
        return "delete from ITEM;";
    }
    public static String retrieveItemSQL(String userId) {
        return "select * from ITEM where userid = '" + userId + "';";
    }



}
