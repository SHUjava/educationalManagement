package cjdemo;

import java.awt.*;
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.*;

import jxl.*;
import jxl.write.*;
import jxl.write.Label;

/**
 * 这个类提供了JTable导出为Excel的功能
 *
 * @author YangJunhao
 * @version 2.0
 * @updateDate: 2022/1/18
 * @updateContent: 1.重写Export构造函数，弃用FileDialog，改用JFileChooser
 * 2.默认导出为.xls格式，在JFileChooser支持下实现
 */
public class Export {
    JButton buttonExport;

    /**
     * @param table 要导出的JTable
     */
    public Export(JTable table) {
        buttonExport = new JButton("导出");
        buttonExport.setContentAreaFilled(false);
        buttonExport.setFont(new java.awt.Font("Dialog", 1, 12));
        buttonExport.setPreferredSize(new Dimension(70, 30));
        buttonExport.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setLocation(400, 250);
            fileChooser.setCurrentDirectory(new File("."));
            fileChooser.setSelectedFile(new File("Untitled.xls"));
            fileChooser.setFileFilter(new FileNameExtensionFilter("Excel", ".xls"));
            fileChooser.showSaveDialog(null);
            File file = fileChooser.getSelectedFile();
            String stringFile = file.getAbsolutePath();
            System.out.println(stringFile);
            exportTable(table, new File(stringFile));
        });
    }

    /**
     * @return 返回提供导出功能的JButton，需要自己添加到容器中
     */
    public JButton getButtonExport() {
        return buttonExport;
    }

    /**
     * @function 将JTable的内容读入Excel中
     */
    public void exportTable(JTable table, File file) {
        try {
            WritableWorkbook workbook1 = Workbook.createWorkbook(file);
            WritableSheet sheet1 = workbook1.createSheet("First Sheet", 0);
            TableModel model = table.getModel();
            for (int i = 0; i < model.getColumnCount(); i++) {
                Label column = new Label(i, 0, model.getColumnName(i));
                sheet1.addCell(column);
            }
            int j = 0;
            for (int i = 0; i < model.getRowCount(); i++) {
                for (j = 0; j < model.getColumnCount(); j++) {
                    Label row = new Label(j, i + 1, model.getValueAt(i, j).toString());
                    sheet1.addCell(row);
                }
            }
            workbook1.write();
            workbook1.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

