package com.echameunapata.backend.services.notifications;

import com.echameunapata.backend.domain.models.Report;
import com.echameunapata.backend.services.impl.MailServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
@AllArgsConstructor
public class ReportStatusChangedNotification implements IReportNotificationStrategy {

    private final MailServiceImpl mailService;

    @Override
    public void sendNotification(Report report) {
        if (report.getIsAnonymous() || report.getPerson() == null) {
            return;
        }

        String subject = "Tu reporte ha sido creado";

        // Convertimos la fecha en un formato legible
        String formattedDate = DateTimeFormatter
                .ofPattern("dd/MM/yyyy HH:mm:ss")
                .withZone(ZoneId.systemDefault())
                .format(report.getReceptionDate());

        String body = String.format(
                "Hola %s,\n\n" +
                        "Tu reporte ha sido registrado exitosamente.\n\n" +
                        "Detalles del reporte:\n" +
                        "• Tipo: %s\n" +
                        "• Descripción: %s\n" +
                        "• Estado: %s\n" +
                        "• Fecha de recepción: %s\n\n" +
                        "Gracias por usar nuestro sistema.",
                report.getPerson().getFirstNames(),
                report.getType(),
                report.getDescription(),
                report.getStatus(),
                formattedDate
        );

        mailService.sendEmail(report.getPerson().getEmail(), subject, body);
    }

}
