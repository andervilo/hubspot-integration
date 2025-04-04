package com.hubspot.integration.app.services;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.stream.Collectors;

@Service
public class RequestBodyReaderService {

    public String readBody(HttpServletRequest request) {
        try {
            Object cached = request.getAttribute("cachedRequestBody");
            if (cached != null) {
                return (String) cached;
            }

            String body = request.getReader()
                    .lines()
                    .collect(Collectors.joining(System.lineSeparator()));

            request.setAttribute("cachedRequestBody", body);
            return body;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler o corpo da requisição", e);
        }
    }
}
