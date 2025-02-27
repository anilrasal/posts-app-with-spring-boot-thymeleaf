package org.anildeveloper.SpringBoot01.configure;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class AppConfig {

    @Value("${mail.transport.protocol}")
    private String mail_transport_protocol;

    @Value("${spring.mail.host}")
    private String mail_host;

    @Value("${spring.mail.port}")
    private String mail_port;

    @Value("${spring.mail.username}")
    private String mail_username;

    @Value("${spring.mail.password}")
    private String mail_password;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String mail_smtp_auth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private String mail_smtp_starttls_enable;

    @Value("${spring.mail.smtp.ssl.trust}")
    private String mail_smtp_ssl_trust;

    @Bean
    public JavaMailSender getJavaMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(mail_host);
        mailSender.setPort(Integer.parseInt(mail_port));

        mailSender.setUsername(mail_username);
        mailSender.setPassword(mail_password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", mail_transport_protocol);
        props.put("mail.smtp.auth", mail_smtp_auth);
        props.put("mail.smtp.starttls.enable", mail_smtp_starttls_enable);
        props.put("mail.smtp.ssl.trust", mail_smtp_ssl_trust);
        props.put("mail.debug", "true");
        
        return mailSender;
    }
    
}
