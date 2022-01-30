package jdbctest;

import java.sql.*;
import java.time.temporal.ValueRange;
import java.util.Arrays;
import java.util.Objects;
import java.util.Vector;

public class DBConnector {
    // MySQL 8.0 以上版本 - JDBC 驱动名及数据库 URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/educationalmanagementdb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    static final String USER = "root";
//    static final String PASS = "Zx010426";
//    static final String PASS = "Zbb123150@";
//    static final String PASS = "yang0417";
    static final String PASS = "1240863915gg";
    Connection conn = null;
    Statement stmt = null;

    String[] major = {"计算机系", "物理系"};

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

    public Object[][] search(String mode, int[] int_args, String[] str_args, Vector<Object> additional)
            throws CustomException, SQLException {
        //additional向量用于返回额外信息用于描述返回的集合
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
            case "管理员班级成绩查询":
                if (int_args.length != 2 || str_args.length != 0) {// int_args[] = teacher_id, course_order
                    throw new CustomException("输入参数个数不正确" + int_args.length + str_args.length + "123456");
                }
                sql = "select used_score.课程名, used_score.学分, used_score.学号, student.student_name, " +
                        "used_score.平时成绩, used_score.考试成绩, used_score.绩点, course.course_id\n" +
                        "from used_score, student, course\n" +
                        "where used_score.工号 = '" + int_args[0] +
                        "' and used_score.课程编号 ='"+ int_args[1] +
                        "' and used_score.学号 =student.student_id and used_score.课程编号 = course.course_order " +
                        "order by used_score.学号;";
                rs = stmt.executeQuery(sql);
                String course_name = null;
                int credit=0;
                int course_id = 0;
                while (rs.next()) {
                    course_name = rs.getString("课程名");
                    credit = rs.getInt("学分");
                    int student_id = rs.getInt("学号");
                    String student_name = rs.getString("student_name");
                    int daily_score = rs.getInt("平时成绩");
                    int exam_score = rs.getInt("考试成绩");
                    double gpa = rs.getDouble("绩点");
                    course_id = rs.getInt("course_id");
                    Vector<Object> row = new Vector<>();
                    row.addElement(student_id);
                    row.addElement(student_name);
                    row.addElement(daily_score);
                    row.addElement(exam_score);
                    row.addElement(gpa);
                    tmp.addElement(row);
                }
                rs.close();
                //additional向量用于返回该课程的信息，本次返回了教师工号、课程名、课程号、学分、课程id
                additional.addElement(int_args[0]);
                additional.addElement(course_name);
                additional.addElement(int_args[1]);
                additional.addElement(credit);
                additional.addElement(course_id);
                System.out.println(sql);
                break;
            case "管理员学生查询":
                if(int_args.length != 2 || str_args.length != 3){//学号、姓名、性别、院系、入学时间
                    throw new CustomException("输入参数个数不正确"+int_args.length+"   "+str_args.length);
                }
                if((int_args[0]<1000000 || int_args[0] >9999999) && int_args[0] != 0){
                    throw new CustomException("学号输入有误"+int_args[0]);
                }
                if (!Objects.equals(str_args[1], "男") && !Objects.equals(str_args[1], "女") && str_args[1] != null){
                    throw new CustomException("性别输入有误"+int_args[0]);
                }
                if((int_args[1]<1980 || int_args[1] >2030) && int_args[1] != 0){
                    throw new CustomException("入学时间输入有误"+int_args[0]);
                }
                String part1;
                String part2;
                String part3;
                String part4;
                String part5;
                String part6;
                String and = " ";
                if (int_args[0] == 0) {
                    part1 = "";
                }else{
                    part1 = "student_id = "+int_args[0];
                    and = " and ";
                }
                if (int_args[1] == 0) {
                    part2 = "";
                }else{
                    part2 = and + "student_grade = "+int_args[1];
                    and = " and ";
                }
                if (str_args[0] == null) {
                    part3 = "";
                }else{
                    part3 = and + "student_name like '%"+str_args[0]+"%'";
                    and = " and ";
                }

                if (part3.contains("or") || part3.contains(";")){
                    throw new CustomException("姓名输入有误"+int_args[0]);
                }
                if (str_args[1] == null) {
                    part4 = "";
                }else{
                    part4 = and + "student_sex like '%"+str_args[1]+"%'";
                    and = " and ";
                }
                if (str_args[2] == null) {
                    part5 = "";
                }else{
                    part5 = and + "student_major like '%"+str_args[2]+"%'";
                    and = " and ";
                }
                if (part5.contains(" or ") || part5.contains(";")){
                    throw new CustomException("院系输入有误"+int_args[0]);
                }
                sql = "select student_id, student_name, student_sex, student_major, student_grade from student where "
                        +part1+part2+part3+part4+part5+";";
                rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    int student_id = rs.getInt("student_id");
                    String student_name = rs.getString("student_name");
                    String student_sex = rs.getString("student_sex");
                    String student_major = rs.getString("student_major");
                    int student_grade = rs.getInt("student_grade");
                    Vector<Object> row = new Vector<>();
                    row.addElement(student_id);
                    row.addElement(student_name);
                    row.addElement(student_sex);
                    row.addElement(student_major);
                    row.addElement(student_grade);
                    tmp.addElement(row);
                }
                rs.close();
                break;
            case "管理员教师查询":
                if(int_args.length != 1 || str_args.length != 2){//工号、姓名、院系
                    throw new CustomException("输入参数个数不正确"+int_args.length+"   "+str_args.length);
                }
                if((int_args[0]<1000000 || int_args[0] >9999999) && int_args[0] != 0){
                    throw new CustomException("工号输入有误"+int_args[0]);
                }
                and = "";
                if (int_args[0] == 0) {
                    part1 = "";
                }else{
                    part1 = "teacher_id = "+int_args[0];
                    and = " and ";
                }
                if (str_args[0] == null) {
                    part2 = "";
                }else{
                    part2 = and + "teacher_name like '%"+str_args[0]+"%'";
                    and = " and ";
                }
                if (part2.contains(" or ") || part2.contains(";")){
                    throw new CustomException("姓名输入有误"+int_args[0]);
                }
                if (str_args[1] == null) {
                    part3 = "";
                }else{
                    part3 = and + "teacher_major like '%"+str_args[1]+"%'";
                    and = " and ";
                }
                System.out.println(part3);
                if (part3.contains(" or ") || part3.contains(";")){
                    throw new CustomException("院系输入有误"+int_args[0]);
                }
                sql = "select teacher_id, teacher_name, teacher_major from teacher where "
                        +part1+part2+part3+";";
                System.out.println(sql);
                rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    int teacher_id = rs.getInt("teacher_id");
                    String teacher_name = rs.getString("teacher_name");
                    String teacher_major = rs.getString("teacher_major");
                    Vector<Object> row = new Vector<>();
                    row.addElement(teacher_id);
                    row.addElement(teacher_name);
                    row.addElement(teacher_major);
                    tmp.addElement(row);
                }
                rs.close();
                break;
            case "管理员课程查询":
                if(int_args.length != 3 || str_args.length != 3){//课号、课名、学分、工号、学期、上课时间
                    throw new CustomException("输入参数个数不正确"+int_args.length+"   "+str_args.length);
                }
                if((int_args[0]<1000000 || int_args[0] >9999999) && int_args[0] != 0){
                    throw new CustomException("课号输入有误"+int_args[0]);
                }
                if((int_args[1]<1 || int_args[1] >8) && int_args[1] != 0){
                    throw new CustomException("学分输入有误"+int_args[1]);
                }
                if((int_args[2]<1000000 || int_args[2] >9999999) && int_args[2] != 0){
                    throw new CustomException("工号输入有误"+int_args[0]);
                }
                and = "";
                if (int_args[0] == 0) {
                    part1 = "";
                }else{
                    part1 = "course_order = "+int_args[0];
                    and = " and ";
                }
                if (str_args[0] == null) {
                    part2 = "";
                }else{
                    part2 = and + "course_name like '%"+str_args[0]+"%'";
                    and = " and ";
                }
                if (part2.contains("or") || part2.contains(";")){
                    throw new CustomException("课名输入有误"+int_args[1]);
                }
                if (int_args[1] == 0) {
                    part3 = "";
                }else{
                    part3 = and + "course_credit = "+int_args[1];
                    and = " and ";
                }
                if (int_args[2] == 0) {
                    part4 = "";
                }else{
                    part4 = and + "teacher_id = "+int_args[2];
                    and = " and ";
                }
                if (str_args[1] == null) {
                    part5 = "";
                }else{
                    part5 = and + "course_semester like '%"+str_args[1]+"%'";
                    and = " and ";
                }
                if (part5.contains("or") || part5.contains(";")){
                    throw new CustomException("学期输入有误"+int_args[1]);
                }
                if (str_args[2] == null) {
                    part6 = "";
                }else{
                    part6 = and + "course_time like '%"+str_args[2]+"%'";
                }
                if (part6.contains("or") || part6.contains(";")){
                    throw new CustomException("上课时间输入有误"+int_args[1]);
                }
                sql = "select course_id, course_order, course_name, course_credit, teacher_id, course_semester, course_time " +
                        "from course where "
                        +part1+part2+part3+part4+part5+part6+";";
                System.out.println(sql);
                rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    course_id = rs.getInt("course_id");
                    int course_order = rs.getInt("course_order");
                    course_name = rs.getString("course_name");
                    int course_credit = rs.getInt("course_credit");
                    int teacher_id = rs.getInt("teacher_id");
                    String course_semester = rs.getString("course_semester");
                    String course_time = rs.getString("course_time");
                    Vector<Object> row = new Vector<>();
                    additional.addElement(course_id);
                    row.addElement(course_order);
                    row.addElement(course_name);
                    row.addElement(course_credit);
                    row.addElement(teacher_id);
                    row.addElement(course_semester);
                    row.addElement(course_time);
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
            default:
                System.out.println("查询模式无匹配");
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

    public void insert(String mode, int[] int_args, String[] str_args)
            throws CustomException, SQLException {
        String sql;
        ResultSet rs;
        switch (mode) {
            case "学生":
                if(int_args.length != 2 || str_args.length != 3){//学号、姓名、性别、院系、入学时间
                    throw new CustomException("输入参数个数不正确"+int_args.length+"   "+str_args.length);
                }
                if(int_args[0]<1000000 || int_args[0] >9999999){
                    throw new CustomException("学号输入有误"+int_args[0]);
                }
                if (!Objects.equals(str_args[1], "男") && !Objects.equals(str_args[1], "女")){
                    throw new CustomException("性别输入有误"+int_args[0]);
                }
                if(int_args[1]<1980 || int_args[1] >2030){
                    throw new CustomException("入学时间输入有误"+int_args[0]);
                }
                sql = "INSERT INTO `educationalmanagementdb`.`student` " +
                        "(`student_id`, `student_name`, `student_password`, `student_sex`, `student_major`, `student_grade`) " +
                        "VALUES ('"+int_args[0]+"', '"+str_args[0]+"', '123456', '"+str_args[1]+"', '"+str_args[2]+"', '"+int_args[1]+"');";
                stmt.execute(sql);
            case "教师":
                if(int_args.length != 2 || str_args.length != 3){//学号、姓名、性别、院系、入学时间
                    throw new CustomException("输入参数个数不正确"+int_args.length+"   "+str_args.length);
                }
                if(int_args[0]<1000000 || int_args[0] >9999999){
                    throw new CustomException("学号输入有误"+int_args[0]);
                }
                if (!Objects.equals(str_args[1], "男") && !Objects.equals(str_args[1], "女")){
                    throw new CustomException("性别输入有误"+int_args[0]);
                }
                if(int_args[1]<1980 || int_args[1] >2030){
                    throw new CustomException("入学时间输入有误"+int_args[0]);
                }
                sql = "INSERT INTO `educationalmanagementdb`.`student` " +
                        "(`student_id`, `student_name`, `student_password`, `student_sex`, `student_major`, `student_grade`) " +
                        "VALUES ('"+int_args[0]+"', '"+str_args[0]+"', '123456', '"+str_args[1]+"', '"+str_args[2]+"', '"+int_args[1]+"');";
                stmt.execute(sql);
            case "课程":
                if(int_args.length != 4 || str_args.length != 3){//课号、课名、学分、工号、学期、时间、平时分占成
                    throw new CustomException("输入参数个数不正确"+int_args.length+"   "+str_args.length);
                }
                if(int_args[0]<1000000 || int_args[0] >9999999){
                    throw new CustomException("课号输入有误"+int_args[0]);
                }
                if(int_args[1]<1 || int_args[1] >8){
                    throw new CustomException("学分输入有误"+int_args[0]);
                }
                if(int_args[2]<1000000 || int_args[2] >9999999){
                    throw new CustomException("工号输入有误"+int_args[0]);
                }
                if(int_args[3]<0 || int_args[3] >10){
                    throw new CustomException("平时分占比输入有误"+int_args[0]);
                }
                double ratio = ((double)int_args[3])/10;
                sql = "INSERT INTO `educationalmanagementdb`.`course` " +
                        "(`course_order`, `course_name`, `course_credit`, `teacher_id`, `course_semester`, `course_time`, `score_ratio`) " +
                        "VALUES ('"+int_args[0]+"', '"+str_args[0]+"', '"+int_args[1]+"', '"+int_args[2]+
                        "', '"+str_args[1]+"', '"+str_args[2]+"', '"+ratio+"');";
                stmt.execute(sql);
            default:
                System.out.println("插入模式无匹配");
        }
    }

    //用于删除某些记录，由于管理员的查询已支持模糊查询(缺失的数字用0代替，字符串用null代替)所以这里不支持模糊删除
    //有时删除输入的不是主键，所以需要关闭数据库的安全模式
    public void delete(String mode, int[] id)
            throws CustomException, SQLException{
        String sql;
        switch (mode) {
            case "学生":
                sql = "delete from student where student_id = "+id[0]+";";
                System.out.println(sql);
                stmt.execute(sql);
                break;
            case "选课":
                sql = "delete from score where student_id = '"+id[0]+"' and course_id = '"+id[1]+"';";
                System.out.println(sql);
                stmt.execute(sql);
                break;
            case"教师":
                int[] int_args = new int[3];
                String[] str_args = {null, null, null};
                int_args[2] = id[0];
                Vector<Object> ignore = new Vector<>();
                Object[][] rs = this.search("管理员课程查询", int_args, str_args, ignore);
                System.out.println(rs);
                for (Object[] course:rs){
                    int[] tmp_id = {(int) course[0], id[0]};
                    this.delete("班级", tmp_id);;
                }
                sql = "delete from teacher where teacher_id = "+id[0]+";";
                System.out.println(sql);
                stmt.execute(sql);
                break;
            case"班级"://id[] = course_order, teacher_id
                System.out.println("正在删除班级");
                str_args = new String[0];
                int_args = new int[2];
                int_args[0] = id[1];
                int_args[1] = id[0];
                System.out.println(Arrays.toString(int_args));
                System.out.println(Arrays.toString(str_args));
                Vector<Object> course_id = new Vector<>();
                rs = this.search("管理员班级成绩查询", int_args, str_args, course_id);
                System.out.println(Arrays.deepToString(rs));
                System.out.println(course_id.get(4));
                for (Object[] student:rs){
                    int[] choose_id = {(int)student[0], (int)course_id.get(4)};
                    this.delete("选课", choose_id);
                }
                System.out.println("已删除该班级学生");
                sql = "delete from course where course_id = "+ course_id.get(4) +";";
                System.out.println(sql);
                stmt.execute(sql);
                break;
            default:
                System.out.println("删除模式无匹配");
        }
    }

}



