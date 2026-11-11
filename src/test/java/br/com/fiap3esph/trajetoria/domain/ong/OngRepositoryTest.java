package br.com.fiap3esph.trajetoria.domain.ong;

import br.com.fiap3esph.trajetoria.adapter.out.repository.OngRepositoryImpl;
import br.com.fiap3esph.trajetoria.adapter.out.repository.mapper.OngEntityMapper;
import br.com.fiap3esph.trajetoria.application.core.domain.Ong;
import br.com.fiap3esph.trajetoria.application.port.out.OngRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import({OngRepositoryImpl.class, OngEntityMapper.class})
public class OngRepositoryTest {
    @Autowired
    OngRepository repository;

    @Test
    @DisplayName("Expectativa: Persistir uma ONG e recuperá-la pelo ID.")
    void salvarEBuscarPorId() {
        //arrange or given
        Ong ong = new Ong(null, "Instituto Recomeço", "contato@recomeco.org.br", "11 99999-0000", true);

        //act or when
        Ong salva = repository.salvar(ong);

        //assert or then
        assertThat(salva.getId()).isNotNull();
        assertThat(repository.buscarPorId(salva.getId()))
                .isPresent()
                .get()
                .extracting(Ong::getNome)
                .isEqualTo("Instituto Recomeço");
    }

    @Test
    @DisplayName("Expectativa: ONG excluída não aparece na listagem de ativas.")
    void naoListarOngInativa() {
        //arrange or given
        Ong ong = repository.salvar(
                new Ong(null, "ONG Encerrada", "fim@ong.org.br", "11 98888-0000", true));
        ong.excluir();
        repository.salvar(ong);

        //act or when
        var pagina = repository.buscarTodos(org.springframework.data.domain.PageRequest.of(0, 10));

        //assert or then
        assertThat(pagina.getContent())
                .extracting(Ong::getId)
                .doesNotContain(ong.getId());
    }
}
