<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body style="">
<shiro:guest></shiro:guest>
<shiro:user></shiro:user>
<shiro:authenticated></shiro:authenticated>
<shiro:principal></shiro:principal>
<shiro:hasRole name="admin"></shiro:hasRole>
<font size=""></font>
</body>
</html>