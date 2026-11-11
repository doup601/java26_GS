package br.com.fiap3esph.trajetoria.adapter.out.repository.mapper;

import br.com.fiap3esph.trajetoria.adapter.out.repository.entity.OngEntity;
import br.com.fiap3esph.trajetoria.application.core.domain.Ong;
import org.springframework.stereotype.Component;

@Component
public class OngEntityMapper {
    public OngEntity toEntity(Ong ong) {
        if (ong == null) {
            return null;
        }
        return new OngEntity(
                ong.getId(),
                ong.getNome(),
                ong.getEmail(),
                ong.getTelefone(),
                ong.isAtivo()
        );
    }

    public Ong toDomain(OngEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Ong(
                entity.getId(),
                entity.getNome(),
                entity.getEmail(),
                entity.getTelefone(),
                entity.isAtivo()
        );
    }
}
