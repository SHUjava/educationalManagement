package cjdemo;

import jdbctest.CustomException;
import jdbctest.DBConnector;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class clearPWFrame extends JFrame implements Exit {
    public clearPWFrame(adminFrame adminFrame) {
        this.setVisible(true);
        //添加图标校徽
        ImageIcon imageIcon = new ImageIcon("image/SHU_LOGO.png");
        this.setIconImage(imageIcon.getImage().getScaledInstance(100, 140, 100));
        this.setTitle("重置密码");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        this.setBounds(100, 100, 240, 200);
        this.setSize(240, 200);
        this.setLocationRelativeTo(null);//居中显示

//        this.setLocation(100, 100);
        this.setLayout(new BorderLayout(5, 20));
        ((JPanel) this.getContentPane()).setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        JPanel rootPanel = new JPanel();
        rootPanel.setPreferredSize(new Dimension(220, 200));
        rootPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10));
        this.add(rootPanel, "Center");

        JLabel inputID = new JLabel("工号/学号");
        inputID.setPreferredSize(new Dimension(70, 30));

        JTextField IDText = new JTextField("");
        IDText.setPreferredSize(new Dimension(60,30));

        rootPanel.add(inputID, "North");
        rootPanel.add(IDText, "North");

        JButton checkButton = new JButton("确认");
        checkButton.setPreferredSize(new Dimension(80, 40));
        rootPanel.add(checkButton, "North");
        checkButton.addActionListener(e -> {
            DBConnector conn = new DBConnector();
            try {
                if(conn.resetPassword(IDText.getText())){
                    JOptionPane.showMessageDialog(null, "密码重置成功！");
                    dispose();
                }
                else{
                    JOptionPane.showMessageDialog(null, "出现错误，请重试！");
                    dispose();
                }
            } catch (CustomException ex) {
                ex.printStackTrace();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }

    @Override
    public void doExit() {

    }
}
