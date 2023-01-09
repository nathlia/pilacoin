package br.ufsm.poli.csi.tapw.pilacoin.server.repositories;

import br.ufsm.poli.csi.tapw.pilacoin.server.model.Transasao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TransasaoRepository extends JpaRepository<Transasao, Long>, JpaSpecificationExecutor<Transasao> {
}