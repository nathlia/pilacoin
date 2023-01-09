package br.ufsm.poli.csi.tapw.pilacoin.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.PublicKey;
import java.util.Date;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PilaCoin implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    private String idCriador;
    private Date dataCriacao;
    private byte[] chaveCriador;
    private byte[] assinaturaMaster;
    private BigInteger nonce; //utilizar precisão de 128 bits

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdCriador() {
        return idCriador;
    }

    public void setIdCriador(String idCriador) {
        this.idCriador = idCriador;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public byte[] getChaveCriador() {
        return chaveCriador;
    }

    public void setChaveCriador(byte[] chaveCriador) {
        this.chaveCriador = chaveCriador;
    }

    public byte[] getAssinaturaMaster() {
        return assinaturaMaster;
    }

    public void setAssinaturaMaster(byte[] assinaturaMaster) {
        this.assinaturaMaster = assinaturaMaster;
    }

    public BigInteger getNonce() {
        return nonce;
    }

    public void setNonce(BigInteger nonce) {
        this.nonce = nonce;
    }
}
