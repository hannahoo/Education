<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<form action="/ocr/jsp/addQuestion" method="post">
 <h1>��������Ŀ��Ϣ:</h1> 
 <p>���:</p> 
 <p><input type="text" name="title" size="100" /></p>
 <p>��Ŀ:</p> 
 <p><textarea rows="4" cols="100" name="question"/></textarea></p>
 <p>��:</p> 
 <p><textarea rows="4" cols="100" name="answer"/></textarea></p>
 <input type="submit" value="�ύ"/>
  </form>
   
</body>
</html>