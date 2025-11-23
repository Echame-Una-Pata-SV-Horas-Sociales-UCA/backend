package com.echameunapata.backend.services.notifications.factory;

import com.echameunapata.backend.services.notifications.IReportNotificationStrategy;
import com.echameunapata.backend.services.notifications.ReportCreatedNotification;
import com.echameunapata.backend.services.notifications.ReportStatusChangedNotification;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ReportNotificationFactory {

    private final ReportCreatedNotification reportCreated;
    private final ReportStatusChangedNotification reportStatusChanged;

    public IReportNotificationStrategy getStrategy(NotificationType type) {
        return switch (type) {
            case CREATED -> reportCreated;
            case STATUS_CHANGED -> reportStatusChanged;
        };
    }

    public enum NotificationType {
        CREATED,
        STATUS_CHANGED
    }
}
