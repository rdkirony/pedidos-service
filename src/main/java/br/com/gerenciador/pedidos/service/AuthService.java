package br.com.gerenciador.pedidos.service;

import br.com.gerenciador.pedidos.core.records.input.LoginInput;
import br.com.gerenciador.pedidos.core.records.output.UserLoginOutput;
import br.com.gerenciador.pedidos.core.exception.UserLoginException;
import br.com.gerenciador.pedidos.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repository;
    private final AuthenticationManager authenticationManager;
    private final UserTokenService userTokenService;


    public UserLoginOutput authenticate(LoginInput request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.userName(),
                        request.password()
                )
        );

        final var userdb = repository.findByUsername(request.userName());

        if (userdb.isEmpty()) {
            throw new UserLoginException("Usuário não encontrado");
        }

        final var accessToken = userTokenService.generateAccessToken(request.userName());
        final var refreshToken = userTokenService.generateRefreshToken(request.userName());

        return new UserLoginOutput(accessToken, refreshToken);
    }
}
