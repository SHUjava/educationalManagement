package cjdemo;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import jdbctest.DBConnector;
import jdbctest.CustomException;
import jdbctest.Export;

/**
 * @description : 显示学生界面，包含成绩查询功能
 * @Content : 组件内容：1.Jtable类型的成绩表格cjtable
 *                    2.JPanel类型的学生信息面板stuPanel
 *                    2.JPanel类型的功能面板panel_function
 *                    3.JPanel类型的成绩表格显示面板panel_show
 *
 */
public class stuFrame extends JFrame {
    int id;
    String name;
    JPanel panel_show;

    /**
     * @param ID: 根据学生ID来创建学生页面
     * @throws SQLException
     * @throws CustomException
     */
    public  stuFrame(int ID,String name) throws SQLException, CustomException {
        //初始化ID，NAME
        this.id = ID;
        this.name = name;
        ImageIcon imageIcon = new ImageIcon("image/SHU_LOGO.png");
        this.setIconImage(imageIcon.getImage().getScaledInstance(100,140,100));
        this.setResizable(false);
        // 调用setBounds方法调整页面的左上角坐标以及页面的大小
//        this.setBounds(100, 100, 1000, 800);
        this.setTitle(this.id+"学生成绩管理页面");
        // 将右上角的[x]与退出程序相关联
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        this.setSize(700,600);
        this.setLocation(100,100);
        this.setLayout(new BorderLayout(5,20));
        ((JPanel)this.getContentPane()).setBorder(BorderFactory.createEmptyBorder(5,10,10,10));

        /**
         * @function: 创建显示学生基础信息的窗格。
         */
        JPanel  stuPanel = new JPanel();
        stuPanel.setPreferredSize(new Dimension(700,80));
        stuPanel.setLayout(new FlowLayout(FlowLayout.RIGHT,3,5));
        this.add(stuPanel,"North");

        JLabel stuInfo = new JLabel();
        stuInfo.setPreferredSize(new Dimension(120,60));
        Font font = new Font("Dialog",1,12);
        Font labelFont = new Font("Dialog",1,12);
        stuInfo.setText("欢迎你："+this.name);
        stuInfo.setFont(labelFont);
        stuPanel.add(stuInfo);

        JButton changePW = new JButton("修改密码");
        changePW.setPreferredSize(new Dimension(100,30));
        changePW.setFont(font);
        changePW.setContentAreaFilled(false);
        stuPanel.add(changePW);

        JButton exitButton = new JButton("安全退出");
        exitButton.setPreferredSize(new Dimension(100,30));
        exitButton.setFont(font);
        exitButton.setContentAreaFilled(false);
        stuPanel.add(exitButton);


        int[] int_args = new int[2];
        String[] str_args = new String[0];
        int_args[0] = 2001001;
        int_args[1] = 3001001;
        DBConnector conn = new DBConnector();
        Object[][] tableData;
        try {
            tableData = conn.search("课程成绩查询", int_args, str_args);
            System.out.println(tableData);
        } catch (CustomException e) {
            System.out.println(e.getMessage());
            return;
        }
//        System.out.println(tableData.length);
        String[] col_name = {"course_order", "course_name", "teacher_id", "course_time", "course_credit", "score","score1"};

        /**
         * @function： 创建成绩显示表格cjtable
         */
        JTable cjtable = new JTable(tableData, col_name);
//        cjtable.setBounds(0,0,700,300);
        cjtable.setSize(550,300);
//        cjtable.setPreferredSize(new Dimension(700,300));
        // 将表格设置为不可编辑
        cjtable.setEnabled(false);

        /**
         *  @function: 创建成绩显示面板,显示学生成绩以及导出等按钮
         */
//        scrollPane = new JScrollPane(cjtable);
//        scrollPane.setPreferredSize(new Dimension(800,700));
//        scrollPane.setVisible(true);
//        this.add(scrollPane,"East");
        panel_show = new JPanel();
        panel_show.setLayout(new FlowLayout(FlowLayout.RIGHT,5,0));
        panel_show.setPreferredSize(new Dimension(550,500));

        /**
         *  @function: 在成绩显示面板上部增加导出按钮 by yjh
         */
        JPanel panel_export = new JPanel();
        panel_export.setLayout(new FlowLayout(FlowLayout.RIGHT,0,0));
        panel_export.setPreferredSize(new Dimension(550,50));
        panel_show.add(panel_export);
        // 以下三行是一个简单的Export类调用的样例 for 张宝
        Export export = new Export(cjtable);
        JButton buttonExport = export.getButtonExport();
        panel_export.add(buttonExport);

        JButton buttonPrint = new JButton("打印");
        buttonPrint.setContentAreaFilled(false);
        buttonPrint.setFont(new Font("Dialog",1,12));
        buttonPrint.setPreferredSize(new Dimension(70,30));

        panel_export.add(buttonPrint);

        this.add(panel_show,"East");
        panel_show.add(new JScrollPane(cjtable));
        panel_show.setVisible(false);


        /**
         *  @function: 创建功能栏面板panel_function,包含成绩查询按钮button组件
         */
        JPanel panel_function = new JPanel();
        panel_function.setLayout(new FlowLayout(FlowLayout.LEFT,5,5));
        panel_function.setPreferredSize(new Dimension(100,500));
        this.add(panel_function,"Center");


        JButton buttonQuery = new JButton("成绩查询");
        buttonQuery.setContentAreaFilled(false);
        buttonQuery.setFont(font);
        buttonQuery.setPreferredSize(new Dimension(90,30));

        /**
         * @function: 为【成绩查询】按钮创建监听器，点击后成绩显示窗格状态改为【显示】
         * 函数参数采用lambda表达式
         */
        buttonQuery.addActionListener((e) -> {
            panel_show.setVisible(true);
        });
        //将【成绩查询】按钮组件添加到功能栏面板中
        panel_function.add(buttonQuery);

    }

}