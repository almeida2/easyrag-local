package com.fatec.easyrag_local.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RagService {

    private final ChatClient chatClient;

    // Base de conhecimento simulada ("Easy RAG" em memória)
    private final List<String> baseDeConhecimento = List.of(
            "A empresa XYZ foi fundada em 2010 por dois engenheiros de software em São Paulo.",
            "O produto principal da XYZ se chama CodeMaster, um assistente de IA focado em refatoração de código legado.",
            "A política de reembolso da XYZ permite cancelamentos com estorno total em até 15 dias após a compra.",
            "O suporte técnico da XYZ atende de segunda a sexta-feira, das 09:00 às 18:00, via chat oficial.");

    // Injeção do ChatClient configurado pelo Spring AI
    public RagService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String responderComContexto(String pergunta) {
        // 1. Fase de Recuperação (Retrieval): Busca simples contendo palavras-chave da
        // pergunta
        String contextoRecuperado = buscarContexto(pergunta);

        // 2. Fase de Geração (Generation): Criação do Prompt estruturado para o LLM
        String templatePrompt = """
                Você é um assistente virtual prestativo e preciso.
                Utilize estritamente as informações fornecidas no bloco de contexto abaixo para responder à pergunta do usuário.
                Se a resposta não puder ser encontrada no contexto, diga apenas que não possui essa informação.

                [CONTEXTO]
                %s

                [PERGUNTA]
                %s

                [RESPOSTA]
                """
                .formatted(contextoRecuperado, pergunta);

        // 3. Chamada ao Ollama (qwen2.5-coder:7b)
        return this.chatClient.prompt()
                .user(templatePrompt)
                .call()
                .content();
    }

    // Mecanismo rudimentar de busca em memória para ilustrar o "Easy RAG"
    private String buscarContexto(String pergunta) {
        String query = pergunta.toLowerCase();
        List<String> correspondencias = baseDeConhecimento.stream()
                .filter(doc -> {
                    // Divide a pergunta em palavras para uma busca por interseção simples
                    for (String palavra : query.split(" ")) {
                        if (palavra.length() > 3 && doc.toLowerCase().contains(palavra)) {
                            return true;
                        }
                    }
                    return false;
                })
                .collect(Collectors.toList());

        if (correspondencias.isEmpty()) {
            return String.join("\n", baseDeConhecimento); // Retorna tudo se não filtrar nada
        }
        return String.join("\n", correspondencias);
    }
}
