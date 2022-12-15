package br.ufsm.poli.csi.tapw.pilacoin.server.repository;

import br.ufsm.poli.csi.tapw.pilacoin.server.model.Bloco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlocoRepository extends JpaRepository<Bloco, Long> {
}
