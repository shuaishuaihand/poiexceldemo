/* 
 *文 件 名:  aaa
 * 版    权:  Anhui Shixu Intelligent Technology CO.,LTD. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  yx
 * 修改时间:  2017年09月22日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */

package com.agesun.mybatis.util;

import org.apache.poi.hssf.usermodel.*;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

/**
 * <一句话功能简述> <功能详细描述>
 *
 * @author yx
 * @version [版本号, 2017年09月22日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class Export<T>
{
    public void Export(String[] headers, String fileName, List<T> dataset, OutputStream outputStream)
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        // 声明一个工作薄
        HSSFWorkbook wb = new HSSFWorkbook();
        // 声明一个单子并命名
        HSSFSheet sheet = wb.createSheet(fileName);
        
        /****************** 头部样式 ***********************/
        HSSFFont font = wb.createFont();
        font.setFontName("微软雅黑");
        font.setFontHeightInPoints((short)14);// 设置字体大小
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 给单子名称一个长度
        sheet.setDefaultColumnWidth((short)16);
        // 生成一个样式
        HSSFCellStyle style = wb.createCellStyle();
        /*style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中*/
        style.setFont(font);
        /********************* 数据单元格样式 **************************/
        HSSFFont rowFont = wb.createFont();
        rowFont.setFontName("宋体");
        rowFont.setFontHeightInPoints((short)14);// 设置字体大小
        // 给单子名称一个长度
        sheet.setDefaultColumnWidth((short)16);
        // 生成一个样式
        HSSFCellStyle rowStyle = wb.createCellStyle();
        rowStyle.setFont(rowFont);
        // 样式字体居中
        rowStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        
        // 创建第一行（也可以称为表头）
        HSSFRow row = sheet.createRow(0);
        row.setHeight((short)500);
        // 给表头第一行一次创建单元格
        for (short i = 0; i < headers.length; i++)
        {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(style);
        }
        
        // 遍历集合数据，产生数据行
        Iterator<T> it = dataset.iterator();
        int index = 0;
        while (it.hasNext())
        {
            index++;
            row = sheet.createRow(index);
            row.setHeight((short)500);
            T t = it.next();
            // 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
            Field[] fields = t.getClass().getDeclaredFields();
            for (short i = 0; i < headers.length; i++)
            {
                Field field = fields[i];
                String fieldName = field.getName();
                String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                Class tCls = t.getClass();
                Method getMethod = tCls.getMethod(getMethodName, new Class[] {});
                Object value = getMethod.invoke(t, new Object[] {});
                // 判断值的类型后进行强制类型转换
                String textValue = null;
                // 其它数据类型都当作字符串简单处理
                if (value != null && !"".equals(value))
                {
                    textValue = value.toString();
                }
                if (textValue != null)
                {
                    HSSFCell cell = row.createCell(i);
                    cell.setCellStyle(rowStyle);
                    cell.setCellValue(textValue);
                }
            }
        }
        
        try
        {
            this.getExportedFile(wb, outputStream);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     *
     * 方法说明: 指定路径下生成EXCEL文件
     *
     * @return
     */
    public void getExportedFile(HSSFWorkbook workbook, OutputStream outputStream)
        throws Exception
    {
        try
        {
            workbook.write(outputStream);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
