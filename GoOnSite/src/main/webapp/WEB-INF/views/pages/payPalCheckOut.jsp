<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div>
Checkout
	-{{$location}}-
	-{{myvar}}-
	<c:out value="${paymentId}" />	 
	<input name="firstinput" type="text" value="<c:out value="${paymentId}"/>">
	<input name="firstinput" type="text" value=<%=request.getParameter("tokenPP") %>>
	<input name="firstinput" type="text" value=<%=request.getParameter("PayerID") %>>

</div>
<script>
window.onload = function(){
	if(window.opener){
		window.close();
	} 
	else{
		if(top.dg.isOpen() == true){
			top.dg.closeFlow();
			return true;
		}
	}                              
};                             
</script>