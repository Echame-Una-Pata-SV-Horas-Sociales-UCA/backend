package com.echameunapata.backend.services.notifications.strategies;

import com.echameunapata.backend.domain.enums.notifications.NotificationType;
import com.echameunapata.backend.domain.models.Report;
import com.echameunapata.backend.services.contract.ITemplateService;
import com.echameunapata.backend.services.impl.MailServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class ReportCreatedNotification implements INotificationStrategy<Report> {

    private final MailServiceImpl mailService;
    private final ITemplateService templateService;

    @Override
    public NotificationType getType() {
        return NotificationType.REPORT_CREATED;
    }

    @Override
    public void sendNotification(Report report) {
        if (report.getIsAnonymous() || report.getPerson() == null) {
            return;
        }

        String contentHtml = """
        <p>Hola %s,</p>
        <p>Tu reporte ha sido registrado exitosamente.</p>
        <ul>
            <li><strong>Tipo:</strong> %s</li>
            <li><strong>Descripción:</strong> %s</li>
            <li><strong>Estado:</strong> %s</li>
        </ul>
        <p>Gracias por usar nuestro sistema.</p>
    """.formatted(
                report.getPerson().getFirstNames(),
                report.getType(),
                report.getDescription(),
                report.getStatus()
        );

        Map<String, Object> vars = new HashMap<>();
        vars.put("title", "Reporte Creado");
        vars.put("content", contentHtml);
        vars.put("year", LocalDate.now().getYear());

        String html = templateService.renderTemplate("base-template.html", vars);

        mailService.sendEmail(report.getPerson().getEmail(), "Tu reporte ha sido creado", html);
    }
}
