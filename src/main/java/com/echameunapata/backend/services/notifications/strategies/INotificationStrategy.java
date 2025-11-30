package com.echameunapata.backend.services.notifications.strategies;

import com.echameunapata.backend.domain.enums.notifications.NotificationType;

public interface INotificationStrategy<T> {
    NotificationType getType();
    void sendNotification(T data);
}
