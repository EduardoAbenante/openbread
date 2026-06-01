package com.eab.openbread.config

import com.eab.openbread.domain.model.User
import com.eab.openbread.domain.model.Role
import com.eab.openbread.domain.repository.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class DataInit {

    @Bean
    fun initData(
        userRepository: UserRepository,
        passwordEncoder: PasswordEncoder
    ) = CommandLineRunner {

        if (userRepository.count() == 0L) {

            val admin = User(
                nif = "00000000A",
                name = "Admin",
                surname = "OpenBread",
                email = "admin@openbread.com",
                password = passwordEncoder.encode("admin123")!!,
                phone = "600000000",
                postalCode = "08225",
                role = Role.ADMIN
            )

            val user = User(
                nif = "11111111B",
                name = "Usuario",
                surname = "Normal",
                email = "user@openbread.com",
                password = passwordEncoder.encode("user123")!!,
                phone = "611111111",
                postalCode = "08225",
                role = Role.USER
            )

            userRepository.save(admin)
            userRepository.save(user)

            println("✔ Usuarios iniciales creados: admin@openbread.com / user@openbread.com")
        }
    }
}
