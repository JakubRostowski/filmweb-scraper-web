package pl.jrostowski.filmwebscraper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String sender;

    public void sendWelcomeEmail(String recipientEmail, String recipientName) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, MULTIPART_MODE_MIXED_RELATED, UTF_8.name());

            Context context = new Context();
            context.setVariable("recipientName", recipientName);

            String content = templateEngine.process("email-welcome", context);

            helper.setTo(recipientEmail);
            helper.setReplyTo(sender);
            helper.setFrom(sender);
            helper.setSubject("Witamy w FilmwebScraper!");
            helper.setText(content, true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }


        javaMailSender.send(message);
    }
}
