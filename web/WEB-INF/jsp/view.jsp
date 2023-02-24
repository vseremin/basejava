<%@ page import="ru.javawebinar.webapp.model.ListSection" %>
<%@ page import="ru.javawebinar.webapp.model.SectionType" %>
<%@ page import="ru.javawebinar.webapp.model.CompanySection" %>
<%@ page import="ru.javawebinar.webapp.web.UtilForWeb" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <h2>${resume.fullName}&nbsp;<a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/pencil.png"></a></h2>
    <p>
        <c:forEach var="contactEntry" items="${resume.contacts}">
            <jsp:useBean id="contactEntry"
                         type="java.util.Map.Entry<ru.javawebinar.webapp.model.Contacts, java.lang.String>"/>
            <%=contactEntry.getKey().toHtml(contactEntry.getValue())%><br/>
        </c:forEach>
    </p>
    <c:forEach var="sectionEntry" items="${resume.section}">
        <jsp:useBean id="sectionEntry"
                     type="java.util.Map.Entry<ru.javawebinar.webapp.model.SectionType,
                         ru.javawebinar.webapp.model.AbstractSection>"/>
        <h3><%=sectionEntry.getKey().getTitle() + ":\n"%><br/></h3>
        <c:choose>
            <c:when test="<%=sectionEntry.getKey().equals(SectionType.PERSONAL)
                    || sectionEntry.getKey().equals(SectionType.OBJECTIVE)%>">
                <%=sectionEntry.getValue()%><br/>
            </c:when>
            <c:when test="<%=sectionEntry.getKey().equals(SectionType.ACHIEVEMENT)
                    || sectionEntry.getKey().equals(SectionType.QUALIFICATIONS)%>">
                <%=UtilForWeb.getSectionToString(((ListSection)
                        sectionEntry.getValue()).getList()).replace("\n", "<br/>")%><br/>
            </c:when>
            <c:when test="<%=sectionEntry.getKey().equals(SectionType.EDUCATION)
                    || sectionEntry.getKey().equals(SectionType.EXPERIENCE)%>">
                <c:forEach var="company" items="<%=((CompanySection) sectionEntry.getValue()).getCompanies()%>">
                    <jsp:useBean id="company"
                                 type="ru.javawebinar.webapp.model.Company"/>
                    <a href="${company.getWebsite()}"><h4><%=company.getName()%>
                    </h4></a>
                    <c:forEach var="period" items="<%=company.getPeriods()%>">
                        <jsp:useBean id="period"
                                     type="ru.javawebinar.webapp.model.Company.Period"/>
                        <table>
                            <tr align="left">
                                <th width="250"><%=period.getStartDate() != null ? period.getStartDate() +
                                        " - " : ""%> <%=period.getEndDate() != null ? period.getEndDate() : ""%>
                                </th>
                                <th><%=period.getTitle()%>
                                </th>
                            </tr>
                            <tr>
                                <td></td>
                                <td width="700"><%=period.getDescription()%>
                                </td>
                            </tr>
                        </table>
                    </c:forEach>
                </c:forEach>
            </c:when>
        </c:choose>
    </c:forEach>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>