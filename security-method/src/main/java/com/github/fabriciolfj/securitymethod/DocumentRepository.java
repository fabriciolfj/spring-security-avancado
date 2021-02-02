package com.github.fabriciolfj.securitymethod;

import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class DocumentRepository {

    private Map<String, Document> documents =
            Map.of("abc123", new Document("fabricio"),
                    "qwe123", new Document("fabricio"),
                    "asd555", new Document("lucas"));

    public Document findDocument(String code) {
        return documents.get(code);
    }
}
