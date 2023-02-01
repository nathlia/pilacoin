package br.ufsm.poli.csi.tapw.pilacoin.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
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
    private String status;

    public static final String AG_VALIDACAO = "AG_VALIDACAO";
    public static final String AG_BLOCO = "AG_BLOCO";
    public static final String BLOCO_EM_VALIDACAO = "BLOCO_EM_VALIDACAO";
    public static final String VALIDO = "VALIDO";
    public static final String INVALIDO = "INVALIDO";

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

    @JsonSerialize(using = DateSerializer.class)
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
    @JsonSerialize(using = BigIntegerSerializer.class)
    public void setNonce(BigInteger nonce) {
        this.nonce = nonce;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}


