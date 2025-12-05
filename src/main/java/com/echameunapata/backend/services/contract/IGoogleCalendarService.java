package com.echameunapata.backend.services.contract;

import java.util.Date;

public interface IGoogleCalendarService {
    String createEvent(
            String title,
            String description,
            String startDate,
            String endDate,
            boolean monthlyRecurring
    ) throws Exception;
}
