
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
{
	"list": [
		<c:forEach var="tmp" items="${list}" varStatus="status">
			{
				"uid": ${tmp.uid},
				"schecodeinfouid": "${tmp.schecodeinfouid}",
				"lv1code": "${tmp.lv1code}",
				"lv2code": "${tmp.lv2code}",
				"lv3code": "${tmp.lv3code}",
				"lv4code": "${tmp.lv4code}",
				"displaycode": "${tmp.displaycode}",
				"description": "${tmp.description}",
				"dtype": "${tmp.dtype}",
				"ctype": "${tmp.ctype}",
				"loadrate": "${tmp.loadrate}",
				"loadstr": "${tmp.loadstr}",
				"per": "${tmp.per}",
				"readytime": "${tmp.readytime}",
				"seq": "${tmp.seq}",
				"sametcnum": "${tmp.sametcnum}"
			}
			<c:if test="${!status.last}">,</c:if>
		</c:forEach>
	],
	"listCnt": ${listCnt}
}