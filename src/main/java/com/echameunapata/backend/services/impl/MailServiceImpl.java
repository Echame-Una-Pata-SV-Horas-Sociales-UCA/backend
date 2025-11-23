package com.echameunapata.backend.services.impl;

import com.echameunapata.backend.services.contract.IMailService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements IMailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.sender}")
    private String fromEmail;

    public MailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(String userEmail, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");


            helper.setFrom(fromEmail);
            helper.setTo(userEmail);
            helper.setSubject(subject);

            helper.setText(htmlBody, true);

            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Error enviando email", e);
        }

    }
}
