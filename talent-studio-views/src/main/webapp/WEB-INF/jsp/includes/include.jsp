<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
<%@ page pageEncoding="ISO-8859-1" %>
alternative page buffer is to switch it off only needed under a few conditions such as forwarding etc, 
need to verify if none or 24kb is better
--%>

<%@ page import="com.zynap.web.SessionConstants" %>
<%@ page import="com.zynap.talentstudio.web.common.ParameterConstants"%>
<%@ page import="com.zynap.talentstudio.web.common.ControllerConstants"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="c_rt" uri="http://java.sun.com/jstl/core_rt" %>

<%@ taglib prefix="spring" uri="/spring" %>
<%@ taglib prefix="zynap" uri="/zynap" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>