package com.hand.hand.controller;

/*import com.alibaba.fastjson.JSONArray;*/
import com.hand.hand.service.PoiService;
import com.hand.hand.util.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
@RequestMapping(value = "/execel")
public class PoiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PoiController.class);

    @Resource
    private PoiService poiService;

    /**
     * 导入表格数据
     */

    @RequestMapping(value = "/readExcel")
    @ResponseBody
    public Json readExcel()
    {
        Json json=new Json();

        String fileName ="考勤报表.xlsx" ;// 获取文件名
        // String fileName ="假期管理-模板 (4).xls" ;// 获取文件名
        InputStream inputStream = null;
        try
        {
            inputStream = new FileInputStream("C:/Users/xuyan/Desktop/时旭考勤记录/考勤报表.xlsx");
            //inputStream = new FileInputStream("C:/Users/xuyan/Downloads/假期管理-模板 (4).xlsx");

        }
        catch (IOException e)
        {
            LOGGER.error(e.getCause().getMessage());
            throw new RuntimeException("修改失败");
        }
        json = poiService.readExcel(inputStream, fileName);
        return json;
    }

   /* public static void main(String[] args) {
        String i="08:39:00";
        System.out.println(i.length());
    }*/
}
