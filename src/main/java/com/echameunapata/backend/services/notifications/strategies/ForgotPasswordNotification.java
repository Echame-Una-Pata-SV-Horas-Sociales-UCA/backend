package com.echameunapata.backend.services.notifications.strategies;

import com.echameunapata.backend.domain.enums.notifications.NotificationType;
import com.echameunapata.backend.domain.models.Token;
import com.echameunapata.backend.domain.models.User;
import com.echameunapata.backend.services.contract.ITemplateService;
import com.echameunapata.backend.services.impl.MailServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class ForgotPasswordNotification implements INotificationStrategy<Token> {

    private final MailServiceImpl mailService;
    private final ITemplateService templateService;

    @Override
    public NotificationType getType() {
        return NotificationType.FORGOT_PASSWORD;
    }

    @Override
    public void sendNotification(Token data) {
        User user = data.getUser();

        // Construir el contenido HTML dinámico
        String contentHtml = """
            <p>Hola %s,</p>
            <p>Hemos recibido una solicitud para restablecer la contraseña de tu cuenta.</p>
            <p>Utiliza el siguiente token para restablecer tu contraseña (válido por 1 hora):</p>
            <h2 style="font-size: 22px; font-weight: bold; letter-spacing: 3px;">%s</h2>
            <p>Si no solicitaste este cambio, simplemente ignora este mensaje.</p>
        """.formatted(
                user.getName(),
                data.getToken()
        );

        Map<String, Object> vars = new HashMap<>();
        vars.put("title", "Restablecimiento de Contraseña");
        vars.put("content", contentHtml);
        vars.put("year", LocalDate.now().getYear());

        // Render del template base
        String html = templateService.renderTemplate("base-template.html", vars);

        mailService.sendEmail(
                user.getEmail(),
                "Token para restablecer tu contraseña",
                html
        );
    }

}
