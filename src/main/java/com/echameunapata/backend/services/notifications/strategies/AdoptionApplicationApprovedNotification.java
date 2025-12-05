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
public class AdoptionApplicationApprovedNotification implements INotificationStrategy<AdoptionApplication> {

    private final MailServiceImpl mailService;
    private final ITemplateService templateService;

    @Override
    public NotificationType getType() {
        return NotificationType.ADOPTION_APPLICATION_APPROVED;
    }

    @Override
    public void sendNotification(AdoptionApplication data) {

        Person adopter = data.getPerson();
        Animal animal = data.getAnimal();

        String adopterName = adopter.getFirstNames();
        String adopterEmail = adopter.getEmail();
        String dogName = animal.getName();
        String applicationId = data.getId().toString();
        String approvedAt = LocalDate.now().toString();

        String contentHtml = """
            <p>Hola %s,</p>
            <p>¡Buenas noticias! Tu solicitud de adopción ha sido <b style="color:green;">aprobada</b>.</p>
            <p><b>Detalles de tu aplicación:</b></p>
            <ul>
                <li><b>ID de la aplicación:</b> %s</li>
                <li><b>Mascota aprobada:</b> %s</li>
                <li><b>Fecha de aprobación:</b> %s</li>
            </ul>
            <p>Nos estaremos comunicando contigo para coordinar los siguientes pasos del proceso de adopción.</p>
            <p>¡Gracias por brindarle una nueva oportunidad a %s!</p>
        """.formatted(
                adopterName,
                applicationId,
                dogName,
                approvedAt,
                dogName
        );

        Map<String, Object> vars = new HashMap<>();
        vars.put("title", "Solicitud de Adopción Aprobada");
        vars.put("content", contentHtml);
        vars.put("year", LocalDate.now().getYear());

        String html = templateService.renderTemplate("base-template.html", vars);

        mailService.sendEmail(
                adopterEmail,
                "Tu solicitud de adopción ha sido Aprobada – " + dogName,
                html
        );
    }
}
