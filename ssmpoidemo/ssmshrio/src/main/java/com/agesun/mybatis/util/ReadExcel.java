/*
 *文 件 名:  ReadExcel
 * 版    权:  Anhui Shixu Intelligent Technology CO.,LTD. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  yx
 * 修改时间:  2018年07月31日
 */

package com.agesun.mybatis.util;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 组织机构导入
 *
 * @author ns
 * @date 2018年07月31日
 */
@Component
public class ReadExcel {
    // 总行数
    private int totalRows = 0;

    // 总条数
    private int totalCells = 0;

    // 错误信息接收器
    private String errorMsg;

    // 构造方法
    public ReadExcel() {
    }

    // 获取总行数
    public int getTotalRows() {
        return totalRows;
    }

    // 获取总列数
    public int getTotalCells() {
        return totalCells;
    }

    // 获取错误信息
    public String getErrorInfo() {
        return errorMsg;
    }


    public static ReadExcel readExcel;

    @PostConstruct
    public void init(){
        readExcel=this;
    }

    /**
     * 读EXCEL文件，获取信息集合
     *
     * @param inputStream 文件流
     * @param fileName    文件名
     * @return
     */
    public List<Map<String, Object>> getExcelInfo(InputStream inputStream, String fileName) {
        List<Map<String, Object>> empList = new ArrayList<Map<String, Object>>();
        try {
            if (!validateExcel(fileName)) {// 验证文件名是否合格
                return null;
            }
            boolean isExcel2003 = true;// 根据文件名判断文件是2003版本还是2007版本
            if (isExcel2007(fileName)) {
                isExcel2003 = false;
            }
            empList = createExcel(inputStream, isExcel2003);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return empList;
    }

    /**
     * 根据excel里面的内容读取客户信息
     *
     * @param is          输入流
     * @param isExcel2003 excel是2003还是2007版本
     * @return
     * @throws IOException
     */
    public List<Map<String, Object>> createExcel(InputStream is, boolean isExcel2003) {
        List<Map<String, Object>> empList = new ArrayList<Map<String, Object>>();
        try {
            Workbook wb = null;
            if (isExcel2003) {// 当excel是2003时,创建excel2003
                wb = new HSSFWorkbook(is);
            } else {// 当excel是2007时,创建excel2007
                wb = new XSSFWorkbook(is);
            }
            empList = readExcelValue(wb);// 读取Excel里面客户的信息
        } catch (IOException e) {
            e.printStackTrace();
        }
        return empList;
    }

    /**
     * 读取Excel里面客户的信息
     *
     * @param wb
     * @return
     */
    @Transactional
    public List<Map<String, Object>> readExcelValue(Workbook wb) {
        Boolean dealresult = false;
        // 得到第一个shell
        Sheet sheet = wb.getSheetAt(0);
        // 得到Excel的行数
        this.totalRows = sheet.getPhysicalNumberOfRows();
        // 得到Excel的列数(前提是有行数)
        if (totalRows > 1 && sheet.getRow(0) != null) {
            this.totalCells = sheet.getRow(2).getPhysicalNumberOfCells();
        }
        List<Map<String, Object>> empList = new ArrayList<Map<String, Object>>();
        for (int r = 1; r < totalRows; r++) {
            Map<String, Object> map = new HashMap<String, Object>();
           /* for (int c=0;r<totalCells;c++){*/
            Row row = sheet.getRow(r);
            Cell id =  row.getCell(0);
            map.put("id",id);
            Cell lastName =  row.getCell(1);
            map.put("lastName",lastName);
            Cell email =  row.getCell(2);
            map.put("email",email);
            Cell gender =  row.getCell(3);
            map.put("gender",gender);

            empList.add(map);

           /* }*/


        }
        return empList;
    }


    /**
     * 验证EXCEL文件
     *
     * @param filePath
     * @return
     */
    public boolean validateExcel(String filePath) {
        if (filePath == null || !(isExcel2003(filePath) || isExcel2007(filePath))) {
            errorMsg = "文件名不是excel格式";
            return false;
        }
        return true;
    }

    // @描述：是否是2003的excel，返回true是2003
    public static boolean isExcel2003(String filePath) {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }

    // @描述：是否是2007的excel，返回true是2007
    public static boolean isExcel2007(String filePath) {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }


}