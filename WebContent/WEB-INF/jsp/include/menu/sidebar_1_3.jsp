<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="sidebar-wrap">
	<div class="sidebar">
   		<div class="logo-area">
       		<div class="logo-container">
       			<a href="${pageContext.request.contextPath}/index.html"><img src="${pageContext.request.contextPath}/img/new/logo.png"></a>
       		</div>
   		</div>
   		<div class="main-item active" data-toggle="collapse" data-target="#main-1-collapse" aria-expanded="true">
       		<div class="main-item-icon-area">
           		<svg class="main-item-icon active" width="32" height="33" viewBox="0 0 32 33" fill="none" xmlns="http://www.w3.org/2000/svg">
               		<path fill-rule="evenodd" clip-rule="evenodd" d="M21.8712 20.1006L24.2779 17.6392C24.8588 17.0451 24.6196 16.0301 23.8366 15.7727L22.4 15.3005V10.375C22.4 9.75368 21.9076 9.25 21.3 9.25H19.1V7.84375C19.1 7.37775 18.7307 7 18.275 7H13.325C12.8694 7 12.5 7.37775 12.5 7.84375V9.25H10.3C9.69253 9.25 9.20004 9.75368 9.20004 10.375V15.3005L7.76344 15.7727C6.98131 16.0298 6.74065 17.0444 7.3222 17.6392L9.72887 20.1006L10.5495 22.9119C11.0452 24.1363 12.2258 25 13.6 25H18C19.3743 25 20.5549 24.1363 21.0505 22.9119L21.8712 20.1006ZM20.2 11.5H11.4V14.5773L15.4634 13.2415C15.6823 13.1695 15.9178 13.1695 16.1366 13.2415L20.2 14.5773V11.5Z"/>
           		</svg>
       		</div>
       		<div class="main-item-title active" data-i18n="share:sidebar.main1.title"></div>
   		</div>
   		<div id="main-1-collapse" class="collapse show">
       		<a onclick="delSearchCookie()" href="${pageContext.request.contextPath}/sche/on.html">
       			<div class="sub-item-wrap sub-item" data-i18n="share:sidebar.main1.sub1"></div>
       		</a>
       		<a onclick="delSearchCookie()" href="${pageContext.request.contextPath}/sche/off.html">
	       		<div class="sub-item-wrap sub-item" data-i18n="share:sidebar.main1.sub2"></div>
       		</a>
       		<a onclick="delSearchCookie()" href="${pageContext.request.contextPath}/mobile/offCrew.html">
       			<div class="sub-item-wrap sub-item-active">
	           		<span class="sub-item-active-bar"></span>
	           		<span class="sub-item-active-title" data-i18n="share:sidebar.main1.sub3"></span>
	       		</div>
       		</a>
   		</div>
   	</div>
</div>
<div class="collapsed-sidebar-wrap">
   	<div class="collapsed-sidebar">
       	<div class="collapsed-logo-area">
           	<a href="${pageContext.request.contextPath}/index.html"><img src="${pageContext.request.contextPath}/img/new/logo_s.png"></a>
   		</div>
   		<div class="collapsed-main-item-icon-area active">
       		<svg class="collapsed-main-item-icon active" width="32" height="33" viewBox="0 0 32 33" fill="none" xmlns="http://www.w3.org/2000/svg">
           		<path fill-rule="evenodd" clip-rule="evenodd" d="M21.8712 20.1006L24.2779 17.6392C24.8588 17.0451 24.6196 16.0301 23.8366 15.7727L22.4 15.3005V10.375C22.4 9.75368 21.9076 9.25 21.3 9.25H19.1V7.84375C19.1 7.37775 18.7307 7 18.275 7H13.325C12.8694 7 12.5 7.37775 12.5 7.84375V9.25H10.3C9.69253 9.25 9.20004 9.75368 9.20004 10.375V15.3005L7.76344 15.7727C6.98131 16.0298 6.74065 17.0444 7.3222 17.6392L9.72887 20.1006L10.5495 22.9119C11.0452 24.1363 12.2258 25 13.6 25H18C19.3743 25 20.5549 24.1363 21.0505 22.9119L21.8712 20.1006ZM20.2 11.5H11.4V14.5773L15.4634 13.2415C15.6823 13.1695 15.9178 13.1695 16.1366 13.2415L20.2 14.5773V11.5Z"/>
       		</svg>
   		</div>
   	</div>
</div>