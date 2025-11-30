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
public class AdoptionApplicationNotification implements INotificationStrategy<AdoptionApplication> {

    private final MailServiceImpl mailService;
    private final ITemplateService templateService;

    @Override
    public NotificationType getType() {
        return NotificationType.ADOPTION_APPLICATION_REGISTERED;
    }

    @Override
    public void sendNotification(AdoptionApplication data) {

        Person adopter = data.getPerson();
        Animal animal = data.getAnimal();

        String adopterName = adopter.getFirstNames();
        String adopterEmail = adopter.getEmail();
        String dogName = animal.getName();
        String applicationId = data.getId().toString();
        String createdAt = data.getCreatedAt().toString();
        String status = data.getStatus().toString();

        String contentHtml = """
            <p>Hola %s,</p>
            <p>Tu solicitud de adopción ha sido registrada exitosamente.</p>
            <p><b>Detalles de tu aplicación:</b></p>
            <ul>
                <li><b>ID de la aplicación:</b> %s</li>
                <li><b>Nombre de la mascota:</b> %s</li>
                <li><b>Fecha de registro:</b> %s</li>
                <li><b>Estado inicial:</b> %s</li>
            </ul>
            <p>Nos pondremos en contacto contigo para continuar con el proceso.</p>
        """.formatted(
                adopterName,
                applicationId,
                dogName,
                createdAt,
                status
        );

        Map<String, Object> vars = new HashMap<>();
        vars.put("title", "Solicitud de Adopción Registrada");
        vars.put("content", contentHtml);
        vars.put("year", LocalDate.now().getYear());

        String html = templateService.renderTemplate("base-template.html", vars);

        mailService.sendEmail(
                adopterEmail,
                "Confirmación de solicitud de adopción – " + dogName,
                html
        );
    }
}
