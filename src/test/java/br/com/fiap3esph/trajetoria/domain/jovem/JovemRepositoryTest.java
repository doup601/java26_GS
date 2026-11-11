package br.com.fiap3esph.trajetoria.domain.jovem;

import br.com.fiap3esph.trajetoria.adapter.out.repository.JovemRepositoryImpl;
import br.com.fiap3esph.trajetoria.adapter.out.repository.mapper.JovemEntityMapper;
import br.com.fiap3esph.trajetoria.adapter.out.repository.mapper.OngEntityMapper;
import br.com.fiap3esph.trajetoria.adapter.out.repository.persistence.OngJpaRepository;
import br.com.fiap3esph.trajetoria.application.core.domain.Jovem;
import br.com.fiap3esph.trajetoria.application.core.domain.Ong;
import br.com.fiap3esph.trajetoria.application.port.out.JovemRepository;
import br.com.fiap3esph.trajetoria.shared.vo.endereco.Endereco;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.Tamanho;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import({JovemRepositoryImpl.class, JovemEntityMapper.class, OngEntityMapper.class})
public class JovemRepositoryTest {
    @Autowired
    JovemRepository repository;

    @Autowired
    OngJpaRepository ongJpaRepository;

    @Test
    @DisplayName("Expectativa: Persistir um jovem vinculado a uma ONG e recuperá-lo.")
    void salvarEBuscarJovem() {
        //arrange or given
        var ongEntity = ongJpaRepository.save(new br.com.fiap3esph.trajetoria.adapter.out.repository.entity.OngEntity(
                null, "Instituto Recomeço", "contato@recomeco.org.br", "11 99999-0000", true));
        Ong ong = new Ong(ongEntity.getId(), ongEntity.getNome(), ongEntity.getEmail(), ongEntity.getTelefone(), true);
        Jovem jovem = new Jovem(
                null,
                "Maria Silva",
                "maria@email.com",
                "11 97777-0000",
                Tamanho.M,
                ong,
                new Endereco("Rua das Flores", "100", null, "Centro", "São Paulo", "SP", "01000-000"),
                false,
                null,
                false,
                true
        );
        jovem.aceitarConsentimento();

        //act or when
        Jovem salvo = repository.salvar(jovem);

        //assert or then
        assertThat(salvo.getId()).isNotNull();
        assertThat(repository.buscarPorId(salvo.getId()))
                .isPresent()
                .get()
                .satisfies(encontrado -> {
                    assertThat(encontrado.getNome()).isEqualTo("Maria Silva");
                    assertThat(encontrado.getTamanho()).isEqualTo(Tamanho.M);
                    assertThat(encontrado.isConsentimentoAceito()).isTrue();
                    assertThat(encontrado.getOng().getId()).isEqualTo(ong.getId());
                });
    }

    @Test
    @DisplayName("Expectativa: Listar apenas jovens ativos da ONG informada.")
    void listarPorOng() {
        //arrange or given
        var ongEntity = ongJpaRepository.save(new br.com.fiap3esph.trajetoria.adapter.out.repository.entity.OngEntity(
                null, "Instituto Recomeço", "contato@recomeco.org.br", "11 99999-0000", true));
        Ong ong = new Ong(ongEntity.getId(), ongEntity.getNome(), ongEntity.getEmail(), ongEntity.getTelefone(), true);
        Jovem ativo = repository.salvar(new Jovem(
                null, "João Ativo", "joao@email.com", "11 96666-0000", Tamanho.G, ong,
                new Endereco("Rua A", "10", null, "Centro", "São Paulo", "SP", "01000-000"),
                false, null, false, true));
        Jovem removido = repository.salvar(new Jovem(
                null, "João Removido", "joao2@email.com", "11 95555-0000", Tamanho.G, ong,
                new Endereco("Rua B", "20", null, "Centro", "São Paulo", "SP", "01000-000"),
                false, null, false, true));
        removido.excluir();
        repository.salvar(removido);

        //act or when
        var pagina = repository.buscarPorOng(ong.getId(), PageRequest.of(0, 10));

        //assert or then
        assertThat(pagina.getContent())
                .extracting(Jovem::getId)
                .contains(ativo.getId())
                .doesNotContain(removido.getId());
    }
}
