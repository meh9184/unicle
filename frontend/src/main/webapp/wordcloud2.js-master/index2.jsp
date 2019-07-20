<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.mongodb.DBCursor"%>
<%@page import="com.mongodb.BasicDBObject"%>
<%@page import="com.mongodb.DBObject"%>
<%@page import="com.mongodb.DBCollection"%>
<%@page import="com.mongodb.DB"%>
<%@page import="com.mongodb.Mongo"%>
<%@page import="com.mongodb.MongoClient"%>
<%@page import="com.mongodb.BasicDBList"%>
<% request.setCharacterEncoding("UTF-8"); %>
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

<link href="//fonts.googleapis.com/earlyaccess/hanna.css"
    id="link-webfont" rel="stylesheet">
<script defer
    src="//ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script>
<script defer
    src="//cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/2.2.2/bootstrap.min.js"></script>
<script defer src="./src/wordcloud2.js"></script>
<script defer src="./index2.js"></script>
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
    box-shadow: 0 0 100px 100px rgba(11, 105, 202, 0.5);
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
-webkit-keyframes blink { 0% {
    opacity: 1;
} 100% {opacity:0;}}
@
keyframes blink { 0% {
    opacity: 1;
} 100% {opacity:0;}}

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



	<script language=javascript>
  	
	<% 
	request.setCharacterEncoding("utf-8"); 
	String item = request.getParameter("item");
	item = URLDecoder.decode(item, "UTF-8") ;
	//String item = "황교안";
	%>
	
	<%
	String kd = "";
	MongoClient mongoClient = new MongoClient();
	DB db = mongoClient.getDB("unicl");
	DBCollection col1 = db.getCollection(item);
	BasicDBObject query = new BasicDBObject();
	BasicDBObject fields = new BasicDBObject("total", 1).append("word", 1);
	   //DBCursor cursor = col1.find({"news":"total", code:"20150525_result"}, {count:1, word:1}).sort({count:-1}).limit(60);
	   DBCursor cursor = col1.find(query, fields);
	   cursor.sort(new BasicDBObject("total", -1)).limit(50);
	   try {
		   int i = 0;
		    while (cursor.hasNext()) {
		    	String sub = "";
		    	String a = "";
		    	String b = "";
		    	double c = 0.0;
		    	int d = 0;
		    	int center = 0;
		    	String centerStr = "";
		    	sub = cursor.next().toString();
		    	sub = sub.substring(sub.indexOf("word"));
		    	a = sub.substring(9, sub.indexOf(",")-2).toString();
		    	if(a==item) continue;
		    	if(a.length() < 2) continue;
		    	b = sub.substring(sub.indexOf("total")).toString();
		    	b = b.substring(9, b.indexOf("}")).toString();
		    	c = Double.parseDouble(b.substring(0, 6)) * 100;
		    	d = (int)c;
		    	center = d + 10;
		    	centerStr = String.valueOf(center);
		    	centerStr = " 40"; //fix4
		    	b = String.valueOf(d);
		    	b = "10"; //fix
		    	if(i==0) {
		    		kd = centerStr + " " + item + "," + b + " " + a + ",";
		    	}
		    	else {
		    		kd = kd + b + " " + a + ",";
		    	}
		    	//kd = kd + a;
		    	//break;	
		    	i=1;
		    }
		} finally {
		    cursor.close();
		}
	   mongoClient.close();
	   kd = kd.substring(1, kd.length()-1);	
	
	/*
	String kd = "";
	MongoClient mongoClient = new MongoClient();
	DB db = mongoClient.getDB("unicl");
	DBCollection col1 = db.getCollection("newswid");
	
	BasicDBObject group = new BasicDBObject();
	BasicDBObject groupparams = new BasicDBObject();
	BasicDBObject key = new BasicDBObject();
	BasicDBObject cond = new BasicDBObject();
	//BasicDBObject reduce = new BasicDBObject();
	String reduce = "";
	BasicDBObject initial = new BasicDBObject();
	
	key.append("date", 1).append("word", 1);
	cond.append("date", "20150525").append("title", "//새누리당//");
	//reduce.append("$reduce", "function(crr, result) { result.total += curr.tfidf; }");
	reduce = "function(curr,result){result.total+=curr.tfidf;}";
	initial.append("total", 0);
	
	group.append("key", key).append("cond", cond)
	                        .append("reduce", reduce)
	                        .append("initial", initial);

	BasicDBObject query = new BasicDBObject("date", "20150525");
	DBCursor cursor = col1.find(query);
	
	BasicDBList returnList = (BasicDBList) col1.group(key, cond, initial, reduce);
	for(Object o : returnList) {
		kd = kd + o.toString();
		kd = kd + "abcd";
	}
	
	//DBObject r = col1.group(key, cond, initial, reduce);
	//kd = r.toString();
	//kd = cursor.next().toString();
	//cursor.close();
	//DBCursor cursor = col1.find(r);
    while (cursor.hasNext()) {
    	kd = cursor.next().toString();    	
    	break;		    	
    }*/	
	
	/*
	   cursor.sort(new BasicDBObject("count", -1)).limit(60);
	   try {
		    while (cursor.hasNext()) {
		    	String sub = "";
		    	String a = "";
		    	String b = "";
		    	sub = cursor.next().toString();
		    	sub = sub.substring(sub.indexOf("word"));
		    	a = sub.substring(9, sub.indexOf(",")-2).toString();
		    	b = sub.substring(sub.indexOf("count")).toString();
		    	b = b.substring(9, b.indexOf("}")).toString();
		    	kd = kd + b + " " + a + ",";
		    	//break;		    	
		    }
		} finally {
		    cursor.close();
		}
	   mongoClient.close();
	   kd = kd.substring(1, kd.length()-1);*/
