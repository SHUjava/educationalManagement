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
    testFrame(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500,300);


        String[] colName = {"A","B","C"};
        Object[][] data = new Object[3][3];
        for(int i=0;i<3;i++)
        {
            for (int j=0;j<3;j++){
                data[i][j] = (i+1)*(j+1);
            }
        }

        JTable table = new JTable(data,colName);
        table.setSize(400,200);
        table.setEnabled(false);
//        JPanel root = new JPanel();
//
//        Container contentPane = this.getContentPane();
        this.add(new JScrollPane(table));
        MessageFormat footer = new MessageFormat("Page - {0}");
        MessageFormat header = new MessageFormat("Final Grades");
        try {
            table.print(JTable.PrintMode.FIT_WIDTH,header,footer,true,null,false,null);
        } catch (PrinterException e) {
            e.printStackTrace();
        }
        this.setVisible(true);



    }


}
