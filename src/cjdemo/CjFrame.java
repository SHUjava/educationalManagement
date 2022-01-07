package cjdemo;

import jdbctest.CustomException;
import jdbctest.DBConnector;

import javax.swing.*;
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
 */
public class CjFrame extends JFrame {
    /**
     * 账号、密码文本框组件
     * id_textField : 账号文本框
     * pw_textField : 密码文本框
     * choose :       登录身份选择
     * id :           传给子页面ID标识
     */
    //账号、密码文本框组件
    JTextField id_textField = new JTextField(20);
    JTextField pw_textField = new JTextField(20);
    String choose = "";
    int id = 0;

    /**
     * @function: 成绩登录页面构造函数
     * @param title [上级页面传入] 页面的标题
     */
    public CjFrame(String title) {
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setResizable(false);

        id_textField.addFocusListener(new JTextFieldHintListener(id_textField,"1001001"));
        pw_textField.addFocusListener(new JTextFieldHintListener(pw_textField,"123456"));
        //设置容器
//        JPanel root_panel = new JPanel();
//        this.setContentPane(root_panel);

        //向容器内添加文本框组件
//        root_panel.add(id_textField);
//        root_panel.add(pw_textField);
        this.add(id_textField);
        this.add(pw_textField);


        //创建label
        JLabel id_label = new JLabel("账号");
        JLabel pw_label = new JLabel("密码");
        //添加Label
        this.add(id_label);
        this.add(pw_label);

        //登录按钮组件
        JButton login_button = new JButton("登录");
        //向容器内添加登录按钮组件
//        root_panel.add(login_button);
        this.add(login_button);
        //按钮添加监听器
        login_button.addActionListener((e)->{
//            System.out.println(choose);
            if (is_login("student") && choose.equals("学生") ) {
               login_success();
                try {
                    new stuFrame(id);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } catch (CustomException ex) {
                    ex.printStackTrace();
                }
                this.setVisible(false);
            }
            else if(is_login("teacher") && choose.equals("教师")){
                login_success();
                new teacherFrame();
                this.setVisible(false);
            }
            else if(is_login("admin") && choose.equals("管理员")){
                login_success();
                new adminFrame();
                this.setVisible(false);
            }
            else{
                System.out.println("登录失败,请重新登录");
                //将账号和密码信息清空
                id_textField.setText("");
                pw_textField.setText("");
                JOptionPane.showMessageDialog(null,"登录失败,请重新登录");


            }
        });

        JLabel label_test = new JLabel("身份:");
        label_test.setFont(new Font("楷体",Font.BOLD,12));
        this.add(label_test);
        JRadioButton radioButton1_test = new JRadioButton("学生",true);
        radioButton1_test.addActionListener(new MyActionListener());
        JRadioButton radioButton2_test = new JRadioButton("教师");
        radioButton2_test.addActionListener(new MyActionListener());
        JRadioButton radioButton3_test = new JRadioButton("管理员");
        radioButton3_test.addActionListener(new MyActionListener());
        ButtonGroup group = new ButtonGroup();
        group.add(radioButton1_test);
        group.add(radioButton2_test);
        group.add(radioButton3_test);
        this.add(radioButton1_test);
        this.add(radioButton2_test);
        this.add(radioButton3_test);

//        设置卡片式布局的面板
//        JPanel cards = new JPanel(new CardLayout());
//        cards.add(root_panel,"card");
//        CardLayout c = (CardLayout)(cards.getLayout()) ;
//        c.show(cards,"card");
//        this.add(cards);

        //绝对布局
//        Container c = getContentPane();
        id_label.setBounds(90,30,100,100);
        id_textField.setBounds(140,60,300,50);
        pw_label.setBounds(90,80,100,200);
        pw_textField.setBounds(140,150,300,50);
        label_test.setBounds(90,200,100,100);
        radioButton1_test.setBounds(140,225,100,50);
        radioButton2_test.setBounds(260,225,100,50);
        radioButton3_test.setBounds(380,225,100,50);
        login_button.setBounds(250,280,100,40);
//        status.setBounds(140,340,320,50);


        //设置窗口大小
//        this.setSize(100,60);
        this.setBounds(700,300,550,500);

        //设置窗口可显示
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
        String Str_id = new String();
        String Str_pw = new String();
        Str_id = id_textField.getText().trim();
        Str_pw = pw_textField.getText().trim();
        if (Str_id.length() == 0 || Str_pw.length() == 0) return false;
        int intID = Integer.parseInt(Str_id);
        try {
            if (connector.login(mode, intID, Str_pw)) {
                id = Integer.parseInt(Str_id);
                return true;
            } else
                return false;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
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
