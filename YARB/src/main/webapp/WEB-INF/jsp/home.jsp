<%@ page session="false"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
 
<t:page>
    <jsp:attribute name="title">Welcome!</jsp:attribute>
    <jsp:body>
        <div class="jumbotron" id="welcome">
 
            <h1>Welcome to Stylease!</h1>
            <br/>
            <p class="bigbutton"><a class="bigbutton btn btn-lg"
               href="${pageContext.request.contextPath}/m_list" role="button">View Messages</a></p>
            <br/>
            <p class="bigbutton"><a class="bigbutton btn btn-lg"
               href="${pageContext.request.contextPath}/m_form" role="button">Post Message</a></p>
            
            
        </div>
    </jsp:body>
</t:page>