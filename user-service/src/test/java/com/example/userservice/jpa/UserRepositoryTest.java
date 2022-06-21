package com.example.userservice.jpa;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("UserRepository")
    public void getUserRepository() throws Exception{
        //arrange
        UserEntity userEntity = new UserEntity();
        userEntity.setEncryptedPwd("hello");
        userEntity.setUserId("AAAA");
        userEntity.setEmail("guldor10@naver.com");
        userEntity.setName("Min");

        //act
        userRepository.save(userEntity);

        //assert
        UserEntity aaaa = userRepository.findByUserId("AAAA");
        Assertions.assertThat(aaaa).isEqualTo(userEntity);

    }

}