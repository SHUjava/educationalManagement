package cjdemo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import jdbctest.CustomException;

public class teacherFrame extends JFrame implements Exit {
    int id;
    String name;
    JPanel panel_show;
    JPanel showPanel;
    JPanel showEnterGradePanel, showQueryGradePanel, showChangeGradePanel;
    JTextField classText1,classText2,classText3, courseNameText1,courseNameText2,courseNameText3,stuIDText1,stuIDText2;
    JButton checkEnterInfoButton;
    JLabel courseNameLabel,classLabel,stuIDLabel;

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
        Font font = new Font("Dialog", 1, 12);
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

        classLabel = new JLabel("班级");
        classLabel.setFont(font);
        classLabel.setPreferredSize(new Dimension(35, 30));
        showEnterGradePanel.add(classLabel);

        classText1 = new JTextField("");
        classText1.setFont(font);
        classText1.setPreferredSize(new Dimension(80, 30));
        showEnterGradePanel.add(classText1);

        checkEnterInfoButton = new JButton("确认");
        checkEnterInfoButton.setFont(font);
        checkEnterInfoButton.setPreferredSize(new Dimension(100, 30));
        checkEnterInfoButton.setContentAreaFilled(false);
        checkEnterInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(1);
                System.out.println("用户输入的课程名称为：" + courseNameText1.getText() + " 用户输入的班级为：" + classText1.getText());
            }
        });
        showEnterGradePanel.add(checkEnterInfoButton);

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
        courseNameLabel = new JLabel("课程名");
        courseNameLabel.setFont(font);
        courseNameLabel.setPreferredSize(new Dimension(45, 30));
        showQueryGradePanel.add(courseNameLabel);

        courseNameText2 = new JTextField("");
        courseNameText2.setFont(font);
        courseNameText2.setPreferredSize(new Dimension(60, 30));
        showQueryGradePanel.add(courseNameText2);

        classLabel = new JLabel("班级");
        classLabel.setFont(font);
        classLabel.setPreferredSize(new Dimension(35, 30));
        showQueryGradePanel.add(classLabel);

        classText2 = new JTextField("");
        classText2.setFont(font);
        classText2.setPreferredSize(new Dimension(80, 30));
        showQueryGradePanel.add(classText2);

        stuIDLabel = new JLabel("学号");
        stuIDLabel.setFont(font);
        stuIDLabel.setPreferredSize(new Dimension(35, 30));
        showQueryGradePanel.add(stuIDLabel);

        stuIDText1 = new JTextField("");
        stuIDText1.setFont(font);
        stuIDText1.setPreferredSize(new Dimension(80, 30));
        showQueryGradePanel.add(stuIDText1);

        checkEnterInfoButton= new JButton("确认");
        checkEnterInfoButton.setFont(font);
        checkEnterInfoButton.setPreferredSize(new Dimension(100, 30));
        checkEnterInfoButton.setContentAreaFilled(false);
        checkEnterInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(2);
                System.out.println("用户输入的课程名称为：" + courseNameText2.getText() + " 用户输入的班级为：" + classText2.getText()
                        +" 用户输入的学号为："+stuIDText1.getText());
            }
        });
        showQueryGradePanel.add(checkEnterInfoButton);

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
        showChangeGradePanel.setPreferredSize(new Dimension(550, 500));
        showChangeGradePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10));
        //默认不显示
        showChangeGradePanel.setVisible(false);
        showPanel.add(showChangeGradePanel, "East");

        /**
         * @function: 在修改成绩的显示面板中添加搜索筛选文本框信息
         * courseNameLabel:课程名标签，classLabel:班级标签，stuIDLabel:学号标签；
         * courseNameText:文本输入框接收用户输入的课程名，classText:班级，stuIDText:学号；
         */
        courseNameLabel = new JLabel("课程名");
        courseNameLabel.setFont(font);
        courseNameLabel.setPreferredSize(new Dimension(45, 30));
        showChangeGradePanel.add(courseNameLabel);

        courseNameText3 = new JTextField("");
        courseNameText3.setFont(font);
        courseNameText3.setPreferredSize(new Dimension(60, 30));
        showChangeGradePanel.add(courseNameText3);

        classLabel = new JLabel("班级");
        classLabel.setFont(font);
        classLabel.setPreferredSize(new Dimension(35, 30));
        showChangeGradePanel.add(classLabel);

        classText3 = new JTextField("");
        classText3.setFont(font);
        classText3.setPreferredSize(new Dimension(80, 30));
        showChangeGradePanel.add(classText3);

        stuIDLabel = new JLabel("学号");
        stuIDLabel.setFont(font);
        stuIDLabel.setPreferredSize(new Dimension(35, 30));
        showChangeGradePanel.add(stuIDLabel);

        stuIDText2 = new JTextField("");
        stuIDText2.setFont(font);
        stuIDText2.setPreferredSize(new Dimension(80, 30));
        showChangeGradePanel.add(stuIDText2);

        checkEnterInfoButton= new JButton("确认");
        checkEnterInfoButton.setFont(font);
        checkEnterInfoButton.setPreferredSize(new Dimension(100, 30));
        checkEnterInfoButton.setContentAreaFilled(false);
        checkEnterInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(3);
                System.out.println("用户输入的课程名称为：" + courseNameText3.getText() + " 用户输入的班级为：" + classText3.getText()
                        +" 用户输入的学号为："+stuIDText2.getText());
            }
        });
        showChangeGradePanel.add(checkEnterInfoButton);

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
