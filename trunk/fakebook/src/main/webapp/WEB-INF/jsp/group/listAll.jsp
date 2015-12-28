<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%> 

<html>
<head>
<link rel="stylesheet" type="text/css"
	href="<c:url value="/css/style.css"/>" />
</head>
<body>
<div align="center">
<table id="list">
	<tbody>
		<c:forEach var="group" items="${groups}">
			<tr>
				<td width="20%" rowspan="3">
					<a href="<c:url value="/group/list.do"/>?id=${group.id}">
						<img class="photo" src="<c:url value="/group/photo.do"/>?id=${group.id}" />
					</a>
				</td>
				<td align="left" valign="top" nowrap="nowrap">
					<c:out value="${group.name}" />
				</td>
			</tr>
			<tr>
				<td align="left" valign="top" nowrap="nowrap" style="border-top-color: #fff">
					<div>
						Size : <c:out value="${fn:length(group.members)}" /> members
					</div>
				</td>
			</tr>
			<tr>
				<td align="left" valign="top" style="border-top-color: #fff">
					<div style="color: #333333">
						<c:out value="${group.description}" />
					</div>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
</div>
</body>
</html>