package org.ide.microserviciocliente.dto;
import lombok.Data;

@Data
public class TransferRequestDto {
    private Long destinatarioId;
    private Double monto;
}
