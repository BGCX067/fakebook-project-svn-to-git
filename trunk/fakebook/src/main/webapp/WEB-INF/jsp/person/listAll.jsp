<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<link rel="stylesheet" type="text/css"
	href="<c:url value="/css/style.css"/>" />
</head>
<body>
<div align="center">
<table id="list">
	<tbody>
		<c:forEach var="person" items="${people}">
			<tr>
				<td width="20%">
					<a href="<c:url value="/message/list.do"/>?id=${person.id}">
						<img class="photo" src="<c:url value="/person/photo.do"/>?id=${person.id}" />
					</a>
				</td>
				<td align="left" valign="top" nowrap="nowrap">
					<c:out value="${person.name}" />
				</td>
				<td align="left" valign="top" nowrap="nowrap">
					Gender : <c:out value="${person.gender}" />
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
</div>
</body>
</html>