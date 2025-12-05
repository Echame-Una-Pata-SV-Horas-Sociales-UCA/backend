package com.echameunapata.backend.services.notifications.strategies;

import com.echameunapata.backend.domain.enums.notifications.NotificationType;
import com.echameunapata.backend.domain.models.Report;
import com.echameunapata.backend.services.contract.ITemplateService;
import com.echameunapata.backend.services.impl.MailServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class ReportStatusChangedNotification implements INotificationStrategy<Report> {

    private final MailServiceImpl mailService;
    private final ITemplateService templateService;

    @Override
    public NotificationType getType() {
        return NotificationType.REPORT_STATUS_CHANGED;
    }

    @Override
    public void sendNotification(Report report) {
        if (report.getIsAnonymous() || report.getPerson() == null) {
            return;
        }

        String formattedDate = DateTimeFormatter
                .ofPattern("dd/MM/yyyy HH:mm:ss")
                .withZone(ZoneId.systemDefault())
                .format(report.getReceptionDate());

        String contentHtml = """
        <p>Hola %s,</p>

            <p>El estado de tu reporte ha sido actualizado.</p>

        <p><strong>Detalles del reporte:</strong></p>
        <ul>
            <li><strong>Tipo:</strong> %s</li>
            <li><strong>Descripción:</strong> %s</li>
            <li><strong>Estado:</strong> %s</li>
            <li><strong>Fecha de recepción:</strong> %s</li>
        </ul>

        <p>Gracias por usar nuestro sistema.</p>
    """.formatted(
                report.getPerson().getFirstNames(),
                report.getType(),
                report.getDescription(),
                report.getStatus(),
                formattedDate
        );

        Map<String, Object> vars = new HashMap<>();
        vars.put("title", "Tu reporte ha sido creado");
        vars.put("content", contentHtml);
        vars.put("year", LocalDate.now().getYear());

        String html = templateService.renderTemplate("base-template.html", vars);

        mailService.sendEmail(
                report.getPerson().getEmail(),
                "Tu reporte ha sido creado",
                html
        );
    }

}
