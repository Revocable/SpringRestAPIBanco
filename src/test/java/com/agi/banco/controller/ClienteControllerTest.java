package com.agi.banco.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.agi.banco.domain.Cliente;
import com.agi.banco.service.ClienteService;
import com.agi.banco.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
class ClienteControllerTest {

    @Mock
    private ClienteService clienteService; // Mock do ClienteService

    @InjectMocks
    private ClienteController clienteController; // Injeta o mock no ClienteController

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente(1L, "João da Silva", "123.456.789-10", "joao@email.com",
                LocalDate.of(1990, 1, 1), "11999999999", new BigDecimal("1000.00"));
    }

    @Test
    void testBuscarClientePorId_Encontrado() {
        // Configura o mock para retornar um cliente quando buscarClientePorId for chamado
        when(clienteService.buscarClientePorId(1L)).thenReturn(ResponseEntity.ok(cliente));

        // Chama o método do controller
        ResponseEntity<Cliente> response = clienteController.buscarCliente(1L);

        // Verifica o status da resposta e o corpo
        assertEquals(200, response.getStatusCode().value());
        assertEquals(cliente, response.getBody());
    }

    @Test
    void testBuscarClientePorId_NaoEncontrado() {
        // Configura o mock para lançar uma exceção quando buscarClientePorId for chamado
        when(clienteService.buscarClientePorId(1L)).thenThrow(new ResourceNotFoundException("Cliente não encontrado"));

        // Verifica se a exceção é lançada
        assertThrows(ResourceNotFoundException.class, () -> clienteController.buscarCliente(1L));
    }

    @Test
    void testCadastrarCliente() {
        // Configura o mock para retornar o cliente quando cadastrarCliente for chamado
        when(clienteService.cadastrarCliente(any(Cliente.class), any(UriComponentsBuilder.class)))
                .thenReturn(ResponseEntity.ok(cliente));

        // Chama o método do controller
        ResponseEntity<Cliente> response = clienteController.cadastrarCliente(cliente, UriComponentsBuilder.newInstance());

        // Verifica o status da resposta e o corpo
        assertEquals(200, response.getStatusCode().value());
        assertEquals(cliente, response.getBody());
    }

    @Test
    void testAtualizarCliente_Encontrado() {
        Cliente clienteAtualizado = new Cliente(1L, "Maria Silva", "123.456.789-10", "maria@email.com",
                LocalDate.of(1985, 5, 20), "11988888888", new BigDecimal("1500.00"));

        // Configura o mock para retornar o cliente atualizado quando atualizarCliente for chamado
        when(clienteService.atualizarCliente(1L, clienteAtualizado)).thenReturn(ResponseEntity.ok(clienteAtualizado));

        // Chama o método do controller
        ResponseEntity<Cliente> response = clienteController.atualizarCliente(1L, clienteAtualizado);

        // Verifica o status da resposta e o corpo
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Maria Silva", response.getBody().getNome());
    }

    @Test
    void testAtualizarCliente_NaoEncontrado() {
        Cliente clienteAtualizado = new Cliente(1L, "Maria Silva", "123.456.789-10", "maria@email.com",
                LocalDate.of(1985, 5, 20), "11988888888", new BigDecimal("1500.00"));

        // Configura o mock para lançar uma exceção quando atualizarCliente for chamado
        when(clienteService.atualizarCliente(1L, clienteAtualizado)).thenThrow(new ResourceNotFoundException("Cliente não encontrado"));

        // Verifica se a exceção é lançada
        assertThrows(ResourceNotFoundException.class, () -> clienteController.atualizarCliente(1L, clienteAtualizado));
    }

    @Test
    void testExcluirCliente_Encontrado() {
        // Configura o mock para retornar uma resposta com status 204 quando excluirCliente for chamado
        when(clienteService.excluirCliente(1L)).thenReturn(ResponseEntity.noContent().build());

        // Chama o método do controller
        ResponseEntity<Void> response = clienteController.excluirCliente(1L);

        // Verifica o status da resposta
        assertEquals(204, response.getStatusCode().value());
    }

    @Test
    void testExcluirCliente_NaoEncontrado() {
        // Configura o mock para lançar uma exceção quando excluirCliente for chamado
        when(clienteService.excluirCliente(1L)).thenThrow(new ResourceNotFoundException("Cliente não encontrado"));

        // Verifica se a exceção é lançada
        assertThrows(ResourceNotFoundException.class, () -> clienteController.excluirCliente(1L));
    }
}