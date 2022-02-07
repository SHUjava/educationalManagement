package cjdemo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.spec.ECField;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Vector;

import jdbctest.DBConnector;
import jdbctest.CustomException;
import org.jfree.chart.ChartPanel;

/**
 * @description : 显示学生界面，包含成绩查询功能
 * @Content : 组件内容：1.Jtable类型的成绩表格cjtable
 *                    2.JPanel类型的学生信息面板stuPanel
 *                    2.JPanel类型的功能面板panel_function
 *                    3.JPanel类型的成绩表格显示面板panel_show
 *
 */
public class stuFrame extends JFrame implements Exit {
    int id;
    String name;
    JPanel panel_show,showHistoryPanel;
    Object[][] tableData;
    DBConnector conn;
    JTable cjtable;
    int[] int_args ;
    String[] str_args;
    String[] col_name;
    Print print;

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
        exitButton.addActionListener(e->{
            doExit();
        });
        exitButton.setPreferredSize(new Dimension(100,30));
        exitButton.setFont(font);
        exitButton.setContentAreaFilled(false);
        stuPanel.add(exitButton);

        panel_show = new JPanel();
        panel_show.setLayout(new FlowLayout(FlowLayout.RIGHT,5,0));
        panel_show.setPreferredSize(new Dimension(550,500));
        this.add(panel_show,"East");
        panel_show.setVisible(false);

        /**
         *  @function: 创建功能栏面板panel_function,包含成绩查询按钮button组件
         */
        JPanel panel_function = new JPanel();
        panel_function.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
        panel_function.setPreferredSize(new Dimension(100,500));
        this.add(panel_function,"West");

        JButton buttonQuery = new JButton("成绩查询");
        buttonQuery.setContentAreaFilled(false);
        buttonQuery.setFont(font);
        buttonQuery.setPreferredSize(new Dimension(90,30));

        /**
         * @function: 为【成绩查询】按钮创建监听器，点击后成绩显示窗格状态改为【显示】
         * 函数参数采用lambda表达式
         */
        buttonQuery.addActionListener((e) -> {
            String[] str={"2019-2020秋季","2019-2020冬季","2019-2020春季","2020-2021秋季","2020-2021冬季","2020-2021春季"};
            Object checkResult = JOptionPane.showInputDialog(null,"请选择学期","选择学期",1,null,str,str[0]);
            System.out.println(checkResult);
            cjtable = getJTable(checkResult);
            panel_show.removeAll();
            JPanel panel_export = new JPanel();
            panel_export.setLayout(new FlowLayout(FlowLayout.RIGHT,0,0));
            panel_export.setPreferredSize(new Dimension(550,50));
            Export export = new Export(cjtable);
            JButton buttonExport = export.getButtonExport();
            panel_export.add(buttonExport);
            Print print = new Print(cjtable, this,this.name);
            JButton buttonPrint = print.getButtonPrint();
            panel_export.add(buttonPrint);
            panel_show.add(panel_export);
            JScrollPane jScrollPane = new JScrollPane(cjtable);
            jScrollPane.setPreferredSize(new Dimension(500,300));
            panel_show.add(jScrollPane);
            cjtable.setPreferredSize(new Dimension(500,300));
            cjtable.setEnabled(false);
            cjtable.getTableHeader().setReorderingAllowed(false);
            panel_show.setVisible(true);
            panel_show.validate();
            panel_show.repaint();
        });
        //将【成绩查询】按钮组件添加到功能栏面板中
        panel_function.add(buttonQuery);

        /*
          @function: 绩点走势
         * @author: YangJunhao
         */
        JButton buttonHistory = new JButton("绩点走势");
        buttonHistory.setContentAreaFilled(false);
        buttonHistory.setFont(font);
        buttonHistory.setPreferredSize(new Dimension(90,30));
        buttonHistory.addActionListener((e) -> {
                showHistoryPanel=new Chart(id,"2020-2021春季","学生绩点走势",0).getChartPanel();
                showHistoryPanel.setLayout(new FlowLayout(FlowLayout.RIGHT,5,0));
                showHistoryPanel.setPreferredSize(new Dimension(550,400));
                showHistoryPanel.setVisible(true);
                panel_show.removeAll();
                panel_show.add(showHistoryPanel);
                panel_show.validate();
                panel_show.repaint();
                panel_show.setVisible(true);
        });
        panel_function.add(buttonHistory);

        /*
         * @function: 成绩排名
         * @author: YangJunhao
         */
        JButton buttonRanking = new JButton("成绩排名");
        buttonRanking.setContentAreaFilled(false);
        buttonRanking.setFont(font);
        buttonRanking.setPreferredSize(new Dimension(90,30));
        buttonRanking.addActionListener((e) ->{
                String[] str={"2019-2020秋季","2019-2020冬季","2019-2020春季","2020-2021秋季","2020-2021冬季","2020-2021春季"};
                Object checkResult = JOptionPane.showInputDialog(null,"请选择学期","选择学期",1,null,str,str[0]);
                System.out.println(checkResult);
                cjtable = getJTable2(checkResult);
                panel_show.removeAll();
                JPanel panel_export = new JPanel();
                panel_export.setLayout(new FlowLayout(FlowLayout.RIGHT,0,0));
                panel_export.setPreferredSize(new Dimension(550,50));
                Export export = new Export(cjtable);
                JButton buttonExport = export.getButtonExport();
                panel_export.add(buttonExport);
                Print print = new Print(cjtable, this,this.name);
                JButton buttonPrint = print.getButtonPrint();
                panel_export.add(buttonPrint);
                panel_show.add(panel_export);
                JScrollPane jScrollPane = new JScrollPane(cjtable);
                jScrollPane.setPreferredSize(new Dimension(500,300));
                panel_show.add(jScrollPane);
                cjtable.setPreferredSize(new Dimension(500,300));
                cjtable.setEnabled(false);
                cjtable.getTableHeader().setReorderingAllowed(false);
                panel_show.setVisible(true);
                panel_show.validate();
                panel_show.repaint();
        });
        panel_function.add(buttonRanking);
    }



    /**
     *
     */
    public JTable getJTable(Object semester)
    {
        int_args = new int[1];
        str_args = new String[1];
        int_args[0] = this.id;
        str_args[0] = semester.toString();
        conn = new DBConnector();
        try {
            tableData = conn.search("学生成绩查询",int_args,str_args,null);
        } catch (CustomException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String[] col_name = {"序号", "课程编号", "课程名", "学分", "成绩", "绩点"};
        JTable cjtable = new JTable(tableData, col_name);
        System.out.println(tableData.toString());
        return cjtable;
    }

    public JTable getJTable2(Object semester)
    {
        conn = new DBConnector();
        tableData = conn.getRanking(id,semester.toString());
        String[] col_name = {"院系", "年级总人数", "排名", "百分比"};
        JTable cjtable = new JTable(tableData, col_name);
        System.out.println(tableData.toString());
        return cjtable;
    }
    @Override
    /**
     * @function : 实现安全退出功能。
     */
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