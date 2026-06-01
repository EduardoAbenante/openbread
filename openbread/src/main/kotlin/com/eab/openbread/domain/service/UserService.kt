package com.eab.openbread.domain.service

import com.eab.openbread.domain.model.User
import com.eab.openbread.domain.repository.UserRepository
import com.eab.openbread.web.dto.user.UserCreateDTO
import com.eab.openbread.web.dto.user.toEntity
import org.springframework.security.crypto.password.PasswordEncoder

class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    //CRUD Create Read Update Delete

    //Create
    fun  createUser(userDTO: UserCreateDTO): Long{
        try {
            val hashedPassword = passwordEncoder.encode(userDTO.password)!!
            val newUser = userDTO.toEntity(hashedPassword)
            val savedUser = userRepository.save(newUser)
            return savedUser.id

        } catch (e: RuntimeException){
            throw e
        }

    }

}