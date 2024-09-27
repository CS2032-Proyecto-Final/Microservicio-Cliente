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

import java.util.List;
import java.util.Optional;
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

        Cuenta cuenta = new Cuenta();
        cuenta.setSaldo(0.0);
        cuenta.setCliente(cliente);

        cliente.setCuenta(cuenta);

        cliente = clienteRepository.save(cliente);

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

        if (remitente.getCuenta().getSaldo() < monto) {
            throw new RuntimeException("Saldo insuficiente");
        }

        remitente.getCuenta().setSaldo(remitente.getCuenta().getSaldo() - monto);
        destinatario.getCuenta().setSaldo(destinatario.getCuenta().getSaldo() + monto);

        clienteRepository.save(remitente);
        clienteRepository.save(destinatario);
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
