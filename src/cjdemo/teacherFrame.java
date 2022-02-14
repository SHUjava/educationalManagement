package cjdemo;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Vector;

import jdbctest.CustomException;
import jdbctest.DBConnector;
import org.jfree.chart.ChartPanel;

public class teacherFrame extends JFrame implements Exit {
    int id;
    String name;
    JPanel panel_show;
    JPanel showPanel;
    Font font;
    Object[][] data ;
    JPanel showEnterGradePanel, showQueryGradePanel, showChangeGradePanel,showGradeAnalyzePanel,tablePanel;
    JTabbedPane showAnalyzePanel;
    JTextField classText1,courseNameText4,timeText2,timeText1,classText2,timeText, courseNameText1,courseNameText2,courseNameText3,stuIDText1,stuIDText2,newGradeText,tIDText;
    JButton checkEnterInfoButton1,checkEnterInfoButton2,checkEnterInfoButton3,checkEnterInfoButton4;
    JComboBox semesterText,choiceComBox,semesterText1,semesterText2;
    DBConnector conn;
    String []int_args;
    int [] int_args1;
    String[] str_args;
    Object[][] tableData;
    JTable cjtable;
    JLabel courseNameLabel,timeLabel1,courseNameLabel2,timeLabel2,courseNameLabel3,semesterLabel,semesterLabel2,semesterLabel1,timeLabel,stuIDLabel,choiceLabel,tIDLabel,newGradeLabel;

    /**
     * @param id: 根据教师ID来创建学生页面
     * @throws SQLException
     * @throws CustomException
     */

    public teacherFrame(int id, String name) throws SQLException, CustomException {
        this.id = id;
        this.name = name;
        this.setTitle(id + "教师成绩管理页面");

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
         * @function: 创建显示老师基础信息的面板teacherPanel.
         */
        JPanel teacherPanel = new JPanel();
        teacherPanel.setPreferredSize(new Dimension(700, 80));
        teacherPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        /**
         * @function: 创建欢迎老师的标签welcomeTeacherLabel.
         */
        JLabel welcomeTeacherLabel = new JLabel("欢迎您: " + this.name);
        welcomeTeacherLabel.setPreferredSize(new Dimension(120, 60));
        font = new Font("Dialog", 1, 12);
        welcomeTeacherLabel.setFont(font);
        //将欢迎老师的信息添加到老师信息面板中
        teacherPanel.add(welcomeTeacherLabel);
        //将老师信息面板添加到主面板中
        this.add(teacherPanel, "North");

        /**
         * @function: 创建修改密码的按钮changePWButton
         */
        JButton changePWButton = new JButton("修改密码");
        changePWButton.setPreferredSize(new Dimension(100, 30));
        changePWButton.setFont(font);
        changePWButton.setContentAreaFilled(false);
        teacherPanel.add(changePWButton);
        changePWButton.addActionListener(e->{
            JFrame frame = new changePWFrame(0,this.id,this);
//            dispose();
        });

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
        teacherPanel.add(exitButton);

        /**
         * @function: 创建教师功能面板functionPanel
         */
        JPanel functionPanel = new JPanel();
        functionPanel.setPreferredSize(new Dimension(100, 500));
        functionPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10));
        this.add(functionPanel, "West");

        /**
         * @function: 创建教师显示面板showPanel
         */
        showPanel = new JPanel();
        showPanel.setPreferredSize(new Dimension(500,500));
        showPanel.setLayout(new FlowLayout(FlowLayout.CENTER,5,10));
        this.add(showPanel,"East");

        /**
         * @function: 在functionPanel中创建并添加【录入成绩】按钮enterGradeButton.
         * 为【录入成绩】按钮添加监听器，实现录入成绩功能
         */
        JButton enterGradeButton = new JButton("录入成绩");
        enterGradeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showEnterGradePanel.setVisible(true);
                showPanel.removeAll();
                showPanel.add(showEnterGradePanel);
                showPanel.validate();
                showPanel.repaint();

            }
        });
        enterGradeButton.setFont(font);
        enterGradeButton.setContentAreaFilled(false);
        enterGradeButton.setPreferredSize(new Dimension(90, 30));
        functionPanel.add(enterGradeButton);

        /**
         * @function: 录入成绩后的显示面板
         * 默认为不显示，当点击【录入成绩】按钮时显示
         */
        showEnterGradePanel = new JPanel();
        showEnterGradePanel.setPreferredSize(new Dimension(500, 500));
        showEnterGradePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10));
        showEnterGradePanel.setVisible(false);
        showPanel.add(showEnterGradePanel, "East");

        /**
         * @function: 在录入成绩的显示面板中添加搜索筛选文本框信息
         * courseNameLabel:课程名标签；
         * courseNameText:文本输入框接收用户输入的课程名；
         */
        courseNameLabel = new JLabel("课程名");
        courseNameLabel.setFont(font);
        courseNameLabel.setPreferredSize(new Dimension(45, 30));
        showEnterGradePanel.add(courseNameLabel);

        courseNameText1 = new JTextField("");
        courseNameText1.setFont(font);
        courseNameText1.setPreferredSize(new Dimension(60, 30));
        showEnterGradePanel.add(courseNameText1);

