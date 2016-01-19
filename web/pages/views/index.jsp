<%--
  Created by IntelliJ IDEA.
  User: azurexsyl
  Date: 2015/6/28
  Time: 13:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <!-- Standard Meta -->
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
    <!-- Site Properities -->
    <title>QA - SYS</title>
    <link rel="stylesheet" type="text/css" href="pages/semanticui/semantic.css">
    <script src="pages/semanticui/semantic.js"></script>
    <script src="pages/js/jquery.min.js"></script>
    <script src="pages/js/iframe.js"></script>
    <script src="pages/js/qa.js"></script>

    <title>中草药智能问答系统接口测试</title>
    <style type="text/css">
        body > .ui.container {
            margin-top: 3em;
        }

        .ui.container > h1 {
            font-size: 3em;
            text-align: center;
            font-weight: normal;
        }

    </style>
</head>

<body>
<div class="ui container">

    <h1>中草药智能问答系统接口测试界面</h1>
    <br/>

    <!-- 搜索框 -->
    <div class="ui equal width grid">
        <div class="column"></div>
        <div class="ten wide column">
            <div class="ui fluid action input">
                <input type="text" placeholder="请输入您的问题" id="question">
                <div class="ui button" id="search">Search</div>
            </div>
        </div>
        <div class="column"></div>
    </div>

    <!-- 答案以及相关 -->
    <div class="ui equal width grid">
        <div class="column">
            <!--<p>实体识别</p>
            <div class="ui feed">
                <div class="event">
                    <div class="content">
                        <div class="summary"><a class="user">蜘蛛</a>被识别为<a class="user">蜘蛛</a> —— 一种单味药</div>
                        <div class="meta">
                            <a class="like"><i class="like icon"></i> 4 Likes </a>
                        </div>
                    </div>
                </div>
                <div class="event">
                    <div class="content">
                        <div class="summary"><a class="user">蜘蛛</a>被识别为<a class="user">蜘蛛</a> —— 一种动物</div>
                        <div class="meta">
                            <a class="like"><i class="like icon"></i> 3 Likes </a>
                        </div>
                    </div>
                </div>
                <div class="event">
                    <div class="content">
                        <div class="summary"><a class="user">蜘蛛</a>被识别为<a class="user">蜘蛛</a> —— 一种动物</div>
                        <div class="meta">
                            <a class="like"><i class="like icon"></i> 4 Likes </a>
                        </div>
                    </div>
                </div>
                <div class="event">
                    <div class="content">
                        <div class="summary"><a class="user">蜘蛛</a>被识别为<a class="user">蜘蛛</a> —— 一种动物</div>
                        <div class="meta">
                            <a class="like"><i class="like icon"></i> 1 Likes </a>
                        </div>
                    </div>
                </div>
            </div>-->
        </div>
        <div class="eight wide column" id="answer">
            <%--<div class="ui items">
                <div class="item">
                    <div class="ui small image">
                        <img src="/pages/images/image.png">
                    </div>
                    <div class="middle aligned content">
                        <div class="header">
                            麻黄的属性是什么？
                        </div>
                        <div class="description">
                            <p>麻黄的属性是XXXX</p>
                        </div>
                    </div>
                </div>
            </div>
            <div class="ui items">
                <div class="item">
                    <div class="ui small image">
                        <img src="/pages/images/image.png">
                    </div>
                    <div class="middle aligned content">
                        <div class="header">
                            麻黄的属性是什么？
                        </div>
                        <div class="description">
                            <p>麻黄的属性是XXXX</p>
                        </div>
                    </div>
                </div>
            </div>--%><%--<div class="ui items">
                <div class="item">
                    <div class="ui small image">
                        <img src="/pages/images/image.png">
                    </div>
                    <div class="middle aligned content">
                        <div class="header">
                            麻黄的属性是什么？
                        </div>
                        <div class="description">
                            <p>麻黄的属性是XXXX</p>
                        </div>
                    </div>
                </div>
            </div>
            <div class="ui items">
                <div class="item">
                    <div class="ui small image">
                        <img src="/pages/images/image.png">
                    </div>
                    <div class="middle aligned content">
                        <div class="header">
                            麻黄的属性是什么？
                        </div>
                        <div class="description">
                            <p>麻黄的属性是XXXX</p>
                        </div>
                    </div>
                </div>
            </div>--%>
        </div>
        <div class="column"></div>
    </div>
</div>
</body>

</html>
