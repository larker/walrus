<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Login Page</title>
</head>
<body onload='document.f.j_username.focus();'>
	<center>
		<h3 style="color: lime">用户登录页面</h3>
		<form name='f' action='${pageContext.request.contextPath}/j_spring_security_check' method='POST'>
			<div style="display:${param.error == true? '' : 'none' }">
				<h4 style="color: red">${sessionScope.SPRING_SECURITY_LAST_EXCEPTION.message}</h4>
				<br />
			</div>
			<table>
				<tr>
					<td>账 号 :</td>
					<td><input type='text' name='j_username' value=''></td>
				</tr>
				<tr>
					<td>密 码 :</td>
					<td><input type='password' name='j_password' /></td>
				</tr>
				<tr>
					<td><input name="submit" type="submit" value=" 登 录 " /></td>
					<td><input name="reset" type="reset" value=" 重 置 " /></td>
				</tr>
				<tr></tr>
			</table>
		</form>
	</center>
</body>
</html>
