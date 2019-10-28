<%@ page contentType = "text/html; charset=utf-8" %>
<html>
<head>
	<title>Register</title>
</head>
<body>
	<form action="insert.jsp" method="post">
	<table border="1">
		<tr>
			<td>ID</td>
			<td><input type="text" name="memberID" size="16"></td>
			<td>PW</td>
			<td><input type="text" name="password" size="16"></td>
		</tr>
		<tr>
			<td>NAME</td>
			<td><input type="text" name="name" size="10"></td>
			<td>ADDRESS</td>
			<td><input type="text" name="address" size="16"></td>
		</tr>
		<tr>
			<td colspan="4"><input type="submit" value="submit"></td>
		</tr>
	</table>
	</form>
</body>
</html>