package br.com.fiap3esph.trajetoria.domain.usuario;

import br.com.fiap3esph.trajetoria.application.core.domain.Usuario;
import br.com.fiap3esph.trajetoria.application.port.out.UsuarioRepository;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class UsuarioRepositoryTest {
    @Autowired
    UsuarioRepository repository;

    @Test
    @DisplayName("Expectativa: Persistir usuário e recuperá-lo pelo login.")
    void salvarEBuscarPorLogin() {
        //arrange or given
        Usuario usuario = new Usuario(
                null,
                "usuario.teste",
                "senha-cifrada",
                Role.ADMIN
        );

        //act or when
        Usuario salvo = repository.save(usuario);
        UserDetails encontrado = repository.findByLogin("usuario.teste");

        //assert or then
        assertThat(salvo.getId()).isNotNull();
        assertThat(encontrado).isNotNull();
        assertThat(encontrado.getUsername()).isEqualTo("usuario.teste");
        assertThat(encontrado.getPassword()).isEqualTo("senha-cifrada");
        assertThat(((Usuario) encontrado).getPerfil()).isEqualTo(Role.ADMIN);
        assertThat(encontrado.getAuthorities())
                .extracting(GrantedAuthority::getAuthority)
                .containsExactly("ROLE_ADMIN");
    }
}
