<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>UNICL</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta name="description" content="" />
<meta name="author" content="http://bootstraptaste.com" />
<!-- css -->
<link href="css/bootstrap.min.css" rel="stylesheet" />
<link href="css/fancybox/jquery.fancybox.css" rel="stylesheet">
<link href="css/jcarousel.css" rel="stylesheet" />
<link href="css/flexslider.css" rel="stylesheet" />
<link href="css/style.css" rel="stylesheet" />


<!-- Theme skin -->
<link href="skins/default.css" rel="stylesheet" />

<!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

</head>
<body>


    <!-- 페이스북 로그인후 좋아요및 공유 -->
    <script>
        function statusChangeCallback(response) {
            console.log('statusChangeCallback');
            console.log(response);

            if (response.status === 'connected') {
                // Logged into your app and Facebook.
                // 페이스북을 통해서 로그인이 되어있다.
                testAPI();
            } else if (response.status === 'not_authorized') {
                // The person is logged into Facebook, but not your app.
                // 페이스북에는 로그인 했으나, 앱에는 로그인이 되어있지 않다.
                document.getElementById('status').innerHTML = 'Please log '
                        + 'into this app.';
            } else {
                // The person is not logged into Facebook, so we're not sure if
                // they are logged into this app or not.
                // 페이스북에 로그인이 되어있지 않다. 따라서, 앱에 로그인이 되어있는지 여부가 불확실하다.
                document.getElementById('status').innerHTML = 'Please log '
                        + 'into Facebook.';
            }
        }

        function checkLoginState() {
            FB.getLoginStatus(function(response) {
                statusChangeCallback(response);
            });
        }

        window.fbAsyncInit = function() {
            FB.init({

                //https://developers.facebook.com/apps/1603775146530831/settings/ 
                //AppID값
                appId : '{464932637008205}',
                cookie : true, // enable cookies to allow the server to access
                // the session
                xfbml : true, // parse social plugins on this page
                version : 'v2.0' // use version 2.0
            });

            FB.getLoginStatus(function(response) {
                statusChangeCallback(response);
            });

        };

        // Load the SDK asynchronously
        (function(d, s, id) {
            var js, fjs = d.getElementsByTagName(s)[0];
            if (d.getElementById(id))
                return;
            js = d.createElement(s);
            js.id = id;
            js.src = "//connect.facebook.net/en_US/sdk.js";
            fjs.parentNode.insertBefore(js, fjs);
        }(document, 'script', 'facebook-jssdk'));

        // Here we run a very simple test of the Graph API after login is
        // successful.  See statusChangeCallback() for when this call is made.
        function testAPI() {
            console.log('Welcome!  Fetching your information.... ');
            FB
                    .api(
                            '/me',
                            function(response) {
                                console.log('Successful login for: '
                                        + response.name);
                                document.getElementById('status').innerHTML = 'Thanks for logging in, '
                                        + response.name + '!';
                            });
        }
    </script>




	<div id="wrapper">
		<!-- start header -->
		<header>
			<div class="navbar navbar-default navbar-static-top">
				<div class="container">
					<div class="navbar-header">
						<button type="button" class="navbar-toggle" data-toggle="collapse"
							data-target=".navbar-collapse">
							<span class="icon-bar"></span> <span class="icon-bar"></span> <span
								class="icon-bar"></span>
						</button>
						<a class="navbar-brand"><span>U</span>NICL</a>
					</div>
					<div class="navbar-collapse collapse ">
						<ul class="nav navbar-nav">
						
						</ul>
					</div>
				</div>
			</div>
		</header>
		
		
		      <!-- end header -->
        <section id="inner-headline">
            <div class="container">
                <div class="row">
                    <div class="col-lg-12"></div>
                </div>
                <div class="row">
                    <div class="col-lg-12"></div>
                </div>
            </div>
        </section>

		<!-- end header -->
		<section id="featured">
			<!-- start slider -->
			<div class="container">
				<div class="row">
					<div class="col-lg-12">
						<!-- Slider -->
						<div id="main-slider" class="flexslider">
							<ul class="slides">
								<li><img src="img/mainslides/1.jpg" alt="" />
									<div class="flex-caption">
										<h3>Unicl</h3>
										<p>
											Unicl 홈페이지에 오신걸 환영합니다.빅데이터와 텍스트마이닝을 통한 뉴스 키워드 분석하는 홈페이지입니다.<br>즐거운하루
											보내세요.</br>
										</p>
										<a href="index.jsp" class="btn btn-theme">Newswid Go</a>
									</div></li>

								<li><img src="img/mainslides/2.jpg" alt="" />
									<div class="flex-caption">
										<h3>Unicl</h3>
										<p>
											Unicl 홈페이지에 오신걸 환영합니다.빅데이터와 텍스트마이닝을 통한 뉴스 키워드 분석하는 홈페이지입니다.<br>즐거운하루
											보내세요.</br>
										</p>
										<a href="index.jsp" class="btn btn-theme">Newswid Go</a>
									</div></li>

								<li><img src="img/mainslides/3.jpg" alt="" />
									<div class="flex-caption">
										<h3>Unicl</h3>
										<p>
											Unicl 홈페이지에 오신걸 환영합니다.빅데이터와 텍스트마이닝을 통한 뉴스 키워드 분석하는 홈페이지입니다.<br>즐거운하루
											보내세요.</br>
										</p>
										<a href="index.jsp" class="btn btn-theme">Newswid Go</a>
									</div></li>

								<li><img src="img/mainslides/4.jpg" alt="" />
									<div class="flex-caption">
										<h3>Unicl</h3>
										<p>
											Unicl 홈페이지에 오신걸 환영합니다.빅데이터와 텍스트마이닝을 통한 뉴스 키워드 분석하는 홈페이지입니다.<br>즐거운하루
											보내세요.</br>
										</p>
										<a href="index.jsp" class="btn btn-theme">Newswid Go</a>
									</div></li>

								<li><img src="img/mainslides/5.jpg" alt="" />
									<div class="flex-caption">
										<h3>Unicl</h3>
										<p>
											Unicl 홈페이지에 오신걸 환영합니다.빅데이터와 텍스트마이닝을 통한 뉴스 키워드 분석하는 홈페이지입니다.<br>즐거운하루
											보내세요.</br>
										</p>
										<a href="index.jsp" class="btn btn-theme">Newswid Go</a>
									</div></li>
							</ul>
						</div>
						<!-- end slider -->
					</div>
				</div>
			</div>
		</section>
		<footer>
     <div class="container">
        <div class="row">
            <div class="col-lg-3">
                <div class="widget">
                    <address>
                        <strong>Unicl company Inc</strong><br>인천대학교<br>컴퓨터공학부<br>Unite
                        Internet Cooperation Lab
                    </address>

                </div>
            </div>


            <div class="col-lg-3">
                <div class="widget">
                    <address>
                        <strong>Developer-phoneNumber</strong><br>김대규: 010-7384-7447
                        <br> 정종우: 010-2974-7746 <br>서현동: 010-3812-8695 <br>
                        문은환: 010-5524-9184 <br>
                    </address>
                </div>
            </div>

            <div class="col-lg-3">
                <div class="widget">
                    <address>
                        <strong>Developer-eMail</strong><br>김대규: kdg7447@naver.com <br>
                        정종우: wjdwhdvkfdl@naver.com <br>서현동: shd8989@nate.com <br>
                        문은환: meh9184@naver.com <br>
                    </address>
                </div>
            </div>
        </div>
        <div id="sub-footer">
                <div class="container">
                    <div class="row">
                        <div class="col-lg-6">
                            <div class="copyright">
                                <p>
                                    <span>&copy; Unicl 2015 All right reserved. By </span><a
                                        href="http://bootstraptaste.com" target="_blank">Bootstraptaste</a>
                                </p>
                            </div>
                        </div>
                			<div
							  class="fb-like"
							  data-share="true"
							  data-width="450"
							  data-show-faces="true">
							</div>  
                    </div>
                </div>
            </div>
        
		</footer>
	</div>
	<a href="#" class="scrollup"><i class="fa fa-angle-up active"></i></a>
	<!-- javascript
    ================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->
	<script src="js/jquery.js"></script>
	<script src="js/jquery.easing.1.3.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<script src="js/jquery.fancybox.pack.js"></script>
	<script src="js/jquery.fancybox-media.js"></script>
	<script src="js/google-code-prettify/prettify.js"></script>
	<script src="js/portfolio/jquery.quicksand.js"></script>
	<script src="js/portfolio/setting.js"></script>
	<script src="js/jquery.flexslider.js"></script>
	<script src="js/animate.js"></script>
	<script src="js/custom.js"></script>
</body>
</html>