package cjdemo;

import jdbctest.CustomException;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class teacherFrame extends JFrame {
    int id;
    String name;
    JPanel panel_show;

    /**
     * @param id : 根据教师ID创建教师页面
     * @throws SQLException
     * @throws CustomException
     */
    public teacherFrame(int id,String name) throws SQLException, CustomException {
        this.setTitle(id+"教师成绩管理页面");
        ImageIcon imageIcon = new ImageIcon("image/SHU_LOGO.png");
        this.setIconImage(imageIcon.getImage().getScaledInstance(100,140,100));
//        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        this.setSize(700,600);
        this.setLocation(100,100);
        //采用Border布局，水平间距50，垂直间距5
        this.setLayout(new BorderLayout(5,20));
        ((JPanel)this.getContentPane()).setBorder(BorderFactory.createEmptyBorder(5,10,10,10));

        /**
         * @function: 创建显示欢迎老师的窗格。
         */
        JPanel welcomeTeacherPanel = new JPanel();
        welcomeTeacherPanel.setPreferredSize(new Dimension(700,80));



    }
}
