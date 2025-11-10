package com.echameunapata.backend.services.impl;

import com.echameunapata.backend.services.contract.IMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
    public void sendEmail(String userEmail, String subject, String body) {
        // SimpleMailMessage es la clase de Spring para correos de texto plano
        SimpleMailMessage message = new SimpleMailMessage();

        // Configuramos el remitente (leído desde properties)
        message.setFrom(fromEmail);

        // Configuramos el destinatario
        message.setTo(userEmail);

        // Asunto del correo
        message.setSubject(subject);

        // Cuerpo del correo
        message.setText(body);

        // ¡El envío!
        // Esto usa la conexión SMTP a Brevo y envía el correo.
        mailSender.send(message);
    }
}
