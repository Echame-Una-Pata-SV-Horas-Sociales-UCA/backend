package com.echameunapata.backend.services.notifications.strategies;

import com.echameunapata.backend.domain.enums.notifications.NotificationType;
import com.echameunapata.backend.domain.models.AdoptionApplication;
import com.echameunapata.backend.domain.models.Animal;
import com.echameunapata.backend.domain.models.Person;
import com.echameunapata.backend.services.contract.ITemplateService;
import com.echameunapata.backend.services.impl.MailServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class ApplicationInReviewNotification implements INotificationStrategy<AdoptionApplication> {

    private final MailServiceImpl mailService;
    private final ITemplateService templateService;

    @Override
    public NotificationType getType() {
        return NotificationType.ADOPTION_APPLICATION_IN_REVIEW;
    }

    @Override
    public void sendNotification(AdoptionApplication data) {

        Person adopter = data.getPerson();
        Animal animal = data.getAnimal();

        String adopterName = adopter.getFirstNames();
        String adopterEmail = adopter.getEmail();
        String dogName = animal.getName();
        String applicationId = data.getId().toString();

        // Contenido del correo
        String contentHtml = """
                <p>Hola %s,</p>

                <p>Queremos informarte que tu solicitud de adopción para <b>%s</b> ha pasado al estado <b>En Revisión</b>.</p>

                <p>Nuestro equipo ya está evaluando tu aplicación y pronto recibirás más información sobre los siguientes pasos del proceso.</p>

                <p>Gracias por tu interés y por brindarle una oportunidad a %s.</p>
            """.formatted(
                adopterName,
                dogName,
                dogName
        );

        Map<String, Object> vars = new HashMap<>();
        vars.put("title", "Solicitud en Revisión – Adopción");
        vars.put("content", contentHtml);
        vars.put("year", LocalDate.now().getYear());

        String html = templateService.renderTemplate("base-template.html", vars);

        mailService.sendEmail(
                adopterEmail,
                "Tu solicitud de adopción está en revisión – " + dogName,
                html
        );
    }
}

