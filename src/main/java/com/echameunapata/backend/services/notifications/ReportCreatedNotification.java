package com.echameunapata.backend.services.notifications;

import com.echameunapata.backend.domain.models.Report;
import com.echameunapata.backend.services.impl.MailServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ReportCreatedNotification implements  IReportNotificationStrategy {

    private final MailServiceImpl mailService;

    @Override
    public void sendNotification(Report report) {
        if (report.getIsAnonymous() || report.getPerson() == null) {
            return;
        }

        String subject = "Tu reporte ha sido creado";
        String body =
                "Hola " + report.getPerson().getFirstNames() + ",\n\n" +
                        "Tu reporte ha sido registrado exitosamente.\n\n" +
                        "Detalles:\n" +
                        "• Tipo: " + report.getType() + "\n" +
                        "• Descripción: " + report.getDescription() + "\n" +
                        "• Estado: " + report.getStatus() + "\n\n" +
                        "Gracias por usar nuestro sistema.";

        mailService.sendEmail(report.getPerson().getEmail(), subject, body);
    }
}
