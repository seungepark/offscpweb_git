<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript">
	contextPath = "${pageContext.request.contextPath}";
</script>
</head>
<img src="${pageContext.request.contextPath}/img/loading.gif" id="loading" class="loading-img">
<div class="modal fade" id="alertPopLg" tabindex="-10" role="dialog" aria-labelledby="alertPopLg" aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered modal-lg" role="document">
		<div class="modal-content">
	      	<div class="modal-body text-center pop-alert">
	        	<div class="pop-alert-msg" id="alertPopDescLg"></div>
	        	<button class="bt-obj bt-primary" data-dismiss="modal" data-i18n="share:btnOk"></button>
			</div>
		</div>
	</div>
</div>
<div class="modal fade" id="alertPop" tabindex="-10" role="dialog" aria-labelledby="alertPop" aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered" role="document">
		<div class="modal-content">
	      	<div class="modal-body text-center pop-alert">
	        	<div class="pop-alert-msg" id="alertPopDesc"></div>
	        	<button class="bt-obj bt-primary" data-dismiss="modal" data-i18n="share:btnOk"></button>
			</div>
		</div>
	</div>
</div>
<div class="position-fixed bottom-0 right-0 p-3" style="z-index: 200000; right: 0; bottom: 0;">
	<div id="toastPop" class="toast" role="alert" data-delay="1500" aria-live="assertive" aria-atomic="true">
	  <div class="toast-header">
	    <strong class="mr-auto"><span id="toastTitle"></span></strong>
	    <button type="button" class="ml-2 mb-1 close" data-dismiss="toast" aria-label="Close">
	      <span aria-hidden="true">&times;</span>
	    </button>
	  </div>
	</div>
</div>
<div  class="position-fixed top-50 start-50 p-5 d-flex justify-content-center align-items-center" style="z-index: 1000000; top:40%; left:35%; width:30%;">
	<div id="progressPop" class="toast fade show hide" role="alert" aria-live="assertive" aria-atomic="true" data-autohide="false">
	  <div class="toast-header">
	    <strong class="mr-auto"><span id="progressTitle"></span></strong>
	  </div>
	</div>
</div>
</html>