//        classLabel = new JLabel("班级");
//        classLabel.setFont(font);
//        classLabel.setPreferredSize(new Dimension(35, 30));
//        showEnterGradePanel.add(classLabel);

        classText1 = new JTextField("");
        classText1.setFont(font);
        classText1.setPreferredSize(new Dimension(80, 30));
        showEnterGradePanel.add(classText1);

        checkEnterInfoButton1 = new JButton("确认");
        checkEnterInfoButton1.setFont(font);
        checkEnterInfoButton1.setPreferredSize(new Dimension(100, 30));
        checkEnterInfoButton1.setContentAreaFilled(false);
        checkEnterInfoButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(1);
                System.out.println("用户输入的课程名称为：" + courseNameText1.getText() + " 用户输入的班级为：" + classText1.getText());
            }
        });
        showEnterGradePanel.add(checkEnterInfoButton1);

        //以下为显示录入成绩的表格，暂时未详细查看db中相关代码，陆续会更新


        /**
         * @function: 在functionPanel中创建并添加【成绩查询】按钮queryGradeButton.
         * 为【成绩查询】按钮添加监听器，实现成绩查询功能
         */
        JButton queryGradeButton = new JButton("成绩查询");
        queryGradeButton.setPreferredSize(new Dimension(90, 30));
        queryGradeButton.setFont(font);
        queryGradeButton.setContentAreaFilled(false);
        functionPanel.add(queryGradeButton);
        queryGradeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showQueryGradePanel.setVisible(true);
                showPanel.removeAll();
                showPanel.add(showQueryGradePanel);
                showPanel.validate();
                showPanel.repaint();

            }
        });

        /**
         * @function: 成绩查询后的显示面板
         * 默认为不显示，当点击【成绩查询】按钮时显示
         */
        showQueryGradePanel = new JPanel();
        showQueryGradePanel.setPreferredSize(new Dimension(550, 500));
        showQueryGradePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10));
        //默认不显示
        showQueryGradePanel.setVisible(false);
        showPanel.add(showQueryGradePanel, "East");

        /**
         * @function: 在成绩查询的显示面板中添加搜索筛选文本框信息
         * courseNameLabel:课程名标签，classLabel:班级标签，stuIDLabel:学号标签；
         * courseNameText:文本输入框接收用户输入的课程名，classText:班级，stuIDText:学号；
         */
        courseNameLabel2 = new JLabel("课程名");
        courseNameLabel2.setFont(font);
        courseNameLabel2.setPreferredSize(new Dimension(45, 30));
        showQueryGradePanel.add(courseNameLabel2);

        courseNameText2 = new JTextField("");
        courseNameText2.setFont(font);
        courseNameText2.setPreferredSize(new Dimension(80, 30));
        showQueryGradePanel.add(courseNameText2);

        semesterLabel1 = new JLabel("学期选择");
        semesterLabel1.setFont(font);
        semesterLabel1.setPreferredSize(new Dimension(60, 30));
        showQueryGradePanel.add(semesterLabel1);

        semesterText1 = new JComboBox();
        semesterText1.setSelectedItem("2019-2020秋季");
        semesterText1.addItem("2019-2020秋季");
        semesterText1.addItem("2019-2020冬季");
        semesterText1.addItem("2019-2020春季");
        semesterText1.addItem("2020-2021秋季");
        semesterText1.addItem("2020-2021冬季");
        semesterText1.addItem("2020-2021春季");
        semesterText1.setFont(font);
        semesterText1.setPreferredSize(new Dimension(110, 30));
        showQueryGradePanel.add(semesterText1);

        timeLabel1 = new JLabel("上课时间");
        timeLabel1.setFont(font);
        timeLabel1.setPreferredSize(new Dimension(60, 30));
        showQueryGradePanel.add(timeLabel1);

        timeText1 = new JTextField("");
        timeText1.setFont(font);
        timeText1.setPreferredSize(new Dimension(80, 30));
        showQueryGradePanel.add(timeText1);

        checkEnterInfoButton2= new JButton("确认");
        checkEnterInfoButton2.setFont(font);
        checkEnterInfoButton2.setPreferredSize(new Dimension(100, 30));
        checkEnterInfoButton2.setContentAreaFilled(false);
        checkEnterInfoButton2.addActionListener(e -> {
            cjtable = getJTable(courseNameText2.getText(),semesterText1.getSelectedItem().toString(),timeText1.getText());
            DefaultTableCellRenderer r = new DefaultTableCellRenderer();
            r.setHorizontalAlignment(JLabel.CENTER);
            cjtable.setDefaultRenderer(Object.class, r);
            JPanel panel_export = new JPanel();
            panel_export.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            panel_export.setPreferredSize(new Dimension(500, 50));
            Export export = new Export(cjtable);
            JButton buttonExport = export.getButtonExport();
            panel_export.add(buttonExport);
            Print print = new Print(cjtable, this, this.name+"  "+semesterText1.getSelectedItem().toString()+"  "+courseNameText2.getText());
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

        });
        showQueryGradePanel.add(checkEnterInfoButton2);
        tablePanel = new JPanel();
        tablePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        tablePanel.setPreferredSize(new Dimension(500, 300));
        showQueryGradePanel.add(tablePanel);

        /**
         * @function: 在functionPanel中创建并添加修改成绩按钮changeGradeButton
         * 为【修改成绩】按钮添加监听器，实现相关功能
         */
        JButton changeGradeButton = new JButton("修改成绩");
        changeGradeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showChangeGradePanel.setVisible(true);
                showPanel.removeAll();
                showPanel.add(showChangeGradePanel);
                showPanel.validate();
                showPanel.repaint();
            }
        });
        changeGradeButton.setPreferredSize(new Dimension(90, 30));
        changeGradeButton.setFont(font);
        changeGradeButton.setContentAreaFilled(false);
        functionPanel.add(changeGradeButton);


        /**
         * @function: 修改成绩后的显示面板
         * 默认为不显示，当点击【修改成绩】按钮时显示
         */
        showChangeGradePanel = new JPanel();
        showChangeGradePanel.setPreferredSize(new Dimension(500, 500));
        showChangeGradePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 10));
        //默认不显示
        showChangeGradePanel.setVisible(false);
        showPanel.add(showChangeGradePanel, "East");

        /**
         * @function: 在修改成绩的显示面板中添加搜索筛选文本框信息
         * courseNameLabel:课程名标签，classLabel:班级标签，stuIDLabel:学号标签；
         * courseNameText:文本输入框接收用户输入的课程名，classText:班级，stuIDText:学号；
         */

        stuIDLabel = new JLabel("学号");
        stuIDLabel.setFont(font);
        stuIDLabel.setPreferredSize(new Dimension(50, 30));
        showChangeGradePanel.add(stuIDLabel);

        stuIDText2 = new JTextField("");
        stuIDText2.setFont(font);
        stuIDText2.setPreferredSize(new Dimension(80, 30));
        showChangeGradePanel.add(stuIDText2);

        courseNameLabel = new JLabel("课程名");
        courseNameLabel.setFont(font);
        courseNameLabel.setPreferredSize(new Dimension(65, 30));
        showChangeGradePanel.add(courseNameLabel);

        courseNameText3 = new JTextField("");
        courseNameText3.setFont(font);
        courseNameText3.setPreferredSize(new Dimension(80, 30));
        showChangeGradePanel.add(courseNameText3);

        semesterLabel = new JLabel("课程学期");
        semesterLabel.setFont(font);
        semesterLabel.setPreferredSize(new Dimension(60, 30));
        showChangeGradePanel.add(semesterLabel);

        semesterText = new JComboBox();
        semesterText.setSelectedItem("2019-2020秋季");
        semesterText.addItem("2019-2020秋季");
        semesterText.addItem("2019-2020冬季");
        semesterText.addItem("2019-2020春季");
        semesterText.addItem("2020-2021秋季");
        semesterText.addItem("2020-2021冬季");
        semesterText.addItem("2020-2021春季");
        semesterText.setFont(font);
        semesterText.setPreferredSize(new Dimension(110, 30));
        showChangeGradePanel.add(semesterText);

        timeLabel = new JLabel("上课时间");
        timeLabel.setFont(font);
        timeLabel.setPreferredSize(new Dimension(60, 30));
        showChangeGradePanel.add(timeLabel);

        timeText = new JTextField("");
        timeText.setFont(font);
        timeText.setPreferredSize(new Dimension(60, 30));
        showChangeGradePanel.add(timeText);

        choiceLabel = new JLabel("成绩选择");
        choiceLabel.setFont(font);
        choiceLabel.setPreferredSize(new Dimension(60, 30));
        showChangeGradePanel.add(choiceLabel);

        choiceComBox = new JComboBox();
        choiceComBox.setSelectedItem("平时成绩");
        choiceComBox.addItem("平时成绩");
        choiceComBox.addItem("考试成绩");
        choiceComBox.setFont(font);
        choiceComBox.setPreferredSize(new Dimension(80, 30));
        showChangeGradePanel.add(choiceComBox);

        newGradeLabel = new JLabel("修改后成绩");
        newGradeLabel.setFont(font);
        newGradeLabel.setPreferredSize(new Dimension(70, 30));
        showChangeGradePanel.add(newGradeLabel);

        newGradeText = new JTextField("");
        newGradeText.setFont(font);
        newGradeText.setPreferredSize(new Dimension(60, 30));
        showChangeGradePanel.add(newGradeText);

        checkEnterInfoButton3= new JButton("确认");
        checkEnterInfoButton3.setFont(font);
        checkEnterInfoButton3.setPreferredSize(new Dimension(80, 30));
        checkEnterInfoButton3.setContentAreaFilled(false);
        checkEnterInfoButton3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                conn = new DBConnector();
                int_args1 = new int[]{id,Integer.parseInt(stuIDText2.getText()),
                        Integer.parseInt(newGradeText.getText()),choiceComBox.getSelectedItem().toString().equals("平时成绩") ? 0 : 1};
                str_args = new String[]{courseNameText3.getText(),semesterText.getSelectedItem().toString(),timeText.getText()};
                try {
                    if(conn.teacherScoreChange(int_args1,str_args)) {
                        JOptionPane.showMessageDialog(null, "成绩修改成功");
                    }
                } catch (CustomException ex) {
                    JOptionPane.showMessageDialog(null, "出现异常，请重新尝试");
                    ex.printStackTrace();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "出现异常，请重新尝试");
                    ex.printStackTrace();
                }

            }
        });
        showChangeGradePanel.add(checkEnterInfoButton3);

        /**
         * @function: 在functionPanel中创建并添加成绩分析按钮gradeAnalyzeButton
         * 为【成绩分析】按钮添加监听器，实现相关功能
         */
        JButton gradeAnalyzeButton = new JButton("成绩分析");
        gradeAnalyzeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showGradeAnalyzePanel.setVisible(true);
                showPanel.removeAll();
                showPanel.add(showGradeAnalyzePanel);
                showPanel.validate();
                showPanel.repaint();
            }
        });
        gradeAnalyzeButton.setFont(font);
        gradeAnalyzeButton.setContentAreaFilled(false);
        gradeAnalyzeButton.setPreferredSize(new Dimension(90,30));
        functionPanel.add(gradeAnalyzeButton);

        /**
         * @function: 录入成绩后的显示面板
         * 默认为不显示，当点击【成绩分析】按钮时显示
         */
        showGradeAnalyzePanel = new JPanel();
        showGradeAnalyzePanel.setPreferredSize(new Dimension(500, 500));
        showGradeAnalyzePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10));
        showGradeAnalyzePanel.setVisible(false);
        showPanel.add(showGradeAnalyzePanel, "East");

        courseNameLabel3 = new JLabel("课程编号");
        courseNameLabel3.setFont(font);
        courseNameLabel3.setPreferredSize(new Dimension(60, 30));
        showGradeAnalyzePanel.add(courseNameLabel3);

        courseNameText4 = new JTextField("");
        courseNameText4.setFont(font);
        courseNameText4.setPreferredSize(new Dimension(70, 30));
        showGradeAnalyzePanel.add(courseNameText4);
