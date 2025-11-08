package com.echameunapata.backend.services.contract;

public interface IMailService {
    void sendEmail(String to, String subject, String body);
}
