package br.ufsm.poli.csi.tapw.pilacoin.server.service;

import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link br.ufsm.poli.csi.tapw.pilacoin.server.model.Transasao} entity
 */
@Data
public class TransasaoDto implements Serializable {
    private final Long id;
}