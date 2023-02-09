package br.ufsm.poli.csi.tapw.pilacoin.server.repositories;

import br.ufsm.poli.csi.tapw.pilacoin.server.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    @Query(value = "SELECT *  FROM usuario WHERE nome = ?1", nativeQuery = true)
    Usuario findByUsername(String username);

}
