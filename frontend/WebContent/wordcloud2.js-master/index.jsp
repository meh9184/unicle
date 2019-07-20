<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
	%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">

<head>
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
<meta charset="utf-8">
<title>UNICL</title>

<!-- Le styles -->
<link
    href="//netdna.bootstrapcdn.com/bootstrap/2.2.2/css/bootstrap.min.css"
    rel="stylesheet">
<link
    href="//netdna.bootstrapcdn.com/bootstrap/2.2.2/css/bootstrap-responsive.min.css"
    rel="stylesheet">

<link href="//fonts.googleapis.com/earlyaccess/jejuhallasan.css"
    id="link-webfont" rel="stylesheet">
<script defer
    src="//ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script>
<script defer
    src="//cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/2.2.2/bootstrap.min.js"></script>
<script defer src="./src/wordcloud2.js"></script>
<script defer src="./index.js"></script>
  <style>
  @media (min-width: 980px) {
    body { padding-top: 60px; }
  }

  *[hidden] {
    display: none;
  }

  #canvas-container {
    overflow-x: auto;
    overflow-y: visible;
    position: relative;
    margin-top: 20px;
    margin-bottom: 20px;
  }
  .canvas {
    display: block;
    position: relative;
    overflow: hidden;
  }

  .canvas.hide {
    display: none;
  }

  #html-canvas > span {
    transition: text-shadow 1s ease, opacity 1s ease;
    -webkit-transition: text-shadow 1s ease, opacity 1s ease;
    -ms-transition: text-shadow 1s ease, opacity 1s ease;
  }

  #html-canvas > span:hover {
    text-shadow: 0 0 10px, 0 0 10px #000000, 0 0 10px #000000, 0 0 10px #000000;
    opacity: 0.5;
  }


  #box {
    pointer-events: none;
    position: absolute;
    box-shadow: 0 0 200px 200px rgba(255, 255, 255, 0.5);
    border-radius: 50px;
    cursor: pointer;
  }

  textarea {
    height: 300px;
  }
  #config-option {
    font-family: monospace;
  }
  select { width: 100%; }

#loading {
    animation: blink 2s infinite;
    -webkit-animation: blink 2s infinite;
}

@
-webkit-keyframes blink { 
	0% { opacity: 1;}
	100% { opacity : 0;}
}
@
keyframes blink { 0% {
    opacity: 1;
}
100%
{
opacity : 0; }
}
</style>
  <script type="text/javascript">
    if (window.location.hostname === 'timdream.org') {
      var _gaq = _gaq || [];
      _gaq.push(['_setAccount', 'UA-4623408-2']);
      _gaq.push(['_trackPageview']);
    }

    (function() {
      var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
      ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
      var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
    })();

  </script>

