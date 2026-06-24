<%@page import="java.io.PrintWriter"%>
<%@page import="com.cake.entity.User"%>
<%@page import="java.util.Map"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>用户登录</title>
	
	<!-- 屏幕自适应 -->
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
   
    <!-- 导入css用link -->   
    <link type="text/css" rel="stylesheet" href="css/main.css">
    <link type="text/css" rel="stylesheet" href="css/zui.min.css">
    <!-- 导入Javascript用script标签 -->
    <script type="text/javascript" src="js/jquery-3.0.0.js"></script>
    <script type="text/javascript" src="js/zui.min.js"></script>
    
     <!-- 内嵌式css -->       
     <style type="text/css">
         .panel-center{
             margin-left: 33%;
         }
         .foot{
             margin-top: 30px;
         }
         .zhuce{
             margin-left: 36%;
             color: #FF4500;
         }
     </style>

     <!-- 内嵌式javascript 实现光标自动对准-->
     <script type="text/javascript">
          function focusOnLogin(){
        	  $("#username").focus();
          }
     </script>
     
     <script type="text/javascript">
         $(document).ready(function(){

         });
     </script>
      

<%
//讲解完jsp后给他们演示自动登录
      if(session!=null){
          String uname = (String)session.getAttribute("username");
          String paswd = (String)session.getAttribute("passwd");
          
          //String succ = request.getAttribute("resultList");
          User user = (User)session.getAttribute("user");
          if(user!=null){  
        	  //自动登录时用的
         	 //request.getRequestDispatcher("/Cakepages/index.jsp").forward(request, response);
        	  
        	  //查看商品详情时改成这个路径 否则自动登录时没有数据
         	 request.getRequestDispatcher("/api/GoodsServlet").forward(request, response);
          }
      }  
%>  
 
 <c:if test="${!empty resultMap}">
     <c:if test="${resultMap.get('succ')==1}">
       <script>alert("注册成功");</script>
     </c:if>
 </c:if>

 
