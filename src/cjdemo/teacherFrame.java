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

    /**
     * @param id: 根据教师ID来创建学生页面
     * @throws SQLException
     * @throws CustomException
     */

    public teacherFrame(int id,String name) throws SQLException, CustomException {
        this.id = id;
        this.name = name;
        this.setTitle(id+"教师成绩管理页面");

        //添加图标校徽
        ImageIcon imageIcon = new ImageIcon("image/SHU_LOGO.png");
        this.setIconImage(imageIcon.getImage().getScaledInstance(100,140,100));
        this.setBounds(100,100,1000,800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        this.setSize(700,600);
        this.setLocation(100,100);
        //采用Border布局，水平间距50，垂直间距5
        this.setLayout(new BorderLayout(5,20));
        ((JPanel)this.getContentPane()).setBorder(BorderFactory.createEmptyBorder(5,10,10,10));

        /**
         * @function: 创建显示老师基础信息的面板teacherPanel.
         */
        JPanel teacherPanel = new JPanel();
        teacherPanel.setPreferredSize(new Dimension(700,80));
        teacherPanel.setLayout(new FlowLayout(FlowLayout.RIGHT,0,0));
        /**
         * @function: 创建欢迎老师的标签welcomeTeacherLabel.
         */
        JLabel welcomeTeacherLabel = new JLabel("欢迎您: "+this.name);
        welcomeTeacherLabel.setPreferredSize(new Dimension(120,60));
        Font font = new Font("Dialog",1,12);
        welcomeTeacherLabel.setFont(font);
        //将欢迎老师的信息添加到老师信息面板中
        teacherPanel.add(welcomeTeacherLabel);
        //将老师信息面板添加到主面板中
        this.add(teacherPanel,"North");

        /**
         * @function: 创建修改密码的按钮changePWButton
         */
        JButton changePWButton = new JButton("修改密码");
        changePWButton.setPreferredSize(new Dimension(100,30));
        changePWButton.setFont(font);
        changePWButton.setContentAreaFilled(false);
        teacherPanel.add(changePWButton);

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
        exitButton.setPreferredSize(new Dimension(100,30));
        exitButton.setFont(font);
        exitButton.setContentAreaFilled(false);
        teacherPanel.add(exitButton);

        /**
         * @function: 创建教师功能面板functionPanel
         */
        JPanel functionPanel = new JPanel();
        functionPanel.setPreferredSize(new Dimension(100,500));
        functionPanel.setLayout(new FlowLayout(FlowLayout.CENTER,5,10));
        this.add(functionPanel,"West");

        /**
         * @function: 在functionPanel中创建并添加录入成绩按钮enterGradeButton.
         */
        JButton enterGradeButton = new JButton("录入成绩");
        enterGradeButton.setFont(font);
        enterGradeButton.setContentAreaFilled(false);
        enterGradeButton.setPreferredSize(new Dimension(90,30));
        functionPanel.add(enterGradeButton);

        /**
         * @function: 在functionPanel中创建并添加成绩查询按钮queryGradeButton.
         */
        JButton queryGradeButton = new JButton("成绩查询");
        queryGradeButton.setPreferredSize(new Dimension(90,30));
        queryGradeButton.setFont(font);
        queryGradeButton.setContentAreaFilled(false);
        functionPanel.add(queryGradeButton);

        /**
         * @function: 在functionPanel中创建并添加修改成绩按钮changeGradeButton
         */
        JButton changeGradeButton = new JButton("修改成绩");
        changeGradeButton.setPreferredSize(new Dimension(90,30));
        changeGradeButton.setFont(font);
        changeGradeButton.setContentAreaFilled(false);
        functionPanel.add(changeGradeButton);


    }

    @Override
    public void doExit() {
        //使用安全退出时，最好将下面注释的代码将页面默认关闭设置为（DO_NOTHING_ON_CLOSE）什么都不做
        //经测试，下面的代码不修改也可以，好耶ovo
        // this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        int n = JOptionPane.showConfirmDialog(null,"您确定要退出吗？"
                ,"退出提示",JOptionPane.YES_NO_OPTION);
        //取消选择是-1，确定退出是0，取消是1
        System.out.println(n);
        if (n==0){
            //关闭当前界面，并不是推出整个程序。
            dispose();
            //返回登陆页面
            JFrame frame = new CjFrame("成绩管理系统登录界面");

        }

    }
}
