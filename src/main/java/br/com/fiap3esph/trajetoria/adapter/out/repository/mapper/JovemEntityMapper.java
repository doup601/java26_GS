package br.com.fiap3esph.trajetoria.adapter.out.repository.mapper;

import br.com.fiap3esph.trajetoria.adapter.out.repository.entity.JovemEntity;
import br.com.fiap3esph.trajetoria.adapter.out.repository.entity.OngEntity;
import br.com.fiap3esph.trajetoria.application.core.domain.Jovem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JovemEntityMapper {
    private final OngEntityMapper ongEntityMapper;

    public JovemEntity toEntity(Jovem jovem, OngEntity ongRef) {
        if (jovem == null) {
            return null;
        }
        return new JovemEntity(
                jovem.getId(),
                jovem.getNome(),
                jovem.getEmail(),
                jovem.getTelefone(),
                jovem.getTamanho(),
                ongRef,
                jovem.getEndereco(),
                jovem.isConsentimentoAceito(),
                jovem.getDataConsentimento(),
                jovem.isValidado(),
                jovem.isAtivo()
        );
    }

    public Jovem toDomain(JovemEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Jovem(
                entity.getId(),
                entity.getNome(),
                entity.getEmail(),
                entity.getTelefone(),
                entity.getTamanho(),
                ongEntityMapper.toDomain(entity.getOng()),
                entity.getEndereco(),
                entity.isConsentimentoAceito(),
                entity.getDataConsentimento(),
                entity.isValidado(),
                entity.isAtivo()
        );
    }
}
