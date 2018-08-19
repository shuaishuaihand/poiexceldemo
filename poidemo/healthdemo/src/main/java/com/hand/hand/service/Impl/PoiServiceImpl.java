package com.hand.hand.service.Impl;

import com.hand.hand.domain.Person;
import com.hand.hand.mapper.PersonMapper;
import com.hand.hand.mapper.PoiMapper;
import com.hand.hand.service.PoiService;
import com.hand.hand.util.Json;
import com.hand.hand.util.PageData;
import com.hand.hand.util.ReadExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Service
public class PoiServiceImpl implements PoiService {

    @Autowired
    private PoiMapper poiMapper;

    @Resource
    private PersonMapper personMapper;

    /**
     * 导入模板
     */

    @Override
    public Json readExcel(InputStream inputStream, String fileName) {
        Json json = new Json();
        ReadExcel getExcelInfo = new ReadExcel();
        List<Map<String, Object>> mapList = getExcelInfo.getExcelInfo(inputStream, fileName);// 获取附件中数据

        if (mapList == null || mapList.isEmpty()) {
            json.setResult(1);
            json.setMsg("导入失败");
        } else {

            try {

                InsertMysql(mapList);// 新增到数据库中
                json.setResult(0);
                json.setMsg("导入成功");
                return json;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return json;
    }

    //存入数据库
    @Transactional
    public void InsertMysql(List<Map<String, Object>> mapList)
            throws Exception {
        try {
            PageData pd = new PageData();
            Person person=new Person();
            for (Map<String, Object> map : mapList) {
                //姓名
                String p_name = String.valueOf(map.get("p_name"));
                pd.put("p_name", p_name);
                String card_no = String.valueOf(map.get("card_no"));
                String temp_cardid= poiMapper.selectcardidbyname(p_name);
                if(card_no.equals("null")){

                    if(temp_cardid!=null){
                        pd.put("card_no",temp_cardid);
                    }
                    if (temp_cardid==null){
                        pd.put("card_no","");
                    }
                }else {
                    pd.put("card_no", card_no);
                }

                //班次名称
                String classes_name= String.valueOf(map.get("classes_name"));
                pd.put("classes_name", classes_name);
                //上下班标记0上班 下班1 attend_flag
                Integer attend_flag=Integer.parseInt(String.valueOf(map.get("attend_flag")));
                pd.put("attend_flag", attend_flag);

                //考勤时间
                String classes_time = String.valueOf(map.get("classes_time"));
                pd.put("classes_time", classes_time);
                //打卡时间
                String attend_time = String.valueOf(map.get("attend_time"));
                pd.put("attend_time", attend_time);
                //打卡地址
                String dev_address = String.valueOf(map.get("dev_address"));
                pd.put("dev_address", dev_address);
                //打卡结果
                Integer attend_outcome = Integer.valueOf(map.get("attend_outcome").toString());
                pd.put("attend_outcome", attend_outcome);
                //每日工作时间
                Float work_time =Float.parseFloat(String.valueOf(map.get("work_time")));
                pd.put("work_time", work_time);
                //加班，迟到，早退时间
                Float attend_flag_time =Float.parseFloat(String.valueOf(map.get("attend_flag_time")));
                pd.put("attend_flag_time", attend_flag_time);


                //插入样本表
                //判断表中是否有该样本，有则不插入，无则插入
                int num=poiMapper.slectcountperson(p_name);
                if(num<=0){
                    //插入样本
                    person.setPersonName(p_name);
                    person.setCardNo(card_no);
                    person.setAddress(dev_address);
                    //样本状态state
                    person.setState(0);
                    personMapper.insertSample(person);
                }

                //根据样本名称，查询pid
                Integer pid=poiMapper.selectpidbyname(p_name);
                pd.put("p_id",pid);

                //根据班次名称，查询class_id
                Integer cid= poiMapper.selectcidbyname(classes_name);
                pd.put("classes_id",cid);
                //插入考勤记录表
                poiMapper.insertmysql(pd);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