<%
MongoClient mongoClient = new MongoClient();
DB db = mongoClient.getDB("unicl");
DBCollection col1 = db.getCollection("newswid");
%>
	<script language=javascript>
	
  		<% 
  			String Keyword = "37 북한,32 청와대,23 국회,20 박근혜,17 국방,17 정당,15 외교,14 홍준표,13 정청래,12 공무원연금," +
  				"11 문재인,9 연금,8 SLBM,8 개혁,8 야당,8 잠수함,7 검찰,7 대표,7 새누리당,7 원내," +
  				"6 50,6 국민,6 국민연금,6 대체,6 현영철,5 국방부,5 국정,5 비판,5 사실상,5 소득," +
  				"5 직무정지,5 처리,5 최고,5 출석,4 가지,4 결정,4 남북,4 반대,4 발사,4 사과," +
  				"4 장관,4 주승용,4 처형,4 한국,3 경제,3 공갈,3 김격식,3 김정,3 논란,3 막말," +
  				"3 명기,3 무산,3 미사,3 복지,3 북,3 불발,3 사망,3 세계,3 속보,3 압박"; 

			String[] Keyword_Main;
			Keyword_Main = Keyword.split("\\,");
			for (int i = 0; i < Keyword_Main.length; i++)
				Keyword_Main[i] = Keyword_Main[i].substring(Keyword_Main[i].indexOf(" "));
			
		%>
  		
  		var Keyword = '<%=Keyword%>';
  		
	</script>




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
                appId : '{1603775146530831}',
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
            js.src = "//connect.facebook.net/ko_KR/sdk.js#xfbml=1&appId=1408018492811950&version=v2.0";
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



 <div class="navbar navbar-fixed-top navbar-inverse">
        <div class="navbar-inner">
            <div class="container">
                <a class="brand" href="unicl.jsp">UNICL</a>
                <ul class="nav">
                    <li class="active"><a href="index.jsp">Newswid</a></li>
                    <li><a href="graph.jsp">Graph</a></li>
                    <li><a href="info.jsp">Info</a></li>
                    <li><a href="people.jsp">People</a></li>
                    
                    
                </ul>
            </div>
        </div>
    </div>

    <div class="container">
        <h3 class="lead" align="center">
            <strong>NEWSWID</strong>
        </h3>
        <p class="lead_s" align="center">빅데이터와 텍스트마이닝을 이용한 뉴스 키워드 분석 프로그램</p>
        <div id="not-supported" class="alert" hidden>
            <strong>Your browser is not supported.</strong>
        </div>
        <form id="form" method="get" action="">
            <div class="row">
                <div class="span12" id="canvas-container">
                    <canvas id="canvas" class="canvas"></canvas>
                    <div id="html-canvas" class="canvas hide"></div>
                </div>
            </div>
            <div class="tabbable">
                <ul class="nav nav-tabs">
                
                 <!-- 순위 오늘키워드리스트 -->    
                    <li class="active"><a href="#tab-list" data-toggle="tab">오늘의키워드</a></li>
                            
                <!-- 선택창 -->  
                            <li><a href="#tab-select" data-toggle="tab">
                            <select id="examples" class="">
                            <option selected>배경 바꾸기</option>
                            <option value="web-tech">Web Technologies</option>
                            <option value="les-miz">Les Misérables</option>
                            </select></a></li>
              <!-- 핫이슈 키워드 그래프 -->    
                        <li><a href="#tab-graph" data-toggle="tab">그래프</a></li>
                </ul>   
                
                                
        <!--순위키워드 UI -->        
            <div class="tab-content">
                    <div class="tab-pane active" id="tab-list">
                        <textarea id="input-list" placeholder="Put your list here."
                            rows="2" cols="30" class="span12"></textarea>
                    </div>
            <!-- 워드클라우드색 UI 색-->
                    <div class="tab-pane" id="tab-config">                      
                        <textarea id="config-option"
                            placeholder="Put your literal option object here." rows="2"
                            cols="30" class="span12"></textarea>                
                    </div>
            <!-- 워드클라우드색 UI 크기 -->      
                    <div class="tab-pane" id="tab-dim">
                        <label for="config-width">Width</label>
                        <div class="input-append">
                            <input type="number" id="config-width" class="input-small"
                                min="1"> <span class="add-on">px</span>
                        </div>

                        <div class="input-append">
                            <input type="number" id="config-height" class="input-small"
                                min="1">

                        </div>

                        <div class="input-append">
                            <input type="number" id="config-dppx" class="input-mini" min="1"
                                value="1" required>
                        </div>
                    </div>
        
                
             <!--그래프 UI -->                
                    <div class="tab-pane" id="tab-graph">                                     
                     
                      <!-- 그래프뿌리는곳 : Data , FusionCharts, WEB-INF -->
                    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
                    <%@ taglib uri="http://www.fusioncharts.com/jsp/core" prefix="fc"%>
                    <c:set var="folderPath" value="FusionCharts/" />
                    <c:set var="jsPath" value="${folderPath}" scope="request" />
                    <script src="${jsPath}fusioncharts.js"></script>
                    
                    <div id="BarIdDiv" align="left">
                        <fc:render chartId="BarId" swfFilename="bar2d" width="100%" height="400"
                            debugMode="false" registerWithJS="false" xmlUrl="Data/mainData.xml"
                            renderer="javascript" windowMode="transparent" />
                              </div>
                    </div>
                </div>
                
            </div>
        
        
        
        </form>
        
        
    </div>



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
                    <div class="col-lg-6">
                        <ul class="social-network">
                            <!-- 페이스북로그인 아이콘 -->
                            <li class="active"><a href="#tab-list" data-toggle="tab">
                                    <fb:login-button scope="public_profile,email" onlogin="checkLoginState();" id="status"> </fb:login-button>
                            </a></li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
        </div>
    </footer>
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