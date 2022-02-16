package com.group.contestback.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.Properties;
@Slf4j

@Component
public class EmailServiceCS{

    @Value("${spring.mail.password}")
    private String userPassword;

    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.mail.ru");
        mailSender.setPort(587);

        mailSender.setUsername("contest-server@mail.ru");
        mailSender.setPassword(userPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
        public void sendSimpleMessage(
                String to, String subject, String text) {

            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom("contest-server@mail.ru");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            log.info("before email sent");

            getJavaMailSender().send(message);
        }
}
