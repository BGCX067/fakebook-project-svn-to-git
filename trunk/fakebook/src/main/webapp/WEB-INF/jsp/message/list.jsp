<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
<head>
<link rel="stylesheet" type="text/css"
	href="<c:url value="/css/style.css"/>" />
</head>
<body>
 
<div style="float: left">
	<img src="<c:url value="/person/photo.do"/>?id=${person.id}" />
	<h2><c:out value="${person.name}" /></h2>
	<h2><c:out value="${person.gender}" /></h2>
</div>
<div align="center">
	<table id="list">
		<tr style="background-color: #3b5998">
			<td colspan="3">
				<a href="<c:url value="/message/list.do"/>?id=${person.id}" rel="country2" class="link">Wall</a>
				<a href="<c:url value="/person/list.do"/>?id=${person.id}" rel="country4" class="link">Friends</a>
				<a href="<c:url value="/person/listGroup.do"/>?personId=${person.id}" class="link">Group</a>
			</td>
		</tr>
		<c:forEach var="message" items="${person.sendToMe}">
			<tr>
				<td width="20%">
					<a href="<c:url value="/message/list.do"/>?id=${message.from.id}">
						<img class="photo" src="<c:url value="/person/photo.do"/>?id=${message.from.id}" />
					</a>
				</td>
				<td align="left" valign="top">
					<div style="font-weight: bold;color:#3b59a9">
						<c:out value="${message.from.name}" /> Says  : 
					</div>
					<div style="color: #333333">
						<c:out value="${message.content}" />
					</div>
					<br>
					<div style="font-size: 10pt;color:##8e8ebb">
						<fmt:formatDate value="${message.stamp}" pattern="yyyy-MM-dd HH:mm:ss" />
					</div>
				</td>
			</tr>
		</c:forEach>
	</table>
</div>
</body>
</html>