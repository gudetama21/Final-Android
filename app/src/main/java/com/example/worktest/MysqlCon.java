package com.example.worktest;

import android.util.Log;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MysqlCon {

    String mysql_ip = "192.168.1.108";
    int mysql_port = 3306; // Port 預設為 3306
    String db_name = "test?characterEncoding=utf-8";
    String url = "jdbc:mysql://"+mysql_ip+":"+mysql_port+"/"+db_name;
    String db_user = "test";
    String db_password = "0123";
    Connection con = null;

    public void run() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Log.v("DB","加載驅動成功");
        }catch( ClassNotFoundException e) {
            Log.e("DB","加載驅動失敗");
            return;
        }

        // 連接資料庫
        try {
            con = DriverManager.getConnection(url,db_user,db_password);
            Log.v("DB","遠端連接成功");
        }catch(SQLException e) {
            Log.e("DB","遠端連接失敗");
            Log.e("DB", e.toString());
        }
    }

    public String getData() {
        String data = "";
        try {
            con = DriverManager.getConnection(url, db_user, db_password);
            String sql = "SELECT * FROM test";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("id");
                String email = rs.getString("email");
                String password = rs.getString("password");
                data += id + ": " + email+ " " + password + "\n";
            }
            st.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
    public boolean insertData(String email, String password) {
        try {
            con = DriverManager.getConnection(url, db_user, db_password);
            Statement st = con.createStatement();
            String check = "SELECT `email` FROM `test` WHERE email = \'" + email + "\'";
            ResultSet rs = st.executeQuery(check);
            if(rs.next()){
                rs.close();
                st.close();
                con.close();
                return false;
            }
            String sql = "INSERT INTO `test` (`email`, `password`) VALUES ('" + email + "', '" + password + "')";
            st.executeUpdate(sql);
            rs.close();
            st.close();
            con.close();
            Log.v("DB", "寫入資料完成：" + email + password);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("DB", "寫入資料失敗");
            Log.e("DB", e.toString());
            return false;
        }
    }

    public boolean checkCusData(String email, String password) {
        try {
            con = DriverManager.getConnection(url, db_user, db_password);
            Statement st = con.createStatement();
            String check = "SELECT `email` FROM `test` WHERE email=\'" + email + "\' AND password=\'" + password + "\'";
            ResultSet rs = st.executeQuery(check);

            if(!rs.next()) {
                rs.close();
                st.close();
                con.close();
                return false;
            }
            rs.close();
            st.close();
            con.close();
            Log.v("DB", "資料確認符合");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkResData(String email, String password) {
        try {
            con = DriverManager.getConnection(url, db_user, db_password);
            Statement st = con.createStatement();
            String check = "SELECT `email` FROM `restaurant` WHERE email=\'" + email + "\' AND password=\'" + password + "\'";
            ResultSet rs = st.executeQuery(check);

            if(!rs.next()) {
                rs.close();
                st.close();
                con.close();
                return false;
            }
            rs.close();
            st.close();
            con.close();
            Log.v("DB", "資料確認符合");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertInfo(String email, String birthday, String gender, String city, String town, int b1,int b2,int b3,int b4,int b5,int b6) {
        try {
            con = DriverManager.getConnection(url, db_user, db_password);
            Statement st = con.createStatement();
            String sql = "INSERT INTO `info` (`email`, `birthday`, `gender`, `city`, `town`,`蔥`, "
            +"`蒜`, `洋蔥`, `薑`, `香菜`, `芹菜`) "
            +"VALUES ('"+ email +"', '"+ birthday +"','"+ gender+"','"+ city +"','"+ town + "',"+b1+","+b2+","+b3+","+b4+","+b5+","+b6+")";
            st.executeUpdate(sql);
            st.close();
            con.close();
            Log.v("DB", "寫入資料完成：" + email);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("DB", "寫入資料失敗");
            Log.e("DB", e.toString());
            return false;
        }
    }

    public boolean checkAccount(String email) {
        try {
            con = DriverManager.getConnection(url, db_user, db_password);
            Statement st = con.createStatement();
            String check = "SELECT `email` FROM `test` WHERE email=\'" + email + "\'";
            ResultSet rs = st.executeQuery(check);

            if(!rs.next()) {
                rs.close();
                st.close();
                con.close();
                return false;
            }
            rs.close();
            st.close();
            con.close();
            Log.v("DB", "資料確認符合");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
