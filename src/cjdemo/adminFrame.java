package cjdemo;

import jdbctest.CustomException;
import jdbctest.DBConnector;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Vector;

public class adminFrame extends JFrame implements Exit {
    JPanel showPanel;
    JPanel showCourseGradePanel, tablePanel, queryInfoPanel, queryTeacherInfoPanel, queryCourseInfoPanel, showStudentQueryPanel, showTeacherQueryPanel, showCourseQueryPanel;
    JTabbedPane showAddInfoPanel,showDropInfoPanel;
    JLabel teacherIDLabel1, courseIDLabel, studentIDLabel, studentAdmissionDateLabel, studentNameLabel, studentGenderLabel, studentFacultyLabel, teacherIDLabel2, teacherNameLabel, teacherFacultyLabel, courseIDLabel2, courseCreditLabel, teacherIDLabel3, courseNameLabel2, courseSemesterLabel, courseTimeLabel;
    JTextField teacherIDText1, courseIDText, studentIDText1, studentAdmissionDateText, studentNameText1, teacherIDText2, teacherNameText, courseIDText2, courseCreditText, teacherIDText3, courseNameText2, courseTimeText;
    JComboBox studentGenderComboBox, studentFacultyComboBox, teacherFacultyComboBox, courseSemesterComboBox;
    JButton checkCouseGradeQueryInfoButton, checkStudentQueryInfoButton, checkTeacherQueryInfoButton, checkCourseQueryInfoButton;
    JTable cjtable;
    Font font;
    JTextArea showTextArea;
    String[] int_args;
    String[] str_args;
    DBConnector conn;
    Object[][] tableData, data;

