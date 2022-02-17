package cjdemo;

import jdbctest.CustomException;
import jdbctest.DBConnector;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

public class Import {
    JButton buttonImport;
    int[][] result;
    int id;
    String courseName;
    String courseTime;

    public void setCourse(String cn, String ct) {
        courseName = cn;
        courseTime = ct;
    }

    public Import(int id) {
        this.id = id;
        buttonImport = new JButton("导入");
        buttonImport.setContentAreaFilled(false);
        buttonImport.setFont(new java.awt.Font("Dialog", 1, 12));
        buttonImport.setPreferredSize(new Dimension(70, 30));
    }

    public JButton getButtonImport() {
        return buttonImport;
    }

    public int[][] getResult() {
        return result;
    }

    public void importTable(File file) {
        try {
            Workbook workbook = Workbook.getWorkbook(file);
            Sheet sheet = workbook.getSheet(0);
            result = new int[sheet.getRows() - 1][sheet.getColumns() - 2];
            for (int i = 1; i < sheet.getRows(); i++) {
                for (int j = 2; j < sheet.getColumns(); j++) {
                    Cell cell = sheet.getCell(j, i);
                    result[i - 1][j - 2] = Integer.parseInt(cell.getContents());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(Arrays.deepToString(result));
    }

    public void startImport() {
        DBConnector t = new DBConnector();
        int[] int_args = new int[]{this.id};
        String[] str = null;
        try {
            str = new String[]{courseName, DBConnector.getSeme(), courseTime};
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setLocation(400, 250);
        fileChooser.setCurrentDirectory(new File("."));
        fileChooser.setSelectedFile(new File("Untitled.xls"));
        fileChooser.setFileFilter(new FileNameExtensionFilter(null, "xls"));
        fileChooser.showOpenDialog(null);
        File file = fileChooser.getSelectedFile();
        importTable(file);
        try {
            t.teacherEnterResultEnd(int_args, str, result);
        } catch (CustomException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        JOptionPane.showMessageDialog(null, "成绩录入成功", "提示", JOptionPane.INFORMATION_MESSAGE);
    }
}