<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<script src="/static/js/jquery-3.3.1.min.js"></script>
<script type="text/javascript">
    function exportXMl(){
        document.getElementById("empExe").submit();
    }
    function download(){
        document.getElementById("download").submit();
    }

  /*  function download(){

        $.ajax({
            type:"GET",
            url:"/employee/download",
            dataType: "json"
        });
    }*/
</script>
<button type="button" onclick="exportXMl()">导出表格</button>
<button type="button" onclick="download()">模板下载</button>
<%--    <form id="empExe" action="${pageContext.request.contextPath}/employee/Export">--%>
<form method="post" action="${pageContext.request.contextPath}/employee/readExcel" enctype="multipart/form-data">
    <input type="file" name="excelfile" id="excelfile">
    <input type="submit" value="确定">
</form>
<form id="download" action="${pageContext.request.contextPath}/employee/download">
<table>

   <c:forEach items="${allemps}" var="emp">
     <tr>
         <td>${emp.id}</td>>
         <td>${emp.lastName}</td>>
         <td>${emp.email}</td>>
         <td>${emp.gender}</td>>
     
     </tr>
   
   </c:forEach>

</table>
</form>


<%--</form>--%>

</body>
</html>