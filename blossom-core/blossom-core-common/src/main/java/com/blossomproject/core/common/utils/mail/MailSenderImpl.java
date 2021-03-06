package com.blossomproject.core.common.utils.mail;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import freemarker.template.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.CollectionUtils;

import javax.mail.Message;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@Async
public class MailSenderImpl implements MailSender {

    private final static Logger LOGGER = LoggerFactory.getLogger(MailSenderImpl.class);

    private final JavaMailSender javaMailSender;
    private final Configuration freemarkerConfiguration;
    private final MessageSource messageSource;
    private final String basePath;
    private final Locale defaultLocale;
    private final MailFilter filter;

    public MailSenderImpl(JavaMailSender javaMailSender, Configuration freemarkerConfiguration,
                          MessageSource messageSource, String basePath, Locale defaultLocale,
                          MailFilter filter) {
        Preconditions.checkNotNull(javaMailSender);
        Preconditions.checkNotNull(freemarkerConfiguration);
        Preconditions.checkNotNull(messageSource);
        Preconditions.checkNotNull(basePath);
        Preconditions.checkNotNull(defaultLocale);
        Preconditions.checkNotNull(filter);
        this.javaMailSender = javaMailSender;
        this.freemarkerConfiguration = freemarkerConfiguration;
        this.messageSource = messageSource;
        this.basePath = basePath;
        this.defaultLocale = defaultLocale;
        this.filter = filter;
    }


