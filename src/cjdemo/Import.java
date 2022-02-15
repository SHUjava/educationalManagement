package cjdemo;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.Arrays;

public class Import {
    JButton buttonImport;
    Object[][] result;

    public Import() {
        buttonImport = new JButton("导入");
        buttonImport.setContentAreaFilled(false);
        buttonImport.setFont(new java.awt.Font("Dialog", 1, 12));
        buttonImport.setPreferredSize(new Dimension(70, 30));
        buttonImport.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setLocation(400, 250);
            fileChooser.setCurrentDirectory(new File("."));
            fileChooser.setSelectedFile(new File("Untitled.xls"));
            fileChooser.setFileFilter(new FileNameExtensionFilter( null,"xls"));
            fileChooser.showOpenDialog(null);
            File file = fileChooser.getSelectedFile();
            importTable(file);
        });
    }

    public JButton getButtonImport() {
        return buttonImport;
    }

    public Object[][] getResult() {
        return result;
    }

    public void importTable(File file) {
        try {
            Workbook workbook = Workbook.getWorkbook(file);
            Sheet sheet = workbook.getSheet(0);
            result = new Object[sheet.getRows()][sheet.getColumns()];
            for (int i = 0; i < sheet.getRows(); i++) {
                for (int j = 0; j < sheet.getColumns(); j++) {
                    Cell cell = sheet.getCell(j, i);
                    result[i][j] = cell.getContents();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(Arrays.deepToString(result));
    }
}
//Import ipt = new Import();
//JButton buttonImport = ipt.getButtonImport();
//panel_export.add(buttonImport);