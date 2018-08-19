package com.agesun.mybatis.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import com.agesun.mybatis.util.Json;
import org.apache.commons.io.IOUtils;
import com.agesun.mybatis.util.Export;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.LoggerFactory;
import com.agesun.mybatis.bean.Employee;
import com.agesun.mybatis.service.EmployeeService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
@RequestMapping(value = "/employee")
public class EmployeeController {
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

	
	@Autowired
	private EmployeeService employeeService;
	
	/*@Autowired
	private EmployeeMapper  employeeMapper;*/
	
	@RequestMapping("/getemps")
	public String  emps(Map<String,Object> map){
		//Map<String,Object> map
		System.out.println("getemps");
	List<Employee>	emps=employeeService.getEmps();
	map.put("allemps", emps);
		return "list";
		
	}


	/**
	 * 导出数据
	 */
	@RequestMapping("/Export")
	public void export(HttpServletRequest request, HttpServletResponse response){
		Export<Employee> ee = new Export<Employee>();
		List<Employee> equiplist = employeeService.getEmps();
		String [] headers = {"编号","姓名","邮件","性别"};
		String fileName = "员工列表数据";
		response.setContentType("application/x-msdownload");
		ServletOutputStream outputStream = null;
		try
		{
			fileName = fileName + ".xls";// 定义文件格式
			if (request.getHeader("USER-AGENT").toLowerCase().contains("firefox"))// 火狐
			{
				response.setHeader("Content-Disposition",
						"attachment;fileName=" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
			}
			else
			{
				response.setHeader("Content-Disposition",
						"attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));
			}
			outputStream = response.getOutputStream();
			try {
				ee.Export(headers, fileName, equiplist, outputStream);
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		catch (UnsupportedEncodingException e)
		{
			throw new RuntimeException("导出失败!");
		}
		catch (IOException e)
		{
			throw new RuntimeException("导出失败!");
		}
		catch (NoSuchMethodException e)
		{
			LOGGER.error(e.getMessage());
		}
		catch (IllegalAccessException e)
		{
			LOGGER.error(e.getMessage());
		} finally
		{
			IOUtils.closeQuietly(outputStream);
		}
	}


	/**
	 * 导入模板
	 *
	 *
	 * @return
	 */
	@RequestMapping(value = "/readExcel")
	public String readExcel(@RequestParam(value = "excelfile", required = false) MultipartFile file){
		/* public Json readExcel(){*/
		Boolean result=false;

		String fileName = file.getOriginalFilename();// 获取文件名
		/*  String fileName ="员工列表数据.xls" ;// 获取文件名*/
		InputStream inputStream = null;
		try
		{

			inputStream = file.getInputStream();// 文件流
			/*inputStream = new FileInputStream("E:\\nishuai\\example\\shrio\\ssmshrio\\src\\main\\webapp\\WEB-INF\\template\\员工列表数据.xls");*/
		}
		catch (IOException e)
		{
			LOGGER.error(e.getCause().getMessage());
			throw new RuntimeException("修改失败");
		}
		result = employeeService.readExcel(inputStream, fileName);

		return "success";
	}


	/**
	 * 下载模板
	 *
	 * @param request 项目路径
	 * @return
	 */
	@RequestMapping(value = "/download")
	@ResponseBody
	public Boolean downloadTemplate(HttpServletRequest request, HttpServletResponse response)
	{
		Boolean result=false;
		try
		{
			employeeService.downloadTemplate(request, response);// 下载模板
			result=true;

		}
		catch (IOException e)
		{
			LOGGER.error(e.getCause().getMessage());
		}
		return result;
	}








	@RequestMapping("/getempsid")
	public String  empsid(Map<String,Object> map){
		//Map<String,Object> map
		System.out.println("getemps");
	Employee	emp=employeeService.getEmpById();
	map.put("empsid", emp);
		return "list";
		
	}

	//批量保存操作
	@RequestMapping("/batch")
	public String batch(){
		
	   employeeService.batchEmp();
	
		return "list";
		
	}

}
