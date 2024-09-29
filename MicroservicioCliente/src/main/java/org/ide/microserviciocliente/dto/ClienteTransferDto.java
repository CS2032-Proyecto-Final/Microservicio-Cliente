package org.ide.microserviciocliente.dto;
import lombok.Data;

@Data
public class ClienteTransferDto {
    private Long remitenteId;
    private Long destinatarioId;
}
