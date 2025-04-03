<%--
- form.jsp
-
- Copyright (C) 2012-2025 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>


<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:print code="assistance-agent.assistance-agent-dashboard.form.label.resolvedClaimsRatio" />
		</th>
		<td>
			<jstl:choose>
				<jstl:when test="${resolvedClaimsRatio != null}">
					<acme:print value="${resolvedClaimsRatio}" />
				</jstl:when>
				<jstl:when test="${resolvedClaimsRatio == null}">
					<acme:print value="N/A" />
				</jstl:when>
			</jstl:choose>
			
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="assistance-agent.assistance-agent-dashboard.form.label.rejectedClaimsRatio" />
		</th>
		<td>
			<jstl:choose>
				<jstl:when test="${rejectedClaimsRatio != null}">
					<acme:print value="${rejectedClaimsRatio}" />
				</jstl:when>
				<jstl:when test="${rejectedClaimsRatio == null}">
					<acme:print value="N/A" />
				</jstl:when>
			</jstl:choose>
			
		</td>
	</tr>
</table>
<acme:return />
