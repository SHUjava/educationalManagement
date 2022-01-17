package cjdemo;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import jdbctest.DBConnector;
import jdbctest.CustomException;
import jdbctest.Export;

public class teacherFrame extends JFrame {
    int id;
    JPanel panel_show;

    /**
     * @param ID: 根据教师ID来创建学生页面
     * @throws SQLException
     * @throws CustomException
     */
    public teacherFrame(int ID) throws SQLException, CustomException{
        this.setTitle(this.id+"教师界面");
        ImageIcon imageIcon = new ImageIcon("image/SHU_LOGO.png");
        this.setBounds(100,100,1000,800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setVisible(true);

    }
}
