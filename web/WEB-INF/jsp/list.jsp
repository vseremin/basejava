<%@ page import="ru.javawebinar.webapp.model.Contacts" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <title>Список всех резюме</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>

<td><a href="resume?action=add"><img src="img/addR.jpg"></a></td>

<section>
    <center>
        <table border="1" cellpadding="8" cellspacing="0">
            <tr>
                <th>Имя</th>
                <th>Email</th>
                <th>Удалить</th>
                <th>Редактировать</th>
            </tr>
            <c:forEach items="${resumes}" var="resume">
                <jsp:useBean id="resume" type="ru.javawebinar.webapp.model.Resume"/>
                <tr>
                    <td><a href="resume?uuid=${resume.uuid}&action=view">${resume.fullName}</a></td>
                    <td><%=Contacts.MAIL.toHtml(resume.getContacts().get(Contacts.MAIL))%>
                    </td>
                    <td><a href="resume?uuid=${resume.uuid}&action=delete"><img src="img/delete.png"></a></td>
                    <td><a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/pencil.png"></a></td>
                </tr>
            </c:forEach>
        </table>
    </center>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>












