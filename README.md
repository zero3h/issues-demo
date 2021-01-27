# issues-demo
A minimal GitHub sample application. https://github.com/spring-projects/spring-security/issues/9313.

# starter
Run the  class `com.demo.App.java`

# reproduce
1. Access address through browser: http://localhost:8080/front/login/normal?username=demo&password=demo1232
2. `CustomAuthenticationException` can be catch.

```
console output:
2021-01-22 17:56:43.237 DEBUG 26376 --- [nio-8080-exec-1] c.d.l.AuthenticationEventListener        : -------------------
2021-01-22 17:56:43.237 DEBUG 26376 --- [nio-8080-exec-1] c.d.l.AuthenticationEventListener        : -------------------
2021-01-22 17:56:43.237 DEBUG 26376 --- [nio-8080-exec-1] c.d.l.AuthenticationEventListener        : -------------------
2021-01-22 17:56:43.237 DEBUG 26376 --- [nio-8080-exec-1] c.d.l.AuthenticationEventListener        : custom exception enter listener!! event is com.demo.data.event.CustomAuthenticationExceptionEvent
2021-01-22 17:56:43.237 DEBUG 26376 --- [nio-8080-exec-1] c.d.l.AuthenticationEventListener        : -------------------
2021-01-22 17:56:43.237 DEBUG 26376 --- [nio-8080-exec-1] c.d.l.AuthenticationEventListener        : -------------------
2021-01-22 17:56:43.237 DEBUG 26376 --- [nio-8080-exec-1] c.d.l.AuthenticationEventListener        : -------------------
```


3. If I add second provider, `AuthenticationEventListener` can't catch this error! (`com.demo.provider.SecondLoginAuthenticationProvider` add `@Component`.

