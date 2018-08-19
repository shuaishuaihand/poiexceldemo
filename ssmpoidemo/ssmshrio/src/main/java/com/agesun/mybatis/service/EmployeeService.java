package com.agesun.mybatis.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.agesun.mybatis.util.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agesun.mybatis.bean.Employee;
import com.agesun.mybatis.dao.EmployeeMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface EmployeeService {

	/**
	 查询员工信息
	 */
	public List<Employee> getEmps();

	/**
	 * 导入组织机构模板数据
	 *
	 */
	Boolean readExcel(InputStream inputStream, String fileName);

	/**
	 * 下载组织机构模板
	 *
	 */
	void downloadTemplate(HttpServletRequest request, HttpServletResponse response) throws IOException;
	
	public Employee getEmpById();
	
	public Integer batchEmp();

	

}
