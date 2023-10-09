<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Bookshop Website</title>
</head>
<body>
	<h3>Welcome <%= request.getAttribute("firstname") %>!</h3>
	<p> Your emailid: <%= request.getAttribute("emailid") %></p>
</body>
</html>