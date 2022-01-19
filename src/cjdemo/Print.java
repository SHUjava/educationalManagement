package cjdemo;

import java.awt.*;
import javax.swing.*;

import static java.awt.Toolkit.getDefaultToolkit;
/**
 * 这个类提供了打印功能，也可以将其输出为PDF
 *
 * @author YangJunhao
 * @version 1.0
 * @updateDate: 2022/1/19
 * @updateContent: 1.打印出的表格缺少属性名一行
 * 2.缺少左上两条边框，怀疑是该JTable本身就没有显示这两条边框
 */
public class Print {
    PrintJob p = null;
    Graphics g = null;
    JButton buttonPrint;

    /**
     * @param table,frame 要打印的JTable及其所在JFrame
     */
    public Print(JTable table, JFrame frame) {
        buttonPrint = new JButton("打印");
        buttonPrint.setContentAreaFilled(false);
        buttonPrint.setFont(new java.awt.Font("Dialog", 1, 12));
        buttonPrint.setPreferredSize(new Dimension(70, 30));
        buttonPrint.addActionListener(e -> {
            p = getDefaultToolkit().getPrintJob(frame, "ok", null);
            g = p.getGraphics();    //p获取一个用于打印的 Graphics对象
            g.translate(120, 200);
            table.printAll(g);     //打印当前文本区及其内容
            g.dispose();          //释放对象 g
            p.end();
        });
    }
    /**
     * @return 返回提供打印功能的JButton，需要自己添加到容器中
     */
    public JButton getButtonPrint() {
        return buttonPrint;
    }
}