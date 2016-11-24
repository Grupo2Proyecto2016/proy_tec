<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <!-- This file has been downloaded from Bootsnipp.com. Enjoy! -->
    <script src="<c:url value='/static/js/tokenLogic.js' />"></script>
    <script src="https://code.jquery.com/jquery-1.11.1.min.js"></script>
</head>
<body>
	<form class="form-signin" method="post" id="EntryForm">
		<input type="hidden" name="token" id="token"/>
		<input type="hidden" name="${_csrf.parameterName}"   value="${_csrf.token}" />
	</form>
<script type="text/javascript">
	$('#token').val(createAuthorizationTokenHeader()["Authorization"]);
	$("#EntryForm").submit();
</script>
</body>
</html>
