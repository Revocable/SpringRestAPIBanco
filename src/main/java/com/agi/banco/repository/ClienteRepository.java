package com.agi.banco.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.agi.banco.domain.Cliente;


@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

}
