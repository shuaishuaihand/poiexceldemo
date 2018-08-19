package com.hand.hand.util;
import com.hand.hand.mapper.PoiMapper;
import org.apache.ibatis.javassist.bytecode.stackmap.BasicBlock;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolver.iterator;

/**
 * 表格数据导入数据库
 *
 * @author nishuai
 * @date 2018年06月30日
 */
public class ReadExcel {

    @Autowired
    private PoiMapper poiMapper;

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



    /**
     * 读EXCEL文件，获取信息集合
     *
     * @param inputStream 文件流
     * @param fileName    文件名
     * @return
     */
    public List<Map<String, Object>> getExcelInfo(InputStream inputStream, String fileName) {
        List<Map<String, Object>> communityList = new ArrayList<Map<String, Object>>();
        try {
            if (!validateExcel(fileName)) {// 验证文件名是否合格
                return null;
            }
            boolean isExcel2003 = true;// 根据文件名判断文件是2003版本还是2007版本
            if (isExcel2007(fileName)) {
                isExcel2003 = false;
            }
            communityList = createExcel(inputStream, isExcel2003);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return communityList;
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
        List<Map<String, Object>> attendList = new ArrayList<Map<String, Object>>();
        try {
            Workbook wb = null;
            if (isExcel2003) {// 当excel是2003时,创建excel2003
                wb = new HSSFWorkbook(is);
            } else {// 当excel是2007时,创建excel2007
                wb = new XSSFWorkbook(is);
            }

            attendList = readExcelValue(wb);// 读取Excel里面客户的信息
        } catch (IOException e) {
            e.printStackTrace();
        }
        return attendList;
    }

    /**
     * 读取Excel里面客户的信息
     *
     * @param wb
     * @return
     */
    //4月份原始记录
    private List<Map<String, Object>> readExcelValue(Workbook wb) {
        List<Map<String, Object>> attendListdeleteUselessRecord= new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> attendListInsertEmptyRecord= new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> attendListInsertWorkTime = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> attendList1 = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> attendList2 = new ArrayList<Map<String, Object>>();

        //删除sheet2，打开无效，sheet1表中，没有的数据
        attendListdeleteUselessRecord=this.deleteSheet2UselessRecord(wb);

        //向sheet2中，下班考勤记录中插入空缺的考勤记录
       /* attendListInsertEmptyRecord=this.insertSheetEmptyRecord(wb,attendListdeleteUselessRecord);*/


        //向sheet2中，下班考勤记录中插入工作时长
       /* attendListInsertWorkTime=  this.insertSheetWorkTime(wb);*/

        // 得到第1个shell,目的为了获取工作时长
        Sheet sheet1 = wb.getSheetAt(1);
        attendList1 = this.readSheet0Value(sheet1);
        // 得到第3个shell
        attendList2 = this.insertSheetEmptyRecord(wb,attendListdeleteUselessRecord);


            for (Map<String, Object> map2 :attendList2) {
                //sheet中2的员工名称pname
                String pname2 = String.valueOf(map2.get("p_name"));
                //sheet中2的打卡时间attend_time
                String attend_time=String.valueOf(map2.get("attend_time"));
                //截取例如日期为18-04-05 08:19，的前一段2018-04-05
                String param[] = attend_time.split(" ");
                String attendtime1 = param[0];  //格式 2018-04-05
                String attendtime2 = param[1]; //格式 08:19
                //上下班标识
                Integer attend_flag=Integer.parseInt(String.valueOf(map2.get("attend_flag")));

                for (Map<String, Object> map1 : attendList1) {
                    //sheet中1的员工名称pname
                    String pname1 = String.valueOf(map1.get("p_name"));
                    //sheet1中的考勤日期attend_date
                    String attend_date = String.valueOf(map1.get("attend_date"));
                    //sheet1中上班打卡时间
                    String attend_time_on= String.valueOf(map1.get("attend_time_on"));
                    //sheet1中下班打卡时间
                    String attend_time_off = String.valueOf(map1.get("attend_time_off"));
                    //sheet1中上班班打卡结果（为了获取迟到时间）
                    String am_outcome = String.valueOf(map1.get("am_outcome"));
                    //迟到时间
                    Float late_time = Float.parseFloat(String.valueOf(map1.get("late_time")));
                    //sheet1中下班打卡结果（为了获取早退时间）
                    String pm_outcome = String.valueOf(map1.get("pm_outcome"));
                    //早退时间
                    Float leave_time = Float.parseFloat(String.valueOf(map1.get("leave_time")));

                    //sheet1中的工作时长
                    Float work_time = Float.parseFloat(String.valueOf(map1.get("work_time")));

                    //判断姓名和工作日是否相等，向考勤记录里，插入相应数据
                   if(pname1.equals(pname2)&&attend_date.equals(attendtime1)){

                       //判断姓名和工作日是否相等，向考勤记录里，插入工作时长
                       if(attend_flag==1) {
                               map2.put("work_time", work_time);

                           //根据上下班打卡结果，插入考勤数据
                           //如果迟到，则插入迟到时间
                           //如果早退，则插入早退时间
                           if(pm_outcome.contains("早退")){
                               map2.put("attend_flag_time",leave_time);
                           }else{
                               map2.put("attend_flag_time",0);
                           }

                         break;
                       }


                     /*  if(attend_flag==0){
                           map2.put("work_time", 0);
                           if(am_outcome.contains("出差")&&(attend_time_on.compareTo("08:00:00")==)){
                               map2.put("attend_flag_time",late_time);
                           }
                           if(am_outcome.contains("")){

                           } else {

                               map2.put("attend_flag_time", 0);
                           }
                           break;
                       }*/

                       SimpleDateFormat sdf2=new SimpleDateFormat("HH:mm:ss");
                       Date date2= null;
                       String attend_time_on_temp=null;
                       int i=0;
                       try {
                           if(!attend_time_on.equals("")) {
                               if (attend_time_on.length() == 5) {
                                   attend_time_on_temp = attend_time_on + ":00";
                               }
                               if (attend_time_on.length() == 8) {
                                   attend_time_on_temp = attend_time_on;
                               }
                               date2 = sdf2.parse(attend_time_on_temp);
                               Date date1 = sdf2.parse("08:30:00");
                               i = date2.compareTo(date1);
                           }
                       } catch (ParseException e) {
                           e.printStackTrace();
                       }


                       if(attend_flag==0){
                           map2.put("work_time", 0);
                           if(am_outcome.contains("出差")&&(i==1)&&(!attend_time_on.equals(""))){
                               map2.put("attend_outcome",1);

                           }if(am_outcome.contains("请假")&&(i==1)&&(!attend_time_on.equals(""))){
                               map2.put("attend_outcome",1);

                           }
                           if(am_outcome.contains("迟到")){
                               map2.put("attend_flag_time",late_time);
                           }
                           else {

                               map2.put("attend_flag_time", 0);
                           }
                           break;
                       }



                   }
               }

          }

         return  attendList2;
    }


    //删除sheet2，打卡无效，sheet1表中，没有的数据
    private List<Map<String, Object>> deleteSheet2UselessRecord(Workbook wb) {
        List<Map<String, Object>> attendList1 = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> attendList2 = new ArrayList<Map<String, Object>>();



        // 得到第1个shell,目的为了获取工作日，的打卡时间
        Sheet sheet1 = wb.getSheetAt(1);
        attendList1 = this.readSheet0Value(sheet1);
        // 得到第3个shell
        Sheet sheet2 = wb.getSheetAt(2);
        attendList2 = this.readSheet2Value(sheet2);

        Iterator<Map<String,Object>> attendList2iter=attendList2.iterator();
        while (attendList2iter.hasNext()){
            Map<String, Object> map2=attendList2iter.next();
      /*  for (Map<String, Object> map2 : attendList2) {*/

            //sheet中2的员工名称pname
            String pname2 = String.valueOf(map2.get("p_name"));
            //sheet中2的打卡时间attend_time
            String attend_time = String.valueOf(map2.get("attend_time"));
            //截取例如日期为18-04-05 08:19，的前一段2018-04-05
            String param[] = attend_time.split(" ");
            String attendtime1 = param[0];  //格式 2018-04-05
            String attendtime2 = param[1]; //格式 08:19
            //上下班标识
            Integer attend_flag = Integer.parseInt(String.valueOf(map2.get("attend_flag")));


            for (Map<String, Object> map1 : attendList1) {
                //sheet中1的员工名称pname
                String pname1 = String.valueOf(map1.get("p_name"));
                //sheet中的考勤日期attend_date
                String attend_date = String.valueOf(map1.get("attend_date"));
                //sheet中上班打卡时间
                String attend_time_on = String.valueOf(map1.get("attend_time_on"));
                //sheet中下班打卡时间
                String attend_time_off = String.valueOf(map1.get("attend_time_off"));
                //sheet1中上班班打卡结果（为了获取迟到时间）
                String am_outcome = String.valueOf(map1.get("am_outcome"));
                //sheet1中下班打卡结果（为了获取早退时间）
                String pm_outcome = String.valueOf(map1.get("pm_outcome"));
                //sheet中的工作时长
                Float work_time = Float.parseFloat(String.valueOf(map1.get("work_time")));


                //判断姓名和工作日是否相等，删除sheet1中的无效数据
                if (pname1.equals(pname2) && attend_date.equals(attendtime1)) {
                    //上下班标识
                    if(attend_flag==0) {
                        //sheet1上班打卡时间，为空，或者两表时间不等，删除sheet2数据
                        if (!attend_time_on.equals(attendtime2)) {
                            attendList2iter.remove();
                            /*    attendList2.remove(map2);*/


                            break;

                        }

                    }
                    if(attend_flag==1){
                        //解决数据异常情况，如上午出差，打卡，出勤记录出现在下午
                        /*if(pm_outcome.contains("出差")&&(!attend_time_on.equals(""))){
                            map2.put("attend_flag",0);
                          break;
                        }*/

                       //sheet1下班打卡时间，为空，或者两表时间不等，删除sheet2数据
                      if (!attend_time_off.equals(attendtime2)) {


                          //解决数据异常情况，如上午打卡，出勤记录出现在下午
                          if(attendtime2.equals(attend_time_on)){
                              map2.put("attend_flag",0);

                              //异常情况，迟到为下午记录（迟到记录在下午)
                              if(am_outcome.contains("迟到")){
                                  map2.put("attend_outcome",1);
                              }
                              break;
                          }else {

                              attendList2iter.remove();
                              /* attendList2.remove(map2);*/
                          }
                          break;


                      }

                    }



                }


            }

        }

        return attendList2;
    }


    //向sheet2中，下班考勤记录中插入空缺的考勤记录
    private List<Map<String, Object>> insertSheetEmptyRecord(Workbook wb,  List<Map<String, Object>> attendList2) {
        List<Map<String, Object>> attendList1 = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> attendList3 = new ArrayList<Map<String, Object>>();
        //map3
        // 得到第1个shell,目的为了获取工作日，空缺的打卡时间
        Sheet sheet1 = wb.getSheetAt(1);
        attendList1 = this.readSheet0Value(sheet1);
        // 得到第3个shell attendList2


        for (Map<String, Object> map1 : attendList1) {
            //sheet中1的员工名称pname
            String pname1 = String.valueOf(map1.get("p_name"));
            //sheet中的考勤日期attend_date
            String attend_date = String.valueOf(map1.get("attend_date"));
            //sheet中上班打卡时间
            String attend_time_on = String.valueOf(map1.get("attend_time_on"));
            //sheet中下班打卡时间
            String attend_time_off = String.valueOf(map1.get("attend_time_off"));
            //sheet1中上班班打卡结果（为了获取迟到时间）
            String am_outcome = String.valueOf(map1.get("am_outcome"));
            //sheet1中下班打卡结果（为了获取早退时间）
            String pm_outcome = String.valueOf(map1.get("pm_outcome"));
            //sheet中的工作时长
            Float work_time = Float.parseFloat(String.valueOf(map1.get("work_time")));
            //sheet1中的班次字符串
            String classString= String.valueOf(map1.get("classString"));
            Map<String,Object> map3=new HashMap<>();
            Map<String,Object> map4=new HashMap<>();
            int flag=0;
            Integer pid=null;


           /* //根据姓名查询用户的工号
            System.out.println(pname1);
            pid= poiMapper.selectpidbyname(pname1);
            System.out.println(pid);
            *//*if(pid.equals()){*//*
                System.out.println("--------------------------------------");
            *//*}*//*
*/


            //如果这天上下打卡结果都为缺卡，说明打卡记录list中，没有记录，则没必要，执行循环，直接添加两条缺卡记录就可以了
            //记录基本都为周六
            if (am_outcome.contains("缺卡")&&pm_outcome.contains("缺卡")) {
                //姓名
                map3.put("p_name",pname1);

               //工号
                /*map3.put("card_no",String.valueOf(pid));*/
                //考勤时间
                map3.put("classes_time", "09:00:00");
                //上下班标记0上班 下班1 attend_flag
                map3.put("attend_flag", 0);
                //班次名称
                map3.put("classes_name","B");
                //打卡时间,补卡过后的当日上班打卡时间
                String attendtime_am=attend_date+" "+"09:00:00";
                map3.put("attend_time",attendtime_am);
                //打卡地址
                map3.put("dev_address","安徽时旭智能科技有限公司");
                //打卡结果
                map3.put("attend_outcome",4);
                //工时
                map3.put("work_time",0);

                //姓名
                map4.put("p_name",pname1);
                //工号
              /*  map4.put("card_no",String.valueOf(pid));*/
                //考勤时间
                map4.put("classes_time", "11:00:00");
                //上下班标记0上班 下班1 attend_flag
                map4.put("attend_flag", 1);
                //班次名称
                map4.put("classes_name","B");
                //打卡时间,补卡过后的当日上班打卡时间
                String attendtime_am2=attend_date+" "+"11:00:00";
                map4.put("attend_time",attendtime_am2);
                //打卡地址
                map4.put("dev_address","安徽时旭智能科技有限公司");
                //打卡结果
                map4.put("attend_outcome",4);
                //工时
                map4.put("work_time",0);


                attendList3.add(map3);
                attendList3.add(map4);
                continue;
            }


            for (Map<String, Object> map2 : attendList2) {
                //sheet中2的员工名称pname
                String pname2 = String.valueOf(map2.get("p_name"));
                //sheet中2的打卡时间attend_time
                String attend_time = String.valueOf(map2.get("attend_time"));
                //截取例如日期为18-04-05 08:19，的前一段2018-04-05
                String param[] = attend_time.split(" ");
                String attendtime1 = param[0];  //格式 2018-04-05
                String attendtime2 = param[1]; //格式 08:19
                //上下班标识
                Integer attend_flag = Integer.parseInt(String.valueOf(map2.get("attend_flag")));





                //判断姓名和工作日是否相等，向考勤记录里，插入空的上班考勤记录
                if (pname1.equals(pname2) && attend_date.equals(attendtime1)) {
                        //上午缺卡，下午不缺卡
                        if (attend_time_on.equals("")&&(!attend_time_off.equals(""))) {
                            //姓名
                            map3.put("p_name",pname2);
                            //工号
                            map3.put("card_no",String.valueOf(map2.get("card_no")));
                            //考勤时间
                            map3.put("classes_time", "8:30:00");
                            //上下班标记0上班 下班1 attend_flag
                            map3.put("attend_flag", 0);
                            //班次名称
                            map3.put("classes_name",String.valueOf(map2.get("classes_name")));
                            //打卡时间,补卡过后的当日上班打卡时间
                            String attendtime_am=attendtime1+" "+"8:30:00";
                            map3.put("attend_time",attendtime_am);
                            //打卡地址
                            map3.put("dev_address",String.valueOf(map2.get("dev_address")));
                            //打卡结果
                            map3.put("attend_outcome",4);
                            //工时
                            map3.put("work_time",0);
                            attendList3.add(map3);
                            attendList3.add(map2);
                            break;
                        }


                    //上午不缺卡，下午缺卡
                    if((!attend_time_on.equals(""))&&attend_time_off.equals("")) {
                        //姓名
                        map3.put("p_name",pname2);
                        //工号
                        map3.put("card_no",String.valueOf(map2.get("card_no")));
                        //考勤时间
                        map3.put("classes_time","17:30:00");
                        //上下班标记0上班 下班1 attend_flag
                        map3.put("attend_flag", 1);
                        //打卡时间,补卡过后的当日上班打卡时间
                        String attendtime_pm=attendtime1+" "+"17:30:00";
                        map3.put("attend_time",attendtime_pm);
                        //班次名称
                        map3.put("classes_name",String.valueOf(map2.get("classes_name")));
                        //打卡地址
                        map3.put("dev_address",String.valueOf(map2.get("dev_address")));
                        //打卡结果
                        map3.put("attend_outcome",4);
                        //工时
                        map3.put("work_time",0);
                        attendList3.add(map2);
                        attendList3.add(map3);
                        break;
                    }
                    if((!attend_time_on.equals(""))&&(!attend_time_off.equals(""))){

                        attendList3.add(map2);


                        if(flag==1){
                            break;
                        }
                        flag++;

                    }

                }


            }




        }

        return attendList3;
    }



    //向sheet2中，下班考勤记录中插入工作时长
    /*private List<Map<String, Object>> insertSheetWorkTime(Workbook wb) {
        List<Map<String, Object>> attendList1 = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> attendList2 = new ArrayList<Map<String, Object>>();


        // 得到第1个shell,目的为了获取工作时长
        Sheet sheet1 = wb.getSheetAt(1);
        attendList1 = this.readSheet0Value(sheet1);
        // 得到第3个shell
        Sheet sheet2 = wb.getSheetAt(2);
        attendList2 = this.readSheet2Value(sheet2);


        for (Map<String, Object> map2 : attendList2) {
            //sheet中2的员工名称pname
            String pname2 = String.valueOf(map2.get("p_name"));
            //sheet中2的打卡时间attend_time
            String attend_time = String.valueOf(map2.get("attend_time"));
            //截取例如日期为18-04-05 08:19，的前一段2018-04-05
            String param[] = attend_time.split(" ");
            String attendtime = param[0];
            //上下班标识
            Integer attend_flag = Integer.parseInt(String.valueOf(map2.get("attend_flag")));

            for (Map<String, Object> map1 : attendList1) {
                //sheet中1的员工名称pname
                String pname1 = String.valueOf(map1.get("p_name"));
                //sheet中的考勤日期attend_date
                String attend_date = String.valueOf(map1.get("attend_date"));
                //sheet中上班打卡时间
                String attend_time_on = String.valueOf(map1.get("attend_time_on"));
                //sheet中下班打卡时间
                String attend_time_off = String.valueOf(map1.get("attend_time_off"));
                //sheet中的工作时长
                Float work_time = Float.parseFloat(String.valueOf(map1.get("work_time")));


                //判断姓名和工作日是否相等，向考勤记录里，插入工作时长
                if (pname1.equals(pname2) && attend_date.equals(attendtime)) {
                    if (attend_flag == 1) {
                        map2.put("work_time", work_time);
                        *//* attendList2.add(map2);*//*
                        break;
                    } else if (attend_flag == 0) {
                        map2.put("work_time", 0);
                        break;
                    }
                }
            }

        }

        return attendList2;
    }
*/
    /*

     得到第1个shell表里的数据

    */
    private List<Map<String, Object>> readSheet0Value(Sheet sheet1) {
        this.totalRows = sheet1.getPhysicalNumberOfRows();
         // 得到Excel的列数(前提是有行数)
        if (totalRows > 1 && sheet1.getRow(0) != null) {
            this.totalCells = sheet1.getRow(2).getPhysicalNumberOfCells();
        }
        List<Map<String, Object>> attend1List = new ArrayList<Map<String, Object>>();
        for (int r = 4; r < this.totalRows ; r++) {
            Map<String, Object> map = new HashMap<String, Object>();
            Row  row = sheet1.getRow(r);
            //姓名
            Cell  p_name=  row.getCell(0);
            map.put("p_name",p_name);
            //出勤日期
            Cell  attend_date=row.getCell(1);
            //截取18-04-05 星期四，的前一段18-04-05
            String param[] = String.valueOf(attend_date).split(" ");
            String attenddate ="20"+ param[0];
            map.put("attend_date",attenddate);
            //上班打卡时间
            Cell  attend_time_on=  row.getCell(3);
            map.put("attend_time_on",attend_time_on);
            //上班打卡结果（为了获取迟到时间）
            Cell  am_outcome=  row.getCell(4);
            map.put("am_outcome",am_outcome);
            //迟到时间
            Cell  late_time=  row.getCell(16);
            map.put("late_time",late_time);
            //下班打卡时间
            Cell  attend_time_off=  row.getCell(5);
            map.put("attend_time_off",attend_time_off);
            //下班打卡结果（为了获取迟到时间）
            Cell  pm_outcome=  row.getCell(6);
            map.put("pm_outcome",pm_outcome);
            //早退时间
            Cell  leave_time=  row.getCell(21);
            map.put("leave_time",leave_time);
            //工作时长
            Cell  work_time=row.getCell(14);
            //以分钟为单位，转化为以小时为单位
            Float worktime= Float.parseFloat(String.valueOf(work_time))/60;
            map.put("work_time",worktime);

            //班次，若为休息，直接跳出循环
            Cell  classString=row.getCell(2);
            map.put("classString",classString);
            if (String.valueOf(classString).equals("休息")){
                //跳过当前循环，进入下一循环
                continue;
            }



            attend1List.add(map);

        }
        return attend1List;
    }


    /*

    得到第3个shell表里的数据

   */
    private List<Map<String, Object>> readSheet2Value(Sheet sheet2) {

        // 得到Excel的行数
        this.totalRows = sheet2.getPhysicalNumberOfRows();
        /*   this.totalCells = sheet.getLastRowNum();*/
        // 得到Excel的列数(前提是有行数)
        if (totalRows > 1 && sheet2.getRow(0) != null) {
            this.totalCells = sheet2.getRow(2).getPhysicalNumberOfCells();
        }
        List<Map<String, Object>> attend2List = new ArrayList<Map<String, Object>>();

        for (int r = 3; r < this.totalRows ; r++) {
            Map<String, Object> map = new HashMap<String, Object>();
            Row  row = sheet2.getRow(r);
            //姓名
            Cell  p_name=  row.getCell(0);
            map.put("p_name",p_name);
            //工号
            Cell  card_no=  row.getCell(2);
            map.put("card_no",card_no);
            //考勤时间
            Cell  classes_time=  row.getCell(5);
            String classtime = null;
            if(!String.valueOf(classes_time).equals("")) {
                //对考勤时间处理，将2018-04-04 08:30转化为08:30:00
                String param[] = String.valueOf(classes_time).split(" ");
                classtime = param[1] + ":00";
                map.put("classes_time", classtime);


            }else if(String.valueOf(classes_time).equals("")){
                //跳过当前循环，进入下一循环
                continue;
            }
            //打卡时间
            Cell attend_time=  row.getCell(6);
            map.put("attend_time",attend_time);


            //4月份根据考勤时间计算是周几，周一至五（C班次）周六（B班次）
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            //获取是是周几
            Calendar c = Calendar.getInstance();
            //初始化节假日日期
            String holidaysunday="2018-04-08";  //节假日为周日
            String holidaysaturday="2018-04-28"; //节假日为周六
            int dayForWeek = 0;
            try {
                c.setTime(sdf.parse(String.valueOf(attend_time)));
                if(c.get(Calendar.DAY_OF_WEEK) == 1){
                    dayForWeek = 7;
                }else{
                    dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
                }
                if(dayForWeek>=1&&dayForWeek<=5){
                    //周一至五（C班次）
                    map.put("classes_name","C");
                }
                //周六为节假日后加班的话，需要做处理，是的话为C，否的话B
                if(dayForWeek==6){
                    //将年月日格式的string转化为date类型的日期
                    Date date=sdf.parse(String.valueOf(attend_time));
                    Date holidaydate=sdf.parse(String.valueOf(holidaysaturday));
                    if(date.compareTo(holidaydate)==0){
                        //周六正常上班（C班次）
                        map.put("classes_name","C");
                    }else{
                        //周六（B班次）
                        map.put("classes_name","B");
                    }

                }

                //周日为节假日的话，需要做处理
                if(dayForWeek==7){
                    //将年月日格式的string转化为date类型的日期
                    Date date=sdf.parse(String.valueOf(attend_time));
                    Date holidaydate=sdf.parse(String.valueOf(holidaysunday));
                    if(date.compareTo(holidaydate)==0){
                        //周日正常上班（C班次）
                        map.put("classes_name","C");
                    }else{
                        //跳过当前循环，进入下一循环
                        continue;
                    }


                }
            } catch (ParseException e) {
                e.printStackTrace();
            }


            //上下班标记0上班 下班1 attend_flag
            //和中午12:00:00作比较，小于attend_flag=0，attend_flag=1
            //string=>date
            SimpleDateFormat sdf2=new SimpleDateFormat("HH:mm:ss");

            try {
                Date date2= sdf2.parse(classtime);
                Date date1= sdf2.parse("12:00:00");
                int i=  date2.compareTo(date1);
               /* if(String.valueOf(p_name).equals("曹春艳")&&String.valueOf(classes_time).equals("2018-04-02 17:30")&&String.valueOf(attend_time).equals("2018-04-02 12:35")){
                        map.put("attend_flag", 0);
                }else {*/
               //周一至周五，或者周日为工作日(班次为C)
                if(dayForWeek>=1&&dayForWeek<=5||dayForWeek==7) {
                    if (i == -1) {
                        map.put("attend_flag", 0);
                    } else if (i == 1) {
                        map.put("attend_flag", 1);
                    }
                    /* }*/
                }


                //周六以11点为界限，小于为上班记录，大于为下班记录
                Date date3= sdf2.parse("11:00:00");
                 i=  date2.compareTo(date3);
                //周六(班次为B)
                if(dayForWeek==6){

                    //将年月日格式的string转化为date类型的日期
                    Date date=sdf.parse(String.valueOf(attend_time));
                    Date holidaydate=sdf.parse(String.valueOf(holidaysaturday));

                    Date date5= sdf2.parse(classtime);
                    Date date4= sdf2.parse("12:00:00");
                    int m=  date5.compareTo(date4);
                    //节假日加班
                    if(date.compareTo(holidaydate)==0){
                        //周六正常上班（C班次）
                        if (m == -1) {
                            map.put("attend_flag", 0);
                        } else if (i == 1) {
                            map.put("attend_flag", 1);
                        }

                    }else{
                        //周六正常上班（B班次）
                        if (i == -1) {
                            map.put("attend_flag", 0);
                        }
                        else if(i == 0){
                            map.put("attend_flag", 1);
                        }
                        else if (i == 1) {
                            map.put("attend_flag", 1);
                        }
                    }



                }



            } catch (ParseException e) {
                e.printStackTrace();
            }




            //打卡设备
            /*Cell attend_time=  row.getCell(6);*/
            //打卡地址
            Cell  dev_address=row.getCell(8);
            map.put("dev_address",dev_address);
            //打卡结果
            Cell  attend_outcome=row.getCell(7);
            if(attend_outcome.equals("正常")){
                map.put("attend_outcome",0);
            }else if(String.valueOf(attend_outcome).equals("迟到")){
                map.put("attend_outcome",1);
            }else if(String.valueOf(attend_outcome).equals("早退")){
                map.put("attend_outcome",2);
            }else if(String.valueOf(attend_outcome).equals("打卡无效:此记录已被更新")){
                map.put("attend_outcome",0);
                /*//跳过当前循环，进入下一循环
                continue;*/
            }else {
                map.put("attend_outcome",0);
            }
            attend2List.add(map);

        }
        return attend2List;
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