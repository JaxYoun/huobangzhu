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
    认证：<input type="password" name="j_pass"/><br/>
    <img src="/api/user/authCode/peek"/></br>
    <input type="submit" value="提交"/>
</form>
</body>
</html>