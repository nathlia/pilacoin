package br.ufsm.poli.csi.tapw.pilacoin.server.repository;

import br.ufsm.poli.csi.tapw.pilacoin.server.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
}
