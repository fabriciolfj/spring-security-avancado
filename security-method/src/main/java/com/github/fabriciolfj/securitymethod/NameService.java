package com.github.fabriciolfj.securitymethod;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class NameService {

    private Map<String, List<String>> secretNames = Map.of("fabricio", List.of("Energico", "Perfecto"), "lucas", List.of("Fantastico"));

    @PreAuthorize("hasAuthority('write')")
    public String getName() {
        return "Fantastico";
    }

    @PreAuthorize("#name == authentication.principal.username")
    public List<String> getSecretNames(String name) {
        return secretNames.get(name);
    }
}
