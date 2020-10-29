package com.vivek.discussion.service;

import com.vivek.discussion.exceptions.SpringDiscussionException;
import com.vivek.discussion.model.NotificationEmail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {
    private final JavaMailSender javaMailSender;
    private final MailContextBuilder mailContextBuilder;


    @Async
    public void sendMail(NotificationEmail notificationEmail){
        MimeMessagePreparator mimeMessagePreparator=mimeMessage -> {
            MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setFrom("poolcoolsemwal@Gmail.com");
            mimeMessageHelper.setTo(notificationEmail.getRecipient());
            mimeMessageHelper.setSubject(notificationEmail.getSubject());
            mimeMessageHelper.setText(mailContextBuilder.build(notificationEmail.getBody()));
        };
        
        try{
            javaMailSender.send(mimeMessagePreparator);
            log.info("Activation link is sent");
        }catch (MailException e){
            throw new SpringDiscussionException("exception occured when sneding mail to"+notificationEmail.getRecipient());

        }

    }
}
