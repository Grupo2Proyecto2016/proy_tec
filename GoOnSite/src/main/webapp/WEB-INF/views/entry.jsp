<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="utf-8">
    <!-- This file has been downloaded from Bootsnipp.com. Enjoy! -->
    <script src="<c:url value='/static/js/tokenLogic.js' />"></script>
    <script src="http://code.jquery.com/jquery-1.11.1.min.js"></script>
</head>
<body>
<script type="text/javascript">
	$.ajax({
	    url: window.location.href + "/index",
	    type: "GET",
	    dataType: "html",
	    headers: createAuthorizationTokenHeader(),
	    success: function (data) {
	    	var newDoc = document.open("text/html", "replace");
	        newDoc.write(data);
	        newDoc.close();
	    },
	});
</script>
</body>
</html>
