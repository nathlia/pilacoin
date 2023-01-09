package br.ufsm.poli.csi.tapw.pilacoin.server.repositories;

import br.ufsm.poli.csi.tapw.pilacoin.server.model.Bloco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BlocoRepository extends JpaRepository<Bloco, Long>, JpaSpecificationExecutor<Bloco> {
}