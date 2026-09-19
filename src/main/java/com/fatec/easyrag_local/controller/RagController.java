package com.fatec.easyrag_local.controller;

import com.fatec.easyrag_local.dto.PerguntaRequest;
import com.fatec.easyrag_local.service.RagService;
import com.fatec.easyrag_local.service.ResponseApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RagController {

    private final RagService ragService;

    public RagController(RagService ragService) {
        this.ragService = ragService;
    }

    @PostMapping("/api/perguntar")
    public ResponseEntity<ResponseApi<String>> perguntar(@RequestBody PerguntaRequest request) {
        String resposta = ragService.responderComContexto(request.getPerguntar());
        ResponseApi<String> response = new ResponseApi<>(resposta, "Operação realizada com sucesso.");
        return ResponseEntity.ok(response);
    }
}
