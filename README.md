# issues-demo
A minimal GitHub sample application. https://github.com/spring-projects/spring-security/issues/9313.

# starter
1. run the  class `App`
2. request the url to debug

```
POST url: http://localhost:8080/front/login/normal
header: Content-Type=application/x-www-form-urlencoded
param:username=xx&password=xx
```

3. `AuthenticationEventListener` can catch the event unless I add the code `.and().apply(secondAuthenticationSecurityConfig)` in `WebSecurityConfig.configure(HttpSecurity http)`.