%>			
	
  	<% 
  		//String Keyword = "40 " + item + ",6 청와대자유게시판 ,6 청와대신문고 ,6 청와대견학 ,6 대통령비서실 ,6 국회,6 청와대 주소,6 청와대조직도 ,6 청와대 비서실 ,6 청와대 위치 ,6국회의사당," +
  		//	"6 국가안보실 ,6 청와대민원실 ,6 이번방문 ,6 경복궁 ,6 청와대경제수석,6 큰바위사랑 ,6 기획재정부 ,6 청와대춘추관 ,6 외교부 ,6 청와대방문";
  	  	
  	
		
			String Keyword = "37 황교안,32 총리,23 일,20 북한,17 혁신,17 야당,15 박근혜,14 김상곤,13 김무성,12 위원장," +
				"11 후보,9 정부,8 개혁,8 공단,8 개성,8 청문회,7 청와대,7 문재인,7 새누리당,7 노건호," +
				"6 50,6 국민,6 국민연금,6 대체,6 현영철,5 국방부,5 국정,5 비판,5 사실상,5 소득," +
				"5 직무정지,5 처리,5 최고,5 출석,4 가지,4 결정,4 남북,4 반대,4 발사,4 사과," +
				"4 장관,4 주승용,4 처형,4 한국,3 경제,3 공갈,3 김격식,3 김정,3 논란,3 막말," +
				"3 명기,3 무산,3 미사,3 복지,3 북,3 불발,3 사망,3 세계,3 속보,3 압박"; 

			String[] Keyword_Main;
			
			Keyword_Main = Keyword.split("\\,");
			for (int i = 0; i < Keyword_Main.length; i++)
				Keyword_Main[i] = Keyword_Main[i].substring(Keyword_Main[i].indexOf(" ")+1);
	
			
			String[] Keyword_Detail = new String[Keyword_Main.length];
			for (int i = 0; i < Keyword_Main.length; i++)
				Keyword_Detail[i] = "40 " + Keyword_Main[i] + ",6 청와대자유게시판 ,6 청와대신문고 ,6 청와대견학 ,6 대통령비서실 ,6 국회,6 청와대 주소,6 청와대조직도 ,6 청와대 비서실 ,6 청와대 위치 ,6국회의사당,6 국가안보실 ,6 청와대민원실 ,6 이번방문 ,6 경복궁 ,6 청와대경제수석,6 큰바위사랑 ,6 기획재정부 ,6 청와대춘추관 ,6 외교부 ,6 청와대방문";
	
			String Keyword_Result = "";
			
			for (int i = 0; i < Keyword_Detail.length; i++){
				String[] tmp = Keyword_Detail[i].split("\\,");
				String[] tmp2= tmp[0].split(" ");
				if(tmp2[1].equals(item))
					Keyword_Result = Keyword_Detail[i];
			}
	%>
	    var preItem = '<%=item+"->"%>';
		var Keyword = '<%=kd%>';
					
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

 <div class="navbar navbar-fixed-top navbar-inverse">
        <div class="navbar-inner">
            <div class="container">
                <a class="brand" href="unicl.jsp">UNICL</a>
                <ul class="nav">
                    <li class="active"><a href="index.jsp">Newswid</a></li>
                    <li><a href="info.jsp">Info</a></li>
                    <li><a href="people.jsp">People</a></li>
                    
                    
                </ul>
                                			<div
							  class="fb-like"
							  data-share="true"
							  data-width="450"
							  data-show-faces="true">
							</div>  
            </div>
        </div>
    </div>

    <div class="container">
        <h3 class="lead" align="center">
            <strong>NEWSWID</strong> 
        </h3>
        <p class="lead_s" align="center">원하는 키워드 클릭시, 뉴스페이지로 이동</p>
        
              
        <form id="form" method="get" action="" >
	            <div class="row">
	                <div class="span12" id="canvas-container">
	                    <canvas id="canvas" class="canvas"></canvas>
	                    <div id="html-canvas" class="canvas hide"></div>
	                </div>
	            </div>       
  <div class="tabbable">                           
        <!--순위키워드 UI -->        
            <div class="tab-content" hidden>
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
          
                </div>
     		 </div>     
        </form>      
    </div>
 

        <p class="lead_s" align="center">
            <strong><font color="red" size=6>"<%=item %>"</font> 탐색어 추이 </strong>
        </p>


