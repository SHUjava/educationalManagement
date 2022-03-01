package cjdemo;

import jdbctest.CustomException;
import jdbctest.DBConnector;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

/**
 * @description:   显示成绩管理系统登录首页
 * @classname:     CjFrame
 * @localProblem:  1.登录时账号、密码文本框提示内容在输入时不能自动清除
 *                 2.登录状态文本框多余
 *                 3.成绩显示表格内容可以被随意编辑
 *                 4.代码缺少注释
 *                 5.大量代码存在冗余
 *                 6.登录身份默认改为“学生”后，出现问题，原因是登录逻辑必须点击三者其中之一

 * @updateDate:    2022/1/4
 * @updateContent: 1.删除多余的状态文本框
 *                 2.成绩显示表格内容不允许被修改
 *                 3.添加JavaDoc注释
 *
 * @updateDate:    2022/1/6
 * @updateContent: 1.将项目代码托管到GitHub
 *                 2.登录时账号、密码文本框提示内容在输入时不能自动清除
 *                 3.登录身份默认选择为“学生”
 *
 * @updateDate:    2022/1/7
 * @updateContent: 1.将密码输入文本框内容设置为“·”,增强安全性
 *
 * @updateDate: 2022/1/8
 * @updateContent: 1.修改学生页面窗格的布局
 *                 2.增加欢迎xxx某位学生的文本框
 *                 3.对于学生界面的组件增加了更多的注释
 *
 */
public class CjFrame extends JFrame implements Exit {
    /**
     * 账号、密码文本框组件
     * id_textField : 账号文本框
     * pw_textField : 密码文本框
     * choose :       登录身份选择
     * id :           传给子页面ID标识
     */
    //账号、密码文本框组件
    JTextField IDTextField = new JTextField(20);
    JPasswordField PWTextField = new JPasswordField(20);
    String choose = "学生";
    int id = 0;
    String name;

    /**
     * @function: 成绩登录页面构造函数
     * @param title [上级页面传入] 页面的标题
     */
    public CjFrame(String title) {
        super(title);
        //添加图标校徽
        ImageIcon imageIcon = new ImageIcon("image/SHU_LOGO.png");
        this.setIconImage(imageIcon.getImage().getScaledInstance(100,140,100));

//        this.setSize(800,700);
//        this.setLocationRelativeTo(null);//居中显示
//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        JLabel label0 = new JLabel();
//        ImageIcon img0 = new ImageIcon("image/face.jpg");//创建图片对象
//        label0.setBounds(0,0, img0.getIconWidth(), img0.getIconHeight());
//        label0.setIcon(img0);
//        this.getLayeredPane().add(label0,Integer.MIN_VALUE);
//
//        ((JPanel)this.getContentPane()).setOpaque(false);
//        this.add(label0);


        ImageIcon icon=new ImageIcon("image/face7_7.png");
        //Image im=new Image(icon);
        //将图片放入label中
        JLabel label=new JLabel(icon);

        //设置label的大小
        label.setBounds(0,0,icon.getIconWidth(),icon.getIconHeight());

//        JFrame frame=new JFrame();

        //获取窗口的第二层，将label放入
        this.getLayeredPane().add(label,new Integer(Integer.MIN_VALUE));
        this.setLayout(new BorderLayout(5, 20));
        ((JPanel) this.getContentPane()).setBorder(BorderFactory.createEmptyBorder(150, 210, 150, 210));
        //获取frame的顶层容器,并设置为透明
        JPanel j=(JPanel)this.getContentPane();
        j.setOpaque(false);


        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(380,380));
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 8));
        panel.setOpaque(false);
//        panel.setBackground(Color.WHITE);
//        panel.setBackground(Color.GRAY);

        JLabel label1 = new JLabel();
        label1.setPreferredSize(new Dimension(340,70));
        ImageIcon img = new ImageIcon("image/logo.png");//创建图片对象
        label1.setIcon(img);
        panel.add(label1);

