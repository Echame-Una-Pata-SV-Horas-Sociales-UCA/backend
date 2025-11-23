package com.echameunapata.backend.services.notifications;

import com.echameunapata.backend.domain.models.Report;

public interface IReportNotificationStrategy {
    void sendNotification(Report report);
}
