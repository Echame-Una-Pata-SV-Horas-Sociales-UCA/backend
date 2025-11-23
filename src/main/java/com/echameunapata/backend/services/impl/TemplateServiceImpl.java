package com.echameunapata.backend.services.impl;

import com.echameunapata.backend.services.contract.ITemplateService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class TemplateServiceImpl implements ITemplateService {

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

