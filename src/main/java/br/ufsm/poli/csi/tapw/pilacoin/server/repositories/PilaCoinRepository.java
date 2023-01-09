package br.ufsm.poli.csi.tapw.pilacoin.server.repositories;

import br.ufsm.poli.csi.tapw.pilacoin.model.PilaCoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PilaCoinRepository extends JpaRepository<PilaCoin, Long>, JpaSpecificationExecutor<PilaCoin> {
}