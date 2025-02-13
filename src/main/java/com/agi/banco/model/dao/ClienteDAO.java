package com.agi.banco.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agi.banco.domain.Cliente;

public interface ClienteDAO extends JpaRepository<Cliente, Long> {

}
