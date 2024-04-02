package com.example.lecture_connect.repository;
import com.example.lecture_connect.models.Userx;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@EnableJpaRepositories
@Repository
public interface UserxRepository  extends JpaRepository<Userx,Integer> {
        Optional<Userx> findOneByNameAndPassword(String name, String password);
        Userx findByName(String name);
}
