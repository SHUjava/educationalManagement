package cjdemo;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

import jdbctest.DBConnector;
import jdbctest.CustomException;

public class stuFrame extends JFrame {
    int id;
    public  stuFrame(int ID) throws SQLException, CustomException {
//        super(title);
        id = ID;
        this.setBounds(100, 100, 1000, 800);
        this.setTitle("学生页面");
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(0, 0));//设置边界布局
        setContentPane(contentPane);//应用内容面板


//        this.setAlwaysOnTop(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
// 创建显示面板
        //创建表
        //jdbctest stud = new jdbctest(id);
//        Vector tmp;
//        tmp = stud.getResult();
//        Object tempp = tmp.get(0);
//        Vector temppp = (Vector)tempp;
//        int row = tmp.size();
//        int col = temppp.size();
//        Object [][] tableDate = new Object[row][col];
//        //读取数据库
//
//        for(int i=0;i<row;i++)
//        {
//            for(int j=0;j<col;j++)
//            {
//                Object tmpp = tmp.get(i);
//                Vector tmppp = (Vector)tmpp;
//                tableDate[i][j] = tmppp.get(j);
//            }
//        }
        int[] int_args = new int[2];
        String[] str_args = new String[0];
        int_args[0] = 3002001;
        int_args[1] = 2002001;
        DBConnector conn = new DBConnector();
        Object[][] tableData;
        try {
            tableData = conn.search("课程成绩查询", int_args, str_args);
        } catch (CustomException e) {
            System.out.println(e.getMessage());
            return;
        }
        System.out.println(tableData.length);
        String[] col_name = {"course_order", "course_name", "teacher_id", "course_time", "course_credit", "score"};
        JPanel panel_show = new JPanel();
        contentPane.add(panel_show);
        JTable table = new JTable(tableData, col_name);
        table.setEnabled(false);
        panel_show.add(new JScrollPane(table));
        panel_show.setVisible(false);


//创建功能栏面板
        JPanel panel_function = new JPanel();
        contentPane.add(panel_function, BorderLayout.WEST);
        JButton button = new JButton("成绩查询");

        button.addActionListener((e) -> {
//            panel_show.add(new JScrollPane(table));
            panel_show.setVisible(true);
        });
        panel_function.add(button);


    }

    private void cj_show() {

    }

}