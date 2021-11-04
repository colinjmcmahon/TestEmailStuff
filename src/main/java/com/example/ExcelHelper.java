package com.example;

import org.apache.commons.math3.analysis.function.Log;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.Iterator;
import java.util.logging.LogManager;

public class ExcelHelper {

    public static String fetchNextUnusedEmail() throws IOException {
        File excel = new File("src/main/resources/emailAddresses.xlsx");
        FileInputStream fis = new FileInputStream(excel);
        XSSFWorkbook book = new XSSFWorkbook(fis);
        XSSFSheet sheet = book.getSheetAt(0);
        String emailNotRegistered = "";

        Iterator<Row> itr = sheet.iterator();

        // go through rows
        while (itr.hasNext()) {
            Row row = itr.next();

            Cell emailAddress = row.getCell(0);
            Cell indicator = row.getCell(1);

            if (indicator.getStringCellValue().equals("not registered")) {
                emailNotRegistered = emailAddress.getStringCellValue();
                indicator.setCellValue("registered");
                break;
            }
        }
        //mark the email as registered in excel
        if(emailNotRegistered==""){
            System.out.println("There are no unregistered emails left - please get IAA database cleared or create new accounts and update emailAddresses.xlxs");
            return null;
        }else {
            FileOutputStream os = new FileOutputStream(excel);
            book.write(os);
            System.out.println("Next available unregistered email: " + emailNotRegistered);
            return emailNotRegistered;
        }
    }

}
