package com.agi.banco.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.agi.banco.domain.Cliente;
import com.agi.banco.model.dao.ClienteDAO;
import com.agi.banco.exception.ResourceNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/clientes")
@Tag(name = "Clientes", description = "API para gerenciamento de clientes")

public class ClienteController {

    @Autowired
    private ClienteDAO cdao;

    @Operation(summary = "Buscar cliente por ID", description = "Retorna um cliente único")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> cliente(@PathVariable("id") Long id) {
        return cdao.findById(id)
                   .map(ResponseEntity::ok)
                   .orElseThrow(() -> new ResourceNotFoundException("404 Not Found: Cliente não encontrado."));
    }

    @Operation(summary = "Cadastrar novo cliente", description = "Cadastra um novo cliente no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente cadastrado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<Cliente> cadastrar(@Valid @RequestBody Cliente cliente) {
        Cliente novoCliente = cdao.save(cliente);
        return ResponseEntity.ok(novoCliente);
    }

    @Operation(summary = "Atualizar cliente", description = "Atualiza os dados de um cliente existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizar(@PathVariable("id") Long id, @Valid @RequestBody Cliente clienteAtualizado) {
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
                   .orElseThrow(() -> new ResourceNotFoundException("404 Not Found: Cliente não encontrado."));
    }

    @Operation(summary = "Excluir cliente", description = "Remove um cliente do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Cliente excluído com sucesso"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable("id") Long id) {
        return cdao.findById(id)
                   .map(cliente -> {
                       cdao.delete(cliente);
                       return ResponseEntity.noContent().<Void>build();
                   })
                   .orElseThrow(() -> new ResourceNotFoundException("404 Not Found: Cliente não encontrado."));
    }
}