    @Override
    public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject,
      String... mailTo)
      throws Exception {
      this.sendMail(htmlTemplate, ctx, mailSubject, this.defaultLocale, mailTo);
    }

    @Override
    public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale, String... mailTo) throws Exception {
      this.sendMail(htmlTemplate, ctx, mailSubject, locale, Lists.newArrayList(), mailTo);
    }

    @Override
    public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale, List<File> attachedFiles, String... mailTo) throws Exception {
      this.sendMail(htmlTemplate, ctx, mailSubject, locale, attachedFiles, mailTo, null, null);
    }

    @Override
    public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, String[] mailTo, String[] mailCc, String[] mailBcc) throws Exception {
      this.sendMail(htmlTemplate, ctx, mailSubject, this.defaultLocale, mailTo, mailCc, mailBcc);
    }

    @Override
    public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale, String[] mailTo, String[] mailCc, String[] mailBcc) throws Exception {
      this.sendMail(htmlTemplate, ctx, mailSubject, locale, Lists.newArrayList(), mailTo, mailCc, mailBcc);
    }

    @Override
    public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale, List<File> attachedFiles, String[] mailTo, String[] mailCc, String[] mailBcc) throws Exception {
      this.sendMail(htmlTemplate, ctx, mailSubject, locale, attachedFiles, mailTo, mailCc, mailBcc, false);
    }

    @Override
    public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale, List<File> attachedFiles, String[] mailTo, String[] mailCc, String[] mailBcc, boolean highPriority) throws Exception {
      this.sendMail(locale,ctx, convertToInternetAddress(mailTo),mailSubject,htmlTemplate,convertToInternetAddress(mailCc),convertToInternetAddress(mailBcc),highPriority,null,null,null, attachedFiles);
    }

    @Override
    public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale, String attachmentName, InputStreamSource attachmentInputStreamSource, String attachmentContentType, String[] mailTo, String[] mailCc, String[] mailBcc, boolean highPriority) throws Exception {
      this.sendMail(locale,ctx,convertToInternetAddress(mailTo),mailSubject,htmlTemplate,convertToInternetAddress(mailCc),convertToInternetAddress(mailBcc),highPriority,attachmentName,attachmentInputStreamSource,attachmentContentType,null);
    }

    @Override
    public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, InternetAddress... mailTo)
            throws Exception {
        this.sendMail(htmlTemplate, ctx, mailSubject, this.defaultLocale, mailTo);
    }

    @Override
    public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale,InternetAddress... mailTo) throws Exception {
        this.sendMail(htmlTemplate, ctx, mailSubject, locale, Lists.newArrayList(), mailTo);
    }

    @Override
    public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale, List<File> attachedFiles, InternetAddress... mailTo) throws Exception {
        this.sendMail(htmlTemplate, ctx, mailSubject, locale, attachedFiles, mailTo, null, null);
    }

    @Override
    public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, InternetAddress[] mailTo, InternetAddress[] mailCc, InternetAddress[] mailBcc) throws Exception {
        this.sendMail(htmlTemplate, ctx, mailSubject, this.defaultLocale, mailTo, mailCc, mailBcc);
    }

    @Override
    public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale, InternetAddress[] mailTo, InternetAddress[] mailCc, InternetAddress[] mailBcc) throws Exception {
        this.sendMail(htmlTemplate, ctx, mailSubject, locale, Lists.newArrayList(), mailTo, mailCc, mailBcc);
    }

    @Override
    public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale, List<File> attachedFiles, InternetAddress[] mailTo, InternetAddress[] mailCc, InternetAddress[] mailBcc) throws Exception {
        this.sendMail(htmlTemplate, ctx, mailSubject, locale, attachedFiles, mailTo, mailCc, mailBcc, false);
    }

    @Override
    public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale, List<File> attachedFiles, InternetAddress[] mailTo, InternetAddress[] mailCc, InternetAddress[] mailBcc, boolean highPriority) throws Exception {
        this.sendMail(locale,ctx,mailTo,mailSubject,htmlTemplate,mailCc,mailBcc,highPriority,null,null,null,attachedFiles);
    }

    @Override
    public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale, String attachmentName, InputStreamSource attachmentInputStreamSource, String attachmentContentType, InternetAddress[] mailTo, InternetAddress[] mailCc, InternetAddress[] mailBcc, boolean highPriority) throws Exception {
        this.sendMail(locale,ctx,mailTo,mailSubject,htmlTemplate,mailCc,mailBcc,highPriority,attachmentName,attachmentInputStreamSource,attachmentContentType,null);
    }

    private void sendMail(Locale locale, Map<String, Object> ctx, InternetAddress[] mailTo, String mailSubject, String htmlTemplate, InternetAddress[] mailCc, InternetAddress[] mailBcc, boolean highPriority, String attachmentName, InputStreamSource attachmentInputStreamSource, String attachmentContentType, List<File> attachedFiles) throws Exception {
      Preconditions.checkArgument(locale != null);
      Preconditions.checkArgument(ctx != null);
      Preconditions.checkArgument(mailTo != null && mailTo.length > 0 || mailBcc != null && mailBcc.length > 0);
      Preconditions.checkArgument(mailSubject != null);

      this.enrichContext(ctx, locale);

      final Template template = this.freemarkerConfiguration
        .getTemplate("mail/" + htmlTemplate + ".ftl");
      final String htmlContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, ctx);
      final String subject = this.messageSource
        .getMessage(mailSubject, new Object[]{}, mailSubject, locale);

      final MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
      final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
      message.setSubject(subject);
      message.setText(htmlContent, true);

      if (mailTo != null && mailTo.length > 0) {
        message.setTo(mailTo);
      }

      if (mailCc != null && mailCc.length > 0) {
        message.setCc(mailCc);
      }

      if (mailBcc != null && mailBcc.length > 0) {
        message.setBcc(mailBcc);
      }

      if (highPriority) {
        //https://www.chilkatsoft.com/p/p_471.asp
        mimeMessage.addHeader("X-Priority", "1");
        mimeMessage.addHeader("X-MSMail-Priority", "High");
        mimeMessage.addHeader("Importance", "High");
      }

      if (attachmentName != null) {
        message.addAttachment(attachmentName, attachmentInputStreamSource, attachmentContentType);
      } else {
        if (attachedFiles != null && !CollectionUtils.isEmpty(attachedFiles)) {
          for (File file : attachedFiles) {
            message.addAttachment(file.getName(), file);
          }
        }
      }

      try {
        this.javaMailSender.send(this.filter.filter(message));
      } catch (Exception e) {
        LOGGER.error("Error when sending", e);
      }

      LOGGER.info("Mail with recipient(s) {To: '{}', CC: '{}', BCC: '{}'} sent.",
        Arrays.toString(message.getMimeMessage().getRecipients(Message.RecipientType.TO)),
        Arrays.toString(message.getMimeMessage().getRecipients(Message.RecipientType.CC)),
        Arrays.toString(message.getMimeMessage().getRecipients(Message.RecipientType.BCC)));
    }

    private void enrichContext(Map<String, Object> ctx, Locale locale) {
        ctx.put("basePath", this.basePath);
        ctx.put("message", new MessageResolverMethod(this.messageSource, locale));
        ctx.put("lang", locale);
    }

    private class MessageResolverMethod implements TemplateMethodModelEx {

        private MessageSource messageSource;
        private Locale locale;

        public MessageResolverMethod(MessageSource messageSource, Locale locale) {
            this.messageSource = messageSource;
            this.locale = locale;
        }

        @Override
        public Object exec(List arguments) throws TemplateModelException {
            if (arguments.size() != 1) {
                throw new TemplateModelException("Wrong number of arguments");
            }
            String code = ((SimpleScalar) arguments.get(0)).getAsString();
            if (code == null || code.isEmpty()) {
                throw new TemplateModelException("Invalid code value '" + code + "'");
            }
            return messageSource.getMessage(code, null, locale);
        }
    }

  private InternetAddress[] convertToInternetAddress(String[] mails) throws AddressException {
    if(mails == null){
      return null;
    }

    int i=0;
    InternetAddress[] addresses = new InternetAddress[mails.length];
    for(String mail : mails){
      addresses[i++] = new InternetAddress(mail);
    }

    return addresses;
  }
}
