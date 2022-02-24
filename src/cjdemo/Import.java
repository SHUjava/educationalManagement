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
    JButton buttonImport,buttonConfirm;
    Object[][] preRes;
    int[][] result;
    int id;
    int flag = JFileChooser.CANCEL_OPTION;
    String courseName;
    File file;

    public void setCourse(String cn) {
        courseName = cn;
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

    public JButton getButtonConfirm(){
        buttonConfirm = new JButton("确认");
        buttonConfirm.setContentAreaFilled(false);
        buttonConfirm.setFont(new java.awt.Font("Dialog", 1, 12));
        buttonConfirm.setPreferredSize(new Dimension(70, 30));
        buttonConfirm.addActionListener(e ->{
            realImport();
        });
        return buttonConfirm;
    }

    public Object[][] readTable() {
        try {
            Workbook workbook = Workbook.getWorkbook(file);
            Sheet sheet = workbook.getSheet(0);
            preRes = new Object[sheet.getRows()-1][sheet.getColumns()];
            for (int i = 1; i < sheet.getRows(); i++) {
                for (int j = 0; j < sheet.getColumns(); j++) {
                    Cell cell = sheet.getCell(j, i);
                    preRes[i-1][j] = cell.getContents();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(Arrays.deepToString(preRes));
        return preRes;
    }

    public void importTable() {
        try {
            Workbook workbook = Workbook.getWorkbook(file);
            Sheet sheet = workbook.getSheet(0);
            result = new int[sheet.getColumns() - 2][sheet.getRows() - 1];
            for (int i = 1; i < sheet.getRows(); i++) {
                for (int j = 2; j < sheet.getColumns(); j++) {
                    Cell cell = sheet.getCell(j, i);
                    result[j - 2][i - 1] = Integer.parseInt(cell.getContents());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(Arrays.deepToString(result));
    }

    public void startImport() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setLocation(400, 250);
        fileChooser.setCurrentDirectory(new File("."));
        fileChooser.setSelectedFile(new File("Untitled.xls"));
        fileChooser.setFileFilter(new FileNameExtensionFilter(null, "xls"));
        flag = fileChooser.showOpenDialog(null);
        file = fileChooser.getSelectedFile();
        System.out.println(file);
        if (flag == JFileChooser.APPROVE_OPTION) {
            JOptionPane.showMessageDialog(null, "成绩导入成功，请再次确认", "提示", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void realImport(){
        importTable();
        DBConnector t = new DBConnector();
        int[] int_args = new int[]{this.id};
        String[] str;
        str = new String[]{courseName};
        try {
            t.teacherEnterResultEnd(int_args, str, result);
        } catch (CustomException | SQLException ex) {
            ex.printStackTrace();
        }
        JOptionPane.showMessageDialog(null, "成绩录入成功", "提示", JOptionPane.INFORMATION_MESSAGE);
    }
}