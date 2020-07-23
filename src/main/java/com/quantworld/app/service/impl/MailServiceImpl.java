package com.quantworld.app.service.impl;

import com.quantworld.app.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.UnsupportedEncodingException;

@Service("mailService")
public class MailServiceImpl implements MailService {
  private Logger logger = LoggerFactory.getLogger(this.getClass());

  //默认编码
  public static final String DEFAULT_ENCODING = "UTF-8";

  //本身邮件的发送者，来自邮件配置
  @Value("${spring.mail.username}")
  private String userName;
  @Value("${spring.mail.nickname}")
  private String nickname;

  //模板引擎解析对象，用于解析模板
  @Autowired
  private TemplateEngine templateEngine;

  @Autowired(required = false)
  private JavaMailSender mailSender;

  @Override
  public void sendSimpleTextMailActual(String subject, String content, String[] toWho, String[] ccPeoples, String[] bccPeoples, String[] attachments) {
    //检验参数：邮件主题、收件人、邮件内容必须不为空才能够保证基本的逻辑执行
    if (subject == null || toWho == null || toWho.length == 0 || content == null) {
      logger.error("邮件-> {} 无法继续执行，因为缺少基本的参数：邮件主题、收件人、邮件内容", subject);
      throw new RuntimeException("模板邮件无法继续发送，因为缺少必要的参数！");
    }
    logger.info("开始发送简单文本邮件：主题->{}，收件人->{}，抄送人->{}，密送人->{}，附件->{}", subject, toWho, ccPeoples, bccPeoples, attachments);

    //附件处理，需要处理附件时，需要使用二进制信息，使用 MimeMessage 类来进行处理
    if (attachments != null && attachments.length > 0) {
      try {
        //附件处理需要进行二进制传输
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, DEFAULT_ENCODING);
        //设置邮件的基本信息：这些函数都会在后面列出来
        boolean continueProcess = handleBasicInfo(helper, subject, content, toWho, ccPeoples, bccPeoples, false);
        //如果处理基本信息出现错误
        if (!continueProcess) {
          logger.error("邮件基本信息出错: 主题->{}", subject);
          return;
        }
        //处理附件
        handleAttachment(helper, subject, attachments);
        //发送该邮件
        mailSender.send(mimeMessage);
        logger.info("发送邮件成功: 主题->{}", subject);
      } catch (MessagingException e) {
        logger.error("发送邮件失败: 主题->{}", subject);

      }
    } else {
      //创建一个简单邮件信息对象
      SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
      //设置邮件的基本信息
      handleBasicInfo(simpleMailMessage, subject, content, toWho, ccPeoples, bccPeoples);
      //发送邮件
      mailSender.send(simpleMailMessage);
      logger.info("发送邮件成功: 主题->{}", subject, toWho, ccPeoples, bccPeoples, attachments);
    }
  }

  @Override
  public void sendHtmlMail(String subject, String content, String[] toWho) {

    //检验参数：邮件主题、收件人、邮件内容必须不为空才能够保证基本的逻辑执行
    if (subject == null || toWho == null || toWho.length == 0 || content == null) {
      logger.error("邮件-> {} 无法继续执行，因为缺少基本的参数：邮件主题、收件人、邮件内容", subject);
      throw new RuntimeException("模板邮件无法继续发送，因为缺少必要的参数！");
    }
    logger.info("开始发送Html邮件：主题->{}，收件人->{}", subject, toWho);
    //html
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    try {
      MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, DEFAULT_ENCODING);
      //设置邮件的基本信息
      handleBasicInfo(helper, subject, content, toWho);
      //发送邮件
      mailSender.send(mimeMessage);
      logger.info("html邮件发送成功");
    } catch (MessagingException e) {
      logger.error("发送邮件出错->{}", subject);
    }
    logger.info("发送邮件成功: 主题->{}", subject, toWho);
  }

  @Override
  public boolean handleBasicInfo(MimeMessageHelper mimeMessageHelper, String subject, String content, String[] toWho, String[] ccPeoples, String[] bccPeoples, boolean isHtml) {
    try {
      //设置必要的邮件元素
      //设置发件人
      String fromName = new String((nickname + '<' + userName + '>').getBytes("UTF-8"));
      mimeMessageHelper.setFrom(fromName);
      //设置邮件的主题
      mimeMessageHelper.setSubject(subject);
      //设置邮件的内容，区别是否是HTML邮件
      mimeMessageHelper.setText(content, isHtml);
      //设置邮件的收件人
      mimeMessageHelper.setTo(toWho);
      //设置非必要的邮件元素，在使用helper进行封装时，这些数据都不能够为空
      if (ccPeoples != null)
        //设置邮件的抄送人：MimeMessageHelper # Assert.notNull(cc, "Cc address array must not be null");
        mimeMessageHelper.setCc(ccPeoples);

      if (bccPeoples != null)
        //设置邮件的密送人：MimeMessageHelper # Assert.notNull(bcc, "Bcc address array must not be null");
        mimeMessageHelper.setBcc(bccPeoples);
      return true;
    } catch (MessagingException | UnsupportedEncodingException e) {
      logger.error("邮件基本信息出错->{}", subject);
    }
    return false;
  }

  @Override
  public void handleBasicInfo(SimpleMailMessage simpleMailMessage, String subject, String content, String[] toWho, String[] ccPeoples, String[] bccPeoples) {
    String fromName = null;
    try {
      fromName = new String((nickname + '<' + userName + '>').getBytes("UTF-8"));
    } catch (UnsupportedEncodingException e) {
      logger.error("html邮件基本信息出错->{}", subject);
    }
    //设置发件人
    assert fromName != null;
    simpleMailMessage.setFrom(fromName);
    //设置邮件的主题
    simpleMailMessage.setSubject(subject);
    //设置邮件的内容
    simpleMailMessage.setText(content);
    //设置邮件的收件人
    simpleMailMessage.setTo(toWho);
    //设置邮件的抄送人
    simpleMailMessage.setCc(ccPeoples);
    //设置邮件的密送人
    simpleMailMessage.setBcc(bccPeoples);
  }

  @Override
  public void handleBasicInfo(MimeMessageHelper mimeMessageHelper, String subject, String content, String[] toWho) {
    try {
      //设置发件人
      String fromName = new String((nickname + '<' + userName + '>').getBytes("UTF-8"));
      mimeMessageHelper.setFrom(fromName);
      //设置邮件的主题
      mimeMessageHelper.setSubject(subject);
      //设置邮件的内容
      mimeMessageHelper.setText(content, true);
      //设置邮件的收件人
      mimeMessageHelper.setTo(toWho);
    } catch (MessagingException | UnsupportedEncodingException e) {
      logger.error("html邮件基本信息出错->{}", subject);
    }
  }

  @Override
  public void handleAttachment(MimeMessageHelper mimeMessageHelper, String subject, String[] attachmentFilePaths) {
    //判断是否需要处理邮件的附件
    if (attachmentFilePaths != null && attachmentFilePaths.length > 0) {
      FileSystemResource resource;
      String fileName;
      //循环处理邮件的附件
      for (String attachmentFilePath : attachmentFilePaths) {
        //获取该路径所对应的文件资源对象
        resource = new FileSystemResource(new File(attachmentFilePath));
        //判断该资源是否存在，当不存在时仅仅会打印一条警告日志，不会中断处理程序。
        // 也就是说在附件出现异常的情况下，邮件是可以正常发送的，所以请确定你发送的邮件附件在本机存在
        if (!resource.exists()) {
          logger.warn("邮件->{} 的附件->{} 不存在！", subject, attachmentFilePath);
          //开启下一个资源的处理
          continue;
        }
        //获取资源的名称
        fileName = resource.getFilename();
        try {
          //添加附件
          mimeMessageHelper.addAttachment(fileName, resource);
        } catch (MessagingException e) {
          logger.error("邮件->{} 添加附件->{} 出现异常->{}", subject, attachmentFilePath, e.getMessage());
        }
      }
    }
  }
}