<!-- 그래프뿌리는곳 : Data , FusionCharts, WEB-INF -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.fusioncharts.com/jsp/core" prefix="fc"%>
<c:set var="folderPath" value="FusionCharts/" />
<c:set var="jsPath" value="${folderPath}" scope="request" />
<script src="${jsPath}fusioncharts.js"></script>

<!--그래프 , 키워드 마다 xml변경  -->
<%
String name1 ="황교안";
String name2 ="총리";
String name3 ="일";
String name4 ="북한";
String name5 ="혁신";
String name6 ="야당";
String name7 ="박근혜";
String name8 ="김상곤";
String name9 ="김무성";
String name10 ="위원장";
String name11 ="후보";
String name12 ="정부";
String name13 ="개혁";
String name14 ="공단";
String name15 ="개성";
String name16 ="청문회";
String name17 ="청와대";
String name18 ="문재인";
String name19 ="새누리당";
String name20 ="노건호";

String xmlUrl1 = "";
String xmlUrl2 = "";

if(item.equals(name1)){ xmlUrl1 ="Data/line/line1.xml"; xmlUrl2="Data/pie/pie1.xml";}
else if(item.equals(name2)){ xmlUrl1="Data/line/line2.xml"; xmlUrl2="Data/pie/pie2.xml";}
else if(item.equals(name3)){ xmlUrl1="Data/line/line3.xml"; xmlUrl2="Data/pie/pie3.xml";}
else if(item.equals(name4)){ xmlUrl1="Data/line/line4.xml"; xmlUrl2="Data/pie/pie4.xml";}
else if(item.equals(name5)){ xmlUrl1="Data/line/line5.xml"; xmlUrl2="Data/pie/pie5.xml";}
else if(item.equals(name6)){ xmlUrl1="Data/line/line6.xml"; xmlUrl2="Data/pie/pie6.xml";}
else if(item.equals(name7)){ xmlUrl1="Data/line/line7.xml"; xmlUrl2="Data/pie/pie7.xml";}
else if(item.equals(name8)){ xmlUrl1="Data/line/line8.xml"; xmlUrl2="Data/pie/pie8.xml";}
else if(item.equals(name9)){ xmlUrl1="Data/line/line9.xml"; xmlUrl2="Data/pie/pie9.xml";}
else if(item.equals(name10)){ xmlUrl1="Data/line/line10.xml"; xmlUrl2="Data/pie/pie10.xml";}
else if(item.equals(name11)){ xmlUrl1="Data/line/line11.xml"; xmlUrl2="Data/pie/pie11.xml";}
else if(item.equals(name12)){ xmlUrl1="Data/line/line12.xml"; xmlUrl2="Data/pie/pie12.xml";}
else if(item.equals(name13)){ xmlUrl1="Data/line/line13.xml"; xmlUrl2="Data/pie/pie13.xml";}
else if(item.equals(name14)){ xmlUrl1="Data/line/line14.xml"; xmlUrl2="Data/pie/pie14.xml";}
else if(item.equals(name15)){ xmlUrl1="Data/line/line15.xml"; xmlUrl2="Data/pie/pie15.xml";}
else if(item.equals(name16)){ xmlUrl1="Data/line/line16.xml"; xmlUrl2="Data/pie/pie16.xml";}
else if(item.equals(name17)){ xmlUrl1="Data/line/line17.xml"; xmlUrl2="Data/pie/pie17.xml";}
else if(item.equals(name18)){ xmlUrl1="Data/line/line18.xml"; xmlUrl2="Data/pie/pie18.xml";}
else if(item.equals(name19)){ xmlUrl1="Data/line/line19.xml"; xmlUrl2="Data/pie/pie19.xml";}
else if(item.equals(name20)){ xmlUrl1="Data/line/line20.xml"; xmlUrl2="Data/pie/pie20.xml";}
%>


<div id="LineIdDiv" align="left">
    <fc:render chartId="LineId" swfFilename="msline" width="55%" height="400"
        debugMode="false" registerWithJS="false" xmlUrl="<%=xmlUrl1%>"
        renderer="javascript" windowMode="transparent" />

<span id="PieIdDiv" align="center">
    <fc:render chartId="PieId" swfFilename="pie3d" width="40%" height="350"
        debugMode="false" registerWithJS="false" xmlUrl="<%=xmlUrl2%>"
        renderer="javascript" windowMode="transparent" />
</span>
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