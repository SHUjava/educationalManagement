package cjdemo;

import javax.swing.*;
import java.awt.*;
import java.awt.print.PrinterException;
import java.text.MessageFormat;

/**
 *  @projectName : 成绩管理系统
 *  @package : cjdemo
 *  @classname : login_demo
 *  @description : 学生成绩管理系统登录页面
 *  @author : Shu-rainbow
 *  @createDate : 2021/12/10
 *  @UpdateDate : 2022/1/8
 *  @version : v1.1
 *
 */
public class login_demo {
    /**
     * @description : 主函数，跳转到登录首页
     */
    public static void main(String[] args)
    {
        /**
         * frame:登录首页
         */
        JFrame frame = new CjFrame("成绩管理系统登录界面");
//        JFrame frame = new testFrame();
    }
}
class testFrame extends JFrame{
    JTextField IDTextField = new JTextField();
    JPasswordField PWTextField = new JPasswordField();
    testFrame(){
        ImageIcon icon=new ImageIcon("image/face.jpg");
        //Image im=new Image(icon);
        //将图片放入label中
        JLabel label=new JLabel(icon);

        //设置label的大小
        label.setBounds(0,0,icon.getIconWidth(),icon.getIconHeight());

//        JFrame frame=new JFrame();

        //获取窗口的第二层，将label放入
        this.getLayeredPane().add(label,new Integer(Integer.MIN_VALUE));
        this.setLayout(new BorderLayout(5, 20));
        ((JPanel) this.getContentPane()).setBorder(BorderFactory.createEmptyBorder(180, 210, 180, 210));
        //获取frame的顶层容器,并设置为透明
        JPanel j=(JPanel)this.getContentPane();
        j.setOpaque(false);

        JPanel panel=new JPanel();
        JTextField jta=new JTextField(10);
        //JTextArea jta=new JTextArea(10,60);
        JButton jb=new JButton("确定");
        JButton jb2=new JButton("重置");

        panel.add(jta);
        panel.add(jb);
        panel.add(jb2);

        IDTextField.setPreferredSize(new Dimension(320,40));
        PWTextField.setPreferredSize(new Dimension(320,40));
//        IDTextField.addFocusListener(new CjFrame.JTextFieldHintListener(IDTextField,"7777777"));
//        PWTextField.addFocusListener(new CjFrame.JTextFieldHintListener(PWTextField,"7777777"));
        JLabel IDLabel = new JLabel("账号:");
        IDLabel.setPreferredSize(new Dimension(50,50));
        //给此标签主动设置焦点，避免最初焦点定位到账号输入框，使提示文字消失
        IDLabel.setFocusable(true);
        IDLabel.requestFocus();
        IDLabel.setFont(new Font("Dialog",1,14));
//        id_label.dispatchEvent(new FocusEvent(id_label,FocusEvent.FOCUS_GAINED,true));
//        id_label.requestFocusInWindow();
        panel.add(IDLabel);
        panel.add(IDTextField);
        JLabel PWLabel = new JLabel("密码:");
        PWLabel.setPreferredSize(new Dimension(50,50));
        PWLabel.setFont(new Font("Dialog",1,14));
//        添加Label
        panel.add(PWLabel);
        panel.add(PWTextField);

        //必须设置为透明的。否则看不到图片
        panel.setOpaque(false);

        this.add(panel);
        this.add(panel);
        this.setSize(icon.getIconWidth(),icon.getIconHeight());
        this.setVisible(true);




    }


}
