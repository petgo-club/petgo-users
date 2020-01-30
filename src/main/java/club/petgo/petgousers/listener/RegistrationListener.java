package club.petgo.petgousers.listener;

import club.petgo.petgousers.domain.User;
import club.petgo.petgousers.event.OnRegistrationCompleteEvent;
import club.petgo.petgousers.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    @Autowired
    private IUserService service;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.url}")
    private String appUrl;

    @Value("${app.version}")
    private String appVersion;

    @Value("${email.confirmation}")
    private String confirmation;

    private static Logger LOGGER = LoggerFactory.getLogger(RegistrationListener.class);

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        service.createVerificationToken(user, token);

        String recipientAddress = user.getEmail();
        String subject = messageSource.getMessage("registration.subject", null, Locale.ENGLISH);
        String message = messageSource.getMessage("registration.success", null, Locale.ENGLISH);
        String confirmationUrl = appUrl + appVersion +confirmation + token;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "\n" + confirmationUrl);
        mailSender.send(email);
        LOGGER.info("Registration email sent to [{}]", recipientAddress);
    }
}
