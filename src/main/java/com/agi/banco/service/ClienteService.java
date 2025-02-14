package com.agi.banco.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.agi.banco.domain.Cliente;
import com.agi.banco.exception.ResourceNotFoundException;
import com.agi.banco.repository.ClienteRepository;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteService {

    public final ClienteRepository cdao;
    public final String notFound = "404 Not Found: Cliente não encontrado.";

    public ResponseEntity<Cliente> buscarClientePorId(final Long id) {
        return cdao.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException(notFound));
    }

    public ResponseEntity<Cliente> cadastrarCliente(@Valid @RequestBody final Cliente cliente, final UriComponentsBuilder uriBuilder) {
        Cliente novoCliente = cdao.save(cliente);
        return ResponseEntity.created(uriBuilder.path("/clientes/{id}").buildAndExpand(novoCliente.getId()).toUri()).body(novoCliente);
    }

    public ResponseEntity<Cliente> atualizarCliente(final Long id, final Cliente clienteAtualizado) {
        return cdao.findById(id)
                .map(clienteExistente -> {
                    clienteExistente.setNome(clienteAtualizado.getNome());
                    clienteExistente.setCpf(clienteAtualizado.getCpf());
                    clienteExistente.setEmail(clienteAtualizado.getEmail());
                    clienteExistente.setDataNascimento(clienteAtualizado.getDataNascimento());
                    clienteExistente.setTelefone(clienteAtualizado.getTelefone());
                    clienteExistente.setSaldo(clienteAtualizado.getSaldo());
                    Cliente clienteAtualizadoSalvo = cdao.save(clienteExistente);
                    return ResponseEntity.ok(clienteAtualizadoSalvo);
                })
                .orElseThrow(() -> new ResourceNotFoundException(notFound));
    }

    public ResponseEntity<Void> excluirCliente(final Long id) {
        return cdao.findById(id)
                .map(cliente -> {
                    cdao.delete(cliente);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElseThrow(() -> new ResourceNotFoundException(notFound));
    }
}
