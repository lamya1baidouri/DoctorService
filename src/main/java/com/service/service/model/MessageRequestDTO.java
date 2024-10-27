package com.service.service.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Données de la requête pour envoyer un message à un destinataire")
public class MessageRequestDTO {

    @Schema(description = "ID du destinataire", example = "patient123")
    private String recipientId;

    @Schema(description = "Contenu du message", example = "Bonjour, comment allez-vous ?")
    private String message;

    public MessageRequestDTO(String recipientId, String message) {
        this.recipientId = recipientId;
        this.message = message;
    }
}