    public adminFrame() {
        this.setTitle("管理员登陆页面");
        //添加图标校徽
        ImageIcon imageIcon = new ImageIcon("image/SHU_LOGO.png");
        this.setIconImage(imageIcon.getImage().getScaledInstance(100, 140, 100));
        this.setBounds(100, 100, 1000, 800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        this.setSize(700, 600);
        this.setLocation(100, 100);
        //采用Border布局，水平间距50，垂直间距5
        this.setLayout(new BorderLayout(5, 20));
        ((JPanel) this.getContentPane()).setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        /**
         * @function: 创建显示管理员基础信息的面板adminPanel.
         */
        JPanel adminPanel = new JPanel();
        adminPanel.setPreferredSize(new Dimension(700, 80));
        adminPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        /**
         * @function: 创建欢迎老师的标签welcomeAadminLabel.
         */
        JLabel welcomeAdminLabel = new JLabel("欢迎您，管理员 ");
        welcomeAdminLabel.setPreferredSize(new Dimension(120, 60));
        font = new Font("Dialog", 1, 12);
        welcomeAdminLabel.setFont(font);
        //将欢迎管理员的信息添加到管理员信息面板中
        adminPanel.add(welcomeAdminLabel);
        //将管理员信息面板添加到主面板中
        this.add(adminPanel, "North");


        /**
         * @function: 创建安全退出的按钮exitButton.
         */
        JButton exitButton = new JButton("安全退出");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doExit();
            }
        });
        exitButton.setPreferredSize(new Dimension(100, 30));
        exitButton.setFont(font);
        exitButton.setContentAreaFilled(false);
        adminPanel.add(exitButton);

        /**
         * @function: 创建管理员功能面板functionPanel
         */
        JPanel functionPanel = new JPanel();
        functionPanel.setPreferredSize(new Dimension(120, 600));
        functionPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10));
        this.add(functionPanel, "West");

        /**
         * @function: 创建管理员显示面板showPanel
         */
        showPanel = new JPanel();
        showPanel.setPreferredSize(new Dimension(500, 600));
        showPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10));
        this.add(showPanel, "East");

        /*
         * @function: 在functionPanel中创建并添加【开始新学期】按钮newSemeButton.
         * 为【开始新学期】按钮添加监听器，实现开始新学期功能
         */
        JButton newSemeButton = new JButton("开始新学期");
        newSemeButton.setPreferredSize(new Dimension(120, 30));
        newSemeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPanel.removeAll();
                showPanel.validate();
                showPanel.repaint();
                Object[] options = {"确认",
                        "取消"};
                DBConnector t =new DBConnector();
                int n = 0;
                try {
                    n = JOptionPane.showOptionDialog(null,
                            "当前学期为"+t.getSeme()+"，将开始新学期"+t.getNextSeme(),
                            "开始新学期",
                            JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.INFORMATION_MESSAGE,
                            null,
                            options,
                            options[0]);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                if(n==0){
                    try {
                        t.setSeme(t.getNextSeme());
                        JOptionPane.showMessageDialog(null, "学期更新成功！即将返回登录页面");
                        dispose();
                        JFrame newFrame = new CjFrame("成绩管理系统登录界面");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        functionPanel.add(newSemeButton);

        /**
         * @function: 在functionPanel中创建并添加【班级成绩查询】按钮courseGradeQueryButton.
         * 为【班级成绩查询】按钮添加监听器，实现班级成绩查询功能
         */
        JButton courseGradeQueryButton = new JButton("班级成绩查询");
        courseGradeQueryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCourseGradePanel.setVisible(true);
                showPanel.removeAll();
                showPanel.add(showCourseGradePanel);
                showPanel.validate();
                showPanel.repaint();

            }
        });
        courseGradeQueryButton.setFont(font);
        courseGradeQueryButton.setContentAreaFilled(false);
        courseGradeQueryButton.setPreferredSize(new Dimension(120, 30));
        functionPanel.add(courseGradeQueryButton);

        /**
         * @function: 班级成绩查询后的显示面板
         * 默认为不显示，当点击【班级成绩查询】按钮时显示
         */
        showCourseGradePanel = new JPanel();
        showCourseGradePanel.setPreferredSize(new Dimension(500, 500));
        showCourseGradePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10));
        showCourseGradePanel.setVisible(false);
        showPanel.add(showCourseGradePanel, "East");

        /**
         * @function: 在班级成绩查询的显示面板中添加搜索筛选文本框信息
         * teacherIDLabel:教师工号标签；
         * teacherIDText:文本输入框接收用户输入的教师工号；
         */
        teacherIDLabel1 = new JLabel("教师工号");
        teacherIDLabel1.setFont(font);
        teacherIDLabel1.setPreferredSize(new Dimension(60, 30));
        showCourseGradePanel.add(teacherIDLabel1);

        teacherIDText1 = new JTextField("");
        teacherIDText1.setFont(font);
        teacherIDText1.setPreferredSize(new Dimension(60, 30));
        showCourseGradePanel.add(teacherIDText1);

        courseIDLabel = new JLabel("课程编号");
        courseIDLabel.setFont(font);
        courseIDLabel.setPreferredSize(new Dimension(60, 30));
        showCourseGradePanel.add(courseIDLabel);

        courseIDText = new JTextField("");
        courseIDText.setFont(font);
        courseIDText.setPreferredSize(new Dimension(80, 30));
        showCourseGradePanel.add(courseIDText);

        checkCouseGradeQueryInfoButton = new JButton("确认");
        checkCouseGradeQueryInfoButton.setFont(font);
        checkCouseGradeQueryInfoButton.setPreferredSize(new Dimension(100, 30));
        checkCouseGradeQueryInfoButton.setContentAreaFilled(false);
        checkCouseGradeQueryInfoButton.addActionListener(e -> {
                    System.out.println("用户输入的教师工号为：" + teacherIDText1.getText() + " 用户输入的课程编号为：" + courseIDText.getText());
                    cjtable = getJTable(teacherIDText1.getText(), courseIDText.getText());
                    DefaultTableCellRenderer r = new DefaultTableCellRenderer();
                    r.setHorizontalAlignment(JLabel.CENTER);
                    cjtable.setDefaultRenderer(Object.class, r);
                    JPanel panel_export = new JPanel();
                    panel_export.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
                    panel_export.setPreferredSize(new Dimension(500, 50));
                    Export export = new Export(cjtable);
                    JButton buttonExport = export.getButtonExport();
                    panel_export.add(buttonExport);
                    Print print = new Print(cjtable, this, "");
                    JButton buttonPrint = print.getButtonPrint();
                    panel_export.add(buttonPrint);
                    JScrollPane jScrollPane = new JScrollPane(cjtable);
                    jScrollPane.setPreferredSize(new Dimension(500, 300));
                    tablePanel.removeAll();
                    tablePanel.add(panel_export);
                    tablePanel.add(jScrollPane);
                    tablePanel.setVisible(true);
                    tablePanel.validate();
                    tablePanel.repaint();
                    cjtable.setPreferredSize(new Dimension(500, 300));
                    cjtable.setEnabled(false);  //不可编辑
                    cjtable.getTableHeader().setReorderingAllowed(false);   //不可整列移动
                    cjtable.getTableHeader().setResizingAllowed(false);   //不可拉动表格

                }
        );
        showCourseGradePanel.add(checkCouseGradeQueryInfoButton);

        tablePanel = new JPanel();
        tablePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        tablePanel.setPreferredSize(new Dimension(500, 300));
        showCourseGradePanel.add(tablePanel);


        /**
         * @function: 在functionPanel中创建并添加【学生查询】按钮studentQueryButton.
         * 为【学生查询】按钮添加监听器，实现班级成绩查询功能
         */
        JButton studentQueryButton = new JButton("学生查询");
        studentQueryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showStudentQueryPanel.setVisible(true);
                showPanel.removeAll();
                showPanel.add(showStudentQueryPanel);
                showPanel.validate();
                showPanel.repaint();

            }
        });
        studentQueryButton.setFont(font);
        studentQueryButton.setContentAreaFilled(false);
        studentQueryButton.setPreferredSize(new Dimension(120, 30));
        functionPanel.add(studentQueryButton);

        /**
         * @function: 学生查询后的显示面板
         * 默认为不显示，当点击【学生查询】按钮时显示
         */
        showStudentQueryPanel = new JPanel();
        showStudentQueryPanel.setPreferredSize(new Dimension(500, 500));
        showStudentQueryPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        showStudentQueryPanel.setVisible(false);
        showPanel.add(showStudentQueryPanel, "East");

        /**
         * @function: 在学生查询的显示面板中添加搜索筛选文本框信息
         * courseNameLabel:课程名标签；
         * courseNameText:文本输入框接收用户输入的课程名；
         */
        studentIDLabel = new JLabel("学号");
        studentIDLabel.setFont(font);
        studentIDLabel.setPreferredSize(new Dimension(50, 30));
        showStudentQueryPanel.add(studentIDLabel);

        studentIDText1 = new JTextField("");
        studentIDText1.setFont(font);
        studentIDText1.setPreferredSize(new Dimension(60, 30));
        showStudentQueryPanel.add(studentIDText1);

        studentAdmissionDateLabel = new JLabel("入学日期");
        studentAdmissionDateLabel.setFont(font);
        studentAdmissionDateLabel.setPreferredSize(new Dimension(80, 30));
        showStudentQueryPanel.add(studentAdmissionDateLabel);

        studentAdmissionDateText = new JTextField("");
        studentAdmissionDateText.setFont(font);
        studentAdmissionDateText.setPreferredSize(new Dimension(80, 30));
        showStudentQueryPanel.add(studentAdmissionDateText);

        studentNameLabel = new JLabel("姓名");
        studentNameLabel.setFont(font);
        studentNameLabel.setPreferredSize(new Dimension(80, 30));
        showStudentQueryPanel.add(studentNameLabel);

        studentNameText1 = new JTextField("");
        studentNameText1.setFont(font);
        studentNameText1.setPreferredSize(new Dimension(90, 30));
        showStudentQueryPanel.add(studentNameText1);

        studentGenderLabel = new JLabel("性别");
        studentGenderLabel.setFont(font);
        studentGenderLabel.setPreferredSize(new Dimension(60, 30));
        showStudentQueryPanel.add(studentGenderLabel);

        studentGenderComboBox = new JComboBox();
        studentGenderComboBox.setSelectedItem("任意");
        studentGenderComboBox.addItem("任意");
        studentGenderComboBox.addItem("男");
        studentGenderComboBox.addItem("女");
        studentGenderComboBox.setFont(font);
        studentGenderComboBox.setPreferredSize(new Dimension(60, 30));
        showStudentQueryPanel.add(studentGenderComboBox);

        studentFacultyLabel = new JLabel("院系");
        studentFacultyLabel.setFont(font);
        studentFacultyLabel.setPreferredSize(new Dimension(60, 30));
        showStudentQueryPanel.add(studentFacultyLabel);

        studentFacultyComboBox = new JComboBox();
        studentFacultyComboBox.setSelectedItem("任意");
        studentFacultyComboBox.addItem("任意");
        studentFacultyComboBox.addItem("计算机系");
        studentFacultyComboBox.addItem("物理系");
        studentFacultyComboBox.setFont(font);
        studentFacultyComboBox.setPreferredSize(new Dimension(80, 30));
        showStudentQueryPanel.add(studentFacultyComboBox);

        checkStudentQueryInfoButton = new JButton("确认");
        checkStudentQueryInfoButton.setFont(font);
        checkStudentQueryInfoButton.setPreferredSize(new Dimension(100, 30));
        checkStudentQueryInfoButton.setContentAreaFilled(false);
        checkStudentQueryInfoButton.addActionListener(e -> {
                    System.out.println("用户输入的学号为：" + studentIDText1.getText() + " 用户输入的入学日期为：" + studentAdmissionDateText.getText() + " 用户输入的姓名为：" + studentNameText1.getText() + " 用户输入的性别为：" +
                            studentGenderComboBox.getSelectedItem().toString() + " 用户输入的院系为：" + studentFacultyComboBox.getSelectedItem().toString());

                    String stuGender = "";
                    String stuFaculty = "";
                    if (!Objects.equals(studentGenderComboBox.getSelectedItem().toString(), "任意")){
                        stuGender = studentGenderComboBox.getSelectedItem().toString();
                    }
                    if (!Objects.equals(studentFacultyComboBox.getSelectedItem().toString(), "任意")){
                        stuFaculty = studentFacultyComboBox.getSelectedItem().toString();
                    }
                    cjtable = getStudentQueryInfo(studentIDText1.getText(), studentAdmissionDateText.getText(), studentNameText1.getText(), stuGender, stuFaculty);
                    DefaultTableCellRenderer r = new DefaultTableCellRenderer();
                    r.setHorizontalAlignment(JLabel.CENTER);
                    cjtable.setDefaultRenderer(Object.class, r);
                    JScrollPane jScrollPane = new JScrollPane(cjtable);
                    jScrollPane.setPreferredSize(new Dimension(500, 300));
                    queryInfoPanel.removeAll();
                    queryInfoPanel.add(jScrollPane);
                    queryInfoPanel.setVisible(true);
                    queryInfoPanel.validate();
                    queryInfoPanel.repaint();
                    cjtable.setPreferredSize(new Dimension(500, 300));
                    cjtable.setEnabled(false);  //不可编辑
                    cjtable.getTableHeader().setReorderingAllowed(false);   //不可整列移动
                    cjtable.getTableHeader().setResizingAllowed(false);   //不可拉动表格

                }
        );
        showStudentQueryPanel.add(checkStudentQueryInfoButton);
        queryInfoPanel = new JPanel();
        queryInfoPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        queryInfoPanel.setPreferredSize(new Dimension(500, 300));
        showStudentQueryPanel.add(queryInfoPanel);

        /**
         * @function: 在functionPanel中创建并添加【教师查询】按钮teacherQueryButton.
         * 为【教师查询】按钮添加监听器，实现班级成绩查询功能
         */
        JButton teacherQueryButton = new JButton("教师查询");
        teacherQueryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showTeacherQueryPanel.setVisible(true);
                showPanel.removeAll();
                showPanel.add(showTeacherQueryPanel);
                showPanel.validate();
                showPanel.repaint();
            }
        });

        teacherQueryButton.setFont(font);
        teacherQueryButton.setContentAreaFilled(false);
        teacherQueryButton.setPreferredSize(new Dimension(120, 30));
        functionPanel.add(teacherQueryButton);

        /**
         * @function: 教师查询后的显示面板
         * 默认为不显示，当点击【教师查询】按钮时显示
         */
        showTeacherQueryPanel = new JPanel();
        showTeacherQueryPanel.setPreferredSize(new Dimension(500, 500));
        showTeacherQueryPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        showTeacherQueryPanel.setVisible(false);
        showPanel.add(showTeacherQueryPanel, "East");

        /**
         * @function: 在教师查询的显示面板中添加搜索筛选文本框信息
         * courseNameLabel:课程名标签；
         * courseNameText:文本输入框接收用户输入的课程名；
         */
        teacherIDLabel2 = new JLabel("教师工号");
        teacherIDLabel2.setFont(font);
        teacherIDLabel2.setPreferredSize(new Dimension(70, 30));
        showTeacherQueryPanel.add(teacherIDLabel2);

        teacherIDText2 = new JTextField("");
        teacherIDText2.setFont(font);
        teacherIDText2.setPreferredSize(new Dimension(120, 30));
        showTeacherQueryPanel.add(teacherIDText2);

        teacherNameLabel = new JLabel("教师姓名");
        teacherNameLabel.setFont(font);
        teacherNameLabel.setPreferredSize(new Dimension(70, 30));
        showTeacherQueryPanel.add(teacherNameLabel);

        teacherNameText = new JTextField("");
        teacherNameText.setFont(font);
        teacherNameText.setPreferredSize(new Dimension(120, 30));
        showTeacherQueryPanel.add(teacherNameText);

        teacherFacultyLabel = new JLabel("教师院系");
        teacherFacultyLabel.setFont(font);
        teacherFacultyLabel.setPreferredSize(new Dimension(70, 30));
        showTeacherQueryPanel.add(teacherFacultyLabel);

        teacherFacultyComboBox = new JComboBox();
        teacherFacultyComboBox.setSelectedItem("任意");
        teacherFacultyComboBox.addItem("任意");
        teacherFacultyComboBox.addItem("计算机系");
        teacherFacultyComboBox.addItem("物理系");
        teacherFacultyComboBox.setFont(font);
        teacherFacultyComboBox.setPreferredSize(new Dimension(90, 30));
        showTeacherQueryPanel.add(teacherFacultyComboBox);

        checkTeacherQueryInfoButton = new JButton("确认");
        checkTeacherQueryInfoButton.setFont(font);
        checkTeacherQueryInfoButton.setPreferredSize(new Dimension(60, 30));
        checkTeacherQueryInfoButton.setContentAreaFilled(false);
        checkTeacherQueryInfoButton.addActionListener(e -> {
            System.out.println("用户输入的教师工号为：" + teacherIDText2.getText() + " 用户输入的教师姓名为：" + teacherNameText.getText() + " 用户输入的教师院系为：" + teacherFacultyComboBox.getSelectedItem().toString());

            String teaFaculty = "";
            if (!Objects.equals(teacherFacultyComboBox.getSelectedItem().toString(), "任意")){
                teaFaculty = teacherFacultyComboBox.getSelectedItem().toString();
            }
            cjtable = getTeacherQueryInfo(teacherIDText2.getText(), teacherNameText.getText(), teaFaculty);
            DefaultTableCellRenderer r = new DefaultTableCellRenderer();
            r.setHorizontalAlignment(JLabel.CENTER);
            cjtable.setDefaultRenderer(Object.class, r);
            JScrollPane jScrollPane = new JScrollPane(cjtable);
            jScrollPane.setPreferredSize(new Dimension(500, 300));
            queryTeacherInfoPanel.removeAll();
            queryTeacherInfoPanel.add(jScrollPane);
            queryTeacherInfoPanel.setVisible(true);
            queryTeacherInfoPanel.validate();
            queryTeacherInfoPanel.repaint();
            cjtable.setPreferredSize(new Dimension(500, 300));
            cjtable.setEnabled(false);  //不可编辑
            cjtable.getTableHeader().setReorderingAllowed(false);   //不可整列移动
            cjtable.getTableHeader().setResizingAllowed(false);   //不可拉动表格

        });
        showTeacherQueryPanel.add(checkTeacherQueryInfoButton);
        queryTeacherInfoPanel = new JPanel();
        queryTeacherInfoPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        queryTeacherInfoPanel.setPreferredSize(new Dimension(500, 300));
        showTeacherQueryPanel.add(queryTeacherInfoPanel);


        /**
         * @function: 在functionPanel中创建并添加【课程查询】按钮courseQueryButton.
         * 为【课程查询】按钮添加监听器，实现班级成绩查询功能
         */
        JButton courseQueryButton = new JButton("课程查询");
        courseQueryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCourseQueryPanel.setVisible(true);
                showPanel.removeAll();
                showPanel.add(showCourseQueryPanel);
                showPanel.validate();
                showPanel.repaint();
            }
        });

        courseQueryButton.setFont(font);
        courseQueryButton.setContentAreaFilled(false);
        courseQueryButton.setPreferredSize(new Dimension(120, 30));
        functionPanel.add(courseQueryButton);

        /**
         * @function: 课程查询后的显示面板
         * 默认为不显示，当点击【课程查询】按钮时显示
         */
        showCourseQueryPanel = new JPanel();
        showCourseQueryPanel.setPreferredSize(new Dimension(500, 500));
        showCourseQueryPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 10));
        showCourseQueryPanel.setVisible(false);
        showPanel.add(showCourseQueryPanel, "East");

        /**
         * @function: 在课程查询的显示面板中添加搜索筛选文本框信息
         * courseNameLabel:课程名标签；
         * courseNameText:文本输入框接收用户输入的课程名；
         */
        courseIDLabel2 = new JLabel("课号");
        courseIDLabel2.setFont(font);
        courseIDLabel2.setPreferredSize(new Dimension(50, 30));
        showCourseQueryPanel.add(courseIDLabel2);

        courseIDText2 = new JTextField("");
        courseIDText2.setFont(font);
        courseIDText2.setPreferredSize(new Dimension(50, 30));
        showCourseQueryPanel.add(courseIDText2);

        courseCreditLabel = new JLabel("学分");
        courseCreditLabel.setFont(font);
        courseCreditLabel.setPreferredSize(new Dimension(50, 30));
        showCourseQueryPanel.add(courseCreditLabel);

        courseCreditText = new JTextField("");
        courseCreditText.setFont(font);
        courseCreditText.setPreferredSize(new Dimension(50, 30));
        showCourseQueryPanel.add(courseCreditText);

        teacherIDLabel3 = new JLabel("教师工号");
        teacherIDLabel3.setFont(font);
        teacherIDLabel3.setPreferredSize(new Dimension(70, 30));
        showCourseQueryPanel.add(teacherIDLabel3);

        teacherIDText3 = new JTextField("");
        teacherIDText3.setFont(font);
        teacherIDText3.setPreferredSize(new Dimension(50, 30));
        showCourseQueryPanel.add(teacherIDText3);

        courseNameLabel2 = new JLabel("课名");
        courseNameLabel2.setFont(font);
        courseNameLabel2.setPreferredSize(new Dimension(40, 30));
        showCourseQueryPanel.add(courseNameLabel2);

        courseNameText2 = new JTextField("");
        courseNameText2.setFont(font);
        courseNameText2.setPreferredSize(new Dimension(50, 30));
        showCourseQueryPanel.add(courseNameText2);

        courseSemesterLabel = new JLabel("学期");
        courseSemesterLabel.setFont(font);
        courseSemesterLabel.setPreferredSize(new Dimension(50, 30));
        showCourseQueryPanel.add(courseSemesterLabel);

        courseSemesterComboBox = new JComboBox();
        courseSemesterComboBox.setSelectedItem("任意");
        courseSemesterComboBox.addItem("任意");
        DBConnector t = new DBConnector();
        String[] sl = t.getSemeList();
        for(int i=0;i< sl.length;i++){
            courseSemesterComboBox.addItem(sl[i]);
            if(Objects.equals(sl[i + 1], null)){
                break;
            }
        }
        courseSemesterComboBox.setFont(font);
        courseSemesterComboBox.setPreferredSize(new Dimension(120, 30));
        showCourseQueryPanel.add(courseSemesterComboBox);

        courseTimeLabel = new JLabel("上课时间");
        courseTimeLabel.setFont(font);
        courseTimeLabel.setPreferredSize(new Dimension(50, 30));
        showCourseQueryPanel.add(courseTimeLabel);

        courseTimeText = new JTextField("");
        courseTimeText.setFont(font);
        courseTimeText.setPreferredSize(new Dimension(50, 30));
        showCourseQueryPanel.add(courseTimeText);

        checkCourseQueryInfoButton = new JButton("确认");
        checkCourseQueryInfoButton.setFont(font);
        checkCourseQueryInfoButton.setPreferredSize(new Dimension(60, 30));
        checkCourseQueryInfoButton.setContentAreaFilled(false);
        checkCourseQueryInfoButton.addActionListener(e -> {
            String courseSeme = "";
            if (!Objects.equals(courseSemesterComboBox.getSelectedItem().toString(), "任意")){
                courseSeme = courseSemesterComboBox.getSelectedItem().toString();
            }
            System.out.println("用户输入的课号为：" + courseIDText2.getText() + " 用户输入的学分为：" + courseCreditText.getText() + " 用户输入的教师工号为：" + teacherIDText3.getText() + " 用户输入的课名为：" +
                    courseNameText2.getText() + " 用户输入的学期为：" + courseSemesterComboBox.getSelectedItem().toString() + " 用户输入的上课时间为：" + courseTimeText.getText());

            cjtable = getCourseQueryInfo(courseIDText2.getText(), courseCreditText.getText(), teacherIDText3.getText(), courseNameText2.getText(), courseSeme, courseTimeText.getText());
            DefaultTableCellRenderer r = new DefaultTableCellRenderer();
            r.setHorizontalAlignment(JLabel.CENTER);
            cjtable.setDefaultRenderer(Object.class, r);
            JScrollPane jScrollPane = new JScrollPane(cjtable);
            jScrollPane.setPreferredSize(new Dimension(500, 200));
            queryCourseInfoPanel.removeAll();
            queryCourseInfoPanel.add(jScrollPane);
            queryCourseInfoPanel.setVisible(true);
            queryCourseInfoPanel.validate();
            queryCourseInfoPanel.repaint();
            cjtable.setPreferredSize(new Dimension(500, 200));
            cjtable.setEnabled(false);  //不可编辑
            cjtable.getTableHeader().setReorderingAllowed(false);   //不可整列移动
            cjtable.getTableHeader().setResizingAllowed(false);   //不可拉动表格

        });

        showCourseQueryPanel.add(checkCourseQueryInfoButton);
        queryCourseInfoPanel = new JPanel();
        queryCourseInfoPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        queryCourseInfoPanel.setPreferredSize(new Dimension(500, 200));
        showCourseQueryPanel.add(queryCourseInfoPanel);

        /**
         * @function: 在functionPanel中创建并添加【新增信息】按钮addInfoButton.
         * 为【新增】按钮添加监听器，实现新增功能
         */
        JButton addInfoButton = new JButton("新增信息");
        addInfoButton.addActionListener(e -> {
            showAddInfoPanel.setVisible(true);
            showPanel.removeAll();
            showPanel.add(showAddInfoPanel);
            showPanel.validate();
            showPanel.repaint();
        });
        addInfoButton.setFont(font);
        addInfoButton.setPreferredSize(new Dimension(120,30));
        functionPanel.add(addInfoButton);

        showAddInfoPanel = new JTabbedPane();
        showAddInfoPanel.setPreferredSize(new Dimension(500,400));
        JComponent panel1=makeAddInfoPanel(1);
        showAddInfoPanel.addTab("新增学生", panel1);
        JComponent panel2=makeAddInfoPanel(2);
        showAddInfoPanel.addTab("新增教师",panel2);
        JComponent panel3=makeAddInfoPanel(3);
        showAddInfoPanel.addTab("新增课程",panel3);
        JComponent panel4=makeAddInfoPanel(4);
        showAddInfoPanel.addTab("新增选课",panel4);
        showAddInfoPanel.setVisible(false);
        showPanel.add(showAddInfoPanel);


        /**
         * @function: 在functionPanel中创建并添加【删除信息】按钮dropInfoButton.
         * 为【删除】按钮添加监听器，实现新增功能
         */
        JButton dropInfoButton = new JButton("删除信息");
        dropInfoButton.addActionListener(e -> {
            showDropInfoPanel.setVisible(true);
            showPanel.removeAll();
            showPanel.add(showDropInfoPanel);
            showPanel.validate();
            showPanel.repaint();
        });
        dropInfoButton.setFont(font);
        dropInfoButton.setPreferredSize(new Dimension(120,30));
        functionPanel.add(dropInfoButton);

        showDropInfoPanel = new JTabbedPane();
        showDropInfoPanel.setPreferredSize(new Dimension(500,400));
        JComponent panel5=makeDropInfoPanel(1);
        showDropInfoPanel.addTab("删除学生", panel5);
        JComponent panel6=makeDropInfoPanel(2);
        showDropInfoPanel.addTab("删除教师",panel6);
        JComponent panel7=makeDropInfoPanel(3);
        showDropInfoPanel.addTab("删除班级",panel7);
        JComponent panel8=makeDropInfoPanel(4);
        showDropInfoPanel.addTab("删除选课",panel8);
        showDropInfoPanel.setVisible(false);
        showPanel.add(showDropInfoPanel);
    }

    /**
     * @function: 为新增操作的选项卡生成tabbed标签内的面板
     * @param mode 身份码
     * @return panel对象
     */
    protected JComponent makeAddInfoPanel(int mode)
    {
        JPanel panel=new JPanel(false);
        panel.setPreferredSize(new Dimension(500,400) );
        panel.setLayout(new FlowLayout(FlowLayout.CENTER,8,10));
        if(mode==1) {
            JLabel studentIDLabel4 = new JLabel("学号");
            studentIDLabel4.setFont(font);
            studentIDLabel4.setPreferredSize(new Dimension(50,30));
            panel.add(studentIDLabel4);

            JTextField studentIDText4 = new JTextField("");
            studentIDText4.setFont(font);
            studentIDText4.setPreferredSize(new Dimension(70,30));
            panel.add(studentIDText4);

            JLabel dateLabel4 = new JLabel("入学日期");
            dateLabel4.setFont(font);
            dateLabel4.setPreferredSize(new Dimension(80,30));
            panel.add(dateLabel4);

            JTextField dateText4 = new JTextField("");
            dateText4.setFont(font);
            dateText4.setPreferredSize(new Dimension(70,30));
            panel.add(dateText4);

            JLabel nameLabel = new JLabel("姓名");
            nameLabel.setFont(font);
            nameLabel.setPreferredSize(new Dimension(50,30));
            panel.add(nameLabel);

            JTextField nameText = new JTextField("");
            nameText.setFont(font);
            nameText.setPreferredSize(new Dimension(70,30));
            panel.add(nameText);

            JLabel sexLabel = new JLabel("性别");
            sexLabel.setFont(font);
            sexLabel.setPreferredSize(new Dimension(50,30));
            panel.add(sexLabel);

            JComboBox sexComboBox = new JComboBox();
            sexComboBox.setSelectedItem("男");
            sexComboBox.addItem("男");
            sexComboBox.addItem("女");
            sexComboBox.setFont(font);
            sexComboBox.setPreferredSize(new Dimension(50,30));
            panel.add(sexComboBox);

            JLabel facultyLabel = new JLabel("院系");
            facultyLabel.setFont(font);
            facultyLabel.setPreferredSize(new Dimension(50,30));
            panel.add(facultyLabel);

            JComboBox facultyComboBox = new JComboBox();
            facultyComboBox.setSelectedItem("计算机系");
            facultyComboBox.addItem("计算机系");
            facultyComboBox.addItem("物理系");
            facultyComboBox.setFont(font);
            facultyComboBox.setPreferredSize(new Dimension(80,30));
            panel.add(facultyComboBox);

            JButton checkButton = new JButton("确认");
            checkButton.addActionListener(e -> {
                conn = new DBConnector();
                System.out.println(studentIDText4.getText());
                int_args = new String[2];
                str_args = new String[3];
                int_args[0] = studentIDText4.getText();
                int_args[1] = dateText4.getText();
                str_args[0] = nameText.getText();

                try {
                    conn.insert("学生",int_args,str_args);
                } catch (CustomException ex) {
                    ex.printStackTrace();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });
            checkButton.setPreferredSize(new Dimension(60,30));
            checkButton.setContentAreaFilled(false);
            checkButton.setFont(font);
            panel.add(checkButton);
        }
        else if(mode==2)
        {
            JLabel teacherIDLabel4 = new JLabel("教师工号");
            teacherIDLabel4.setFont(font);
            teacherIDLabel4.setPreferredSize(new Dimension(50,30));
            panel.add(teacherIDLabel4);

            JTextField teacherIDText4 = new JTextField("");
            teacherIDText4.setFont(font);
            teacherIDText4.setPreferredSize(new Dimension(50,30));
            panel.add(teacherIDText4);

            JLabel teacherNameLabel4 = new JLabel("教师姓名");
            teacherNameLabel4.setFont(font);
            teacherNameLabel4.setPreferredSize(new Dimension(50,30));
            panel.add(teacherNameLabel4);

            JTextField teacherNameText4 = new JTextField("");
            teacherNameText4.setFont(font);
            teacherNameText4.setPreferredSize(new Dimension(50,30));
            panel.add(teacherNameText4);

            JLabel facultyLabel = new JLabel("院系");
            facultyLabel.setFont(font);
            facultyLabel.setPreferredSize(new Dimension(50,30));
            panel.add(facultyLabel);

            JComboBox facultyComboBox = new JComboBox();
            facultyComboBox.setSelectedItem("计算机系");
            facultyComboBox.addItem("计算机系");
            facultyComboBox.addItem("物理系");
            facultyComboBox.setFont(font);
            facultyComboBox.setPreferredSize(new Dimension(80,30));
            panel.add(facultyComboBox);

            JButton checkButton = new JButton("确认");
            checkButton.addActionListener(e -> {
                conn = new DBConnector();
                int_args = new String[1];
                str_args = new String[2];
                int_args[0] = teacherIDText4.getText();
                str_args[0] = teacherNameText4.getText();
                try {
                    conn.insert("教师",int_args,str_args);
                } catch (CustomException ex) {
                    ex.printStackTrace();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
//                System.out.println(teacherIDText4.getText());
            });
            checkButton.setPreferredSize(new Dimension(60,30));
            checkButton.setContentAreaFilled(false);
            checkButton.setFont(font);
            panel.add(checkButton);
        }
        else if(mode==3)
        {
            JLabel IDLabel = new JLabel("课程编号");
            IDLabel.setFont(font);
            IDLabel.setPreferredSize(new Dimension(50,30));
            panel.add(IDLabel);

            JTextField IDText = new JTextField("");
            IDText.setFont(font);
            IDText.setPreferredSize(new Dimension(50,30));
            panel.add(IDText);

            JLabel creditLabel = new JLabel("学分");
            creditLabel.setFont(font);
            creditLabel.setPreferredSize(new Dimension(30,30));
            panel.add(creditLabel);

            JTextField creditText = new JTextField("");
            creditText.setFont(font);
            creditText.setPreferredSize(new Dimension(50,30));
            panel.add(creditText);

            JLabel tIDLabel = new JLabel("教师工号");
            tIDLabel.setFont(font);
            tIDLabel.setPreferredSize(new Dimension(60,30));
            panel.add(tIDLabel);

            JTextField tIDText = new JTextField("");
            tIDText.setFont(font);
            tIDText.setPreferredSize(new Dimension(50,30));
            panel.add(tIDText);

            JLabel percentLabel = new JLabel("平时分比例");
            percentLabel.setFont(font);
            percentLabel.setPreferredSize(new Dimension(80,30));
            panel.add(percentLabel);

            JTextField percentText = new JTextField("");
            percentText.setFont(font);
            percentText.setPreferredSize(new Dimension(40,30));
            panel.add(percentText);

            JLabel cNameLabel = new JLabel("课名");
            cNameLabel.setFont(font);
            cNameLabel.setPreferredSize(new Dimension(30,30));
            panel.add(cNameLabel);

            JTextField cNameText = new JTextField("");
            cNameText.setFont(font);
            cNameText.setPreferredSize(new Dimension(40,30));
            panel.add(cNameText);

            JLabel semesterLabel = new JLabel("学期");
            semesterLabel.setFont(font);
            semesterLabel.setPreferredSize(new Dimension(30,30));
            panel.add(semesterLabel);

            JComboBox semesterText = new JComboBox();
            semesterText.setSelectedItem("2019-2020秋季");
            DBConnector t = new DBConnector();
            String[] sl = t.getSemeList();
            for(int i=0;i< sl.length;i++){
                semesterText.addItem(sl[i]);
                if(Objects.equals(sl[i + 1], null)){
                    break;
                }
            }
            semesterText.setFont(font);
            semesterText.setPreferredSize(new Dimension(110,30));
            panel.add(semesterText);

            JLabel timeLabel = new JLabel("上课时间");
            timeLabel.setFont(font);
            timeLabel.setPreferredSize(new Dimension(50,30));
            panel.add(timeLabel);

            JTextField timeText = new JTextField("");
            timeText.setFont(font);
            timeText.setPreferredSize(new Dimension(40,30));
            panel.add(timeText);

            JButton checkButton = new JButton("确认");
            checkButton.addActionListener(e -> {
                conn = new DBConnector();
                int_args = new String[4];
                int_args[0] = IDText.getText();
                int_args[1] = creditText.getText();
                int_args[2] = tIDText.getText();
                int_args[3] = percentText.getText();
                str_args = new String[3];
                str_args[0] = cNameText.getText();
                str_args[1] = semesterText.getSelectedItem().toString();
                str_args[2] = timeText.getText();
                try {
                    conn.insert("课程",int_args,str_args);
                } catch (CustomException ex) {
                    ex.printStackTrace();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

//                System.out.println(teacherIDText4.getText());
            });
            checkButton.setPreferredSize(new Dimension(60,30));
            checkButton.setContentAreaFilled(false);
            checkButton.setFont(font);
            panel.add(checkButton);
        }

        else if(mode==4)
        {
            JLabel IDLabel4 = new JLabel("学号");
            IDLabel4.setFont(font);
            IDLabel4.setPreferredSize(new Dimension(50,30));
            panel.add(IDLabel4);

            JTextField IDText4 = new JTextField("");
            IDText4.setFont(font);
            IDText4.setPreferredSize(new Dimension(50,30));
            panel.add(IDText4);

            JButton checkButton = new JButton("确认");
            checkButton.addActionListener(e -> {
                conn = new DBConnector();

                System.out.println(IDText4.getText());
            });
            checkButton.setPreferredSize(new Dimension(60,30));
            checkButton.setContentAreaFilled(false);
            checkButton.setFont(font);
            panel.add(checkButton);
        }
        return panel;
    }

    /**
     * 为删除操作的选项卡生成tabbed标签内的面板
     * @param mode：身份码
     * @return
     */
    protected JComponent makeDropInfoPanel(int mode)
    {
        JPanel panel=new JPanel(false);
        panel.setPreferredSize(new Dimension(500,400) );
        panel.setLayout(new FlowLayout(FlowLayout.CENTER,8,10));
        if(mode==1) {
            JLabel studentIDLabel5 = new JLabel("学号");
            studentIDLabel5.setFont(font);
            studentIDLabel5.setPreferredSize(new Dimension(50,30));
            panel.add(studentIDLabel5);

            JTextField studentIDText5 = new JTextField("");
            studentIDText5.setFont(font);
            studentIDText5.setPreferredSize(new Dimension(50,30));
            panel.add(studentIDText5);

            JButton checkButton = new JButton("确认");
            checkButton.addActionListener(e -> {
                conn = new DBConnector();
                System.out.println(studentIDText5.getText());
                int_args = new String[1];
                int_args[0] = studentIDText5.getText();
                try {
                    conn.delete("学生",int_args);
                } catch (CustomException ex) {
                    ex.printStackTrace();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });
            checkButton.setPreferredSize(new Dimension(60,30));
            checkButton.setContentAreaFilled(false);
            checkButton.setFont(font);
            panel.add(checkButton);
        }
        else if(mode==2)
        {
            JLabel teacherIDLabel5 = new JLabel("教师工号");
            teacherIDLabel5.setFont(font);
            teacherIDLabel5.setPreferredSize(new Dimension(50,30));
            panel.add(teacherIDLabel5);

            JTextField teacherIDText5 = new JTextField("");
            teacherIDText5.setFont(font);
            teacherIDText5.setPreferredSize(new Dimension(50,30));
            panel.add(teacherIDText5);

            JButton checkButton = new JButton("确认");
            checkButton.addActionListener(e -> {
                conn = new DBConnector();
                System.out.println(teacherIDText5.getText());
                int_args = new String[1];
                int_args[0] = teacherIDText5.getText();
                try {
                    conn.delete("教师",int_args);
                } catch (CustomException ex) {
                    ex.printStackTrace();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

            });
            checkButton.setPreferredSize(new Dimension(60,30));
            checkButton.setContentAreaFilled(false);
            checkButton.setFont(font);
            panel.add(checkButton);
        }
        else if(mode==3)
        {
            JLabel courseLabel = new JLabel("课号");
            courseLabel.setFont(font);
            courseLabel.setPreferredSize(new Dimension(50,30));
            panel.add(courseLabel);

            JTextField courseText = new JTextField("");
            courseText.setFont(font);
            courseText.setPreferredSize(new Dimension(50,30));
            panel.add(courseText);

            JLabel teacherIDLabel6 = new JLabel("教师工号");
            teacherIDLabel6.setFont(font);
            teacherIDLabel6.setPreferredSize(new Dimension(50,30));
            panel.add(teacherIDLabel6);

            JTextField teacherIDText6 = new JTextField("");
            teacherIDText6.setFont(font);
            teacherIDText6.setPreferredSize(new Dimension(50,30));
            panel.add(teacherIDText6);



            JButton checkButton = new JButton("确认");
            checkButton.addActionListener(e -> {
                conn = new DBConnector();
                System.out.println(courseText.getText()+teacherIDText6.getText());
                int_args = new String[2];
                int_args[0] = courseText.getText();
                int_args[1] = teacherIDText6.getText();
                try {
                    conn.delete("班级",int_args);
                } catch (CustomException ex) {
                    ex.printStackTrace();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

            });
            checkButton.setPreferredSize(new Dimension(60,30));
            checkButton.setContentAreaFilled(false);
            checkButton.setFont(font);
            panel.add(checkButton);
        }
        else if(mode==4)
        {
            JLabel courseLabel = new JLabel("课号");
            courseLabel.setFont(font);
            courseLabel.setPreferredSize(new Dimension(50,30));
            panel.add(courseLabel);

            JTextField courseText = new JTextField("");
            courseText.setFont(font);
            courseText.setPreferredSize(new Dimension(50,30));
            panel.add(courseText);

            JLabel stuIDLabel6 = new JLabel("学号");
            stuIDLabel6.setFont(font);
            stuIDLabel6.setPreferredSize(new Dimension(50,30));
            panel.add(stuIDLabel6);

            JTextField stuIDText6 = new JTextField("");
            stuIDText6.setFont(font);
            stuIDText6.setPreferredSize(new Dimension(50,30));
            panel.add(stuIDText6);



            JButton checkButton = new JButton("确认");
            checkButton.addActionListener(e -> {
                conn = new DBConnector();
                System.out.println(courseText.getText()+stuIDText6.getText());
                int_args = new String[2];
                int_args[0] = courseText.getText();
                int_args[1] = stuIDText6.getText();
                try {
                    conn.delete("选课",int_args);
                } catch (CustomException ex) {
                    ex.printStackTrace();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

            });
            checkButton.setPreferredSize(new Dimension(60,30));
            checkButton.setContentAreaFilled(false);
            checkButton.setFont(font);
            panel.add(checkButton);
        }

        return panel;
    }


    /**
     * @function : 构造课程查询表格。
     */
    private JTable getCourseQueryInfo(String parseInt, String parseInt1, String parseInt2, String text, String toString, String text1) {
        int_args = new String[3];
        str_args = new String[3];
        int_args[0] = ""+parseInt;
        int_args[1] = ""+parseInt1;
        int_args[2] = ""+parseInt2;
        str_args[0] = text;
        str_args[1] = toString;
        str_args[2] = text1;
        conn = new DBConnector();
        Vector<Object> addtional = new Vector();
        try {
            data = conn.search("管理员课程查询", int_args, str_args, addtional);
        } catch (CustomException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String[] col_name = {"课号", "课名", "学分", "工号", "学期", "上课时间"};
        JTable table = new JTable(data, col_name);
//        System.out.println(data.toString());
        return table;
    }


    /**
     * @function : 构造学生查询表格。
     */
    public JTable getStudentQueryInfo(String studentID, String studentAdmissionDate, String studentName, String studentGender, String studentFaculty) {
        int_args = new String[2];
        str_args = new String[3];
        int_args[0] = ""+studentID;
        int_args[1] = ""+studentAdmissionDate;
        str_args[0] = studentName;
        str_args[1] = studentGender;
        str_args[2] = studentFaculty;
        conn = new DBConnector();
        Vector<Object> addtional = new Vector();
        try {
            data = conn.search("管理员学生查询", int_args, str_args, addtional);
        } catch (CustomException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String[] col_name = {"学号", "学生姓名", "性别", "专业", "入学日期"};
        JTable table = new JTable(data, col_name);
//        System.out.println(data.toString());
        return table;
    }

    /**
     * @function : 构造教师查询表格。
     */
    public JTable getTeacherQueryInfo(String teacherID, String teacherName, String teacherFaculty) {
        int_args = new String[1];
        str_args = new String[2];
        int_args[0] = ""+teacherID;
        str_args[0] = teacherName;
        str_args[1] = teacherFaculty;
        conn = new DBConnector();
        Vector<Object> addtional = new Vector();
        try {
            data = conn.search("管理员教师查询", int_args, str_args, addtional);
        } catch (CustomException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String[] col_name = {"教师姓名", "教师工号", "教师院系"};
        JTable table = new JTable(data, col_name);
//        System.out.println(data.toString());
        return table;
    }

    /**
     * @function : 构造成绩查询表格。
     */
    public JTable getJTable(String teacherID, String courseID) {
        int_args = new String[2];
        str_args = new String[0];
        int_args[0] = ""+teacherID;
        int_args[1] = ""+courseID;
        conn = new DBConnector();
        Vector<Object> addtional = new Vector();
        try {
            tableData = conn.search("管理员班级成绩查询", int_args, str_args, addtional);
        } catch (CustomException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String[] col_name = {"学号", "学生姓名", "平时成绩", "考试成绩", "绩点"};
        JTable cjtable = new JTable(tableData, col_name);
        System.out.println(tableData.toString());
        return cjtable;
    }

    @Override
    public void doExit() {
        //使用安全退出时，最好将下面注释的代码将页面默认关闭设置为（DO_NOTHING_ON_CLOSE）什么都不做
        //经测试，下面的代码不修改也可以，好耶ovo
        // this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        int n = JOptionPane.showConfirmDialog(null, "您确定要退出吗？"
                , "退出提示", JOptionPane.YES_NO_OPTION);
        //取消选择是-1，确定退出是0，取消是1
        System.out.println(n);
        if (n == 0) {
            //关闭当前界面，并不是推出整个程序。
            dispose();
            //返回登陆页面
            JFrame frame = new CjFrame("成绩管理系统登录界面");

        }
    }
}
