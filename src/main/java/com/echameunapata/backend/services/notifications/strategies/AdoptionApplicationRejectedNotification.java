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
public class AdoptionApplicationRejectedNotification implements INotificationStrategy<AdoptionApplication> {

    private final MailServiceImpl mailService;
    private final ITemplateService templateService;

    @Override
    public NotificationType getType() {
        return NotificationType.ADOPTION_APPLICATION_REJECTED;
    }

    @Override
    public void sendNotification(AdoptionApplication data) {

        Person adopter = data.getPerson();
        Animal animal = data.getAnimal();

        String adopterName = adopter.getFirstNames();
        String adopterEmail = adopter.getEmail();
        String dogName = animal.getName();
        String applicationId = data.getId().toString();
        String rejectedAt = LocalDate.now().toString();

        String rejectionReason = data.getObservations() != null
                ? data.getObservations()
                : "No se especificó un motivo.";

        String contentHtml = """
            <p>Hola %s,</p>
            <p>Lamentamos informarte que tu solicitud de adopción para <b>%s</b> ha sido <b style="color:red;">rechazada</b>.</p>
            
            <p><b>Detalles de tu solicitud:</b></p>
            <ul>
                <li><b>ID de la aplicación:</b> %s</li>
                <li><b>Mascota:</b> %s</li>
                <li><b>Fecha de revisión:</b> %s</li>
                <li><b>Motivo del rechazo:</b> %s</li>
            </ul>

            <p>Agradecemos tu interés en el bienestar animal y te animamos a explorar otras opciones de adopción dentro de nuestra fundación.</p>
        """.formatted(
                adopterName,
                dogName,
                applicationId,
                dogName,
                rejectedAt,
                rejectionReason
        );

        Map<String, Object> vars = new HashMap<>();
        vars.put("title", "Solicitud de Adopción Rechazada");
        vars.put("content", contentHtml);
        vars.put("year", LocalDate.now().getYear());

        String html = templateService.renderTemplate("base-template.html", vars);

        mailService.sendEmail(
                adopterEmail,
                "Actualización sobre tu solicitud de adopción – " + dogName,
                html
        );
    }
}
