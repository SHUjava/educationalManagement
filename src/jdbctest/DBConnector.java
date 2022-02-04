package jdbctest;

import java.sql.*;
import java.util.Objects;
import java.util.Vector;

public class DBConnector {
    // MySQL 8.0 以上版本 - JDBC 驱动名及数据库 URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/educationalmanagementdb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    static final String USER = "root";
    //static final String PASS = "Zx010426";
    //static final String PASS = "Zbb123150@";
    static final String PASS = "yang0417";
    //    static final String PASS = "1240863915gg";
    Connection conn = null;
    Statement stmt = null;

    public DBConnector() {
        try {
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // 执行查询
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
        } catch (Exception se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        }// 处理 Class.forName 错误


    }

    public boolean login(String mode, int profile, String password) throws SQLException {
        if (Objects.equals(mode, "admin")) {
            return profile == 7777777 && Objects.equals(password, "7777777");
        }
        String sql;
        ResultSet rs;
        sql = "select " + mode + "_password from " + mode + " where " + mode + "_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setObject(1, profile);
        rs = ps.executeQuery();
        rs.next();
        String pwd;
        if (Objects.equals(mode, "student")) {
            pwd = rs.getString("student_password");
        } else {
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
        if(mode.equals("admin")){
            return "管理员";
        }
        String sql;
        String name = null;
        sql = "select " + mode + "_name" + " from " + mode + " where " + mode + "_id = " + ID;
        System.out.println(sql);
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                name = rs.getString(1);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(name);
        return name;
    }

    public Object[][] search(String mode, int[] int_args, String[] str_args, int[][] arg_args) throws CustomException, SQLException {
        Object[][] result = new Object[0][];
        Vector<Vector<Object>> tmp = new Vector<>();
        String sql;
        ResultSet rs;
        switch (mode) {
            case "学生成绩查询":
                if (int_args.length != 1 || str_args.length != 0) {
                    throw new CustomException("输入参数个数不正确" + int_args.length + "   " + str_args.length);
                }
                sql = "select 课程编号, 课程名, 学分, 成绩, 绩点\n" +
                        "from used_score\n" +
                        "where 学号='" + int_args[0] + "';\n";
                rs = stmt.executeQuery(sql);
                int id = 0;
                while (rs.next()) {
                    id++;
                    int course_order = rs.getInt("课程编号");
                    String course_name = rs.getString("课程名");
                    int credit = rs.getInt("学分");
                    int score = (int) Double.parseDouble(rs.getString("成绩"));
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
                if (int_args.length != 2 || str_args.length != 0) {
                    throw new CustomException("输入参数个数不正确" + int_args.length + str_args.length + "123456");
                }
                sql = "select used_score.课程编号, used_score.课程名, used_score.学号, student.student_name, used_score.平时成绩, used_score.考试成绩, used_score.绩点\n" +
                        "from used_score, student\n" +
                        "where used_score.工号 = '2001001' and used_score.课程编号 ='3001001' and used_score.学号=student.student_id\n" +
                        "order by used_score.成绩;";
                rs = stmt.executeQuery(sql);
                while (rs.next()) {
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
                if (int_args.length != 1 || str_args.length != 3) {//工号  课程名称，课程学期，上课时间
                    throw new CustomException("输入参数个数不正确" + int_args.length + "   " + str_args.length);
                }
                sql = "select course_id from course where teacher_id = '" + int_args[0] +
                        "' and course_name = '" + str_args[0] +
                        "' and course_semester = '" + str_args[1] +
                        "' and course_time = '" + str_args[2] +
                        "';\n";
                rs = stmt.executeQuery(sql);
                rs.next();
                int course_order = rs.getInt("course_order");
                sql = "select used_score.学号, student.student_name, used_score.平时成绩, used_score.考试成绩, used_score.成绩, used_score.绩点\n" +
                        "from used_score, student\n" +
                        "where used_score.工号 = '" + int_args[0] +
                        "' and used_score.课程编号 ='" + course_order +
                        "'\n" +
                        "order by used_score.学号;";
                rs = stmt.executeQuery(sql);
                id = 0;
                while (rs.next()) {
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
            case "教师成绩修改":
                if (int_args.length != 3 || str_args.length != 4) {
                    //工号，学号，修改后成绩  课程名称，课程学期，上课时间，平时成绩/考试成绩
                    throw new CustomException("输入参数个数不正确" + int_args.length + "   " + str_args.length);
                }
                sql = "select course_id from course where teacher_id = '" + int_args[0] +
                        "' and course_name = '" + str_args[0] +
                        "' and course_semester = '" + str_args[1] +
                        "' and course_time = '" + str_args[2] +
                        "';\n";
                rs = stmt.executeQuery(sql);
                rs.next();
                int course_id = rs.getInt("course_id");
                rs.close();
                String score_witch;
                if (str_args[3] == "平时成绩") {
                    score_witch = "usual_score";
                } else {
                    score_witch = "test_score";
                }
                sql = "update score set " + score_witch + " = '" + str_args[2] + "' where student_id = '" + int_args[1] +
                        "' and course_id = '" + course_id + "';\n";
                int row_count = stmt.executeUpdate(sql);//记录被修改的行数
                if (row_count == 1) {
                    System.out.println("修改成绩成功");
                }
                break;
            case "教师成绩录入准备"://判断此门课程成绩是否已经录入，若未录入则输出两列数据，分别为该门课程学生的学号和姓名
                if (int_args.length != 1 || str_args.length != 4) {
                    //工号  课程名称，课程学期，上课时间
                    throw new CustomException("输入参数个数不正确" + int_args.length + "   " + str_args.length);
                }
                sql = "select * from course where teacher_id = '" + int_args[0] +
                        "' and course_name = '" + str_args[0] +
                        "' and course_semester = '" + str_args[1] +
                        "' and course_time = '" + str_args[2] +
                        "';\n";
                rs = stmt.executeQuery(sql);
                rs.next();
                String score_entered = rs.getString("score_entered");
                if (score_entered == "n") {
                    course_id = rs.getInt("course_id");
                    rs.close();
                    sql = "select score.student_id as '学号',student.student_name as '姓名' " +
                            "from score,student where student.student_id=score.student_id and score.course_id = '" + course_id + "';\n";
                    rs = stmt.executeQuery(sql);
                    id = 0;
                    while (rs.next()) {
                        id++;
                        int student_id = rs.getInt("学号");
                        String student_name = rs.getString("student_name");
                        Vector<Object> row = new Vector<>();
                        row.addElement(student_id);
                        row.addElement(student_name);
                        tmp.addElement(row);
                    }
                    rs.close();
                    System.out.println(tmp);//输出两列数据:学号和学生姓名
                } else {
                    System.out.println("已完成成绩录入");
                }
                break;
            case "教师成绩二次录入"://完成将学生成绩录入两遍，对两次录入数据不同的记录进行选择
                if (int_args.length != 1 || str_args.length != 3 || arg_args.length != 4) {
                    //工号  课程名称，课程学期，上课时间 平时成绩1，考试成绩1，平时成绩2，考试成绩2
                    throw new CustomException("输入参数个数不正确" + int_args.length + "   " + str_args.length + "   " + arg_args.length);
                }
                sql = "select * from course where teacher_id = '" + int_args[0] +
                        "' and course_name = '" + str_args[0] +
                        "' and course_semester = '" + str_args[1] +
                        "' and course_time = '" + str_args[2] +
                        "';\n";
                rs = stmt.executeQuery(sql);
                rs.next();
                course_id = rs.getInt("course_id");
                rs.close();
                System.out.println("两次成绩录入情况不同的学生与录入成绩：");
                sql = "select score.student_id as '学号',student.student_name as '姓名' " +
                        "from score,student where student.student_id=score.student_id and score.course_id = '" + course_id + "';\n";
                rs = stmt.executeQuery(sql);
                id = 0;
                while (rs.next()) {
                    if (arg_args[0][id] != arg_args[2][id] || arg_args[1][id] != arg_args[3][id]) {
                        int student_id = rs.getInt("学号");
                        String student_name = rs.getString("student_name");
                        Vector<Object> row = new Vector<>();
                        row.addElement(student_id);
                        row.addElement(student_name);
                        row.addElement(arg_args[0][id]);
                        row.addElement(arg_args[2][id]);
                        row.addElement(arg_args[1][id]);
                        row.addElement(arg_args[3][id]);
                        tmp.addElement(row);
                    }
                    id++;
                }
                rs.close();
                System.out.println(tmp);//输出所有两次录入数据不同的学生学号姓名和成绩，对两次录入成绩进行选择最终得到正确的录入数据
                break;
            case "教师成绩录入完成"://将正确的数据写入数据库中
                if (int_args.length != 1 || str_args.length != 3 || arg_args.length != 2) {
                    //工号  课程名称，课程学期，上课时间 平时成绩，考试成绩
                    throw new CustomException("输入参数个数不正确" + int_args.length + "   " + str_args.length + "   " + arg_args.length);
                }
                sql = "select * from course where teacher_id = '" + int_args[0] +
                        "' and course_name = '" + str_args[0] +
                        "' and course_semester = '" + str_args[1] +
                        "' and course_time = '" + str_args[2] +
                        "';\n";
                rs = stmt.executeQuery(sql);
                rs.next();
                course_id = rs.getInt("course_id");
                rs.close();
                sql = "select score.student_id as '学号' " +
                        "from score where score.course_id = '" + course_id + "';\n";
                rs = stmt.executeQuery(sql);
                id = 0;
                while (rs.next()) {
                    int student_id = rs.getInt("学号");
                    sql = "update score set usual_score = '" + arg_args[0][id] + "' where student_id = '" + student_id +
                            "' and course_id ='" + course_id + "';";
                    stmt.executeUpdate(sql);
                    sql = "update score set test_score = '" + arg_args[1][id] + "' where student_id = '" + student_id +
                            "' and course_id ='" + course_id + "';";
                    stmt.executeUpdate(sql);
                    id++;
                }
                sql = "update course set score_entered = 'y' where course_id = '" + course_id + "';\n";
                stmt.executeUpdate(sql);
                break;
            case "修改密码":
                if (int_args.length != 2 || str_args.length != 2) {
                    //身份标志位（0：教师，1：学生），id  第一遍输入密码，第二遍输入密码
                    throw new CustomException("输入参数个数不正确" + int_args.length + "   " + str_args.length);
                }
                if (str_args[0] != str_args[1]) {
                    System.out.println("两次输入密码不一致");
                    break;
                }
                if (str_args[0].length() < 6) {
                    System.out.println("密码过短（短于6个字符）");
                    break;
                }
                if (str_args[0].length() > 20) {
                    System.out.println("密码过长（长于20个字符）");
                    break;
                }
                if (int_args[0] == 0) {
                    sql = "update teacher set teacher_password = '" + str_args[0] + "' where teacher_id = '" + int_args[1] + "';";
                    row_count = stmt.executeUpdate(sql);
                    if (row_count == 1) {
                        System.out.println("修改密码成功");
                    }
                } else if (int_args[0] == 1) {
                    sql = "update student set student_password = '" + str_args[0] + "' where student_id = '" + int_args[1] + "';";
                    row_count = stmt.executeUpdate(sql);
                    if (row_count == 1) {
                        System.out.println("修改密码成功");
                    }
                }
                break;
        }
        int row = tmp.size();
        int col = tmp.get(0).size();
        result = new Object[row][col];
        //读取数据库

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                result[i][j] = ((Vector<?>) tmp.get(i)).get(j);
            }
        }
        return result;
    }

    /**
     * @author YangJunhao
     * @param ID,semester 学号，学期
     * @function 生成该同学某学期的均绩
     * @return aver double型均绩
     */
    public double getAverageScore(int ID, String semester) {
        String sql = "select 学分,绩点 from used_score where 学号='" + ID + "'and 学期='" + semester + "';";
        Vector<Vector<Double>> tmp = new Vector<>();
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                double credit = rs.getDouble("学分");
                double gpa = rs.getDouble("绩点");
                Vector<Double> row = new Vector<>();
                row.addElement(credit);
                row.addElement(gpa);
                tmp.addElement(row);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        double creditSum = 0;
        double gpaSum = 0;
        for (int j = 0; j < tmp.size(); j++) {
            creditSum += tmp.get(j).get(0);
            gpaSum += tmp.get(j).get(0) * tmp.get(j).get(1);
        }
        double aver=0.0;
        if(creditSum!=0){
            aver = gpaSum / creditSum;
        }
        java.math.BigDecimal b = new java.math.BigDecimal(aver);
        aver = b.setScale(2,java.math.RoundingMode.HALF_UP).doubleValue();
        return aver;
    }

    public int getStuGrade(int ID)
    {
        String sql;
        int grade = 2019;
        sql = "select student_grade from student where student_id = " + ID;
        System.out.println(sql);
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                grade = rs.getInt(1);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(grade);
        return grade;
    }

    public Object[][] getEverySemesterGPA(int ID, String semester){
        int grade = this.getStuGrade(ID);
        String tmp = grade+"-"+(grade+1)+"秋季";
        Object[][] history = new Object[12][2];
        // 不考虑夏季学期
        for(int i=0;i<12;i++){
            history[i][0] = tmp;
            if(!tmp.equals(semester)){
                if(tmp.charAt(9)=='春'){
                    int year1 = java.lang.Integer.parseInt(tmp.substring(0, 4));
                    int year2 = java.lang.Integer.parseInt(tmp.substring(5, 9));
                    tmp=(year1+1)+"-"+(year2+1)+"秋季";
                }
                else if(tmp.charAt(9)=='秋'){
                    tmp=tmp.substring(0, 9)+"冬季";
                }
                else if(tmp.charAt(9)=='冬'){
                    tmp=tmp.substring(0, 9)+"春季";
                }
            }
            else{
                break;
            }
        }
        for(int i=0;i<12;i++){
            history[i][1]=getAverageScore(ID,history[i][0].toString());
            if(history[i+1][0]==null){
                break;
            }
        }
//        for(int i=0;i<12;i++){
//            System.out.println(history[i][0]);
//            System.out.println(Double.parseDouble(String.valueOf(history[i][1])));
//            if(history[i+1][0]==null){
//                break;
//            }
//        }
        return history;
    }
}


