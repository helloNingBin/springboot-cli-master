package com.springboot.cli.security.hander;

import com.springboot.cli.exception.CaptchaInvalidException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录失败
 *
 * @author ding
 */
@Component
@Slf4j
public class LoginFailure extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException {
        log.info("登录失败");
        ex.printStackTrace();
        this.saveException(request, ex);
        if ("验证码错误".equals(ex.getMessage())) {
            this.getRedirectStrategy().sendRedirect(request, response, "/toLogin?captchaInvalid=true");
        } else {
            this.getRedirectStrategy().sendRedirect(request, response, "/toLogin?error=true");
        }
    }
}
