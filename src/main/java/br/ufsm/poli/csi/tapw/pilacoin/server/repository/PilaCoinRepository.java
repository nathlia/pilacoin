package br.ufsm.poli.csi.tapw.pilacoin.server.repository;

import br.ufsm.poli.csi.tapw.pilacoin.model.PilaCoin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PilaCoinRepository extends JpaRepository<PilaCoin, Long> {
}
