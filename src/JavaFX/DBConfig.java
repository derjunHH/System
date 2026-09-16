package JavaFX;

import java.io.InputStream;
import java.io.FileInputStream;
import java.util.Properties;

public class DBConfig {
    private static String driver;
    private static String url;
    private static String urlBase;
    private static String urlConfig;
    private static String user;
    private static String password;
    private static String dbName;
    private static String tableName;

    static{ 
        loadConfig();
    }
    private static void loadConfig()
    {
        Properties props = new Properties();
        try(
            InputStream is = new FileInputStream("db.properties");//获取配置文件输入流
        ){
            if(is == null)
                throw new RuntimeException("找不到配置文件:db.properties");

            //加载配置文件
            props.load(is);

            driver = props.getProperty("driver","com.mysql.cj.jdbc.Driver");//获取driver属性，默认值为com.mysql.cj.jdbc.Driver
            urlBase = props.getProperty("urlBase","jdbc:mysql://localhost:3306/");//获取urlBase属性，默认值为jdbc:mysql://localhost:3306/
            user = props.getProperty("user","root");//获取user属性，默认值为root
            password = props.getProperty("password","root");//获取password属性，默认值为password
            dbName = props.getProperty("dbName","testdb");
            tableName = props.getProperty("tableName","test");
            urlConfig = props.getProperty("urlConfig","?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");
            url = urlBase+dbName+urlConfig;//构建完整的数据库连接URL
        }catch(Exception e){
            System.out.println("加载配置文件失败!"+e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("加载配置文件失败",e);/*问题一:这里传入e异常是什么含义*/ 
        }
    }

    //getters
    public static String getDriver(){
        return driver;
    }
    public static String getUrlBase(){
        return urlBase;
    }
    public static String getUser(){
        return user;
    }
    public static String getPassword(){
        return password;
    }
    public static String getdbName(){
        return dbName;
    }
    public static String getTableName(){
        return tableName;
    }
    public static String getUrlConfig(){
        return urlConfig;
    }
    public static String getUrl(){
        return url;
    }
}