//        id_textField.addFocusListener(new JTextFieldHintListener(id_textField,"学号/工号"));
        //下面一行代码便于测试老师页面，需要时可注释

        IDTextField.setPreferredSize(new Dimension(320,40));
//        IDTextField.setOpaque(false);
        PWTextField.setPreferredSize(new Dimension(320,40));
        IDTextField.addFocusListener(new JTextFieldHintListener(IDTextField,""));
        IDTextField.setBorder(new EmptyBorder(0,0,0,0));
        PWTextField.addFocusListener(new JTextFieldHintListener(PWTextField,""));
        PWTextField.setBorder(new EmptyBorder(0,0,0,0));




//        创建label
        JLabel IDLabel = new JLabel("账号:");
        IDLabel.setPreferredSize(new Dimension(50,50));
        //给此标签主动设置焦点，避免最初焦点定位到账号输入框，使提示文字消失
        IDLabel.setFocusable(true);
        IDLabel.requestFocus();
        IDLabel.setFont(new Font("Dialog",1,15));
//        id_label.dispatchEvent(new FocusEvent(id_label,FocusEvent.FOCUS_GAINED,true));
//        id_label.requestFocusInWindow();
        panel.add(IDLabel);
        panel.add(IDTextField);
        JLabel PWLabel = new JLabel("密码:");
        PWLabel.setPreferredSize(new Dimension(50,50));
        PWLabel.setFont(new Font("Dialog",1,15));
//        添加Label
        panel.add(PWLabel);
        panel.add(PWTextField);


        //登录按钮组件
        JButton loginButton = new JButton("登录");
        loginButton.setPreferredSize(new Dimension(120,40));
        loginButton.setFont(new Font("Dialog",1,15));

        loginButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));

        loginButton.addActionListener((e)->{

            if (is_login("student") && choose.equals("学生") ) {
               login_success();
                try {
                    new stuFrame(id,name);
                } catch (SQLException | CustomException ex) {
                    ex.printStackTrace();
                }
                doExit();
            }
            else if(is_login("teacher") && choose.equals("教师")){
                login_success();
                try{
                new teacherFrame(id,name);
                } catch (SQLException | CustomException ex){
                    ex.printStackTrace();
                }
                doExit();

            }
            else if(is_login("admin") && choose.equals("管理员")){
                login_success();
                new adminFrame();
                doExit();
            }
            else{
                System.out.println("登录失败,请重新登录");
                //将账号和密码信息清空
                IDTextField.setText("");
                PWTextField.setText("");
                JOptionPane.showMessageDialog(null,"登录失败,请重新登录");

            }
        });

        JPanel statusPanel = new JPanel();
        statusPanel.setPreferredSize(new Dimension(430,50));
        statusPanel.setOpaque(false);
        statusPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 8));

        JLabel labelStatus = new JLabel("身份:");
        labelStatus.setPreferredSize(new Dimension(50,50));
        labelStatus.setFont(new Font("Dialog",1,15));
        statusPanel.add(labelStatus,"Center");

        JRadioButton radioButtonStu = new JRadioButton("学生",true);
        radioButtonStu.addActionListener(new MyActionListener());
        radioButtonStu.setContentAreaFilled(false);
        JRadioButton radioButtonTeacher = new JRadioButton("教师");
        radioButtonTeacher.addActionListener(new MyActionListener());
        radioButtonTeacher.setContentAreaFilled(false);
        JRadioButton radioButtonAdmin = new JRadioButton("管理员");
        radioButtonAdmin.addActionListener(new MyActionListener());
        radioButtonAdmin.setContentAreaFilled(false);
        ButtonGroup group = new ButtonGroup();
        group.add(radioButtonStu);
        group.add(radioButtonTeacher);
        group.add(radioButtonAdmin);
//        panel.add(group);
        statusPanel.add(radioButtonStu,"Center");
        statusPanel.add(radioButtonTeacher,"Center");
        statusPanel.add(radioButtonAdmin,"Center");
        panel.add(statusPanel);

