package com.echameunapata.backend.services.notifications.factory;

import com.echameunapata.backend.domain.enums.notifications.NotificationType;
import com.echameunapata.backend.services.notifications.strategies.*;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.echameunapata.backend.domain.enums.notifications.NotificationType.*;

@Component
@AllArgsConstructor
public class NotificationFactory {

    private final ReportCreatedNotification reportCreated;
    private final ReportStatusChangedNotification reportStatusChanged;
    private final ForgotPasswordNotification forgotPassword;
    private final AdoptionApplicationNotification applicationNotification;
    private final AdoptionApplicationApprovedNotification approvedNotification;
    private final AdoptionApplicationRejectedNotification rejectedNotification;

    private final Map<NotificationType, INotificationStrategy<?>> strategies = new HashMap<>();

    @PostConstruct
    void init() {
        strategies.put(REPORT_CREATED, reportCreated);
        strategies.put(REPORT_STATUS_CHANGED, reportStatusChanged);
        strategies.put(FORGOT_PASSWORD, forgotPassword);
        strategies.put(ADOPTION_APPLICATION_REGISTERED, applicationNotification);
        strategies.put(ADOPTION_APPLICATION_APPROVED, approvedNotification);
        strategies.put(ADOPTION_APPLICATION_REJECTED, rejectedNotification);
    }

    @SuppressWarnings("unchecked")
    public <T> INotificationStrategy<T> getStrategy(NotificationType type) {
        return (INotificationStrategy<T>) strategies.get(type);
    }


}
