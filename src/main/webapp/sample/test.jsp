<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

<h1>フォルダ階層テスト</h1>

<p id="test">ID重複テスト1</p>
<p id="test">ID重複テスト2</p>

<form action="/team_dev_training/test/test1.jsp" method="get">
	<button>テスト1</button>
</form>
<form action="/team_dev_training/SampleServlet" method="get">
	<input type="hidden" name="action" value="test1">
	<button>テスト2</button>
</form>
</body>
</html>