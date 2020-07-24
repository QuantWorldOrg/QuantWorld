/*
 * Copyright 2019-2020 Shawn Peng
 * Email: shawnpeng@yeah.net
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.quantworld.app.controller;

import com.quantworld.app.comm.Const;
import com.quantworld.app.comm.aop.LoggerManage;
import com.quantworld.app.domain.User;
import com.quantworld.app.domain.result.ExceptionMsg;
import com.quantworld.app.domain.result.Response;
import com.quantworld.app.domain.result.ResponseData;
import com.quantworld.app.repository.UserRepository;
import com.quantworld.app.service.ConfigService;
import com.quantworld.app.utils.DateUtils;
import com.quantworld.app.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.UUID;

@RestController
@RequestMapping(value = "/user")
public class UserController extends BaseController {

  @Value("${static.url}")
  private String staticUrl;

  @Value("${file.profilepictures.url}")
  private String fileProfilepicturesUrl;

  @Value("${file.backgroundpictures.url}")
  private String fileBackgroundpicturesUrl;
  @Autowired
  UserRepository userRepository;

  @Autowired
  ConfigService configService;

  @RequestMapping(value = "/login", method = RequestMethod.POST)
  @LoggerManage(description = "登陆")
  public Response login(User user, HttpServletResponse response) {
    try {
      //这里不是bug，前端userName有可能是邮箱也有可能是昵称。
      User loginUser = userRepository.findByUserNameOrEmail(user.getUserName(), user.getUserName());
      if (loginUser == null) {
        return new ResponseData(ExceptionMsg.LoginNameNotExists);
      } else if (!loginUser.getPassWord().equals(getPwd(user.getPassWord()))) {
        return new ResponseData(ExceptionMsg.LoginNameOrPassWordError);
      }
      Cookie cookie = new Cookie(Const.LOGIN_SESSION_KEY, cookieSign(loginUser.getId().toString()));
      cookie.setMaxAge(Const.COOKIE_TIMEOUT);
      cookie.setPath("/");
      response.addCookie(cookie);
      getSession().setAttribute(Const.LOGIN_SESSION_KEY, loginUser);
      return new ResponseData(ExceptionMsg.SUCCESS, "/");
    } catch (Exception e) {
      // TODO: handle exception
      logger.error("login failed, ", e);
      return new ResponseData(ExceptionMsg.FAILED);
    }
  }

  @RequestMapping(value = "/register", method = RequestMethod.POST)
  @LoggerManage(description = "注册")
  public Response create(User user) {

    try {
      User userByEmail = userRepository.findByEmail(user.getEmail());
      if (userByEmail != null) {
        return new ResponseData(ExceptionMsg.EmailUsed);
      }

      User userByUserName = userRepository.findByUserName(user.getUserName());
      if (userByUserName != null) {
        return new ResponseData(ExceptionMsg.UserNameUsed);
      }

      user.setPassWord(getPwd(user.getPassWord()));
      user.setCreateTime(DateUtils.getCurrentTime());
      user.setLastModifyTime(DateUtils.getCurrentTime());
      userRepository.save(user);
      configService.saveConfig(user.getId());
      getSession().setAttribute(Const.LOGIN_SESSION_KEY, user);
    } catch (Exception e) {
      logger.error("Cannot create user!", e);
    }
    return result();
  }

  /**
   * 修改个人简介
   *
   * @param introduction
   * @return
   */
  @RequestMapping(value = "/updateIntroduction", method = RequestMethod.POST)
  @LoggerManage(description = "修改个人简介")
  public ResponseData updateIntroduction(String introduction) {
    try {
      User user = getUser();
      userRepository.setIntroduction(introduction, user.getEmail());
      user.setIntroduction(introduction);
      getSession().setAttribute(Const.LOGIN_SESSION_KEY, user);
      return new ResponseData(ExceptionMsg.SUCCESS, introduction);
    } catch (Exception e) {
      // TODO: handle exception
      logger.error("updateIntroduction failed, ", e);
      return new ResponseData(ExceptionMsg.FAILED);
    }
  }

  /**
   * 上传头像
   *
   * @param dataUrl
   * @return
   */
  @RequestMapping(value = "/uploadHeadPortrait", method = RequestMethod.POST)
  public ResponseData uploadHeadPortrait(String dataUrl) {
    logger.info("执行 上传头像 开始");
    try {
      String filePath = staticUrl + fileProfilepicturesUrl;
      String fileName = UUID.randomUUID().toString() + ".png";
      String savePath = fileProfilepicturesUrl + fileName;
      String image = dataUrl;
      String header = "data:image";
      String[] imageArr = image.split(",");
      if (imageArr[0].contains(header)) {
        image = imageArr[1];
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decodedBytes = decoder.decode(image);
        FileUtil.uploadFile(decodedBytes, filePath, fileName);
        User user = getUser();
        userRepository.setProfilePicture(savePath, user.getId());
        user.setProfilePicture(savePath);
        getSession().setAttribute(Const.LOGIN_SESSION_KEY, user);
      }
      logger.info("头像地址：" + savePath);
      logger.info("执行 上传头像 结束");
      return new ResponseData(ExceptionMsg.SUCCESS, savePath);
    } catch (Exception e) {
      logger.error("upload head portrait failed, ", e);
      return new ResponseData(ExceptionMsg.FAILED);
    }
  }

  /**
   * 修改昵称
   *
   * @param userName
   * @return
   */
  @RequestMapping(value = "/updateUserName", method = RequestMethod.POST)
  @LoggerManage(description = "修改昵称")
  public ResponseData updateUserName(String userName) {
    try {
      User loginUser = getUser();
      if (userName.equals(loginUser.getUserName())) {
        return new ResponseData(ExceptionMsg.UserNameSame);
      }
      User user = userRepository.findByUserName(userName);
      if (null != user && user.getUserName().equals(userName)) {
        return new ResponseData(ExceptionMsg.UserNameUsed);
      }

      if (userName.length() > 12) {
        return new ResponseData(ExceptionMsg.UserNameLengthLimit);
      }
      userRepository.setUserName(userName, loginUser.getEmail());
      loginUser.setUserName(userName);
      getSession().setAttribute(Const.LOGIN_SESSION_KEY, loginUser);
      return new ResponseData(ExceptionMsg.SUCCESS, userName);
    } catch (Exception e) {
      logger.error("update userName failed", e);
      return new ResponseData(ExceptionMsg.FAILED);
    }
  }

  /**
   * 修改密码
   *
   * @param oldPassword
   * @param newPassword
   * @return
   */
  @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
  @LoggerManage(description = "修改密码")
  public Response updatePassword(String oldPassword, String newPassword) {
    try {
      User user = getUser();
      String newPwd = getPwd(newPassword);
      String currentPwd = getUser().getPassWord();
      if (currentPwd.equals(getPwd(oldPassword))) {
        userRepository.setNewPassword(newPwd, user.getEmail());
        user.setPassWord(newPwd);
        getSession().setAttribute(Const.LOGIN_SESSION_KEY, user);
        return result();
      } else {
        return result(ExceptionMsg.PassWordError);
      }
    } catch (Exception e) {
      logger.error("update password failed", e);
      return result(ExceptionMsg.FAILED);
    }
  }
}
