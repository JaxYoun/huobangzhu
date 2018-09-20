<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>登录页面</title>
</head>
<body>
<form action="/security" method="post">
    用户：<input type="text" name="j_user"/><br/>
    密码：<input type="password" name="j_pass"/><br/>
    认证：<input type="password" name="authCode"/><br/>
    <img src="/api/user/authCode/peek?width=600&height=111&size=5"/></br>
    <input type="submit" value="提交"/>
</form>
</body>
</html>