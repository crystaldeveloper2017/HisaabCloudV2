<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="locationpath" value="${requestScope['outputObject'].get('locationpath')}" />

<script>
window.location='?a=${locationpath}';
</script>
