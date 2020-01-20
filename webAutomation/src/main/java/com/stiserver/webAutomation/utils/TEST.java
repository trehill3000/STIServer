package com.stiserver.webAutomation.utils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;

import java.sql.SQLException;

import java.util.*;

public class TEST {

    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {

        ArrayList arList=null;
        ArrayList al=null;
        String fName = "C:\\Users\\UMS\\OneDrive - UMS\\SIServer\\Sites\\Active\\Columbia\\Network Analysis\\SEND\\Columbia_Network_Analysis_OPEN_01-16-2020.csv";
        String thisLine;
        FileInputStream fis = new FileInputStream(fName);
        DataInputStream myInput = new DataInputStream(fis);
        arList = new ArrayList<>();
        while ((thisLine = myInput.readLine()) != null)
        {
            al = new ArrayList<>();
            String[] strar = thisLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            Collections.addAll(al, strar);
            arList.add(al);
            System.out.println();
        }

        //CUSTOMIZE EXCEL SHEET
        try
        {
            XSSFWorkbook workbook = new XSSFWorkbook();

            //ADD STYLE
            XSSFSheet sheet = workbook.createSheet("Network Analysis");
            sheet.createFreezePane(0, 1); //freeeze








            //ADD DATA TO EXCEL
            for(int k=0;k<arList.size();k++)
            {
                List ardata = (ArrayList)arList.get(k);
                XSSFRow row = sheet.createRow(k);
                for(int p=0;p<ardata.size();p++)
                {
                    XSSFCell cell = row.createCell((short) p);
                    String data = ardata.get(p).toString();
                    if(data.startsWith("=")){
                        data=data.replaceAll("\"", "");
                        data=data.replaceAll("=", "");
                        cell.setCellValue(data);
                    }else if(data.startsWith("\"")){
                        data=data.replaceAll("\"", "");
                        cell.setCellValue(data);
                    }else{
                        data=data.replaceAll("\"", "");

                        cell.setCellValue(data);
                    }
                    //*/
                    //   cell.setCellValue(ardata.get(p).toString());
                }
                System.out.println();
            }





            XSSFCellStyle style = workbook.createCellStyle();
            style.setBorderTop(BorderStyle.valueOf((short) 6)); // double lines border
            style.setBorderBottom(BorderStyle.valueOf((short) 1)); // single line border
            XSSFFont font = workbook.createFont();
            font.setFontHeightInPoints((short) 15);
            font.setBold(true);;
            style.setFont(font);

            Row row = sheet.createRow(0);
            Cell cell0 = row.createCell(0);
            cell0.setCellValue("Nav Value");
            cell0.setCellStyle(style);
            for(int j = 0; j<=3; j++)
                row.getCell(j).setCellStyle(style);

            style= (XSSFCellStyle) row.getRowStyle();

            style.setFont(font);











            FileOutputStream fileOut = new FileOutputStream("C:\\Users\\UMS\\OneDrive - UMS\\SIServer\\Sites\\Active\\Columbia\\Network Analysis\\SEND\\test.xlsx");
            workbook.write(fileOut);
            fileOut.close();
            System.out.println("Your excel file has been generated");
        } catch ( Exception ex ) {
            ex.printStackTrace();
        } //main method ends
    }
}
