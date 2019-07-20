<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.sql.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
  <meta charset="utf-8">
  <title>NEWSWID</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta name="description" content="">
  <meta name="author" content="">
  <!-- Le styles -->
  <link href="//netdna.bootstrapcdn.com/bootstrap/2.2.2/css/bootstrap.min.css" rel="stylesheet">
  <link href="//netdna.bootstrapcdn.com/bootstrap/2.2.2/css/bootstrap-responsive.min.css" rel="stylesheet">

  <link href="//fonts.googleapis.com/earlyaccess/jejuhallasan.css" id="link-webfont" rel="stylesheet">
  <script defer src="//ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script>
  <script defer src="//cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/2.2.2/bootstrap.min.js"></script>
  <script defer src="./src/wordcloud2.js"></script>
  <script defer src="./index.js"></script>

  <!-- 게시판 css -->
  <link href="css/board.css" rel="stylesheet">

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
    text-shadow: 0 0 10px, 0 0 10px #fff, 0 0 10px #fff, 0 0 10px #fff;
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
  @-webkit-keyframes blink {
    0% { opacity: 1; }
    100% { opacity: 0; }
  }
  @keyframes blink {
    0% { opacity: 1; }
    100% { opacity: 0; }
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



</head>
<body>
  <div class="navbar navbar-fixed-top navbar-inverse">
    <div class="navbar-inner">
      <div class="container">
        <a class="brand">NEWSWID</a>
        <ul class="nav">
          <li class="active"><a href="http://www.naver.com">Word Cloud</a></li>
          <li><a href="http://www.naver.com">Emotion Graph</a></li>
          <li><a href="http://www.naver.com">Read Me</a></li>
          <li><a href="http://www.naver.com">About Making</a></li>
        </ul>
      </div>
    </div>
  </div>
  <div class="container">
  <div class="board">
    <table class="list">
      <caption>연관 검색 뉴스</caption>
        <colgroup>
          <col width="20%" />
          <col width="60%" />
        </colgroup>
      <thead>
        <tr>
          <th>제목</th>
        </tr>
      </thead>
      <%
Connection conn = null;
PreparedStatement stmt = null;
ResultSet rs = null;

String wordc_word;
int wordc_num;
String wordc_title;
String wordc_url;

String url = "jdbc:mysql://localhost:3306/testdb";

try{
	Class.forName("com.mysql.jdbc.Driver");
	conn = DriverManager.getConnection(url,"root","8695");

	String Query1 = "delete from wordc";
	String Query2 = "load data local infile 'D:/shd.txt' into table wordc";
	String Query3 = "select * from wordc";
	stmt = conn.prepareStatement(Query1);
	int q1 = stmt.executeUpdate(Query1);
	stmt = conn.prepareStatement(Query2);
	int q2 = stmt.executeUpdate(Query2);
	stmt = conn.prepareStatement(Query3);
	rs = stmt.executeQuery();

	while(rs.next()) {
		wordc_word = rs.getString(1);
		wordc_num = rs.getInt(2);
		wordc_title = rs.getString(3);
		wordc_url = rs.getString(4);
%>
      <tbody>
        <tr>
          <td><h4><a href ="<%=wordc_url %>"><%=wordc_title %></a></h4></td>
        </tr>
        <tr>
          <td>a</td>
        </tr>
      </tbody>
<%
	}
} catch(ClassNotFoundException e) {
	out.println("Where is your mysql jdbc driver?");
	e.printStackTrace();
	return ;
}
rs.close();
stmt.close();
conn.close();
%>
    </table>
  </div>
  
  </div>
</body>
</html>
