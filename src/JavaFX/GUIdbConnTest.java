package JavaFX;

import JavaFX.DBConfig;
import java.sql.*;
import java.util.ArrayList;

import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

import java.util.List;
// import java.util.ArrayList;

public class GUIdbConnTest extends Application{

    private String sql = "SELECT * FROM " + DBConfig.getTableName();
    // private String tableName = DBConfig.getTableName();
    // private String dbName = DBConfig.getdbName();
    private String driver;
    private String url;
    private String user;
    private String password;
    @Override
    public void start(Stage primaryStage)
    {
        BorderPane bp = new BorderPane();

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20,40,20,40));//设置边距

        Label lblDriver = new Label("JDBC Driver");
        gridPane.add(lblDriver,0,0);
        TextField txtDriver = new TextField("com.mysql.cj.jdbc.Driver");
        txtDriver.setPromptText("请输入Driver");
        gridPane.add(txtDriver,1,0);

        Label lblDataBaseUrl = new Label("DataBase URL");
        gridPane.add(lblDataBaseUrl,0,1);
        TextField txtDataBaseUrl = new TextField("jdbc:mysql://localhost:3306/");
        txtDataBaseUrl.setPromptText("请输入数据库URL");
        gridPane.add(txtDataBaseUrl,1,1);
        
        Label lblDataBass = new Label("数据库名");
        gridPane.add(lblDataBass,0,2);
        TextField txtDataBase = new TextField();
        txtDataBase.setPromptText("请输入数据库名");
        gridPane.add(txtDataBase,1,2);

        Label lblUser = new Label("用户名");
        gridPane.add(lblUser,0,3);
        TextField txtUser = new TextField();
        txtUser.setPromptText("请输入用户名");
        gridPane.add(txtUser,1,3);

        Label lblPassword = new Label("密码");
        gridPane.add(lblPassword,0,4);
        PasswordField txtPwd = new PasswordField();
        txtPwd.setPromptText("请输入密码");
        gridPane.add(txtPwd,1,4);

        Button btnConnect = new Button("连接");
        btnConnect.setOnAction(
            e->{
                driver = txtDriver.getText();
                url = txtDataBaseUrl.getText() + txtDataBase.getText();
                user = txtUser.getText();
                password = txtPwd.getText();
                List<String> list = new ArrayList<>();
                if(!(list = link(driver,url,user,password,sql)).isEmpty())
                {
                    // System.out.println(list);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("连接成功");
                    alert.setHeaderText(null);
                    alert.setContentText("数据库连接成功！");
                    alert.showAndWait();
                    showAlert(list);
                }
                else
                {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("连接失败");
                    alert.setHeaderText(null);
                    alert.setContentText("数据库连接失败！");
                    alert.showAndWait();
                }
            }
        );
        HBox paneForButton = new HBox(10);
        paneForButton.getChildren().addAll(btnConnect);
        paneForButton.setPadding(new Insets(0,10,50,10));
        paneForButton.setAlignment(Pos.CENTER);

        bp.setCenter(gridPane);
        bp.setBottom(paneForButton);

        Scene scene = new Scene(bp,400,300);
        primaryStage.setTitle("JDBC 数据库连接");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    private List<String> link(String driver, String url, String user, String password, String sql)
    {
        List<String> result = new ArrayList<>();
        try{
            Class.forName(driver);
            try(
                Connection conn = DriverManager.getConnection(url,user,password);
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery();
            ){
                while(rs.next())
                {
                    result.add(rs.getString("id")+" "+rs.getString("name")+" "+rs.getString("score"));
                }
            }catch(SQLException e){
                System.out.println("连接数据库失败！"+e.getMessage());
                
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("连接失败");
                alert.setHeaderText(null);
                alert.setContentText(e.getMessage());
                alert.showAndWait();

                e.printStackTrace();
                return result;
            }
        }catch(ClassNotFoundException e){
            System.out.println("驱动类未找到！");
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("连接失败");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            
            e.printStackTrace();
            return result;
        }
        return result;
    }
    private void showAlert(List<String> list)
    {
        Stage alertStage = new Stage();
        alertStage.initModality(Modality.APPLICATION_MODAL);//锁定父窗口
        alertStage.setTitle("结果显示");

        //总布局
        BorderPane bp = new BorderPane();
        BorderPane.setMargin(bp,new Insets(10));

        //TextArea区域
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPadding(new Insets(2,5,2,5));
        textArea.setPrefWidth(200);
        textArea.setPrefHeight(100);
        textArea.setText(String.join("\n",list));

        //滚动布局
        ScrollPane paneForTextArea = new ScrollPane(textArea);
        paneForTextArea.setFitToWidth(true);
        paneForTextArea.setFitToHeight(true);
        paneForTextArea.setPadding(new Insets(0));
        bp.setCenter(paneForTextArea);
        
        Button btnClose = new Button("关闭");
        btnClose.setOnAction(
            e->{
                alertStage.close();
            }
        );
        HBox paneForButton = new HBox(10);
        paneForButton.getChildren().addAll(btnClose);
        paneForButton.setAlignment(Pos.CENTER);
        // paneForButton.setPadding(new Insets(10,10,10,10));
        bp.setBottom(paneForButton);

        Scene scene = new Scene(bp,400,300);
        alertStage.setScene(scene);
        alertStage.showAndWait();
    }
    public static void main(String args[])
    {
        launch(args);
    }
}
