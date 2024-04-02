package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;


public interface UserService {
    void saveUser(User user);

    List<User> findAll();

    User getById(Long id);

    void delete(Long id);

    void update(User user, Long id);

    User getCurrentUser();
}
