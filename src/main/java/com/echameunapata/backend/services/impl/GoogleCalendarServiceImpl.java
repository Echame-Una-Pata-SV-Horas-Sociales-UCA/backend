package com.echameunapata.backend.services.impl;

import com.echameunapata.backend.services.contract.IGoogleCalendarService;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;

@Service
public class GoogleCalendarServiceImpl implements IGoogleCalendarService {

    private final Calendar calendar;

    @Value("${calendar.id}")
    private String calendarId;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final ZoneId ZONE_ID = ZoneId.of("America/El_Salvador");

    public GoogleCalendarServiceImpl(
            @Value("${calendar.credentials}")String calendarCredentials
    ) throws Exception {

        InputStream credentialsStream = new ByteArrayInputStream(
                calendarCredentials.getBytes(StandardCharsets.UTF_8)
        );

        GoogleCredential credential = GoogleCredential
                .fromStream(credentialsStream)
                .createScoped(Collections.singleton(CalendarScopes.CALENDAR));

        this.calendar = new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                credential
        )
                .setApplicationName("Sponsorship Calendar Service")
                .build();
    }

    @Override
    public String createEvent(
            String title,
            String description,
            String startDateStr,
            String finalDateStr,  // fecha hasta la cual dura la recurrencia
            boolean monthlyRecurring
    ) throws Exception {

        // Parsear la fecha de inicio desde string
        LocalDate startLocalDate = LocalDate.parse(startDateStr, DATE_FORMATTER);

        // Crear ZonedDateTime con hora fija a las 10:00 AM
        ZonedDateTime startZdt = startLocalDate
                .atTime(10, 0, 0)
                .atZone(ZONE_ID);

        // El evento dura 1 hora
        ZonedDateTime endZdt = startZdt.plusHours(1);

        Event event = new Event()
                .setSummary(title)
                .setDescription(description);

        // Configurar fecha/hora de inicio
        event.setStart(
                new EventDateTime()
                        .setDateTime(new DateTime(startZdt.toInstant().toEpochMilli()))
                        .setTimeZone(ZONE_ID.getId())
        );

        // Configurar fecha/hora de fin
        event.setEnd(
                new EventDateTime()
                        .setDateTime(new DateTime(endZdt.toInstant().toEpochMilli()))
                        .setTimeZone(ZONE_ID.getId())
        );

        // Recurrencia mensual
        if (monthlyRecurring) {
            // Día del mes (1–31)
            int dayOfMonth = startZdt.getDayOfMonth();
            String rrule = "RRULE:FREQ=MONTHLY;BYMONTHDAY=" + dayOfMonth;

            // Si hay fecha de finalización → agregamos UNTIL
            if (finalDateStr != null && !finalDateStr.isEmpty()) {
                // Parsear la fecha final
                LocalDate finalLocalDate = LocalDate.parse(finalDateStr, DATE_FORMATTER);

                // El formato UNTIL necesita estar en UTC y en formato básico
                ZonedDateTime untilZdt = finalLocalDate
                        .atTime(23, 59, 59)  // Fin del día
                        .atZone(ZONE_ID)     // Convertir a zona horaria local
                        .withZoneSameInstant(ZoneId.of("UTC"));  // Luego a UTC

                // Formato requerido por RRULE: YYYYMMDDTHHMMSSZ
                String until = untilZdt.format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'"));
                rrule += ";UNTIL=" + until;
            }

            event.setRecurrence(Collections.singletonList(rrule));
        }

        // Recordatorio por correo 1 día antes
        EventReminder[] reminders = new EventReminder[]{
                new EventReminder().setMethod("email").setMinutes(24 * 60) // 1 día = 1440 minutos
        };

        event.setReminders(
                new Event.Reminders()
                        .setUseDefault(false)
                        .setOverrides(Arrays.asList(reminders))
        );

        // Crear evento
        Event created = calendar.events()
                .insert(calendarId, event)
                .execute();

        return created.getHtmlLink();
    }
}
