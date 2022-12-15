package br.ufsm.poli.csi.tapw.pilacoin.server.repository;

import br.ufsm.poli.csi.tapw.pilacoin.server.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
