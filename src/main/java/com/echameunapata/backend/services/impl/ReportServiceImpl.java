package com.echameunapata.backend.services.impl;

import com.echameunapata.backend.domain.dtos.reports.CreateReportDto;
import com.echameunapata.backend.domain.dtos.reports.UpdateReportInfoDto;
import com.echameunapata.backend.domain.enums.reports.ReportStatus;
import com.echameunapata.backend.domain.enums.reports.ReportType;
import com.echameunapata.backend.domain.models.Person;
import com.echameunapata.backend.domain.models.Report;
import com.echameunapata.backend.exceptions.HttpError;
import com.echameunapata.backend.repositories.ReportRepository;
import com.echameunapata.backend.services.contract.IReportService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class ReportServiceImpl implements IReportService {

    private final ReportRepository reportRepository;
    private final PersonServiceImpl personService;

    public ReportServiceImpl(ReportRepository reportRepository, PersonServiceImpl personService) {
        this.reportRepository = reportRepository;
        this.personService = personService;
    }

    /**
     * Este método permite crear un nuevo reporte
     *
     * @param reportDto informacion del nuevo reporte.
     * @return El reporte registrado.
     * @throws HttpError Error inesperado en el proceso.
     */
    @Override
    public Report createReport(CreateReportDto reportDto) {
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
            report.setReportEvidences(new ArrayList<>());

            if(!reportDto.getIsAnonymous()){
                Person person =personService.createPerson(reportDto.getPerson());
                report.setPerson(person);
            }

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
            var report = reportRepository.findById(id).orElse(null);
            if (report == null){
                throw new HttpError(HttpStatus.FOUND, "Report with id not Exist");
            }
            return reportRepository.save(report);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * Este método permite cambiar el estado de un reporte.
     *
     * @param id codigo unico de identificacion par aun reporte.
     * @param status nuevo estado a asignar.
     * @throws HttpError Error inesperado en el proceso.
     */
    @Override
    public void updateStatusReport(UUID id, String status) {
        try{
            var report = reportRepository.findById(id).orElse(null);
            if (report == null){
                throw new HttpError(HttpStatus.FOUND, "Report with id not exists");
            }
            ReportStatus newStatus = ReportStatus.fromString(status);
            report.setStatus(newStatus);

            reportRepository.save(report);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * Este método permite buscar un reporte por su id y actualizar su informacion
     *
     * @param id codigo unico de identificacion par aun reporte
     * @param reportInfoDto nueva informacion a asignar
     * @return El reporte actualizado.
     * @throws HttpError Error inesperado en el proceso.
     */
    @Override
    public Report updateReport(UUID id, UpdateReportInfoDto reportInfoDto) {
       try{
           var report = findReportById(id);
           report.setType(reportInfoDto.getType());
           report.setDescription(reportInfoDto.getDescription());
           report.setLocation(reportInfoDto.getLocation());
           report.setLocationUrl(reportInfoDto.getLocationUrl());
           report.setContactPhone(reportInfoDto.getContactPhone());
           report.setContactEmail(reportInfoDto.getContactEmail());

           return reportRepository.save(report);
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
     * @param pageable informacion de pagina y cantidad de reportes a traer.
     * @throws HttpError Error inesperado en el proceso.
     */
    @Override
    public Page<Report> findAllReportsByFilters(String type, String status, Instant startDate, Instant endDate, Pageable pageable) {
        try{
            ReportType reportType = (type != null && !type.isBlank()) ? ReportType.fromString(type) : null;
            ReportStatus reportStatus = (status != null && !status.isBlank()) ? ReportStatus.fromString(status) : null;
            Page<Report>reports = reportRepository.findByFilters(reportType, reportStatus, startDate, endDate, pageable);

            if (reports.isEmpty()){
                throw new HttpError(HttpStatus.NOT_FOUND, "No reports found for the given filters");
            }

            return reports;
        }catch (Exception e){
            throw e;
        }
    }
}
