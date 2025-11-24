package com.echameunapata.backend.controllers;

import com.echameunapata.backend.domain.dtos.commons.GeneralResponse;
import com.echameunapata.backend.domain.dtos.reportEvidence.CreateEvidenceDto;
import com.echameunapata.backend.domain.dtos.reportEvidence.FindEvidencesDto;
import com.echameunapata.backend.domain.models.ReportEvidence;
import com.echameunapata.backend.exceptions.HttpError;
import com.echameunapata.backend.services.contract.IEvidenceService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.base-path}/evidence")
@AllArgsConstructor
public class EvidenceController {

    private final IEvidenceService evidenceService;
    private final ModelMapper modelMapper;

    /**
     * Crea una nueva evidencia asociada a un reporte existente.
     * Este método permite subir una o varias evidencias mediante un formulario
     * de tipo multipart/form-data, vinculándolas al reporte indicado.
     *
     * @param evidenceDto Objeto con la información de la evidencia a crear. Debe ser validado.
     * @param reportId    ID del reporte al que se asociará la evidencia.
     *
     * @return ResponseEntity<GeneralResponse> que contiene:
     *             - Mensaje de éxito.
     *             - Código HTTP 201.
     *
     * @throws IOException Si ocurre un error al procesar los archivos adjuntos.
     * @throws HttpError   Si el reporte no existe o hay errores de negocio.
     */
    @PostMapping(
            value = "/create/{reportId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<GeneralResponse> createEvidence(@ModelAttribute @Valid CreateEvidenceDto evidenceDto, @PathVariable("reportId")UUID reportId) throws IOException {
        try {
            evidenceService.createEvidence(evidenceDto, reportId);
            return GeneralResponse.getResponse(HttpStatus.CREATED, "success");
        } catch (HttpError e) {
            return GeneralResponse.getResponse(e.getStatus(), e.getMessage());

        }

    }

    /**
     * Elimina una evidencia existente por su ID.
     * Este método permite borrar una evidencia específica, liberando los recursos asociados.
     *
     * @param id ID de la evidencia que se desea eliminar.
     *
     * @return ResponseEntity<GeneralResponse> que contiene:
     *             - Mensaje de éxito.
     *             - Código HTTP 201.
     *
     * @throws HttpError Si la evidencia no existe o hay errores de negocio.
     * @throws IOException Si ocurre un error al eliminar los archivos asociados.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GeneralResponse>deleteEvidence(@PathVariable("id") UUID id){
        try{
           evidenceService.deleteEvidence(id);
            return GeneralResponse.getResponse(HttpStatus.CREATED, "success deleted evidence");
        }catch (HttpError e){
            return GeneralResponse.getResponse(e.getStatus(), e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Obtiene todas las evidencias asociadas a un reporte específico.
     * Este método permite listar todas las evidencias vinculadas a un reporte determinado,
     * devolviendo la información mapeada a DTOs para respuesta.
     *
     * @param reportId ID del reporte cuyas evidencias se desean obtener.
     *
     * @return ResponseEntity<GeneralResponse> que contiene:
     *             - Lista de evidencias (FindEvidencesDto).
     *             - Mensaje de éxito.
     *             - Código HTTP 200.
     *
     * @throws HttpError Si el reporte no existe o hay errores de negocio.
     */
    @GetMapping("/find-by-report/{reportId}")
    public ResponseEntity<GeneralResponse>findAllByReport(@PathVariable("reportId") UUID reportId){
        try{
            List<ReportEvidence> evidences = evidenceService.findAllEvidencesByReport(reportId);

            List<FindEvidencesDto> dtoList = evidences.stream()
                    .map(e -> modelMapper.map(e, FindEvidencesDto.class))
                    .collect(Collectors.toList());

            return GeneralResponse.getResponse(HttpStatus.OK, "success", dtoList);
        }catch (HttpError e){
            return GeneralResponse.getResponse(e.getStatus(), e.getMessage());
        }
    }

    /**
     * Obtiene una evidencia por su ID.
     * Este método devuelve los detalles de una evidencia específica, mapeados a un DTO para respuesta.
     *
     * @param id ID de la evidencia que se desea consultar.
     *
     * @return ResponseEntity<GeneralResponse> que contiene:
     *             - La evidencia encontrada (FindEvidencesDto).
     *             - Mensaje de éxito.
     *             - Código HTTP 200.
     *
     * @throws HttpError Si la evidencia no existe o hay errores de negocio.
     */
    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<GeneralResponse>findById(@PathVariable("id") UUID id){
        try{
            ReportEvidence evidences = evidenceService.findEvidenceById(id);

            FindEvidencesDto resp = modelMapper.map(evidences, FindEvidencesDto.class);

            return GeneralResponse.getResponse(HttpStatus.OK, "success", resp);
        }catch (HttpError e){
            return GeneralResponse.getResponse(e.getStatus(), e.getMessage());
        }
    }

}
