package jdbctest;

import java.sql.*;
import java.util.Objects;
import java.util.Vector;

public class DBConnector {
    // MySQL 8.0 以上版本 - JDBC 驱动名及数据库 URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/educationalmanagementdb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    static final String USER = "root";
//    static final String PASS = "Zx010426";
    static final String PASS = "Zbb123150@";
//    static final String PASS = "yang0417";
//    static final String PASS = "1240863915gg";
    Connection conn = null;
    Statement stmt = null;

    public DBConnector(){
        try {
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // 执行查询
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
        } catch(Exception se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }// 处理 Class.forName 错误


    }

    public boolean login(String mode, int profile, String password) throws SQLException {
        if (Objects.equals(mode, "admin")){
            return profile == 7777777 && Objects.equals(password, "7777777");
        }
        String sql;
        ResultSet rs;
        sql = "select "+mode+"_password from "+mode+" where "+mode+"_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setObject(1, profile);
        rs = ps.executeQuery();
        rs.next();
        String pwd;
        if (Objects.equals(mode, "student")) {
            pwd = rs.getString("student_password");
        }
        else{
            pwd = rs.getString("teacher_password");
        }
        rs.close();
        if (Objects.equals(pwd, password)) {
            return true;
        }
        return false;
    }
    public String queryName(String mode, int ID)
    {
        String sql;
        String name=null;
        sql = "select " + mode +"_name" + " from " +mode+" where "+mode+"_id = "+ID;
        System.out.println(sql);
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                 name = rs.getString(1);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(name);
        return name;
    }

    public Object[][] search(String mode, int[] int_args, String[] str_args) throws CustomException, SQLException {
        Object[][] result = new Object[0][];
        Vector<Vector<Object>> tmp = new Vector<>();
        String sql;
        ResultSet rs;
        switch (mode) {
            case "学生成绩查询":
                if(int_args.length != 1 || str_args.length != 0){
                    throw new CustomException("输入参数个数不正确"+int_args.length+"   "+str_args.length);
                }
                sql = "select 课程编号, 课程名, 学分, 成绩, 绩点\n" +
                        "from used_score\n" +
                        "where 学号='"+int_args[0]+"';\n";
                rs = stmt.executeQuery(sql);
                int id = 0;
                while(rs.next())
                {
                    id++;
                    int course_order = rs.getInt("课程编号");
                    String course_name = rs.getString("课程名");
                    int credit = rs.getInt("学分");
                    int score = (int)Double.parseDouble(rs.getString("成绩"));
                    double gpa = rs.getDouble("绩点");
                    Vector<Object> row = new Vector<>();
                    row.addElement(id);
                    row.addElement(course_order);
                    row.addElement(course_name);
                    row.addElement(credit);
                    row.addElement(score);
                    row.addElement(gpa);
                    tmp.addElement(row);
                }
                rs.close();
                System.out.println(tmp);
                break;
            case "课程成绩查询":
                if(int_args.length != 2 || str_args.length != 0){
                    throw new CustomException("输入参数个数不正确"+int_args.length+str_args.length+"123456");
                }
                sql = "select used_score.课程编号, used_score.课程名, used_score.学号, student.student_name, used_score.平时成绩, used_score.考试成绩, used_score.绩点\n" +
                        "from used_score, student\n" +
                        "where used_score.工号 = '2001001' and used_score.课程编号 ='3001001' and used_score.学号=student.student_id\n" +
                        "order by used_score.成绩;";
                rs = stmt.executeQuery(sql);
                while(rs.next())
                {
                    int course_order = rs.getInt("课程编号");
                    String course_name = rs.getString("课程名");
                    int student_id = rs.getInt("学号");
                    String student_name = rs.getString("student_name");
                    int daily_score = rs.getInt("平时成绩");
                    int exam_score = rs.getInt("考试成绩");
                    double gpa = rs.getDouble("绩点");
                    Vector<Object> row = new Vector<>();
                    row.addElement(course_order);
                    row.addElement(course_name);
                    row.addElement(student_id);
                    row.addElement(student_name);
                    row.addElement(daily_score);
                    row.addElement(exam_score);
                    row.addElement(gpa);
                    tmp.addElement(row);
                }
                rs.close();
                break;
            case "教师成绩查询":
                if(int_args.length != 1 || str_args.length != 3){//工号，课程号  课程名称，课程学期，上课时间
                    throw new CustomException("输入参数个数不正确"+int_args.length+"   "+str_args.length);
                }
                sql = "select used_score.学号, student.student_name, used_score.平时成绩, used_score.考试成绩, used_score.成绩, used_score.绩点\n" +
                        "from used_score, student\n" +
                        "where used_score.工号 = '" + int_args[0] +
                        "' and used_score.课程编号 ='"+ int_args[1] +
                        "'\n" +
                        "order by used_score.学号;";
                rs = stmt.executeQuery(sql);
                id = 0;
                while(rs.next())
                {
                    id++;
                    int student_id = rs.getInt("学号");
                    String student_name = rs.getString("student_name");
                    int daily_score = rs.getInt("平时成绩");
                    int exam_score = rs.getInt("考试成绩");
                    int score = rs.getInt("成绩");
                    double gpa = rs.getDouble("绩点");
                    Vector<Object> row = new Vector<>();
                    row.addElement(student_id);
                    row.addElement(student_name);
                    row.addElement(daily_score);
                    row.addElement(exam_score);
                    row.addElement(score);
                    row.addElement(gpa);
                    tmp.addElement(row);
                }
                rs.close();
                System.out.println(tmp);
                break;

        }
        int row = tmp.size();
        int col = tmp.get(0).size();
        result = new Object[row][col];
        //读取数据库

        for(int i=0;i<row;i++)
        {
            for(int j=0;j<col;j++)
            {
                result[i][j] = ((Vector<?>)tmp.get(i)).get(j);
            }
        }
        return result;
    }

}



