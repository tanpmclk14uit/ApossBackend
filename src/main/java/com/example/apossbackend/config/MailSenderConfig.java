package com.example.apossbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailSenderConfig {
    @Bean
    public JavaMailSender javaMailSender(){
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("localhost");
        javaMailSender.setPort(1025);

        javaMailSender.setUsername("aposs@gmail.com");
        javaMailSender.setPassword("password");

        Properties properties= javaMailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.trust", "*");
        properties.put("mail.smtp.ssl.connectiontimeout", "5000");
        properties.put("mail.smtp.ssl.timeout", "3000");
        properties.put("mail.smtp.ssl.writetimeout", "5000");
        properties.put("mail.debug", "true");

        return javaMailSender;
    }
}
