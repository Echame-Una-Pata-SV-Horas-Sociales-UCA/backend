package com.echameunapata.backend.services.impl;

import com.echameunapata.backend.services.contract.ITemplateService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class TemplateServiceImpl implements ITemplateService {

    /**
     * Renderiza una plantilla HTML de correo reemplazando sus variables.
     *
     * Este método carga un archivo de plantilla ubicado en el classpath dentro de
     * la carpeta `templates/emails/` y sustituye todas las variables declaradas con
     * el formato `{{variable}}` por los valores proporcionados en el mapa.
     *
     * @param templateName nombre del archivo de plantilla a renderizar.
     * @param variables mapa de variables y sus valores a reemplazar dentro de la plantilla.
     * @return Cadena con el contenido de la plantilla renderizada.
     * @throws RuntimeException Si la plantilla no puede ser cargada.
     */
    @Override
    public String renderTemplate(String templateName, Map<String, Object> variables) {
        try {
            ClassPathResource resource = new ClassPathResource("templates/emails/" + templateName);
            String template = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

            for (Map.Entry<String, Object> entry : variables.entrySet()) {
                template = template.replace("{{" + entry.getKey() + "}}", entry.getValue().toString());
            }

            return template;

        } catch (IOException e) {
            throw new RuntimeException("No se pudo cargar la plantilla: " + templateName);
        }
    }
}

