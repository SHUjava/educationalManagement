package cjdemo;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

import jdbctest.CustomException;
import jdbctest.DBConnector;

public class changePWFrame extends JFrame implements Exit {
    int mode;
    int id;


    JPasswordField firstPW,secondPW;
    changePWFrame(int mode,int id,JFrame frame){
        this.mode = mode;
        this.id = id;
        this.setVisible(true);
        //添加图标校徽
        ImageIcon imageIcon = new ImageIcon("image/SHU_LOGO.png");
        this.setIconImage(imageIcon.getImage().getScaledInstance(100,140,100));
        this.setTitle("修改密码");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(100,100,220,200);
        this.setSize(220,200);
        this.setLocation(100,100);
        this.setLayout(new BorderLayout(5,20));
        ((JPanel)this.getContentPane()).setBorder(BorderFactory.createEmptyBorder(5,10,10,10));

        JPanel rootPanel = new JPanel();
        rootPanel.setPreferredSize(new Dimension(220,200));
        rootPanel.setLayout(new FlowLayout(FlowLayout.CENTER,5,10));
        this.add(rootPanel,"Center");

        JLabel changePWFirst = new JLabel("输入密码");
        changePWFirst.setPreferredSize(new Dimension(70,30));
        firstPW = new JPasswordField("");
        firstPW.setPreferredSize(new Dimension(60,30));
        firstPW.setToolTipText("请输入长度在6-20位的新密码");
        rootPanel.add(changePWFirst,"North");
        rootPanel.add(firstPW,"North");

        JLabel changePWSecond = new JLabel("确认密码");
        changePWSecond.setPreferredSize(new Dimension(70,30));
        secondPW = new JPasswordField("");
        secondPW.setPreferredSize(new Dimension(60,30));
        secondPW.setToolTipText("请输入相同的密码");
        rootPanel.add(changePWSecond,"North");
        rootPanel.add(secondPW,"North");

        JButton checkButton = new JButton("确认");
        checkButton.setPreferredSize(new Dimension(80,40));
        rootPanel.add(checkButton,"North");
        checkButton.addActionListener(e -> {
            System.out.println(1);
            System.out.println(firstPW.getText());
            System.out.println(secondPW.getPassword());
            if(!(firstPW.getText().equals(secondPW.getText()))){
                JOptionPane.showMessageDialog(null,"两次输入的密码不一致，请重新输入","修改密码错误提示",JOptionPane.YES_NO_OPTION);
                firstPW.setText("");
                secondPW.setText("");

            }
            else if(firstPW.getPassword().length<6){
                JOptionPane.showMessageDialog(null,"密码长度过短，请重新输入","修改密码错误提示",JOptionPane.YES_NO_OPTION);
                firstPW.setText("");
                secondPW.setText("");

            }
            else if(firstPW.getPassword().length>20){
                JOptionPane.showMessageDialog(null,"密码长度超过20位，请重新输入","修改密码错误提示",JOptionPane.YES_NO_OPTION);
                firstPW.setText("");
                secondPW.setText("");
            }
            else{
            DBConnector conn = new DBConnector();
                try {
                    System.out.println(mode+" "+id+" "+firstPW.getPassword());
                    if(conn.changePassword(mode,id,firstPW.getText())){
                        JOptionPane.showMessageDialog(null,"密码修改成功！即将返回登录页面");
                        dispose();
                        frame.dispose();
                        JFrame newFrame = new CjFrame("成绩管理系统登录界面");
                    }
                } catch (CustomException ex) {
                    ex.printStackTrace();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

        });

    }

    @Override
    public void doExit() {

    }
}