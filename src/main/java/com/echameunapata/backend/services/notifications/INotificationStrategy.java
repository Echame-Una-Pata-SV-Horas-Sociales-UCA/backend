package com.echameunapata.backend.services.notifications;

import com.echameunapata.backend.domain.enums.notifications.NotificationType;
import com.echameunapata.backend.domain.models.Report;

public interface INotificationStrategy<T> {
    NotificationType getType();
    void sendNotification(T data);
}
