package org.ide.microserviciocliente.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TransferRequestDto {

    @JsonProperty("destinatario_id")
    private Long destinatarioId;

    private Double monto;
}