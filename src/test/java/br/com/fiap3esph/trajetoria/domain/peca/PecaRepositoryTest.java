package br.com.fiap3esph.trajetoria.domain.peca;

import br.com.fiap3esph.trajetoria.adapter.out.repository.PecaRepositoryImpl;
import br.com.fiap3esph.trajetoria.adapter.out.repository.mapper.PecaEntityMapper;
import br.com.fiap3esph.trajetoria.application.core.domain.Peca;
import br.com.fiap3esph.trajetoria.application.port.out.PecaRepository;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.CategoriaPeca;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.EstadoConservacao;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.StatusPeca;
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
@Import({PecaRepositoryImpl.class, PecaEntityMapper.class})
public class PecaRepositoryTest {
    @Autowired
    PecaRepository repository;

    @Test
    @DisplayName("Expectativa: Persistir uma peça disponível e recuperá-la pelo ID.")
    void salvarEBuscarPeca() {
        //arrange or given
        Peca peca = new Peca(
                null,
                "Blazer social azul",
                CategoriaPeca.BLAZER,
                Tamanho.M,
                EstadoConservacao.BOM,
                "https://exemplo.com/blazer.jpg",
                "A1",
                "Doação",
                StatusPeca.DISPONIVEL,
                null
        );

        //act or when
        Peca salva = repository.salvar(peca);

        //assert or then
        assertThat(salva.getId()).isNotNull();
        assertThat(repository.buscarPorId(salva.getId()))
                .isPresent()
                .get()
                .satisfies(encontrada -> {
                    assertThat(encontrada.getNome()).isEqualTo("Blazer social azul");
                    assertThat(encontrada.getStatus()).isEqualTo(StatusPeca.DISPONIVEL);
                    assertThat(encontrada.getCategoria()).isEqualTo(CategoriaPeca.BLAZER);
                });
    }

    @Test
    @DisplayName("Expectativa: Filtrar o acervo por categoria e tamanho.")
    void filtrarPorCategoriaETamanho() {
        //arrange or given
        repository.salvar(new Peca(null, "Camisa branca", CategoriaPeca.CAMISA, Tamanho.P,
                EstadoConservacao.NOVO, "https://exemplo.com/camisa.jpg", "A2", "Doação",
                StatusPeca.DISPONIVEL, null));
        Peca alvo = repository.salvar(new Peca(null, "Calça preta", CategoriaPeca.CALCA, Tamanho.G,
                EstadoConservacao.BOM, "https://exemplo.com/calca.jpg", "A3", "Doação",
                StatusPeca.DISPONIVEL, null));

        //act or when
        var pagina = repository.buscarComFiltros(
                StatusPeca.DISPONIVEL,
                CategoriaPeca.CALCA,
                Tamanho.G,
                PageRequest.of(0, 10));

        //assert or then
        assertThat(pagina.getContent())
                .extracting(Peca::getId)
                .contains(alvo.getId())
                .hasSize(1);
    }
}
