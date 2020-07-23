package com.quantworld.app.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;

public interface MailService {
  String NAME = "mailService";

  void sendSimpleTextMailActual(String subject, String content, String[] toWho, String[] ccPeoples, String[] bccPeoples, String[] attachments);

  void sendHtmlMail(String subject, String content, String[] toWho);

  boolean handleBasicInfo(MimeMessageHelper mimeMessageHelper, String subject, String content, String[] toWho, String[] ccPeoples, String[] bccPeoples, boolean isHtml);

  void handleBasicInfo(SimpleMailMessage simpleMailMessage, String subject, String content, String[] toWho, String[] ccPeoples, String[] bccPeoples);

  void handleBasicInfo(MimeMessageHelper mimeMessageHelper, String subject, String content, String[] toWho);

  void handleAttachment(MimeMessageHelper mimeMessageHelper, String subject, String[] attachmentFilePaths);
}
