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

<link href="//fonts.googleapis.com/earlyaccess/jejuhallasan.css"
    id="link-webfont" rel="stylesheet">
<script defer
    src="//ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script>
<script defer
    src="//cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/2.2.2/bootstrap.min.js"></script>
<script defer src="./src/wordcloud2.js"></script>
<script defer src="./index3.js"></script>
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
-webkit-keyframes blink { 0% {
    opacity: 1;
}

100%
{
opacity:0;
}
}
@
keyframes blink { 0% {
    opacity: 1;
}
100%
{
opacity:0;
}
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



    <script language=javascript>
    
	<%
	
    request.setCharacterEncoding("utf-8"); 
    String item = request.getParameter("item");
    item = URLDecoder.decode(item, "UTF-8") ;
	
    //String item = "박근혜->김무성";
    
	String[] itemList = new String[2];
	itemList = item.split("->");
	
	String[][] newsList = new String[21][20];
	
	String kd = "";
	MongoClient mongoClient = new MongoClient();
	DB db = mongoClient.getDB("unicl");
	DBCollection col1 = db.getCollection("newswid");
	BasicDBObject query = new BasicDBObject("code", java.util.regex.Pattern.compile("20150529"))
			.append("content", java.util.regex.Pattern.compile(itemList[0].toString()))
			//new BasicDBObject("content", java.util.regex.Pattern.compile(itemList[0].toString()))
			.append("content", java.util.regex.Pattern.compile(itemList[1].toString()));
	//.append("content","/세종/").append("content", "/윤상직/");
	BasicDBObject fields = new BasicDBObject("news", 1).append("title", 1).append("url", 1).append("content", 1);
	   //DBCursor cursor = col1.find({"news":"total", code:"20150525_result"}, {count:1, word:1}).sort({count:-1}).limit(60);
	   DBCursor cursor = col1.find(query, fields);
	   cursor.sort(new BasicDBObject("news", 1)).limit(10);
	   int x = 0, y = 0;
	   try {
		    while (cursor.hasNext()) {
		    	BasicDBObject bsObj = (BasicDBObject)cursor.next();	   
		    	y=0;
		    	String sub = "";
		    	String a = "";
		    	String b = "";
		    	//sub = cursor.next().toString();
		    	newsList[x][y] = new String();
		    	newsList[x][y] = bsObj.getString("title"); //0
		    	y++;
		    	newsList[x][y] = bsObj.getString("url"); //1
		    	y++;
		    	newsList[x][y] = bsObj.getString("content"); //2
		    	newsList[x][y] = newsList[x][y].substring(0, 150) + ".....";
		    	y++;
		    	newsList[x][y] = bsObj.getString("news"); //3
		    	y++;
		    	//sub = sub.substring(sub.indexOf("word"));
		    	//a = sub.substring(9, sub.indexOf(",")-2).toString();
		    	//b = sub.substring(sub.indexOf("count")).toString();
		    	//b = b.substring(9, b.indexOf("}")).toString();
		    	kd = kd + sub;//b + " " + a + ",";
		    	//break;	
		    	x++;
		    }
		} finally {
		    cursor.close();
		}
	   mongoClient.close();
	   //kd = kd.substring(1, kd.length()-1);
%>    
    
    <% 
        
//        String[][] newsList = new String[7][4];
        /*
        newsList[0][0] = "[뒤카] (14) 청와대와 야당의 '뒤집기' 해프닝";
        newsList[0][1] = "http://news.chosun.com/site/data/html_dir/2015/05/22/2015052201707.html";
        newsList[0][2] = "# 불난 집에 부채질?지난 20일 새정치연합 최고위원 회의장에 한 원로 당원이 들어와 잠시 소동이 일었다. 엄숙했던 분위기가 한 순간에 깨지고…. 관계자들의 제지를 뚫고 간신히 발언권을 얻은 원로 당원은 최고위원들을 향해 “지금, 우리 집이 사방으로 해서 불타고 있다”며 “우리가 비우고 또 비우고, 버리고 또 버리면서 집을 살려내야겠습니다”라고 외쳤다.# 긴박했던(?) 총리 내정자 발표지난 21일 오전 10시쯤. 청와대 총리 내정자 발표를 앞두고 기자들은 분주한 분위기 속에 새로운 총리 후보가 누구인지를 기다리는데…. 하지만 발표 10여초 전에 갑자기 청와대 관계자가 발표가 연기됐다고 한마디 툭 던지고는 한 줄 설명도 없이 가버린다. 허탈한 기자들의 한 마디. “참~ 긴박하네.”# 놀란 유승민 황교안 법무부장관이 새 총리 후보로 지명되고, 당내 회의장에서 스마트폰을 뚫어져라 쳐다보던 유승민 새누리당 원내대표는 메시지를 확인하고서 깜짝 놀라고 만다. 유 원내대표는 기자들의 질문에 “제가 잘못 들었는지 이상한 일이네요, 한번 확인해봐야겠습니다”라고 답했다. 당초 유승민 원내대표가 들었던 ‘이름’은 과연 누구의 이름이었을까.여·야·청와대에서 있었던 해프닝들을 ‘뒤카’에 담았다.";
        newsList[0][2] = newsList[0][2].substring(0, 150) + ".....";
        newsList[0][3] = "chosun";
        
        newsList[1][0] = "김무성, 대선주자 지지율 3주 연속 1위… 문재인에 2.7%P 앞서";
        newsList[1][1] = "http://news.chosun.com/site/data/html_dir/2015/05/26/2015052600272.html";
        newsList[1][2] = "김무성 새누리당 대표가 차기 대선주자 지지율 조사에서 3주 연속 1위를 기록했다. 여론조사 회사 리얼미터가 25일 발표한 5월 3주차(18~ 22일) 주간 여론조사에 따르면, 김 대표는 전주(21.2%)보다 1%포인트 오른 22.2%를 기록해 2위인 문재인 새정치민주연합 대표(19.5%)와의 격차를 1.6%포인트에서 2.7%포인트로 벌렸다. 김 대표는 지난 4·29 재보선 승리 직후 같은 기관의 5월 1주차 조사에서 22.6%로 문 대표(22.5%)를 0.1%포인트 앞선 데 이어 3주 연속 선두를 유지했다. 여기에는 김 대표가 지난 17일 광주 5·18 전야제를 찾아갔다가 일부 참석자들로부터 물세례를 받은 일이 영향을 줬다는 분석도 있다. 지난 23일 노무현 전 대통령 6주기 추도식에서 물세례를 받은 일은 이번 조사 기간 동안 발생한 것이 아니다. 여권 관계자는 \"김 대표가 국민 통합 행보를 하다가 봉변을 당하는 모습은 보수층의 결집과 함께 중도층이 마음을 여는 효과가 있을 것\"이라고 했다. 이번 조사에서 김·문 대표 다음으로는 박원순 서울시장(14.6%), 김문수 전 경기지사(6.9%), 안철수 새정치연합 의원(6.3%) 등의 순이었다. 2500명을 대상으로 한 이번 조사는 전화 면접 및 ARS 방식으로 유·무선 전화를 병행했으며, 표본오차는 95% 신뢰 수준에서 ±2.0%포인트다.";
        newsList[1][2] = newsList[1][2].substring(0, 150) + ".....";
        newsList[1][3] = "yonhap";
        
        newsList[2][0] = "野 \"문형표 해임 안하면 54개 法案도 통과 안시켜\"";
        newsList[2][1] = "http://news.chosun.com/site/data/html_dir/2015/05/26/2015052600247.html";
        newsList[2][2] = "새정치민주연합은 25일 \"문형표 보건복지부 장관의 해임 건의안이 본회의에 상정되지 않으면 공무원연금 개혁안뿐만 아니라 법사위를 통과한 54개 법안의 본회의 처리도 상정해 줄 수 없다\"는 입장을 밝혔다. 새누리당 조해진, 새정치연합 이춘석 원내수석부대표는 이날 국회에서 협상을 벌였지만 30분 만에 합의 없이 끝났다. 조 부대표는 회동 직후 \"야당은 문 장관 해임 건의안의 본회의 상정에 (여당의) 호응이 없으면 나머지 구체적인 안건에 대한 협의도 들어가기 어렵다고 하고 있다\"고 말했다. 이에 대해 이 부대표는 \"문 장관은 잘못된 통계 수치 등을 내세워 (공무원연금 개혁 여야) 합의안을 깨는 데 결정적으로 기여했다\"며 \"문 장관 해임 건의안에 대한 새누리당의 입장을 들어본 뒤 나머지 (법사위를 통과한 54개) 법안 처리 여부를 결정할 것\"이라고 말했다. 그는 \"문 장관 해임 건의안 처리에 대한 합의가 이뤄져야 다른 안건을 논의할 수 있다\"고도 했다. 이와 관련, 새누리당 유승민 원내대표는 \"문 장관 해임 건의안에 응해줄 아무런 이유가 없다. (협상 결렬 등) 모든 가능성에 대비하고 있다\"고 했다. 여야의 대치가 이어지고 있지만, 막판 타협 가능성도 있다. 여야는 그동안 쟁점이 됐던 ‘국민연금 소득 대체율 50%로 인상’ 문구를 국회 규칙에 명기하지 않는 것에는 사실상 합의를 했다. 대신 ‘(국민연금 소득 대체율을 50%로 인상한다는 내용을 담은) 공무원연금 개혁 실무 기구의 합의문을 이행할 세부 계획을 국민연금 등 공적 연금 강화를 위한 사회적 기구에서 논의해 마련한다’는 취지의 문구를 넣기로 했다. 새누리당 관계자는 “공무원연금 개혁안 처리 지연에 여야 모두 정치적 부담을 느끼고 있다”며 “28일 본회의 전에 여야 원내대표 등 당 지도부가 최종 담판 협상을 하지 않겠느냐”고 말했다.";
        newsList[2][2] = newsList[2][2].substring(0, 150) + ".....";
        newsList[2][3] = "yonhap";
        
        newsList[3][0] = "野 일부 \"노건호 발언, 장소 부적절\"… 노건호 \"정치 뜻 없다\"";
        newsList[3][1] = "http://news.chosun.com/site/data/html_dir/2015/05/26/2015052600270.html";
        newsList[3][2] = "야당 일각에서는 25일 노무현 전 대통령의 아들 건호씨가 지난 23일 노 전 대통령 추도식에서 했던 발언에 \"틀린 말이 아닐지라도, 장소는 부적절했다\"는 말이 나왔다. 그러자 친노(親盧) 지지자들은 인터넷 등에서 이들을 집중 공격했다. 새정치민주연합 이종걸 원내대표는 이날 서울 조계사 부처님오신날 기념행사에서 기자들과 만나 건호씨 발언에 대해 \"적절하고 필요한 말이었지만, 추도식 손님에 대한 예의 등은 종합적으로 (고려)되진 않은 것 같다\"며 \"자연스럽지 않았다\"고 말했다. 당 윤리심판원장인 강창일(3선) 의원도 \"할 수 있는 말이었다고는 생각하지만, '추모식'이라는 장소에 논란의 소지가 있다\"고 했다. 또 \"노 전 대통령은 가족과 당을 떠나 국민이 모시는 분\"이라며 \"유족이 가족 차원에서 (노 전 대통령의 뜻을) 해석해버리면 곤란하지 않으냐는 생각도 있다\"고 했다. 박용진 새정치연합 전 홍보위원장도 전날 한 종편 채널에 출연해 \"자리가 적절치 않았다\"고 했다. 이에 대해 친노 지지자들은 이 원내대표를 향해 \"한길이(김한길 전 대표) 똘마니\" \"원내대표란 XX가 이리도 매가리가 없으니 새누리2중대라는 소리를 듣는 것\" 등의 글을 인터넷 포털사이트와 SNS 등에 올렸다. 강 의원과 박 전 위원장에 대해서도 \"꺼져. 뭔 놈의 자리를 따지고 XX이야\" 등의 글이 달렸다. 이런 글은 25일 트위터에서 거의 1분에 하나꼴로 올라왔다. 한편 건호씨는 이날 일부 언론 인터뷰에서 \"정치에 전혀 관심이 없고, 정치할 생각도 전혀 없다\"고 말했다. 그는 자신의 발언에 따른 파장과 관련해서도 \"일절 대응하지 않겠다\"는 입장을 밝혔다.";
        newsList[3][2] = newsList[3][2].substring(0, 150) + ".....";
        newsList[3][3] = "yonhap";
        
        newsList[4][0] = "韓·美·中·日 이번주 연쇄 北核회담";
        newsList[4][1] = "http://news.chosun.com/site/data/html_dir/2015/05/26/2015052600243.html";
        newsList[4][2] = "교착 상태인 북핵(北核) 협상에 돌파구를 마련하기 위해 이번 주 중 한·미·중·일 4국 간 협의가 연쇄적으로 열린다. 특히 중국이 처음으로 한·미 6자회담 수석대표를 동시에 초청해 협의를 갖기로 했다. 사실상 '한·미·중 3자 북핵 대화'가 열리는 것으로, 이는 북한에 상당한 압박이 될 것으로 보인다. 황준국 외교부 한반도평화교섭본부장과 성 김 미 특별대표, 이하라 준이치(伊原純一) 일본 외무성 아시아대양주 국장 등 한·미·일 3국의 6자회담 수석대표는 26~27일 이틀간 서울에서 양자 및 3자협의를 통해 북한을 대화의 장으로 끌어내기 위한 방안을 논의한다. 한·미·일 6자회담 수석대표 회동은 지난 1월 28일 일본 도쿄에서의 만남 이후 4개월 만이다. 황 본부장과 성 김 특별대표는 이어 바로 베이징(北京)을 방문해 중국 측 6자회담 수석대표인 우다웨이(武大偉) 한반도사무특별대표를 각각 만난다. 한·미·일 간 협의된 내용을 들고 중국과 최종적으로 조율하는 모양새다. ";
        newsList[4][2] = newsList[4][2].substring(0, 150) + ".....";
        newsList[4][3] = "yonhap";
        
        newsList[5][0] = "[뉴스쇼 판] \"김정은 9월 방중 가능성 90%\"…\"김정철, 검은돈으로 외유\"";
        newsList[5][1] = "http://news.chosun.com/site/data/html_dir/2015/05/25/2015052501818.html";
        newsList[5][2] = "북한 김정은이 오는 9월 중국 베이징에서 열리는 전승 70주년 기념식에 참석할 것이라는 분석이 나왔다. 중국 인민해방군 소장 출신의 군사평론가 쉬방위는 “김정은이 9월 3일 (항일전쟁 승전) 기념일에 참석할 가능성은 90%”라고 말했다. 그는 김정은이 방중을 통해 중국 체면을 세워주는 대신 6자 회담이나 핵문제 등에서 도움을 받으려 할 것이라고 내다봤다.";
        newsList[5][2] = newsList[5][2].substring(0, 150) + ".....";
        newsList[5][3] = "khan";
        
        newsList[6][0] = "[뉴스쇼 판] 文-安, 만나기만 하면 '진실공방'";
        newsList[6][1] = "http://news.chosun.com/site/data/html_dir/2015/05/22/2015052201707.html";
        newsList[6][2] = "최근 새정치민주연합 문재인 대표와 안철수 전 대표는 만날 때마다 서로 말이 다른 경우가 자주 일어난다. 문 대표는 24일 당내 대선주자 협의체인 ‘희망 스크럼’을 만들기 위해 안철수 전 대표와 만나기로 했다고 했는데, 안 전 대표는 참여하겠다고 한 적이 없다고 했다. 지난 20일에도 문 대표는 안 전 대표가 혁신기구 위원장직 제안에 대해 “유보했다”고 했지만, 안 전 대표는 거절했다고 해 논란이 됐다.";
        newsList[6][2] = newsList[6][2].substring(0, 150) + ".....";    
        newsList[6][3] = "khan";
        */
    %>
    
    
    <%  
        String Keyword_Result = "";
    %>
    
        var Keyword = '<%=Keyword_Result%>';
            
    </script>



<body>
</head>
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
            <p align="center"><%=item%></p>
        </h3>
        
      </div> 
<!--  뉴스기사 -->
<%=kd%>
    <div class="tabbable">
        <ul class="nav nav-tabs" style="padding-left: 10%;">
          <li class="active"><a href="#tab-전체" data-toggle="tab">전체</a></li>
          <li><a href="#tab-경향신문" data-toggle="tab">경향신문</a></li> 
          <li><a href="#tab-국민일보" data-toggle="tab">국민일보</a></li>
          <li><a href="#tab-연합뉴스" data-toggle="tab">연합뉴스</a></li>
          <li><a href="#tab-조선일보" data-toggle="tab">조선일보</a></li>
          <li><a href="#tab-한겨례" data-toggle="tab">한겨례</a></li>
          <li><a href="#tab-한국경제" data-toggle="tab">한국경제</a></li>
          <li><a href="#tab-한국일보" data-toggle="tab">한국일보</a></li>
        </ul>
        <div class="tab-content" style= "border: none; padding-left: 5%;">     
    
          <div class="tab-pane active" style=" border: none;width: 60%; padding-left: 5%;" id="tab-전체" >
          <%for(int i=0; i < x; i++){%> 
            <a href="<%=newsList[i][1]%>" style="color:blue; font-size:20px;" > <%=newsList[i][0]%> </a> <br/>
            <a href="<%=newsList[i][1]%>" style="color:green; font-size:14px;" > <%=newsList[i][1]%> </a> <br/>
            <p style="color:black; font-size:14px;" > <%=newsList[i][2]%> </p> <hr>
          <%}%>
          </div>
          
          <div class="tab-pane" style=" border: none;width: 60%; padding-left: 5%;" id="tab-경향신문">
          <%for(int i=0; i < x; i++){%> 
            <%if(newsList[i][3].toString() == "khan"){%>
              <a href="<%=newsList[i][1]%>" style="color:blue; font-size:20px;" > <%=newsList[i][0]%> </a> <br/>
              <a href="<%=newsList[i][1]%>" style="color:green; font-size:14px;" > <%=newsList[i][1]%> </a> <br/>
              <p style="color:black; font-size:14px;" > <%=newsList[i][2]%> </p> <hr>
            <%}%>
          <%}%>
          </div>
          
          <div class="tab-pane" style=" border: none;width: 60%; padding-left: 5%;" id="tab-국민일보">
          <%for(int i=0; i < x; i++){%> 
            <%if(newsList[i][3].equals("kmib")){%>
              <a href="<%=newsList[i][1]%>" style="color:blue; font-size:20px;" > <%=newsList[i][0]%> </a> <br/>
              <a href="<%=newsList[i][1]%>" style="color:green; font-size:14px;" > <%=newsList[i][1]%> </a> <br/>
              <p style="color:black; font-size:14px;" > <%=newsList[i][2]%> </p> <hr>
            <%}%>
          <%}%>          
          </div> 
    
          <div class="tab-pane" style=" border: none;width: 60%; padding-left: 5%;" id="tab-연합뉴스">
          <%for(int i=0; i < x; i++){%> 
            <%if(newsList[i][3].equals("yonhap")){%>
              <a href="<%=newsList[i][1]%>" style="color:blue; font-size:20px;" > <%=newsList[i][0]%> </a> <br/>
              <a href="<%=newsList[i][1]%>" style="color:green; font-size:14px;" > <%=newsList[i][1]%> </a> <br/>
              <p style="color:black; font-size:14px;" > <%=newsList[i][2]%> </p> <hr>
            <%}%>
          <%}%>         
          </div>    
          
          <div class="tab-pane" style=" border: none;width: 60%; padding-left: 5%;" id="tab-조선일보">
          <%for(int i=0; i < x; i++){%> 
            <%if(newsList[i][3].equals("chosun")){%>
              <a href="<%=newsList[i][1]%>" style="color:blue; font-size:20px;" > <%=newsList[i][0]%> </a> <br/>
              <a href="<%=newsList[i][1]%>" style="color:green; font-size:14px;" > <%=newsList[i][1]%> </a> <br/>
              <p style="color:black; font-size:14px;" > <%=newsList[i][2]%> </p> <hr>
            <%}%>
          <%}%>         
          </div> 
          
          <div class="tab-pane" style=" border: none;width: 60%; padding-left: 5%;" id="tab-한겨례">
          <%for(int i=0; i < x; i++){%> 
            <%if(newsList[i][3].equals("hani")){%>
              <a href="<%=newsList[i][1]%>" style="color:blue; font-size:20px;" > <%=newsList[i][0]%> </a> <br/>
              <a href="<%=newsList[i][1]%>" style="color:green; font-size:14px;" > <%=newsList[i][1]%> </a> <br/>
              <p style="color:black; font-size:14px;" > <%=newsList[i][2]%> </p> <hr>
            <%}%>
          <%}%>
          </div>
          
          <div class="tab-pane" style=" border: none;width: 60%; padding-left: 5%;" id="tab-한국경제">
          <%for(int i=0; i < x; i++){%> 
            <%if(newsList[i][3].equals("hankyung")){%>
              <a href="<%=newsList[i][1]%>" style="color:blue; font-size:20px;" > <%=newsList[i][0]%> </a> <br/>
              <a href="<%=newsList[i][1]%>" style="color:green; font-size:14px;" > <%=newsList[i][1]%> </a> <br/>
              <p style="color:black; font-size:14px;" > <%=newsList[i][2]%> </p> <hr>
            <%}%>
          <%}%>          
          </div>
          
          <div class="tab-pane" style=" border: none;width: 60%; padding-left: 5%;" id="tab-한국일보">
          <%for(int i=0; i < x; i++){%> 
            <%if(newsList[i][3].equals("hankook")){%>
              <a href="<%=newsList[i][1]%>" style="color:blue; font-size:20px;" > <%=newsList[i][0]%> </a> <br/>
              <a href="<%=newsList[i][1]%>" style="color:green; font-size:14px;" > <%=newsList[i][1]%> </a> <br/>
              <p style="color:black; font-size:14px;" > <%=newsList[i][2]%> </p> <hr>
            <%}%>
          <%}%>         
          </div>       
            
        </div>
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