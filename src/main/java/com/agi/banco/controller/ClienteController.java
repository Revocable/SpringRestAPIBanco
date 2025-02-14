package com.agi.banco.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import com.agi.banco.domain.Cliente;
import com.agi.banco.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/clientes")
@Tag(name = "Clientes", description = "API para gerenciamento de clientes")
@RequiredArgsConstructor
public class ClienteController {

    public final ClienteService cService;

    @Operation(summary = "Buscar cliente por ID", description = "Retorna um cliente único")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarCliente(@PathVariable("id") final Long id) {
        return cService.buscarClientePorId(id);
    }


    @Operation(summary = "Cadastrar novo cliente", description = "Cadastra um novo cliente no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente cadastrado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    
    @PostMapping
    public ResponseEntity<Cliente> cadastrarCliente(@Valid @RequestBody final Cliente cliente, final UriComponentsBuilder uriBuilder) {
        return cService.cadastrarCliente(cliente, uriBuilder);
    }

    
    @Operation(summary = "Atualizar cliente", description = "Atualiza os dados de um cliente existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizarCliente(@PathVariable("id") final Long id, @Valid @RequestBody final Cliente clienteAtualizado) {
        return cService.atualizarCliente(id, clienteAtualizado);
    }

    @Operation(summary = "Excluir cliente", description = "Remove um cliente do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Cliente excluído com sucesso"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirCliente(@PathVariable("id") final Long id) {
        return cService.excluirCliente(id);
    }
}