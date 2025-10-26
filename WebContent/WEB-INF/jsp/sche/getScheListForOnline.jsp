<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
{
	"list": [
		<c:forEach var="tmp" items="${list}" varStatus="status">
			{
				"uid": ${tmp.uid},
				"hullnum": "${tmp.hullnum}",
				"schedtype": "${tmp.schedtype}",
				"shiptype": "${tmp.shiptype}",
				"sdate": "${tmp.sdate}",
				"edate": "${tmp.edate}",
				"status": "${tmp.status}",
				"isOff": "${tmp.isOff}",
				"trialStatus": "${tmp.trialStatus}",
				"regOwner": "${tmp.regOwner}",
				"trialKey": "${tmp.trialKey}",
				"projSeq": "${tmp.projSeq}",
				"insertdate": "${tmp.insertdate}",
				"insertName": "${tmp.insertName}"
			}
			<c:if test="${!status.last}">,</c:if>
		</c:forEach>
	],
	"listCnt": ${listCnt}
}