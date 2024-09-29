package org.ide.microserviciocliente.controller;

import org.ide.microserviciocliente.dto.LoginRequestDto;
import org.ide.microserviciocliente.dto.RegisterRequestDto;
import org.ide.microserviciocliente.dto.TransferRequestDto;
import org.ide.microserviciocliente.entity.Cliente;
import org.ide.microserviciocliente.entity.Cuenta;
import org.ide.microserviciocliente.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    // Registrar un cliente nuevo
    @PostMapping("/register")
    public ResponseEntity<Long> register(@RequestBody RegisterRequestDto request) {
        try {
            Cliente cliente = clienteService.registerCliente(request);
            return ResponseEntity.status(201).body(cliente.getId());
        } catch (RuntimeException e) {
            return ResponseEntity.status(409).body(null);
        }
    }

    // Iniciar sesión con número de teléfono
    @PostMapping("/login")
    public ResponseEntity<Long> login(@RequestBody LoginRequestDto request) {
        return clienteService.loginCliente(request)
                .map(cliente -> ResponseEntity.ok().body(cliente.getId()))
                .orElse(ResponseEntity.status(400).body(null));
    }

    // Obtener el saldo de una cuenta
    @GetMapping("/cuenta/{id}/saldo")
    public ResponseEntity<Double> getSaldo(@PathVariable Long id) {
        Optional<Cuenta> cuenta = clienteService.getSaldo(id);
        return cuenta.map(c -> ResponseEntity.ok(c.getSaldo()))
                .orElse(ResponseEntity.status(404).body(null));
    }

    // Obtener saldo del remitente
    @GetMapping("/cliente")
    public ResponseEntity<?> getSaldoRemitente(@RequestParam Long remitente_id, @RequestParam Long destinatario_id) {
        Optional<Cliente> remitente = clienteService.getClienteById(remitente_id);
        if (remitente.isPresent()) {
            if (!remitente.get().getCuenta().isEmpty()) {
                return ResponseEntity.ok(remitente.get().getCuenta().get(0).getSaldo());
            } else {
                return ResponseEntity.status(404).body("El cliente no tiene cuentas asociadas");
            }
        } else {
            return ResponseEntity.status(404).body("Usuario no encontrado");
        }
    }

    // Transferir monto entre cuentas
    @PutMapping("/{remitente_id}/monto")
    public ResponseEntity<?> transferMonto(@PathVariable Long remitente_id, @RequestBody TransferRequestDto transferRequest) {
        try {
            clienteService.transferirMonto(remitente_id, transferRequest.getDestinatarioId(), transferRequest.getMonto());
            return ResponseEntity.ok("Transferencia realizada con éxito");
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    // Obtener los nombres de las personas basadas en una lista de IDs
    @GetMapping("/personas/nombre")
    public ResponseEntity<?> getPersonasNombres(@RequestBody List<Long> ids) {
        return ResponseEntity.ok(clienteService.getPersonasNombres(ids));
    }

    // Obtener los nombres de las tiendas basadas en una lista de IDs
    @GetMapping("/tiendas/nombre")
    public ResponseEntity<List<String>> getTiendasNombres(@RequestBody List<Long> tiendaIds) {
        return clienteService.getTiendasNombres(tiendaIds)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).body(new ArrayList<>()));
    }

    // Obtener el nombre de una tienda por su ID
    @GetMapping("/tienda/{tienda_id}/nombre")
    public ResponseEntity<?> getNombreTienda(@PathVariable Long tienda_id) {
        return clienteService.getNombreTienda(tienda_id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).body("Tienda no encontrada"));
    }
}