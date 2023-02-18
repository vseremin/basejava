<%@ page import="ru.javawebinar.webapp.model.Contacts" %>
<%@ page import="ru.javawebinar.webapp.model.SectionType" %>
<%@ page import="ru.javawebinar.webapp.web.UtilForWeb" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="ru.javawebinar.webapp.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <dl>
            <dt>Имя:</dt>
            <dd><input type="text" name="fullName" size=50 value="${resume.fullName}"></dd>
        </dl>
        <h3>Контакты:</h3>
        <p>
            <c:forEach var="type" items="<%=Contacts.values()%>">
                <dl>
                    <dt>${type.title}</dt>
                    <dd><input type="text" name="${type.name()}" size=30 value="${resume.getContacts().get(type)}"></dd>
                </dl>
            </c:forEach>
        </p>
        <h3>Секции:</h3>
        <c:forEach var="section" items="${SectionType.values()}">

            <c:choose>
                <c:when test="${section.equals(SectionType.PERSONAL) || section.equals(SectionType.OBJECTIVE)}">
                    <dt>${section.title}:</dt>
                    <dd><textarea class="section" style="width:700px; height:100px;" name="${section.name()}"><%
                            %>${resume.getSection().get(section)}</textarea></dd></br>
                </c:when>
                <c:when test="${section.equals(SectionType.ACHIEVEMENT) || section.equals(SectionType.QUALIFICATIONS)}">
                    <dt>${section.title}:</dt>
                    <dd><textarea class="section" style="width:700px; height:180px;" name="${section.name()}"><%
                        %>${resume.getSection().get(section) != null ?
                            UtilForWeb.getSectionToString(resume.getSection().get(section).getList()) :
                            ""}<%
                    %></textarea></dd><br/>
                </c:when>
                <c:when test="${section.equals(SectionType.EXPERIENCE) || section.equals(SectionType.EDUCATION)}">
                <c:forEach var = "company" items="${resume.getSection().get(section).getCompanies()}">
                    <jsp:useBean id="company"
                                 type="ru.javawebinar.webapp.model.Company"/>
                    <a href="${company.getWebsite()}"><h4><%=company.getName()%></h4></a>
                    <c:forEach var = "period" items="<%=company.getPeriods()%>">
                        <jsp:useBean id="period"
                                     type="ru.javawebinar.webapp.model.Company.Period"/>
                            <table>
                                <tr align="left">
                                    <th width="250"><%=period.getStartDate()%> - <%=period.getEndDate()%></th>
                                    <th> <%=period.getTitle()%></th>
                                </tr>
                                <tr>
                                    <td></td>
                                    <td width="700"><%=period.getDescription()%></td>
                                </tr>
                            </table>
                    </c:forEach>
                </c:forEach>
                </c:when>
            </c:choose>
        </c:forEach>
        <hr>
        <button type="submit">Сохранить</button>
        <button type="button" onclick="window.history.back()">Отменить</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>