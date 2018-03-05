<%@ page pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<script type="text/javascript">   
    var remoteUrl = "${remoteLoginUrl}?validated=true";   
    // 构造错误消息,从webflow scope中取出  
    var errorMessage = '${remoteLoginMessage}';   
    /* <spring:hasBindErrors name="credentials"> 
     errorMessage = "&errorMessage=" + encodeURIComponent('<c:forEach var="error" items="${errors.allErrors}"><spring:message code="${error.code}" text="${error.defaultMessage}" /></c:forEach>'); 
    </spring:hasBindErrors> */
    // 如果存在错误消息则追加到 url中  
    if(null != errorMessage && errorMessage.length > 0)   
    {   
        errorMessage = "&errorMessage=" + encodeURIComponent(errorMessage);   
    }   
    // 构造service  
    var service = "${service}";   
    if (service != null && service != "") {
    	service = "&service=" + encodeURIComponent(service);  
    }
    // 跳转回去（客户端）  
    window.location.href = remoteUrl + errorMessage + service;   
</script>  
