package com.agi.banco.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.agi.banco.domain.Cliente;
import com.agi.banco.model.dao.ClienteDAO;
import com.agi.banco.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ClienteControllerTest {

    @Mock
    private ClienteDAO clienteDAO;

    @InjectMocks
    private ClienteController clienteController;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente(1L, "João da Silva", "123.456.789-10", "joao@email.com",
                LocalDate.of(1990, 1, 1), "11999999999", new BigDecimal("1000.00"));
    }

    @Test
    void testBuscarClientePorId_Encontrado() {
        when(clienteDAO.findById(1L)).thenReturn(Optional.of(cliente));

        ResponseEntity<Cliente> response = clienteController.cliente(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(cliente, response.getBody());
    }

    @Test
    void testBuscarClientePorId_NaoEncontrado() {
        when(clienteDAO.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clienteController.cliente(1L));
    }

    @Test
    void testCadastrarCliente() {
        when(clienteDAO.save(any(Cliente.class))).thenReturn(cliente);

        ResponseEntity<Cliente> response = clienteController.cadastrar(cliente);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(cliente, response.getBody());
    }

    @Test
    void testAtualizarCliente_Encontrado() {
        Cliente clienteAtualizado = new Cliente(1L, "Maria Silva", "123.456.789-10", "maria@email.com",
                LocalDate.of(1985, 5, 20), "11988888888", new BigDecimal("1500.00"));
        when(clienteDAO.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteDAO.save(any(Cliente.class))).thenReturn(clienteAtualizado);

        ResponseEntity<Cliente> response = clienteController.atualizar(1L, clienteAtualizado);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Maria Silva", response.getBody().getNome());
    }

    @Test
    void testAtualizarCliente_NaoEncontrado() {
        when(clienteDAO.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clienteController.atualizar(1L, cliente));
    }

    @Test
    void testExcluirCliente_Encontrado() {
        when(clienteDAO.findById(1L)).thenReturn(Optional.of(cliente));
        doNothing().when(clienteDAO).delete(cliente);

        ResponseEntity<Void> response = clienteController.excluir(1L);
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void testExcluirCliente_NaoEncontrado() {
        when(clienteDAO.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clienteController.excluir(1L));
    }
}
