package org.ide.microserviciocliente.service;

import org.ide.microserviciocliente.dto.LoginRequestDto;
import org.ide.microserviciocliente.dto.RegisterRequestDto;
import org.ide.microserviciocliente.entity.Cliente;
import org.ide.microserviciocliente.entity.Cuenta;
import org.ide.microserviciocliente.entity.Persona;
import org.ide.microserviciocliente.entity.Tienda;
import org.ide.microserviciocliente.repository.ClienteRepository;
import org.ide.microserviciocliente.repository.CuentaRepository;
import org.ide.microserviciocliente.repository.PersonaRepository;
import org.ide.microserviciocliente.repository.TiendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private TiendaRepository tiendaRepository;

    @Autowired
    private CuentaRepository cuentaRepository;

    public Cliente registerCliente(RegisterRequestDto request) {
        Optional<Persona> existingPersona = personaRepository.findByTelefono(request.getTelefono());
        if (existingPersona.isPresent()) {
            throw new RuntimeException("Usuario ya registrado con este teléfono.");
        }

        Cliente cliente = new Cliente();
        cliente.setNombre(request.getNombre());

        // Crear y asociar la cuenta a la lista de cuentas del cliente
        Cuenta cuenta = new Cuenta();
        double saldoAleatorio = ThreadLocalRandom.current().nextDouble(100, 1000); // Saldo aleatorio
        cuenta.setSaldo(saldoAleatorio);        cuenta.setCliente(cliente);

        // Inicializar la lista de cuentas y agregar la cuenta creada
        cliente.setCuenta(Collections.singletonList(cuenta));

        // Guardar el cliente con la lista de cuentas asociadas
        cliente = clienteRepository.save(cliente);

        // Crear la persona asociada al cliente
        Persona persona = new Persona();
        persona.setCliente(cliente);
        persona.setTelefono(request.getTelefono());
        personaRepository.save(persona);

        return cliente;
    }

    public Optional<Cliente> loginCliente(LoginRequestDto request) {
        return personaRepository.findByTelefono(request.getTelefono())
                .map(Persona::getCliente);
    }

    public Optional<Cuenta> getSaldo(Long id) {
        return cuentaRepository.findById(id);
    }

    public Optional<Cliente> getClienteById(Long id) {
        return clienteRepository.findById(id);
    }

    public void transferirMonto(Long remitente_id, Long destinatario_id, Double monto) {
        Cliente remitente = clienteRepository.findById(remitente_id)
                .orElseThrow(() -> new RuntimeException("Remitente no encontrado"));

        Cliente destinatario = clienteRepository.findById(destinatario_id)
                .orElseThrow(() -> new RuntimeException("Destinatario no encontrado"));

        // Supongamos que cada cliente solo tiene una cuenta para simplificar
        Cuenta cuentaRemitente = remitente.getCuenta().get(0);
        Cuenta cuentaDestinatario = destinatario.getCuenta().get(0);

        if (cuentaRemitente.getSaldo() < monto) {
            throw new RuntimeException("Saldo insuficiente");
        }

        // Ajustar los saldos de ambas cuentas
        cuentaRemitente.setSaldo(cuentaRemitente.getSaldo() - monto);
        cuentaDestinatario.setSaldo(cuentaDestinatario.getSaldo() + monto);

        // Guardar los cambios
        cuentaRepository.save(cuentaRemitente);
        cuentaRepository.save(cuentaDestinatario);
    }

    public List<String> getPersonasNombres(List<Long> ids) {
        return personaRepository.findAllById(ids).stream()
                .map(persona -> persona.getCliente().getNombre())
                .collect(Collectors.toList());
    }

    public Optional<List<String>> getTiendasNombres(List<Long> tiendaIds) {
        return Optional.of(tiendaRepository.findAllById(tiendaIds).stream()
                .map(tienda -> tienda.getCliente().getNombre())
                .collect(Collectors.toList()));
    }

    public Optional<String> getNombreTienda(Long tiendaId) {
        return tiendaRepository.findById(tiendaId)
                .map(tienda -> tienda.getCliente().getNombre());
    }
}

