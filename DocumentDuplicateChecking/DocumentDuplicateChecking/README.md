1. 用户注册
用户填写手机号码，进行滑动验证码验证，kafka发布消息，消费者生成4位验证码并存入redis(五分钟后失效)，同时前段跳转到详细信息填写页面，完善信息后填写接受到的验证码，
服务端删除redis记录验证格式正确性，若未填写昵称则根据手机号自动生成默认昵称，生成salt值，并将密码进行MD5加密后加上salt再进行一次md5加密，写入数据库
2. 用户登录
token与JWT

- 项目遇到的问题
  - 跨域(jsonp、CROS)
    - jsonp:
    ```
    前端：ajax属性
    dataType : 'jsonp',
    jsonpCallback:"callback",
    后端：
    String jsonpCallback = httpServletRequest.getParameter("callback");
    return new JSONPObject(jsonpCallback,object);
    ```
  - 