//
        semesterLabel2 = new JLabel("学期选择");
        semesterLabel2.setFont(font);
        semesterLabel2.setPreferredSize(new Dimension(60, 30));
        showGradeAnalyzePanel.add(semesterLabel2);

        semesterText2 = new JComboBox();
        semesterText2.setSelectedItem("2019-2020秋季");
        semesterText2.addItem("2019-2020秋季");
        semesterText2.addItem("2019-2020冬季");
        semesterText2.addItem("2019-2020春季");
        semesterText2.addItem("2020-2021秋季");
        semesterText2.addItem("2020-2021冬季");
        semesterText2.addItem("2020-2021春季");
        semesterText2.setFont(font);
        semesterText2.setPreferredSize(new Dimension(110, 30));
        showGradeAnalyzePanel.add(semesterText2);
//
//        timeLabel2 = new JLabel("上课时间");
//        timeLabel2.setFont(font);
//        timeLabel2.setPreferredSize(new Dimension(60, 30));
//        showGradeAnalyzePanel.add(timeLabel2);
//
//        timeText2 = new JTextField("");
//        timeText2.setFont(font);
//        timeText2.setPreferredSize(new Dimension(80, 30));
//        showGradeAnalyzePanel.add(timeText2);

        checkEnterInfoButton4= new JButton("确认");
        checkEnterInfoButton4.addActionListener(e -> {

            showAnalyzePanel.setVisible(true);
            showAnalyzePanel.removeAll();
            JComponent panel1 = makeAnalyzePanel(1);
            showAnalyzePanel.addTab("条形图",panel1);
            JComponent panel2 = makeAnalyzePanel(2);
            showAnalyzePanel.addTab("饼状图",panel2);
            JComponent panel3 = makeAnalyzePanel(3);
            showAnalyzePanel.addTab("文本",panel3);
            showAnalyzePanel.validate();
            showAnalyzePanel.repaint();

        });
        checkEnterInfoButton4.setFont(font);
        checkEnterInfoButton4.setPreferredSize(new Dimension(80, 30));
        checkEnterInfoButton4.setContentAreaFilled(false);
        showGradeAnalyzePanel.add(checkEnterInfoButton4);

        showAnalyzePanel = new JTabbedPane();
        showAnalyzePanel.setVisible(false);
        showAnalyzePanel.setPreferredSize(new Dimension(500,300));
        showGradeAnalyzePanel.add(showAnalyzePanel);


    }

    private JComponent makeAnalyzePanel(int i) {
        JPanel panel = new JPanel();
        conn = new DBConnector();

        if(i == 1) {
            panel = new Chart(id,semesterText2.getSelectedItem().toString(),"课程绩点分布",Integer.parseInt(courseNameText4.getText())).getChartPanel();
        }

        else if(i==2){
            Chart chart = new Chart(id,semesterText2.getSelectedItem().toString(),Integer.parseInt(courseNameText4.getText()));
//            System.out.println(0);
            panel = chart.teacherGradeAnalysis(id,courseNameText4.getText());
        }
        else if(i==3)
        {
            String[] info = new String[3];
            int_args1 = new int[]{id};
            str_args = new String[]{courseNameText4.getText()};
            try {
                data = conn.teacherGradeAnalysisText(int_args1,str_args,info);
                System.out.println(info[0]+info[1]+info[2]);
            } catch (CustomException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            JTextField gradeInfoText = new JTextField();
            gradeInfoText.setFont(font);
            gradeInfoText.setText("平均成绩："+info[0]+"\n优秀率为："+info[1]+"\n挂科率为："+info[2]);
            panel.add(gradeInfoText);
            String[] col_name = {"排名", "学生学号", "学生姓名", "平时成绩","考试成绩","成绩", "绩点"};
            JTable cjtable = new JTable(data,col_name);
            DefaultTableCellRenderer r = new DefaultTableCellRenderer();
            r.setHorizontalAlignment(JLabel.CENTER);
            cjtable.setDefaultRenderer(Object.class, r);
            JScrollPane jScrollPane = new JScrollPane(cjtable);
            jScrollPane.setPreferredSize(new Dimension(500, 300));
            panel.add(jScrollPane);
            cjtable.setPreferredSize(new Dimension(500, 300));
            cjtable.setEnabled(false);  //不可编辑
            cjtable.getTableHeader().setReorderingAllowed(false);   //不可整列移动
            cjtable.getTableHeader().setResizingAllowed(false);   //不可拉动表格


        }
        return panel;
    }


    /**
     * @function : 构造成绩查询表格。
     */
    public JTable getJTable(String cName,String semester,String time) {
        int_args = new String[1];
        int_args[0] = new String(String.valueOf(id));
        str_args = new String[]{cName,semester,time};
        conn = new DBConnector();
        Vector<Object> addtional = new Vector();
        try {
            tableData = conn.search("教师成绩查询", int_args, str_args, addtional);
        } catch (CustomException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String[] col_name = {"学号", "学生姓名", "平时成绩", "考试成绩","成绩", "绩点"};
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
