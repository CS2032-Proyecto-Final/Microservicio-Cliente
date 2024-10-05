package org.ide.microserviciocliente.controller;

import org.ide.microserviciocliente.dto.LoginRequestDto;
import org.ide.microserviciocliente.dto.PersonasIdsDto;
import org.ide.microserviciocliente.dto.RegisterRequestDto;
import org.ide.microserviciocliente.dto.TransferRequestDto;
import org.ide.microserviciocliente.entity.Cliente;
import org.ide.microserviciocliente.entity.Cuenta;
import org.ide.microserviciocliente.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    // Registrar un cliente nuevo
    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDto request) {
        try {
            Cliente cliente = clienteService.registerCliente(request);
            return ResponseEntity.status(201).body(Map.of("id", cliente.getId()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(409).body(Map.of("error", "Usuario ya registrado"));
        }
    }

    // Iniciar sesión con número de teléfono
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto request) {
        return clienteService.loginCliente(request)
                .map(cliente -> ResponseEntity.ok().body(Map.of("id", cliente.getId())))
                .orElse(ResponseEntity.status(400).body(Map.of("error", 400L)));
    }

    // Obtener el saldo de una cuenta
    @GetMapping("/cuenta/{id}/saldo")
    public ResponseEntity<?> getSaldo(@PathVariable Long id) {
        Optional<Cuenta> cuenta = clienteService.getSaldo(id);
        return cuenta.map(c -> ResponseEntity.ok(Map.of("saldo", c.getSaldo())))
                .orElse(ResponseEntity.status(404).body(Map.of("error", 404.0)));
    }

    // Obtener saldo del remitente utilizando query parameters
    @GetMapping("/cliente")
    public ResponseEntity<?> getSaldoRemitente(@RequestParam("remitente_id") Long remitenteId) {
        Optional<Cliente> remitente = clienteService.getClienteById(remitenteId);

        if (remitente.isPresent()) {
            if (!remitente.get().getCuenta().isEmpty()) {
                return ResponseEntity.ok(Map.of("saldo_remitente", remitente.get().getCuenta().get(0).getSaldo()));
            } else {
                return ResponseEntity.status(404).body(Map.of("error", "El cliente no tiene cuentas asociadas"));
            }
        } else {
            return ResponseEntity.status(404).body(Map.of("error", "Usuario no encontrado"));
        }
    }
    // Transferir monto entre cuentas
    @PutMapping("/cliente/{remitente_id}/monto")
    public ResponseEntity<?> transferMonto(@PathVariable Long remitente_id, @RequestBody TransferRequestDto transferRequest) {
        try {
            clienteService.transferirMonto(remitente_id, transferRequest.getDestinatarioId(), transferRequest.getMonto());
            return ResponseEntity.ok("Transferencia realizada con éxito");
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    // Obtener los nombres de las personas basadas en una lista de IDs
    @PatchMapping("/personas/nombre")
    public ResponseEntity<List<Map<String, Object>>> getPersonaNombres(@RequestBody List<Map<String, Long>> request) {
        List<Long> clienteIds = request.stream()
                .map(map -> map.get("id"))
                .collect(Collectors.toList());

        List<Map<String, Object>> personasConNombre = clienteService.getPersonasNombresPorClienteId(clienteIds);
        return ResponseEntity.ok(personasConNombre);
    }

    // Obtener el ID del cliente mediante su teléfono
    @GetMapping("/persona/telefono/{telefono}")
    public ResponseEntity<?> getClientePorTelefono(@PathVariable String telefono) {
        return clienteService.getClientePorTelefono(telefono)
                .map(cliente -> ResponseEntity.ok(Map.of("id", cliente.getId())))
                .orElse(ResponseEntity.status(400).body(Map.of("error", 400L)));
    }

    // Obtener los nombres de las tiendas basadas en una lista de IDs
    @PatchMapping("/tiendas/nombre")
    public ResponseEntity<List<Map<String, Object>>> getTiendaNombres(@RequestBody List<Map<String, Long>> request) {
        List<Long> clienteIds = request.stream()
                .map(map -> map.get("tienda_id"))
                .collect(Collectors.toList());

        List<Map<String, Object>> tiendasConNombre = clienteService.getTiendasNombresPorClienteId(clienteIds);
        return ResponseEntity.ok(tiendasConNombre);
    }

    // Obtener el nombre de una tienda por su ID
    @GetMapping("/tienda/{tienda_id}/nombre")
    public ResponseEntity<Map<String, Object>> getNombreTienda(@PathVariable Long tienda_id) {
        Map<String, Object> tiendaNombre = Map.of(
            "nombre_tienda", (Object) clienteService.getNombreTienda(tienda_id)
        );
        return ResponseEntity.ok(tiendaNombre);
    }

    @GetMapping("/persona/{cliente_id}/nombre")
    public ResponseEntity<String> getNombrePersona(@PathVariable Long cliente_id) {
        String nombre = clienteService.getNombrePersona(cliente_id);
        if (nombre != null) {
            return ResponseEntity.ok(nombre);
        } else {
            return ResponseEntity.status(404).body("Persona no encontrada");
        }
    }

    @GetMapping("/")
    public ResponseEntity<String> checkConnection() {
        return ResponseEntity.ok("Conexion correcta");
    }
}