package com.agesun.mybatis.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agesun.mybatis.util.Download;
import com.agesun.mybatis.util.Json;
import com.agesun.mybatis.util.PageData;
import com.agesun.mybatis.util.ReadExcel;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agesun.mybatis.bean.Employee;
import com.agesun.mybatis.dao.EmployeeMapper;
import com.agesun.mybatis.service.EmployeeService;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Resource
	EmployeeMapper em;

	@Autowired
	private SqlSession sqlSession;


	@Override
	public List<Employee> getEmps() {
		return em.getEmps();
	}
	/**
	 * 导入模板
	 */

	@Override
	public Boolean readExcel(InputStream inputStream, String fileName) {
		Boolean result=false;
		ReadExcel getExcelInfo = new ReadExcel();
		List<Map<String, Object>> mapList = getExcelInfo.getExcelInfo(inputStream, fileName);// 获取附件中数据

		if (mapList == null || mapList.isEmpty()) {
			result=false;
		} else {

			try {

				InsertMysql(mapList);// 新增到数据库中*/
				result= true;
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return result;
	}

	//存入数据库
	@Transactional
	public void InsertMysql(List<Map<String, Object>> mapList)
			throws Exception {
		try {
			PageData pd = new PageData();
			Employee emp=new Employee();
			for (Map<String, Object> map : mapList) {
				//编号
				Integer id =Integer.parseInt(String.valueOf(map.get("id")));
				pd.put("id", id);
				//姓名
				String lastName = String.valueOf(map.get("lastName"));
				pd.put("lastName", lastName);
				//邮件
				String email= String.valueOf(map.get("email"));
				pd.put("email", email);
				//性别
				String gender = String.valueOf(map.get("gender"));
				pd.put("gender", gender);


				//插入员工表
				em.insertmysql(pd);


			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 下载模板
	 */
	@Override
	public void downloadTemplate(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String name = "员工列表数据.xls";
		String path = request.getSession().getServletContext().getRealPath("/WEB-INF/template") + "/" + name;// 组织机构导出模板路径
		Download.downloadTemplate(path, response, request);// 执行下载方法
	}



	@Override
	public Employee getEmpById() {

		return em.getEmpById(1);
	}

	//批量保存员工
	@Override
	public Integer batchEmp() {
		// TODO Auto-generated method stub

		//批量保存执行前时间
		long start=System.currentTimeMillis();

		EmployeeMapper mapper=	sqlSession.getMapper(EmployeeMapper.class);
		for (int i = 0; i < 10000; i++) {
			mapper.addEmp(new Employee(UUID.randomUUID().toString().substring(0,5),"b","1"));

		}
		long end=  System.currentTimeMillis();
		long time2= end-start;
		//批量保存执行后的时间
		System.out.println("执行时长"+time2);


		System.out.println("------------------------------------------------------------");

		//批量保存执行前时间
		 			/*long start2=System.currentTimeMillis();
		 		    for (int i = 0; i < 5000; i++) {
		 		    	em.addEmp(new Employee(UUID.randomUUID().toString().substring(0,5),"b","1"));

		 		    }
		 		    long end2=  System.currentTimeMillis();
		 		    long time2= end2-start2;
		 		    //批量保存执行后的时间
		 		    System.out.println("执行时长"+time2);*/





		return (int) time2;

	}


}
