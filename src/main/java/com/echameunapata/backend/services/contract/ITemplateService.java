package com.echameunapata.backend.services.contract;

import java.util.Map;

public interface ITemplateService {
    String renderTemplate(String templateName, Map<String, Object> variables);
}

