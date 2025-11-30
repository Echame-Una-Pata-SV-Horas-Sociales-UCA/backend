package com.echameunapata.backend.services.notifications.strategies;

import com.echameunapata.backend.domain.enums.notifications.NotificationType;
import com.echameunapata.backend.domain.models.AdoptionApplication;
import com.echameunapata.backend.domain.models.AdoptionVisit;
import com.echameunapata.backend.domain.models.Animal;
import com.echameunapata.backend.domain.models.Person;
import com.echameunapata.backend.services.contract.IAdoptionApplicationService;
import com.echameunapata.backend.services.contract.ITemplateService;
import com.echameunapata.backend.services.impl.MailServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class VisitScheduledNotification implements INotificationStrategy<AdoptionVisit> {

    private final MailServiceImpl mailService;
    private final ITemplateService templateService;

    @Override
    public NotificationType getType() {
        return NotificationType.ADOPTION_VISIT_SCHEDULED;
    }

    @Override
    public void sendNotification(AdoptionVisit data) {


        Person adopter = data.getAdoptionApplication().getPerson();
        Animal animal = data.getAdoptionApplication().getAnimal();

        String adopterName = adopter.getFirstNames();
        String adopterEmail = adopter.getEmail();
        String dogName = animal.getName();
        String applicationId = data.getAdoptionApplication().getId().toString();

        String scheduledDate = data.getScheduledDate();
        String evaluatorName = data.getEvaluatorName() != null ? data.getEvaluatorName() : "No asignado";

        // Contenido HTML
        String contentHtml = """
            <p>Hola %s,</p>
            <p>Tu visita domiciliaria para la adopción de <b>%s</b> ha sido programada con éxito.</p>

            <p><b>Detalles de la visita:</b></p>
            <ul>
                <li><b>ID de la aplicación:</b> %s</li>
                <li><b>Mascota:</b> %s</li>
                <li><b>Fecha programada:</b> %s</li>
                <li><b>Evaluador:</b> %s</li>
            </ul>

            <p>Por favor asegúrate de estar disponible en la fecha indicada. Un miembro de nuestro equipo te visitará para evaluar el entorno donde vivirá la mascota.</p>

            <p>Gracias por tu dedicación y por querer brindarle un hogar a %s.</p>
        """.formatted(
                adopterName,
                dogName,
                applicationId,
                dogName,
                scheduledDate,
                evaluatorName,
                dogName
        );

        Map<String, Object> vars = new HashMap<>();
        vars.put("title", "Visita de Adopción Programada");
        vars.put("content", contentHtml);
        vars.put("year", LocalDate.now().getYear());

        String html = templateService.renderTemplate("base-template.html", vars);

        mailService.sendEmail(
                adopterEmail,
                "Visita Programada – Adopción de " + dogName,
                html
        );
    }
}
