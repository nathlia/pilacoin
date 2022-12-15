package br.ufsm.poli.csi.tapw.pilacoin.server.model;

import br.ufsm.poli.csi.tapw.pilacoin.model.PilaCoin;
import javax.persistence.*;

@Entity
public class Transacao {

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name="id_bloco")
    private Bloco bloco;
    @ManyToOne
    @JoinColumn(name = "id_pila")
    private PilaCoin pilaCoin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Bloco getBloco() {
        return bloco;
    }

    public void setBloco(Bloco bloco) {
        this.bloco = bloco;
    }

    public PilaCoin getPilaCoin() {
        return pilaCoin;
    }

    public void setPilaCoin(PilaCoin pilaCoin) {
        this.pilaCoin = pilaCoin;
    }
}
