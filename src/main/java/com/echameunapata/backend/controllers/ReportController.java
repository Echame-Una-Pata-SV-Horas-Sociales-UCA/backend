package com.echameunapata.backend.controllers;

import com.echameunapata.backend.domain.dtos.commons.GeneralResponse;
import com.echameunapata.backend.domain.dtos.reports.CreateReportDto;
import com.echameunapata.backend.domain.dtos.reports.FindReportAndEvidencesDto;
import com.echameunapata.backend.domain.dtos.reports.FindReportDto;
import com.echameunapata.backend.domain.dtos.reports.UpdateStatusReportDto;
import com.echameunapata.backend.domain.models.Report;
import com.echameunapata.backend.exceptions.HttpError;
import com.echameunapata.backend.services.contract.IReportService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.base-path}/reports")
@AllArgsConstructor
public class ReportController {

    private final IReportService reportService;
    private final ModelMapper modelMapper;

    /**
     * Crea un nuevo reporte en el sistema.
     *
     * Este método recibe los datos necesarios para registrar un reporte,
     * valida la información y almacena el reporte en la base de datos.
     *
     * @param reportDto Objeto con la información del reporte a crear.
     *                  Debe incluir tipo, descripción, ubicación y datos de contacto.
     *
     * @return ResponseEntity con un objeto GeneralResponse que contiene:
     *         - mensaje de éxito
     *         - HTTP 201 (CREATED)
     *         - el reporte creado en formato FindReportDto
     *
     * @throws HttpError Si ocurre un error de validación o si el proceso falla.
     */
    @PostMapping("/create")
    public ResponseEntity<GeneralResponse>createNewReport(@RequestBody @Valid CreateReportDto reportDto){
        try{
            Report report = reportService.createReport(reportDto);
            FindReportDto resp = modelMapper.map(report, FindReportDto.class);

            return GeneralResponse.getResponse(HttpStatus.CREATED, "Success", resp);
        }catch (HttpError e){
            return GeneralResponse.getResponse(e.getStatus(), e.getMessage());
        }
    }

    /**
     * Obtiene una lista paginada de reportes aplicando filtros opcionales.
     *
     * Este método permite buscar reportes filtrando por tipo, estado y rango de fechas.
     * Si no se proporcionan filtros, retorna todos los reportes existentes.
     *
     * @param type       Filtro opcional por tipo de reporte. Puede ser null.
     * @param status     Filtro opcional por estado del reporte. Puede ser null.
     * @param startDate  Fecha inicial del rango de búsqueda (ISO_DATE_TIME). Opcional.
     * @param endDate    Fecha final del rango de búsqueda (ISO_DATE_TIME). Opcional.
     *
     * @return ResponseEntity con un GeneralResponse que contiene:
     *         - listado paginado de reportes (PageResponse)
     *         - mensaje de éxito
     *         - código HTTP 200
     *
     * @throws HttpError Si ocurre algún error en la obtención de los datos.
     */
    @GetMapping("/find-all")
    public ResponseEntity<GeneralResponse>findAllReports(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate
    ){
        try {
            List<Report> reports = reportService.findAllReportsByFilters(type, status, startDate, endDate);
            List<FindReportDto> dtoPage = reports.stream().map(report -> modelMapper.map(report, FindReportDto.class)).toList();

            return GeneralResponse.getResponse(HttpStatus.OK, "Success all reports", dtoPage);
        }catch (HttpError e){
            return GeneralResponse.getResponse(e.getStatus(), e.getMessage());
        }

    }

    /**
     * Obtiene un reporte por su identificador único.
     *
     * Este método busca un reporte específico utilizando su UUID, y retorna
     * la información detallada del mismo. Si el reporte no existe, se lanza una excepción.
     *
     * @param id Identificador único (UUID) del reporte a consultar.
     *
     * @return ResponseEntity con un GeneralResponse que contiene:
     *         - datos del reporte encontrado (FindReportDto)
     *         - mensaje de éxito
     *         - código HTTP 200
     *
     * @throws HttpError Si el reporte no existe o no puede ser obtenido.
     */
    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<GeneralResponse>findById(@PathVariable("id") UUID id){
        try {
            Report report = reportService.findReportById(id);
            FindReportAndEvidencesDto resp = modelMapper.map(report, FindReportAndEvidencesDto.class);

            return GeneralResponse.getResponse(HttpStatus.OK, "Success all reports", resp);
        }catch (HttpError e){
            return GeneralResponse.getResponse(e.getStatus(), e.getMessage());
        }
    }

    /**
     * Elimina un reporte por su identificador único.
     *
     * Este método elimina de forma permanente un reporte existente en el sistema.
     *
     * @param id Identificador único (UUID) del reporte a eliminar.
     *
     * @return ResponseEntity con un GeneralResponse que contiene:
     *         - mensaje de confirmación
     *         - código HTTP 200
     *
     * @throws HttpError Si el reporte no existe o no puede eliminarse.
     */
    @DeleteMapping("/delete-by-id/{id}")
    public ResponseEntity<GeneralResponse>deleteById(@PathVariable("id")UUID id){
        try {
            reportService.deleteOneReport(id);
            return GeneralResponse.getResponse(HttpStatus.OK, "Report deleted is success");
        }catch (HttpError e){
            return GeneralResponse.getResponse(e.getStatus(), e.getMessage());
        }
    }

    /**
     * Actualiza el estado de un reporte existente.
     * Este método permite modificar únicamente el estado de un reporte,
     * recibiendo los datos necesarios mediante un DTO validado.
     *
     * @param reportDto Objeto con la información requerida para actualizar el estado del reporte.
     *                  Debe incluir el ID del reporte y el nuevo estado.
     *
     * @return ResponseEntity con un GeneralResponse que contiene:
     *         - reporte actualizado (FindReportDto)
     *         - mensaje de éxito
     *         - código HTTP 200
     *
     * @throws HttpError Si el reporte no existe o si ocurren errores en la actualización.
     */
    @PatchMapping("/update-status")
    public ResponseEntity<GeneralResponse>updateStatus(@RequestBody @Valid UpdateStatusReportDto reportDto){
        try{
            Report report = reportService.updateStatusReport(reportDto);
            FindReportDto resp = modelMapper.map(report, FindReportDto.class);

            return GeneralResponse.getResponse(HttpStatus.OK, "Success all reports", resp);
        }catch (HttpError e){
            return GeneralResponse.getResponse(e.getStatus(), e.getMessage());
        }
    }

}
