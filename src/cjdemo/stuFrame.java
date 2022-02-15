package cjdemo;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
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
 * 2.JPanel类型的学生信息面板stuPanel
 * 2.JPanel类型的功能面板panel_function
 * 3.JPanel类型的成绩表格显示面板panel_show
 */
public class stuFrame extends JFrame implements Exit {
    int id;
    String name;
    JPanel panel_show;
    Object[][] tableData;
    DBConnector conn;
    JTable cjtable;
    String[] int_args;
    String[] str_args;
    String[] semeList;

    /**
     * @param ID: 根据学生ID来创建学生页面
     * @throws SQLException
     * @throws CustomException
     */
    public stuFrame(int ID, String name) throws SQLException, CustomException {
        //初始化ID，NAME
        this.id = ID;
        this.name = name;
        DBConnector t = new DBConnector();
        this.semeList = t.getStuSemeList(ID);
        ImageIcon imageIcon = new ImageIcon("image/SHU_LOGO.png");
        this.setIconImage(imageIcon.getImage().getScaledInstance(100, 140, 100));
        this.setResizable(false);
        // 调用setBounds方法调整页面的左上角坐标以及页面的大小
//        this.setBounds(100, 100, 1000, 800);
        this.setTitle(this.id + "学生成绩管理页面");
        // 将右上角的[x]与退出程序相关联
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        this.setSize(700, 600);
        this.setLocation(100, 100);
        this.setLayout(new BorderLayout(5, 20));
        ((JPanel) this.getContentPane()).setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        /**
         * @function: 创建显示学生基础信息的窗格。
         */
        JPanel stuPanel = new JPanel();
        stuPanel.setPreferredSize(new Dimension(700, 80));
        stuPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 5));
        this.add(stuPanel, "North");

        JLabel stuInfo = new JLabel();
        stuInfo.setPreferredSize(new Dimension(120, 60));
        Font font = new Font("Dialog", 1, 12);
        Font labelFont = new Font("Dialog", 1, 12);
        stuInfo.setText("欢迎你：" + this.name);
        stuInfo.setFont(labelFont);
        stuPanel.add(stuInfo);

        JButton changePW = new JButton("修改密码");
        changePW.addActionListener(e -> {
            JFrame frame = new changePWFrame(1, this.id, this);
//            dispose();
        });
        changePW.setPreferredSize(new Dimension(100, 30));
        changePW.setFont(font);
        changePW.setContentAreaFilled(false);
        stuPanel.add(changePW);

        JButton exitButton = new JButton("安全退出");
        exitButton.addActionListener(e -> {
            doExit();
        });
        exitButton.setPreferredSize(new Dimension(100, 30));
        exitButton.setFont(font);
        exitButton.setContentAreaFilled(false);
        stuPanel.add(exitButton);

        panel_show = new JPanel();
        panel_show.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        panel_show.setPreferredSize(new Dimension(550, 500));
        this.add(panel_show, "Center");
        panel_show.setVisible(false);

        // 一个空白模板
        JLabel blank = new JLabel();
        blank.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        blank.setPreferredSize(new Dimension(10, 30));
        blank.setText("  ");
        blank.setFont(labelFont);
        /**
         *  @function: 创建功能栏面板panel_function, 包含成绩查询按钮button组件
         */
        JPanel panel_function = new JPanel();
        panel_function.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel_function.setPreferredSize(new Dimension(100, 500));
        this.add(panel_function, "West");

        JButton buttonQuery = new JButton("成绩查询");
        buttonQuery.setContentAreaFilled(false);
        buttonQuery.setFont(font);
        buttonQuery.setPreferredSize(new Dimension(90, 30));

        /**
         * @function: 为【成绩查询】按钮创建监听器，点击后成绩显示窗格状态改为【显示】
         * 函数参数采用lambda表达式
         */
        buttonQuery.addActionListener((e) -> {
            panel_show.removeAll();
            JPanel panel_choose = new JPanel();
            panel_choose.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
            panel_choose.setPreferredSize(new Dimension(500, 50));
            JLabel seme = new JLabel();
            seme.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
            seme.setPreferredSize(new Dimension(40, 30));
            seme.setText("学期：");
            seme.setFont(labelFont);
            panel_choose.add(seme);
            JComboBox jComboBox = new JComboBox(semeList);
            panel_choose.add(jComboBox);
            panel_choose.add(blank);
            JButton buttonOK = new JButton("查询");
            buttonOK.setContentAreaFilled(false);
            buttonOK.setFont(font);
            buttonOK.setPreferredSize(new Dimension(60, 25));
            buttonOK.addActionListener((f)->{
                panel_show.removeAll();
                panel_show.add(panel_choose);
                panel_show.setVisible(true);
                panel_show.validate();
                panel_show.repaint();
                Object checkResult = jComboBox.getSelectedItem();
                cjtable = getJTable(checkResult);
                DefaultTableCellRenderer r = new DefaultTableCellRenderer();
                r.setHorizontalAlignment(JLabel.CENTER);
                cjtable.setDefaultRenderer(Object.class, r);
                JPanel panel_export = new JPanel();
                panel_export.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
                panel_export.setPreferredSize(new Dimension(550, 50));
                Export export = new Export(cjtable);
                JButton buttonExport = export.getButtonExport();
                panel_export.add(buttonExport);
                Print print = new Print(cjtable, this, this.id+this.name+" "+checkResult.toString()+"成绩信息");
                JButton buttonPrint = print.getButtonPrint();
                panel_export.add(buttonPrint);
                panel_show.add(panel_export);
                JScrollPane jScrollPane = new JScrollPane(cjtable);
                jScrollPane.setPreferredSize(new Dimension(500, 300));
                panel_show.add(jScrollPane);
                cjtable.setPreferredSize(new Dimension(500, 300));
                cjtable.setEnabled(false);  //不可编辑
                cjtable.getTableHeader().setReorderingAllowed(false);   //不可整列移动
                cjtable.getTableHeader().setResizingAllowed(false);   //不可拉动表格
                JLabel info = new JLabel();
                info.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
                info.setPreferredSize(new Dimension(500, 30));
                info.setText("总计学分：" + t.getCredit(this.id,checkResult.toString())+
                        "    平均绩点：" + t.getAverageScore(this.id,checkResult.toString()));
                info.setFont(labelFont);
                panel_show.add(info);
                panel_show.setVisible(true);
                panel_show.validate();
                panel_show.repaint();
            });
            panel_choose.add(buttonOK);
            panel_show.add(panel_choose);
            panel_show.setVisible(true);
            panel_show.validate();
            panel_show.repaint();
        });
        //将【成绩查询】按钮组件添加到功能栏面板中
        panel_function.add(buttonQuery);
        /*
         * @function: 成绩大表
         * @author: YangJunhao
         */
        JButton buttonQueryAll = new JButton("成绩大表");
        buttonQueryAll.setContentAreaFilled(false);
        buttonQueryAll.setFont(font);
        buttonQueryAll.setPreferredSize(new Dimension(90, 30));
        buttonQueryAll.addActionListener((e) -> {
            cjtable = getJTable1();
            DefaultTableCellRenderer r = new DefaultTableCellRenderer();
            r.setHorizontalAlignment(JLabel.CENTER);
            cjtable.setDefaultRenderer(Object.class, r);
            panel_show.removeAll();
            JPanel panel_export = new JPanel();
            panel_export.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            panel_export.setPreferredSize(new Dimension(550, 50));
            Export export = new Export(cjtable);
            JButton buttonExport = export.getButtonExport();
            panel_export.add(buttonExport);
            Print print = new Print(cjtable, this, this.id+this.name+" 成绩大表");
            JButton buttonPrint = print.getButtonPrint();
            panel_export.add(buttonPrint);
            panel_show.add(panel_export);
            JScrollPane jScrollPane = new JScrollPane(cjtable);
            jScrollPane.setPreferredSize(new Dimension(500, 300));
            panel_show.add(jScrollPane);
            cjtable.setPreferredSize(new Dimension(500, 300));
            cjtable.setEnabled(false);  //不可编辑
            cjtable.getTableHeader().setReorderingAllowed(false);   //不可整列移动
            cjtable.getTableHeader().setResizingAllowed(false);   //不可拉动表格
            JLabel info = new JLabel();
            info.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
            info.setPreferredSize(new Dimension(500, 30));
            info.setText("总计学分：" + t.getCredit(this.id,"总体")+
                    "    平均绩点：" + t.getAverageScore(this.id,"总体"));
            info.setFont(labelFont);
            panel_show.add(info);
            panel_show.setVisible(true);
            panel_show.validate();
            panel_show.repaint();
        });
        panel_function.add(buttonQueryAll);
        /*
         * @function: 绩点走势
         * @author: YangJunhao
         */
        JButton buttonHistory = new JButton("绩点走势");
        buttonHistory.setContentAreaFilled(false);
        buttonHistory.setFont(font);
        buttonHistory.setPreferredSize(new Dimension(90, 30));
        buttonHistory.addActionListener((e) -> {
            JPanel showHistoryPanel = null;
            try {
                showHistoryPanel = new Chart(id, t.getSeme(), "学生绩点走势", 0).getChartPanel();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            showHistoryPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 0));
            showHistoryPanel.setPreferredSize(new Dimension(550, 400));
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
        buttonRanking.setPreferredSize(new Dimension(90, 30));
        buttonRanking.addActionListener((e) -> {
            panel_show.removeAll();
            String[] sl = new String[semeList.length+1];
            sl[0]="总体";
            for(int i=1;i<=semeList.length;i++){
                sl[i]=semeList[i-1];
            }
            JPanel panel_choose = new JPanel();
            panel_choose.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
            panel_choose.setPreferredSize(new Dimension(500, 50));
            JLabel seme = new JLabel();
            seme.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
            seme.setPreferredSize(new Dimension(40, 30));
            seme.setText("学期：");
            seme.setFont(labelFont);
            panel_choose.add(seme);
            JComboBox jComboBox = new JComboBox(sl);
            panel_choose.add(jComboBox);
            panel_choose.add(blank);
            JButton buttonOK = new JButton("查询");
            buttonOK.setContentAreaFilled(false);
            buttonOK.setFont(font);
            buttonOK.setPreferredSize(new Dimension(60, 25));
            buttonOK.addActionListener((f)->{
                panel_show.removeAll();
                panel_show.add(panel_choose);
                panel_show.setVisible(true);
                panel_show.validate();
                panel_show.repaint();
                Object checkResult = jComboBox.getSelectedItem();
                cjtable = getJTable2(checkResult);
                DefaultTableCellRenderer r = new DefaultTableCellRenderer();
                r.setHorizontalAlignment(JLabel.CENTER);
                cjtable.setDefaultRenderer(Object.class, r);
                JPanel panel_export = new JPanel();
                panel_export.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
                panel_export.setPreferredSize(new Dimension(550, 50));
                Export export = new Export(cjtable);
                JButton buttonExport = export.getButtonExport();
                panel_export.add(buttonExport);
                Print print = new Print(cjtable, this, this.id+this.name+" "+checkResult.toString()+"成绩排名");
                JButton buttonPrint = print.getButtonPrint();
                panel_export.add(buttonPrint);
                panel_show.add(panel_export);
                JScrollPane jScrollPane = new JScrollPane(cjtable);
                jScrollPane.setPreferredSize(new Dimension(500, 300));
                panel_show.add(jScrollPane);
                cjtable.setPreferredSize(new Dimension(500, 300));
                cjtable.setEnabled(false);  //不可编辑
                cjtable.getTableHeader().setReorderingAllowed(false);   //不可整列移动
                cjtable.getTableHeader().setResizingAllowed(false);   //不可拉动表格
                JLabel info = new JLabel();
                info.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
                info.setPreferredSize(new Dimension(500, 30));
                info.setText("总计学分：" + t.getCredit(this.id,checkResult.toString())+
                        "    平均绩点：" + t.getAverageScore(this.id,checkResult.toString()));
                info.setFont(labelFont);
                panel_show.add(info);
                panel_show.setVisible(true);
                panel_show.validate();
                panel_show.repaint();
            });
            panel_choose.add(buttonOK);
            panel_show.add(panel_choose);
            panel_show.setVisible(true);
            panel_show.validate();
            panel_show.repaint();
        });
        panel_function.add(buttonRanking);
    }

    /**
     * @function : 构造成绩查询表格。
     */
    public JTable getJTable(Object semester) {
        int_args = new String[1];
        str_args = new String[1];
        int_args[0] = ""+this.id;
        str_args[0] = semester.toString();
        conn = new DBConnector();
        try {
            tableData = conn.search("学生成绩查询", int_args, str_args, null);
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

    /**
     * @function : 构造成绩大表表格。
     */
    public JTable getJTable1() {
        int_args = new String[1];
        str_args = new String[0];
        int_args[0] = ""+this.id;
        conn = new DBConnector();
        try {
            tableData = conn.search("学生成绩大表", int_args, str_args, null);
        } catch (CustomException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String[] col_name = {"课程号", "课程名", "学分", "成绩", "绩点", "学期"};
        JTable cjtable = new JTable(tableData, col_name);
        System.out.println(tableData.toString());
        return cjtable;
    }

    /**
     * @function : 构造成绩排名表格。
     */
    public JTable getJTable2(Object semester) {
        conn = new DBConnector();
        tableData = conn.getRanking(id, semester.toString());
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