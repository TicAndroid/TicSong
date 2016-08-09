/*
package eu.jpark.ticsong.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import eu.jpark.ticsong.DTO.ScoreDTO;
import model.DBConnection;

public class ScoreDAO {

    private static ScoreDAO scoreDAO;
    static {
        scoreDAO = new ScoreDAO();
    }
    public static ScoreDAO getInstance() {
        return scoreDAO;
    }
    private ScoreDAO(){

    }

    public int insertScore(ScoreDTO score) {
        PreparedStatement pstmt = null;
        Connection conn = null;
        int success = 0;

        try {
            conn = DBConnection.getInstance().getConn();
            conn.setAutoCommit(false);

            String sql = " insert into score (userid,score,userlevel) values(?,?,?);";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, score.getUserId());pstmt.setInt(2, score.getScore());
            pstmt.setInt(3, score.getUserLevel());
            success = pstmt.executeUpdate();
            conn.commit();
        } catch (SQLException e ) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    System.err.print("Transaction is being Rolled back");
                    conn.rollback();
                } catch(SQLException se) {
                    se.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    System.err.print("Transaction is being Rolled back");
                    conn.rollback();
                } catch(SQLException se) {
                    se.printStackTrace();
                }
            }
        } finally {
            if (pstmt != null) try { pstmt.close(); } catch(SQLException ex) {}
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch(SQLException ex) {}
            }
        }
        return success;
    }

    public int updateScore(ScoreDTO score) {

        PreparedStatement pstmt = null;
        Connection conn = null;
        ResultSet rs = null;
        int success = 0;

        try {
            conn = DBConnection.getInstance().getConn();

            String sql = "select * from user where userid=?;";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, score.getUserId());
            rs = pstmt.executeQuery();

            if(rs.next()){
                conn.setAutoCommit(false);
                String updateSql = "update score set score=?, userlevel=? where userid=?;";
                pstmt = conn.prepareStatement(updateSql);
                pstmt.setInt(1, score.getScore());
                pstmt.setInt(2, score.getUserLevel());
                pstmt.setString(3, score.getUserId());
                pstmt.executeUpdate();

                success = 1;
            }else
                success = 0;

            conn.commit();
        } catch (SQLException e ) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    System.err.print("Transaction is being Rolled back");
                    conn.rollback();
                } catch(SQLException se) {
                    se.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    System.err.print("Transaction is being Rolled back");
                    conn.rollback();
                } catch(SQLException se) {
                    se.printStackTrace();
                }
            }
        } finally {
            if (pstmt != null) try { pstmt.close(); } catch(SQLException ex) {}
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch(SQLException ex) {}
            }
        }
        return success;
    }

    public ScoreDTO getScore(String userId) {

        PreparedStatement pstmt = null;
        ScoreDTO scoreDTO = null;
        Connection conn = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getInstance().getConn();

            String sql = "select * from score where userid=?;";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,userId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                scoreDTO = new ScoreDTO(userId, rs.getInt("score"),rs.getInt("userlevel"));
            }
        } catch ( Exception ex ) {
            ex.printStackTrace();
        } finally {
            try {
                pstmt.close();
                rs.close();
                conn.close();
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }
        return scoreDTO;
    }
*/
