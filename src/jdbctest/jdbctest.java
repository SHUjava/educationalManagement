package jdbctest;

import java.sql.*;
import java.util.Vector;

public class jdbctest {
    Integer stu_id;
    Vector result = new Vector();
    // MySQL 8.0 以上版本 - JDBC 驱动名及数据库 URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/educationalmanagementdb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";


    // 数据库的用户名与密码，需要根据自己的设置
    static final String USER = "root";
    static final String PASS = "Zbb123150@";

    public jdbctest(int id) {
        stu_id = id;
    }

    private void request() {
        Connection conn = null;
        Statement stmt = null;
        try{
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            // 执行查询
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT course_id, score FROM score where student_id = "+ stu_id.toString();
            ResultSet rs = stmt.executeQuery(sql);

            // 展开结果集数据库
            int i = 0;
            while(rs.next()){
                // 通过字段检索
                int course_id = rs.getInt("course_id");
                int score = rs.getInt("score");
                Statement stmt2 = conn.createStatement();;
                sql = "SELECT * FROM course where course_id = "+ Integer.toString(course_id);
                ResultSet rs2 = stmt2.executeQuery(sql);
                rs2.next();
                int course_order = rs2.getInt("course_order");
                String course_name = rs2.getString("course_name");
                int teacher_id = rs2.getInt("teacher_id");
                String course_time = rs2.getString("course_time");
                int course_credit = rs2.getInt("course_credit");
                rs2.close();
                stmt2.close();

                // 输出数据
                Vector tmp = new Vector();
                tmp.addElement(course_order);
                tmp.addElement(course_name);
                tmp.addElement(teacher_id);
                tmp.addElement(course_time);
                tmp.addElement(course_credit);
                tmp.addElement(score);
                result.addElement(tmp);
            }
            // 完成后关闭
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
    }

    public Vector getResult() {
        request();
        System.out.println(result);
        return result;
    }
}