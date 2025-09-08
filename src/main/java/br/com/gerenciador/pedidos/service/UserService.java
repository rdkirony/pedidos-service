package br.com.gerenciador.pedidos.service;

import br.com.gerenciador.pedidos.core.exception.UserRegisterException;
import br.com.gerenciador.pedidos.entitys.User;
import br.com.gerenciador.pedidos.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    @Transactional
    public User register(final User user) {
        final Optional<User> userdb = repository.findByUsername(user.getUsername());
        if (userdb.isPresent()) {
            throw new UserRegisterException("Usuário ja está cadastrado");
        }
        return repository.save(user);
    }

    public User findById(final String id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuario não encontrado"));
    }


    public User findByUsername(final String username) {
        return repository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("Usuario não encontrado"));
    }

}
