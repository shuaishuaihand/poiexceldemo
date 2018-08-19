package com.agesun.mybatis.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * 下载
 *
 * @author nishuai
 * @version [版本号, 2018年07月26日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class Download
{
    /**
     * 下载文件
     *
     * @param path 文件地址
     * @param response 输出
     * @param request 请求
     */
    public static void downloadTemplate(String path, HttpServletResponse response, HttpServletRequest request)
    {
        try
        {
            // path是指欲下载的文件的路径。
            File file = new File(path);
            // 取得文件名。
            String fileName = file.getName();
            // 取得文件的后缀名。
            String ext = fileName.substring(fileName.lastIndexOf(".") + 1).toUpperCase();

            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.setCharacterEncoding("utf-8");

            // 防止火狐下载文件名乱码
            if (request.getHeader("USER-AGENT").toLowerCase().contains("firefox"))// 火狐
            {
                response.setHeader("Content-Disposition",
                    "attachment;fileName=" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
            }
            else// 谷歌
            {
                response.setHeader("Content-Disposition",
                    "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));
            }
            // response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        }
        catch (IOException ex)
        {
            //TODO 异常处理
            ex.printStackTrace();
        }
    }
}
