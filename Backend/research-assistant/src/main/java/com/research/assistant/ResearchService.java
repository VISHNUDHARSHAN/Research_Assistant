package com.research.assistant;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class ResearchService
{
    @Value("${gemini.api.url}")
    private  String geminiApiUrl;
    @Value("${gemini.api.key}")
    private String geminiApiKey;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public ResearchService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper)
    {
        this.webClient = webClientBuilder.build();
        this.objectMapper = objectMapper;
    }
    public String processContent(ResearchRequest researchRequest)
    {

        //building my prompt
        String prompt = buildPrompt(researchRequest);

        //Query in the gemini api
        Map<String,Object> requestBody = Map.of(
                "contents", new Object[]{
                        Map.of(
                                "parts", new Object[]
                                        {
                                        Map.of("text",prompt)
                                }
                        )
            }
    );

        String response = webClient.post()
                .uri(geminiApiUrl + geminiApiKey)
                .bodyValue(requestBody)
                .retrieve().bodyToMono(String.class)
                .block();

        return extractTextFromResponse(response);
    }

    private String extractTextFromResponse(String response)
    {
        try
        {
            GeminiResponse geminiResponse = objectMapper.readValue(response, GeminiResponse.class);

            if(geminiResponse.getCandidates() != null && !geminiResponse.getCandidates().isEmpty())
            {
                GeminiResponse.Candidate firstCandidate = geminiResponse.getCandidates().get(0);
                if(firstCandidate.getContent() != null &&
                        firstCandidate.getContent().getParts() != null
                && !firstCandidate.getContent().getParts().isEmpty())
                {
                    return firstCandidate.getContent().getParts().get(0).getText();
                }

            }
            return "No content";
        } catch (Exception e)
        {
            return "Error Parsing: "+e.getMessage();
        }

    }

    public String buildPrompt(ResearchRequest researchRequest)
    {
        StringBuilder prompt = new StringBuilder();
        switch (researchRequest.getOperation())
        {
            case "summarize":
                prompt.append("Provide a clear and concise summary of the following text with few bullet points. Keep it short and simple: \n");
                break;
            case "suggest":
                prompt.append("Based on the following content: suggest related topics and further reading. Format the response with clear headings and bullet points. Keep it short and simple: \n");
                break;
            default:
                throw new IllegalArgumentException("Unknown operaton: "+researchRequest.getOperation());
        }
        prompt.append("in the language: ").append(researchRequest.getLanguage()).append(": ");
        prompt.append(researchRequest.getContent());
        return prompt.toString();
    }
}