</head>
<body onload="focusOnLogin()">
    <!--导航条-->
    <nav class="navbar navbar-inverse" role="navigation">
    
      <div class="container-fluid" style="font-size:1.6em; padding-top:10px; padding-left:70px; ">
      
        <!-- 导航头部 -->
        <div class="navbar-header">
          <!-- 移动设备上的导航切换按钮 -->
          <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse-example">
            <span class="sr-only">切换导航</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <!-- 品牌名称或logo -->
          <a class="navbar-brand" href="./login.jsp">蛋糕系统</a>
        </div> 
        <!-- 导航项目 -->
        <div class="collapse navbar-collapse navbar-collapse-example" >
          <!-- 一般导航项目 -->
          <ul class="nav navbar-nav">
            <li ><a href="<%=request.getContextPath()%>/api/GoodsServlet">首页</a></li>
            <!-- 导航中的下拉菜单 要映入jquery.js以及zui.js才能生效-->
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown">商品分类 <b class="caret"></b></a>
              <ul class="dropdown-menu" role="menu">
                <li><a href="your/nice/url">分类1</a></li>
                <li><a href="your/nice/url">分类2</a></li>
              </ul>
            </li>
            <li><a href="your/nice/url">热销</a></li>
            <li><a href="your/nice/url">新品</a></li>

            <li ><a href="${pageContext.request.contextPath}/register.jsp">注册</a></li>
            <li class="active"><a href="<%=request.getContextPath()%>/login.jsp">登录</a></li>
          </ul>
          
          <!--右侧购物车 导航条里 图标里-->
          <ul class="nav navbar-nav navbar-right" style="padding-right:60px;">
              <li><a href="your/nice/url"><i class="icon icon-shopping-cart icon-2x"></i></a></li>
          </ul>
          
          <!--右侧搜索框表单 导航条里-->
          <form class="navbar-form navbar-right form-search" role="search">
               <!-- 搜索框 -->
              <div class="input-group">
                  <div class="input-control search-box search-box-circle has-icon-left has-icon-right search-example" id="searchboxExample">
                   <input id="inputSearchExample3" type="search" class="form-control search-input" placeholder="搜索">
                   <label for="inputSearchExample3" class="input-control-icon-left search-icon" style="margin-top:-3px;"><i class="icon icon-search"></i></label>
                    <!-- 按钮在div里 -->
                    <span class="input-group-btn">
                         <button class="btn btn-primary" type="button">搜索</button>
                    </span>
                  </div>
              </div> 
          </form>
                
        </div><!-- END .navbar-collapse -->
      </div>
    </nav>
    
    <!--面板-->
    <div class="panel">

      <div class="panel-body panel-center">
          <!--栅格系统 占据6列-->
        <div class="row">
            <div class="col-md-6">
            
     <!-- 获取后端的值 -->
     <c:if test="${!empty msg }">
       <c:if test="${succ==0 }">         
         <script type="text/javascript">
                new $.zui.Messager('${ msg}', {
                	  icon: 'bell-alt',
				      type: 'warning',
				      placement:'center'
				  }).show();
          </script>
        </c:if>
      </c:if>  
      
                <!--输入组 嵌套form表单-->
              <form action="/cakeTest01/UserLoginByMysql?action=login" method="post">
                <div class="input-group zhuce">
                    <h1 class="header-dividing">用户登录</h1>
                </div>
                <!-- 输入组 -->
                <div class="input-group">
                  <span class="input-group-addon">用户名&nbsp;<font color="red">*</font></span>
                  <!-- required代表输入框为必填项 -->
                  <input type="text" class="form-control" placeholder="请输入用户名" id="username" name="username" required>
                </div>

                <div style="margin-top:20px;"></div><!--上下隔开-->
                <div class="input-group">
                  <span class="input-group-addon">密码&nbsp;<font color="red">*</font></span>
                  <input type="password" class="form-control" placeholder="请输入密码" id="passwd" name="passwd" required>
                </div>
                <div style="margin-top:6px;"></div><!--上下隔开--> 
                <div class="input-group"  style="margin-left:70%;">
                   <span><a href="${pageContext.request.contextPath}/register.jsp">还没账号？请先注册</a></span>
                </div>
                
                <div style="margin-top:20px;"></div><!--上下隔开-->
                <div class="input-group" style="padding-left:196px;">
                  
                  <button class="btn btn-success " type="submit" id="regist">登录</button>
                </div>
                </form>
            </div>
        </div>
      </div>
      
      <!--导入main.css-->
      <div class="panel-footer foot">
          <div id="friend">
            <h1 class="friend_t"> <img src="images/friend_ico.gif" alt="123"/> </h1>
            <div class="friend_list">
              <ul>
                <li> <a href="#">百度外卖</a> </li>
                <li> <a href="#">美团外卖</a> </li>
                <li> <a href="#">饿了么</a> </li>
                <li> <a href="#">百度</a> </li>
                <li> <a href="#">谷歌</a> </li>
                <li> <a href="#">新浪</a> </li>
                <li> <a href="#">网易</a> </li>
                <li> <a href="#">搜狐</a> </li>
                <li> <a href="#">人人</a> </li>
                <li> <a href="#">星巴克专星送 (星巴克企业管理(中国)有限公司)</a> </li>
                <li> <a href="#">海底捞外送 (四川海底捞餐饮股份有限公司)</a> </li>
                <li> <a href="#">KFC宅急送 (百胜(中国)投资有限公司)</a> </li>
                <li> <a href="#">到家美食会 (北京到家时代餐饮管理有限公司)</a> </li>
                <li> <a href="#">麦乐送 (金拱门(中国)有限公司)</a> </li>
                <li> <a href="#">口碑 (阿里巴巴集团控股有限公司)</a> </li>
              </ul>
            </div>
          </div>
          <div id="footer">
            <p class=""> 24小时客户服务热线：010-68988888 &#160;&#160;&#160;&#160; <a href="#">常见问题解答</a> &#160;&#160;&#160;&#160; 投诉热线：010-627488888 <br />
              文明办网文明上网举报电话：010-627488888 &#160;&#160;&#160;&#160; 举报邮箱： <a href="#">jubao@jb-aptech.com.cn</a> </p>
            <p class="copyright"> Copyright &copy; 1999-2023 News China gov, All Right Reserver <br />
              网龙网络有限公司 版权所有 </p>
          </div>
    </div>

 </div>

</body>
</html>