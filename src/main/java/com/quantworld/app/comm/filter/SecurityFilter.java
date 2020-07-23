package com.quantworld.app.comm.filter;


import com.quantworld.app.comm.Const;
import com.quantworld.app.domain.User;
import com.quantworld.app.repository.UserRepository;
import com.quantworld.app.utils.Des3EncryptionUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class SecurityFilter implements Filter {

  protected Logger logger = LoggerFactory.getLogger(this.getClass());
  private static Set<String> GreenUrlSet = new HashSet<String>();

  @Autowired
  private UserRepository userRepository;

  @Override
  public void init(FilterConfig arg0) throws ServletException {
    // TODO Auto-generated method stub
    GreenUrlSet.add("/login");
    GreenUrlSet.add("/subscribeEvent");
    GreenUrlSet.add("/index");
    GreenUrlSet.add("/register");
    GreenUrlSet.add("/forgotPassword");
    GreenUrlSet.add("/newPassword");
    GreenUrlSet.add("/tool");
  }

  @Override
  public void doFilter(ServletRequest srequest, ServletResponse sresponse, FilterChain filterChain)
      throws IOException, ServletException {
    // TODO Auto-generated method stub
    HttpServletRequest request = (HttpServletRequest) srequest;
    String uri = request.getRequestURI();
    if (request.getSession().getAttribute(Const.LOGIN_SESSION_KEY) == null) {
      Cookie[] cookies = request.getCookies();
      if (containsSuffix(uri) || GreenUrlSet.contains(uri) || containsKey(uri)) {
        logger.debug("don't check  url , " + request.getRequestURI());
        filterChain.doFilter(srequest, sresponse);
        return;
      } else if (cookies != null) {
        boolean flag = true;
        for (int i = 0; i < cookies.length; i++) {
          Cookie cookie = cookies[i];
          if (cookie.getName().equals(Const.LOGIN_SESSION_KEY)) {
            if (StringUtils.isNotBlank(cookie.getValue())) {
              flag = false;
            } else {
              break;
            }
            String value = getUserId(cookie.getValue());
            String userId = "";
            if (userRepository == null) {
              BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
              userRepository = (UserRepository) factory.getBean("userRepository");
            }
            if (StringUtils.isNotBlank(value)) {
              userId = value;
            }
            Optional<User> user = userRepository.findById(userId);
            String html = "";
            if (!user.isPresent()) {
              html = "<script type=\"text/javascript\">window.location.href=\"_BP_login\"</script>";
            } else {
              User currentUser = user.get();
              logger.info("userId :" + currentUser.getId());
              request.getSession().setAttribute(Const.LOGIN_SESSION_KEY, currentUser);
              String referer = this.getRef(request);
              if (referer.indexOf("/standard?") >= 0 || referer.indexOf("/lookAround") >= 0) {
                filterChain.doFilter(srequest, sresponse);
                return;
              } else {
                html = "<script type=\"text/javascript\">window.location.href=\"_BP_\"</script>";
              }
            }
            html = html.replace("_BP_", Const.BASE_PATH);
            sresponse.getWriter().write(html);
            /**
             * HttpServletResponse response = (HttpServletResponse) sresponse;
             response.sendRedirect("/");
             */
          }
        }
        if (flag) {
          //跳转到登陆页面
          redirectToLoginPage(sresponse, request);
        }
      } else {
        //跳转到登陆页面
        redirectToLoginPage(sresponse, request);
        //	HttpServletResponse response = (HttpServletResponse) sresponse;
        //response.sendRedirect("/");

      }
    } else {
      filterChain.doFilter(srequest, sresponse);
    }
  }

  private void redirectToLoginPage(ServletResponse sresponse, HttpServletRequest request) throws IOException {
    String referer = this.getRef(request);
    logger.debug("security filter, deney, " + request.getRequestURI());
    String html = "";
    if (referer.contains("/standard?") || referer.contains("/lookAround")) {
      html = "<script type=\"text/javascript\">window.location.href=\"_BP_login\"</script>";
    } else {
      html = "<script type=\"text/javascript\">window.location.href=\"_BP_index\"</script>";
    }
    html = html.replace("_BP_", Const.BASE_PATH);
    sresponse.getWriter().write(html);
  }


  /**
   * @param url
   * @return
   * @author neo
   * @date 2016-5-4
   */
  private boolean containsSuffix(String url) {
    if (url.endsWith(".js")
        || url.endsWith(".css")
        || url.endsWith(".jpg")
        || url.endsWith(".gif")
        || url.endsWith(".png")
        || url.endsWith(".html")
        || url.endsWith(".eot")
        || url.endsWith(".svg")
        || url.endsWith(".ttf")
        || url.endsWith(".woff")
        || url.endsWith(".ico")
        || url.endsWith(".woff2")) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * @param url
   * @return
   * @author neo
   * @date 2016-5-4
   */
  private boolean containsKey(String url) {

    if (url.contains("/media/")
        || url.contains("/login") || url.contains("/user/login")
        || url.contains("/subscribeEvent") || url.contains("/user/subscribeEvent") || url.contains("/index")
        || url.contains("/forgotPassword") || url.contains("/user/sendForgotPasswordEmail")
        || url.contains("/newPassword") || url.contains("/user/setNewPassword")
        || (url.contains("/collector") && !url.contains("/standard/detail/"))
        || url.contains("/standard/standard/") || url.contains("/standard/simple/")
        || url.contains("/user") || url.contains("/favorites") || url.contains("/comment")
        || url.contains("/lookAround")
        || url.startsWith("/user/")
        || url.startsWith("/feedback")
        || url.startsWith("/standard/")) {
      return true;
    } else {
      return false;
    }
  }


  @Override
  public void destroy() {
    // TODO Auto-generated method stub
  }

  public String codeToString(String str) {
    String strString = str;
    try {
      byte tempB[] = strString.getBytes("ISO-8859-1");
      strString = new String(tempB);
      return strString;
    } catch (Exception e) {
      return strString;
    }
  }

  public String getRef(HttpServletRequest request) {
    String referer = "";
    String param = this.codeToString(request.getQueryString());
    if (StringUtils.isNotBlank(request.getContextPath())) {
      referer = referer + request.getContextPath();
    }
    if (StringUtils.isNotBlank(request.getServletPath())) {
      referer = referer + request.getServletPath();
    }
    if (StringUtils.isNotBlank(param)) {
      referer = referer + "?" + param;
    }
    request.getSession().setAttribute(Const.LAST_REFERER, referer);
    return referer;
  }

  public String getUserId(String value) {
    try {
      String userId = Des3EncryptionUtil.decode(Const.DES3_KEY, value);
      userId = userId.substring(0, userId.indexOf(Const.PASSWORD_KEY));
      return userId;
    } catch (Exception e) {
      logger.error("解析cookie异常：", e);
    }
    return null;
  }
}