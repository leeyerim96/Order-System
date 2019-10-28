<%@ page contentType="text/html; charset=utf-8" %>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.SQLException" %>

<%
	request.setCharacterEncoding("utf-8");

	String memberID = request.getParameter("memberID");
	String password = request.getParameter("password");
	String discount = null;
	String name = request.getParameter("name");
	String address = request.getParameter("address");

	Class.forName("com.mysql.jdbc.Driver");
	Connection conn = null;
	PreparedStatement pstmt = null;

	try {
		String jdbcDriver = "jdbc:mysql://localhost:3306/sys";
		String dbUser = "root";
		String dbPass = "111111";

		conn = DriverManager.getConnection(jdbcDriver, dbUser, dbPass);
		pstmt = conn.prepareStatement("insert into customers(customerID, password, name, address) values (?, ?, ?, ?)");
		pstmt.setString(1, memberID);
		pstmt.setString(2, password);
		pstmt.setString(3, name);
		pstmt.setString(4, address);

		pstmt.executeUpdate();
	} finally {
		if(pstmt != null) try { pstmt.close(); } catch(SQLException ex) {}
		if(conn != null) try { conn.close(); } catch(SQLException ex) {}
	}

%>
<html>
<head></head>
<body onload="window.close();"></body>
</html>