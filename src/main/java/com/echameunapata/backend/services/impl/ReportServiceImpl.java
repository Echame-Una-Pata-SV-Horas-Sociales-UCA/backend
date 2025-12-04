package com.echameunapata.backend.services.impl;

import com.echameunapata.backend.domain.dtos.reports.CreateReportDto;
import com.echameunapata.backend.domain.dtos.reports.UpdateReportInfoDto;
import com.echameunapata.backend.domain.dtos.reports.UpdateStatusReportDto;
import com.echameunapata.backend.domain.enums.notifications.NotificationType;
import com.echameunapata.backend.domain.enums.reports.ReportStatus;
import com.echameunapata.backend.domain.enums.reports.ReportType;
import com.echameunapata.backend.domain.models.Person;
import com.echameunapata.backend.domain.models.Report;
import com.echameunapata.backend.exceptions.HttpError;
import com.echameunapata.backend.repositories.ReportRepository;
import com.echameunapata.backend.services.contract.IReportService;
//import com.echameunapata.backend.services.notifications.factory.NotificationFactory;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ReportServiceImpl implements IReportService {

    private final ReportRepository reportRepository;
    private final PersonServiceImpl personService;
    private final FileStorageServiceImpl fileStorageService;
//    private final NotificationFactory notificationFactory;

    /**
     * Este método permite crear un nuevo reporte
     *
     * @param reportDto informacion del nuevo reporte.
     * @return El reporte registrado.
     * @throws HttpError Error inesperado en el proceso.
     */
    @Override
    public Report createReport(CreateReportDto reportDto) throws IOException {
        try{
            Report report = new Report();
            ReportType reportType = ReportType.fromString(reportDto.getType());

            report.setType(reportType);
            report.setDescription(reportDto.getDescription());
            report.setLocation(reportDto.getLocation());
            report.setLocationUrl(reportDto.getLocationUrl());
            report.setIsAnonymous(reportDto.getIsAnonymous());
            report.setContactPhone(reportDto.getContactPhone());
            report.setContactEmail(reportDto.getContactEmail());
            String photo  = fileStorageService.uploadFile(reportDto.getPhoto(), "reports/evidences");

            report.setPhoto(photo);
            if(!reportDto.getIsAnonymous()){
                Person person =personService.createPerson(reportDto.getPerson());
                report.setPerson(person);
            }

//                        notificationFactory.getStrategy(NotificationType.REPORT_CREATED)
//                    .sendNotification(newReport);

            return reportRepository.save(report);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * Este método permite buscar un reporte por su id
     *
     * @param id codigo unico de identificacion par aun reporte
     * @return El reporte encontrado.
     * @throws HttpError Error inesperado en el proceso.
     */
    @Override
    public Report findReportById(UUID id) {
        try{
            var report = reportRepository.findByIdWithEvidence(id).orElse(null);
            if (report == null){
                throw new HttpError(HttpStatus.FOUND, "Report with id not Exist");
            }
            return report;
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * Este método permite cambiar el estado de un reporte.
     *
     * @param reportDto contiene el id de reporte y el nuevo estado a asignar
     * @throws HttpError Error inesperado en el proceso.
     */
    @Override
    public Report updateStatusReport(UpdateStatusReportDto reportDto) {
        try{
            var report = reportRepository.findById(reportDto.getReportId()).orElse(null);

            if (report == null){
                throw new HttpError(HttpStatus.FOUND, "Report with id not exists");
            }

            ReportStatus newStatus = ReportStatus.fromString(reportDto.getStatus());

            if(!newStatus.name().equalsIgnoreCase(reportDto.getStatus())){
                throw new HttpError(HttpStatus.BAD_REQUEST, "The status value is not valid. Allowed values are: OPEN, CLOSED");
            }

            if (newStatus.name().equals(report.getStatus().toString())){
                throw new HttpError(HttpStatus.BAD_REQUEST, "The report already has the status: " + reportDto.getStatus());
            }

            report.setStatus(newStatus);

            report = reportRepository.save(report);
//            notificationFactory.getStrategy(NotificationType.REPORT_STATUS_CHANGED)
//                    .sendNotification(report);

           return report;
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * Este método permite eliminar un reporte
     *
     * @param id codigo unico de identificacion par aun reporte.
     * @throws HttpError Error inesperado en el proceso.
     */
    @Override
    public void deleteOneReport(UUID id) {
        try {
            var report = findReportById(id);
            reportRepository.delete(report);
        }catch (Exception e){
            throw e;
        }

    }

    /**
     * Este método permite traer una lista de reportes ya se con filtros o sin filtros
     *
     * @param type el tipo de reporte a buscar.
     * @param status el tipo de status a buscar.
     * @param startDate fecha de inicio para filtrar.
     * @param endDate fecha de fin para filtrar.
     * @throws HttpError Error inesperado en el proceso.
     */
    @Override
    public List<Report> findAllReportsByFilters(String type, String status, Instant startDate, Instant endDate) {
        try{
            ReportType reportType = (type == null || (type != null && type.isBlank())) ? null : ReportType.fromString(type);
            ReportStatus reportStatus = (status != null && !status.isBlank()) ? ReportStatus.fromString(status) : null;

            System.out.println("Repot type: " + reportType);
            System.out.println("Report status: " + reportStatus);

            List<Report> reports = reportRepository.findByFilters(reportType, reportStatus, startDate, endDate);

            if (reports.isEmpty()){
                throw new HttpError(HttpStatus.NOT_FOUND, "No reports found for the given filters");
            }

            return reports;
        }catch (Exception e){
            throw e;
        }
    }
}
