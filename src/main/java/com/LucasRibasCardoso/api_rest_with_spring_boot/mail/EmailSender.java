package com.LucasRibasCardoso.api_rest_with_spring_boot.mail;

import com.LucasRibasCardoso.api_rest_with_spring_boot.config.EmailConfig;
import com.LucasRibasCardoso.api_rest_with_spring_boot.exception.mail.InvalidEmailException;
import com.LucasRibasCardoso.api_rest_with_spring_boot.exception.mail.SendFailedEmailException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.File;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.StringTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailSender implements Serializable {

  @Serial private static final long serialVersionUID = 1L;
  private static final Logger logger = LoggerFactory.getLogger(EmailSender.class);

  private final JavaMailSender mailSender;

  private String to;
  private String subject;
  private String body;
  private ArrayList<InternetAddress> recipients = new ArrayList<>();
  private File attachment;

  public EmailSender(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  public EmailSender to(String to) {
    this.to = to;
    this.recipients = getRecipients(to);
    return this;
  }

  public EmailSender withSubject(String subject) {
    this.subject = subject;
    return this;
  }

  public EmailSender withMessage(String body) {
    this.body = body;
    return this;
  }

  public EmailSender recipients(ArrayList<InternetAddress> recipients) {
    this.recipients = recipients;
    return this;
  }

  public EmailSender attachment(String fileDir) {
    this.attachment = new File(fileDir);
    return this;
  }

  public void send(EmailConfig config) {
    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true);
      helper.setFrom(config.getUsername());
      helper.setTo(recipients.toArray(new InternetAddress[0]));
      helper.setSubject(subject);
      helper.setText(body, true);

      if (attachment != null) {
        helper.addAttachment(attachment.getName(), attachment);
      }

      mailSender.send(message);
      logger.info("Email sent to {} with the subject: {}", to, subject);

      reset();
    } catch (MessagingException e) {
      logger.error("Failed to send email: {}", e.getMessage());
      throw new SendFailedEmailException("Failed to send email. Try again later.");
    }
  }

  private void reset() {
    this.to = null;
    this.subject = null;
    this.body = null;
    this.recipients = null;
    this.attachment = null;
  }

  private ArrayList<InternetAddress> getRecipients(String to) {
    String toWithoutSpaces = to.replaceAll("\\s+", "");
    StringTokenizer tokenizer = new StringTokenizer(toWithoutSpaces, ";");
    ArrayList<InternetAddress> recipientsList = new ArrayList<>();

    while (tokenizer.hasMoreTokens()) {
      try {
        recipientsList.add(new InternetAddress(tokenizer.nextToken()));
      } catch (AddressException e) {
        throw new InvalidEmailException("Invalid email address: " + e.getMessage());
      }
    }
    return recipientsList;
  }
}
