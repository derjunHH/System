package com;

import com.DBConfig;
import java.sql.*;

import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;


public class GUIdb extends Application{
    private static String url = DBConfig.getUrl();
    private static String user = DBConfig.getUser();
    private static String password = DBConfig.getPassword();

    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;
    private String db = DBConfig.getdbName();
    private String table = DBConfig.getTableName();
    private String sql = "SELECT * FROM " + table;

    private TextArea textArea = new TextArea();
    {
        textArea.setEditable(false);
        textArea.setWrapText(true);
    }
    @Override
    public void start(Stage primaryStage){
        BorderPane bp = new BorderPane();
        ScrollPane scrollPane = new ScrollPane(textArea);
        bp.setCenter(scrollPane);

        HBox paneForButton = new HBox(20);
        Button btnQuery = new Button("查询数据");
        btnQuery.setOnAction(
            e ->{
                selectData(sql);
            } 
        );
        paneForButton.getChildren().addAll(btnQuery);
        paneForButton.setAlignment(Pos.CENTER);
        bp.setBottom(paneForButton);

        Scene scene = new Scene(bp,450,200);
        primaryStage.setTitle("数据库应用图形界面");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public void selectData(String sql)
    {
        try{
            // 先加载驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // 使用 try-with-resources 自动管理资源
            try(
                Connection conn = DriverManager.getConnection(url,user,password);
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()  // ✅ 修复：不传入 sql 参数
            ){
                StringBuilder lines = new StringBuilder();
                while(rs.next())
                {
                    lines.append(rs.getInt(1) + "\t" + rs.getString(2) + "\t" + rs.getDouble(3) + "\n");
                }
                textArea.setText(lines.toString());
            }
        }catch(ClassNotFoundException e){
            System.out.println("加载数据库驱动失败！"+e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }catch(SQLException e){
            System.out.println("查询数据失败！"+e.getMessage());
            System.out.println("错误代码: " + e.getErrorCode());
            System.out.println("SQL状态: " + e.getSQLState());
            e.printStackTrace();
            System.exit(1);
        }
    }
    private void close(Connection conn,Statement stmt,ResultSet rs)
    {
        try{
            if(rs != null)
                rs.close();
            if(stmt != null)
                stmt.close();
            if(conn != null)
                conn.close();
        }catch(SQLException e){
            System.out.println("关闭资源失败！"+e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    public static void main(String args[])
    {
        launch(args);
    }
}