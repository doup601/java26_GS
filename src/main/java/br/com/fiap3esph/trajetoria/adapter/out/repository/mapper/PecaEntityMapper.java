package br.com.fiap3esph.trajetoria.adapter.out.repository.mapper;

import br.com.fiap3esph.trajetoria.adapter.out.repository.entity.PecaEntity;
import br.com.fiap3esph.trajetoria.application.core.domain.Peca;
import org.springframework.stereotype.Component;

@Component
public class PecaEntityMapper {
    public PecaEntity toEntity(Peca peca) {
        if (peca == null) {
            return null;
        }
        return new PecaEntity(
                peca.getId(),
                peca.getNome(),
                peca.getCategoria(),
                peca.getTamanho(),
                peca.getEstadoConservacao(),
                peca.getFotoUrl(),
                peca.getLocalArmazem(),
                peca.getOrigem(),
                peca.getStatus(),
                peca.getMotivoDescarte()
        );
    }

    public Peca toDomain(PecaEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Peca(
                entity.getId(),
                entity.getNome(),
                entity.getCategoria(),
                entity.getTamanho(),
                entity.getEstadoConservacao(),
                entity.getFotoUrl(),
                entity.getLocalArmazem(),
                entity.getOrigem(),
                entity.getStatus(),
                entity.getMotivoDescarte()
        );
    }
}
