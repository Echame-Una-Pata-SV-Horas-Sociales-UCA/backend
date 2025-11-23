package com.echameunapata.backend.services.impl;

import com.echameunapata.backend.domain.dtos.reportEvidence.CreateEvidenceDto;
import com.echameunapata.backend.domain.models.Report;
import com.echameunapata.backend.domain.models.ReportEvidence;
import com.echameunapata.backend.exceptions.HttpError;
import com.echameunapata.backend.repositories.EvidenceRepository;
import com.echameunapata.backend.services.contract.IEvidenceService;
import com.echameunapata.backend.services.contract.IFileStorageService;
import com.echameunapata.backend.services.contract.IReportService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
public class EvidenceServiceImpl implements IEvidenceService {

    private final EvidenceRepository evidenceRepository;
    private final IFileStorageService fileStorageService;
    private final IReportService reportService;

    /**
     * Este método permite crear una nueva evidencia para un reporte
     *
     * @param evidenceDto informacion de la nueva entidad a insertar.
     * @param reportId informacion de la nueva entidad a insertar.
     * @throws HttpError Error inesperado en el proceso.
     */
    @Override
    public void createEvidence(CreateEvidenceDto evidenceDto, UUID reportId) throws IOException {
        try{

            Report report = reportService.findReportById(reportId);

            for (MultipartFile image: evidenceDto.getImages()){
                Map<String, Object> dataImage = fileStorageService.uploadFile(image, "reports/" + reportId + "/evidences");
                ReportEvidence evidence = new ReportEvidence();

                evidence.setUrl((String) dataImage.get("url"));
                evidence.setDescription(evidenceDto.getDescription());

                evidence.setReport(report);
                evidence.setProvider("Coudinary");
                evidence.setProviderPublicId((String) dataImage.get("public_id"));
                evidence.setSecureUrl((String) dataImage.get("secure_url"));
                evidence.setContentType((String) dataImage.get("resource_type"));
                evidence.setSizeBytes(
                        Long.parseLong(dataImage.get("bytes").toString())
                );

                evidenceRepository.save(evidence);
            }
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * Este método permite obtener todas las evidencias de un reporte
     *
     * @param reportId informacion del reporte asociado.
     * @return Lista de evidencias asociadas.
     * @throws HttpError Error inesperado en el proceso.
     */
    @Override
    public List<ReportEvidence> findAllEvidencesByReport(UUID reportId) {
        try{
            var evidences = evidenceRepository.findALlByReportId(reportId);
            if(evidences.isEmpty()){
                return new ArrayList<>();
            }

            return evidences;
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * Este método permite eliminar una evidencia
     *
     * @param id de la evidencia a eliminar.
     * @throws HttpError Error inesperado en el proceso.
     */
    @Override
    public void deleteEvidence(UUID id) throws IOException {
        try{
            var evidence = evidenceRepository.findById(id).orElse(null);
            if (evidence == null){
                throw new HttpError(HttpStatus.FOUND, "Evidence with id not Exist");
            }
            fileStorageService.deleteFile(evidence.getProviderPublicId());
            evidenceRepository.delete(evidence);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * Este método permite obtener una evidencia por su id
     *
     * @param id codigo de identificacion de una evidencia.
     * @return Evidencia asociada a ese identificador.
     * @throws HttpError Error inesperado en el proceso.
     */
    @Override
    public ReportEvidence findEvidenceById(UUID id) {
        try{
            var evidence = evidenceRepository.findById(id).orElse(null);
            if (evidence == null){
                throw new HttpError(HttpStatus.FOUND, "Evidence with id not Exist");
            }

            return evidence;
        }catch (Exception e){
            throw e;
        }
    }
}
