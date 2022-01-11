package cjdemo;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

import jdbctest.DBConnector;
import jdbctest.CustomException;

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
    JPanel panel_show;

    /**
     * @param ID: 根据学生ID来创建学生页面
     * @throws SQLException
     * @throws CustomException
     */
    public  stuFrame(int ID) throws SQLException, CustomException {
        id = ID;

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
         * @function: 创建显示欢迎学生的窗格。
         */
        JPanel  stuPanel = new JPanel();
        stuPanel.setPreferredSize(new Dimension(700,80));
        stuPanel.setLayout(new FlowLayout(FlowLayout.RIGHT,10,5));
        this.add(stuPanel,"North");

        JLabel stuInfo = new JLabel();
        stuInfo.setPreferredSize(new Dimension(120,60));
        Font font = new Font("Dialog",1,12);
        Font labelFont = new Font("Dialog",1,14);
        stuInfo.setText("欢迎您："+this.id);
        stuInfo.setFont(labelFont);
        stuPanel.add(stuInfo);

        JButton exitButton = new JButton("安全退出");
        exitButton.setPreferredSize(new Dimension(100,60));
        exitButton.setFont(font);
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
        cjtable.setSize(700,300);
//        cjtable.setPreferredSize(new Dimension(700,300));
        // 将表格设置为不可编辑
        cjtable.setEnabled(false);

        /**
         *  @function: 创建成绩显示面板,显示学生成绩
         */
//        scrollPane = new JScrollPane(cjtable);
//        scrollPane.setPreferredSize(new Dimension(800,700));
//        scrollPane.setVisible(true);
//        this.add(scrollPane,"East");
        panel_show = new JPanel();
        panel_show.setLayout(new FlowLayout(FlowLayout.RIGHT,0,0));
        panel_show.setPreferredSize(new Dimension(500,700));
        this.add(panel_show,"East");
        panel_show.add(new JScrollPane(cjtable));
        panel_show.setVisible(false);


        /**
         *  @function: 创建功能栏面板panel_function,包含成绩查询按钮button组件
         */
        JPanel panel_function = new JPanel();
        panel_function.setLayout(new FlowLayout(FlowLayout.LEFT,5,5));
        panel_function.setPreferredSize(new Dimension(100,700));
        this.add(panel_function,"Center");


        JButton button = new JButton("成绩查询");
        button.setFont(font);
        button.setPreferredSize(new Dimension(100,60));

        /**
         * @function: 为【成绩查询】按钮创建监听器，点击后成绩显示窗格状态改为【显示】
         * 函数参数采用lambda表达式
         */
        button.addActionListener((e) -> {
            panel_show.setVisible(true);
        });
        //将【成绩查询】按钮组件添加到功能栏面板中
        panel_function.add(button);

    }

}