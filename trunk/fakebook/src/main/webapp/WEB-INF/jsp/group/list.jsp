<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<link rel="stylesheet" type="text/css"
	href="<c:url value="/css/style.css"/>" />
</head>
<body>
<div style="width : 200px; float: left">
	<img src="<c:url value="/group/photo.do"/>?id=${group.id}" width="195px" height="195px"/>
	<h2><c:out value="${group.name}" /></h2>
	<c:out value="${group.description}" />
</div>
<div align="center">
<table id="list">
	<tbody>
		<c:forEach var="member" items="${group.members}">
			<tr>
				<td width="20%">
					<a href="<c:url value="/message/list.do"/>?id=${member.id}">
						<img class="photo" src="<c:url value="/person/photo.do"/>?id=${member.id}" />
					</a>
				</td>
				<td align="left" valign="top" nowrap="nowrap">
					<div style="font-size:11pt;font-weight:bold;color:#3b59ba">
						<c:out value="${member.name}" />
					</div>
				</td>
				<td align="left" valign="top" nowrap="nowrap">
					<div style="font-size:11pt;color:#000000">
						Gender : <c:out value="${member.gender}" />
					</div>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
</div>
</body>
</html>