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