//        JLabel label = new JLabel("");
//        label.setPreferredSize(new Dimension(50,50));
//        label.setFont(new Font("楷体",Font.BOLD,12));
//        panel.add(label);


        panel.add(loginButton,"Center");



//        id_textField.setBounds(140,60,300,50);
////        pw_label.setBounds(90,80,100,200);
//        pw_textField.setBounds(140,150,300,50);
//        label_status.setBounds(90,200,100,100);
//        radioButton1_test.setBounds(140,225,100,50);
//        radioButton2_test.setBounds(260,225,100,50);
//        radioButton3_test.setBounds(380,225,100,50);
////        login_button.setBounds(230,280,100,40);
//        status.setBounds(140,340,320,50);


        //设置窗口大小
//        this.setSize(100,60);
//        this.setBounds(700,300,550,500);


        //设置窗口可显示
//        this.setLayout(new BorderLayout(5, 20));
//        ((JPanel) this.getContentPane()).setBorder(BorderFactory.createEmptyBorder(180, 210, 180, 210));
//        this.setResizable(false);
//        this.add(panel,"Center");
        this.add(panel);
        this.add(panel);
        this.setSize(800,640);
        this.setLocationRelativeTo(null);

//        this.setSize(icon.getIconWidth(),icon.getIconHeight());
        this.setVisible(true);
        this.setVisible(true);

    }

    /**
     * @function:登陆成功时显示登陆成功
     */
    private void login_success() {
        System.out.println("登录成功");
//                status_text  += ;


        JOptionPane.showMessageDialog(null,"登录成功");


    }


    /**
     * @param mode: 身份选择
     * @return 登陆成功返回true,否则false
     */
    private boolean is_login(String mode) {
        //登录是否成功
        DBConnector connector = new DBConnector();
        String Str_id;
        String Str_pw;
        Str_id = IDTextField.getText().trim();
        Str_pw = PWTextField.getText().trim();
        if (Str_id.length() == 0 || Str_pw.length() == 0) return false;
        int intID = Integer.parseInt(Str_id);
        try {
            if (connector.login(mode, intID, Str_pw)) {
                id = Integer.parseInt(Str_id);
                name = connector.queryName(mode,id);
                return true;
            }
            else
                return false;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    /**
     * @function: 进行页面切换时关闭当前界面，而不是将页面设置为不可见
     */
    public void doExit() {
        dispose();//关闭当前界面并不是退出整个程序。

    }

    /**
     * @function: 创建监听器
     */
    private class MyActionListener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if(command.equals("学生")){
                choose = "学生";
                System.out.println("选择了学生身份");
            }
            else if(command.equals("教师")){
                choose = "教师";
                System.out.println("选择了教师身份");
            }
            else
            {
                choose = "管理员";
                System.out.println("选择了管理员身份");
            }

        }
    }

    /**
     * @function: 内部类，JTextField的焦点监听器，当鼠标点击时，清除提示词
     */
    private class JTextFieldHintListener implements FocusListener
    {
        private String hintText;
        private JTextField textField;
        public JTextFieldHintListener(JTextField jTextField,String hintText) {
            this.textField = jTextField;
            this.hintText = hintText;
            jTextField.setText(hintText);  //默认直接显示
            jTextField.setForeground(Color.GRAY);
        }

        @Override
        public void focusGained(FocusEvent e) {
            //获取焦点时，清空提示内容
            String temp = textField.getText();
            if(temp.equals(hintText)) {
                textField.setText("");
                textField.setForeground(Color.BLACK);
            }

        }

        @Override
        public void focusLost(FocusEvent e) {
            //失去焦点时，没有输入内容，显示提示内容
            String temp = textField.getText();
            if(temp.equals("")) {
                textField.setForeground(Color.GRAY);
                textField.setText(hintText);
            }

        }

    }


}
