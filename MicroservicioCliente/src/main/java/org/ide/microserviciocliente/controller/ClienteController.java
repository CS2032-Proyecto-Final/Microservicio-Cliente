package org.ide.microserviciocliente.controller;

import org.ide.microserviciocliente.dto.LoginRequestDto;
import org.ide.microserviciocliente.dto.RegisterRequestDto;
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
@RequestMapping("/auth")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody RegisterRequestDto request) {
        try {
            Cliente cliente = clienteService.registerCliente(request);
            return ResponseEntity.status(201).body(cliente);
        } catch (RuntimeException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequestDto request) {
        return clienteService.loginCliente(request)
                .map(cliente -> ResponseEntity.ok().body((Object) cliente))
                .orElse(ResponseEntity.status(400).body("Usuario no encontrado"));
    }

    @GetMapping("/cuenta/{id}/saldo")
    public ResponseEntity<Double> getSaldo(@PathVariable Long id) {
        Optional<Cuenta> cuenta = clienteService.getSaldo(id);
        return cuenta.map(c -> ResponseEntity.ok(c.getSaldo()))
                .orElse(ResponseEntity.status(404).body(null));
    }

    @GetMapping("/cliente")
    public ResponseEntity<?> getSaldoRemitente(@RequestParam Long remitente_id, @RequestParam Long destinatario_id) {
        Optional<Cliente> remitente = clienteService.getClienteById(remitente_id);
        if (remitente.isPresent()) {
            return ResponseEntity.ok(remitente.get().getCuenta().getSaldo());
        } else {
            return ResponseEntity.status(404).body("Usuario no encontrado");
        }
    }

    @PutMapping("/cliente/{remitente_id}/monto")
    public ResponseEntity<?> transferMonto(@PathVariable Long remitente_id, @RequestParam Long destinatario_id, @RequestParam Double monto) {
        try {
            clienteService.transferirMonto(remitente_id, destinatario_id, monto);
            return ResponseEntity.ok("Transferencia realizada con éxito");
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/personas/nombre")
    public ResponseEntity<?> getPersonasNombres(@RequestBody List<Long> ids) {
        return ResponseEntity.ok(clienteService.getPersonasNombres(ids));
    }

    @GetMapping("/tiendas/nombre")
    public ResponseEntity<List<String>> getTiendasNombres(@RequestBody List<Long> tiendaIds) {
        return clienteService.getTiendasNombres(tiendaIds)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).body(new ArrayList<>()));
    }

    @GetMapping("/tienda/{tienda_id}/nombre")
    public ResponseEntity<?> getNombreTienda(@PathVariable Long tienda_id) {
        return clienteService.getNombreTienda(tienda_id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).body("Tienda no encontrada"));
    }
}