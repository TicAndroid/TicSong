/*
package eu.jpark.ticsong.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import eu.jpark.ticsong.DTO.UserDTO;
        import model.DBConnection;

public class UserDAO {

    private static UserDAO userDAO;
    static {
        userDAO = new UserDAO();
    }
    public static UserDAO getInstance() {
        return userDAO;
    }
    private UserDAO(){

    }


    public int insertUser(String userId, String name) {
        PreparedStatement pstmt = null;
        Connection conn = null;
        int success = 0;

        try {
            conn = DBConnection.getInstance().getConn();
            conn.setAutoCommit(false);

            String sql = " insert into user (userid,name) values(?,?);";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userId);pstmt.setString(2, name);
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

    public UserDTO login(String userId, String name) {

        PreparedStatement pstmt = null;
        UserDTO userDTO = null;
        Connection conn = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getInstance().getConn();

            String sql = "select * from user where userid=? and name=?;";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,userId);
            pstmt.setString(2,name);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                userDTO = new UserDTO(rs.getString("userid"),rs.getString("name"));
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
        return userDTO;
    }

}*/
