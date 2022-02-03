package cjdemo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class adminFrame extends JFrame implements Exit{
    public adminFrame(){
        this.setTitle("管理员登陆页面");
        ImageIcon imageIcon = new ImageIcon("image/SHU_LOGO.png");
        this.setBounds(100,100,1000,800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        this.setSize(700,600);
        this.setLocation(100,100);
        //采用Border布局，水平间距50，垂直间距5
        this.setLayout(new BorderLayout(5,20));
        ((JPanel)this.getContentPane()).setBorder(BorderFactory.createEmptyBorder(5,10,10,10));

        /**
         * @function: 创建显示管理员基础信息的面板teacherPanel.
         */
        JPanel adminPanel = new JPanel();
        adminPanel.setPreferredSize(new Dimension(700,80));
        adminPanel.setLayout(new FlowLayout(FlowLayout.RIGHT,0,0));
        /**
         * @function: 创建欢迎老师的标签welcomeTeacherLabel.
         */
        JLabel welcomeAdminLabel = new JLabel("欢迎您，管理员 ");
        welcomeAdminLabel.setPreferredSize(new Dimension(120,60));
        Font font = new Font("Dialog",1,12);
        welcomeAdminLabel.setFont(font);
        //将欢迎管理员的信息添加到管理员信息面板中
        adminPanel.add(welcomeAdminLabel);
        //将管理员信息面板添加到主面板中
        this.add(adminPanel,"North");

        /**
         * @function: 创建修改密码的按钮changePWButton
         */
        JButton changePWButton = new JButton("修改密码");
        changePWButton.setPreferredSize(new Dimension(100,30));
        changePWButton.setFont(font);
        changePWButton.setContentAreaFilled(false);
        adminPanel.add(changePWButton);

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
        adminPanel.add(exitButton);
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
