package com.eab.openbread.domain.service

import com.eab.openbread.domain.exception.DuplicateResourceException
import com.eab.openbread.domain.exception.ResourceNotFoundException
import com.eab.openbread.domain.model.User
import com.eab.openbread.domain.repository.UserRepository
import com.eab.openbread.domain.specification.UserSpecification
import com.eab.openbread.web.dto.user.UserCreateDTO
import com.eab.openbread.web.dto.user.UserPasswordUpdateDTO
import com.eab.openbread.web.dto.user.UserResponseDTO
import com.eab.openbread.web.dto.user.toUserResponseDTO
import com.eab.openbread.web.dto.user.UserRoleUpdateDTO
import com.eab.openbread.web.dto.user.UserUpdateDTO
import com.eab.openbread.web.dto.user.toEntity
import jakarta.validation.constraints.Email
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.jpa.domain.Specification
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    //CRUD Create Read Update Delete
    private val logger = LoggerFactory.getLogger(UserService::class.java)

    /**
     * This method creates a user if it passes domain rules.
     * Rules:
     *  - Email/Nif must be unique
     * Flow:
     *  - Checks for duplicates for nif and/or email
     *  - If everything is correct, hashes password, transforms DTO into a user, and saves it into the database
     *  - If a rule is broken, an exception is thrown
     *  @param userDTO minimal data for a new user
     *  @return ID of the newly created user
     *  @throws DuplicateResourceException if NIF or Email already exists before creation
     */
    fun  createUser(userDTO: UserCreateDTO): Long{
        logger.info("Attempting to create user with email=${userDTO.email} and nif=${userDTO.nif}")

        if (userRepository.existsByNif(userDTO.nif)) {
            logger.warn("User creation failed: NIF already in use (${userDTO.nif})")
            throw DuplicateResourceException("NIF already in use")
        }

        if (userRepository.existsByEmail(userDTO.email)){
            logger.warn("User creation failed: Email already in use (${userDTO.email})")
            throw DuplicateResourceException("Email already in use")
        }

        try {
            val hashedPassword = passwordEncoder.encode(userDTO.password)!!
            val newUser = userDTO.toEntity(hashedPassword)
            val savedUser = userRepository.save(newUser)
            logger.info("User created successfully with id=${savedUser.id}")
            return savedUser.id
        } catch (e: DataIntegrityViolationException) {
            logger.error("User creation failed due to database constraint violation", e)
            throw DuplicateResourceException("User already exists")
        }
    }

    /**
     * Finds all users in the Database
     * @return List of all users
     */
    fun findAllUsers(): List<UserResponseDTO> {
        logger.info("Fetching all users")
        return userRepository.findAll().map { toUserResponseDTO(it) }
    }


    /**
     * Finds users in the database applying the provided optional filters.
     *
     * Business rules:
     * - All filter parameters are optional; only non-null values are applied.
     * - Text fields such as name and surname are matched using case-insensitive partial search.
     * - Exact matches are applied for NIF, email, phone, postal code and active status.
     *
     * Flow:
     * 1. Build a dynamic Specification based on the provided filters.
     * 2. Execute the Specification through the repository.
     * 3. Return all users that satisfy the resulting criteria.
     *
     * @param search Optional search query for smartSearch.
     * @param active Optional active status filter.
     *
     * @return A list of users matching the provided filters.
     */
    fun findUsers(search: String?, active: Boolean?): List<UserResponseDTO> {
        logger.info("Searching users with Smart Search: query='$search', active=$active")

        // Combinamos las especificaciones con un AND lógico
        val spec = Specification
            .where(UserSpecification.smartSearch(search))
            .and(UserSpecification.withActiveStatus(active))

        return userRepository.findAll(spec).map { toUserResponseDTO(it) }
    }

    /**
     * Updates non‑sensitive user data such as name, surname, phone, postal code and active status.
     *
     * Business rules:
     * - Only non-sensitive fields are updated through this method.
     * - Sensitive fields such as password, email and role must be updated through their dedicated endpoints.
     * - Only non-null fields in the DTO are applied, allowing partial updates.
     *
     * Flow:
     * 1. Retrieve the user by ID.
     * 2. Apply only the fields present in the DTO.
     * 3. Persist the updated entity.
     *
     * @param id The ID of the user to update.
     * @param dto The DTO containing the optional fields to modify.
     * @return The ID of the updated user.
     * @throws ResourceNotFoundException if the user does not exist.
     */
    fun updateUser(id: Long, dto: UserUpdateDTO): Long {
        logger.info("Updating user data for userId=$id")
        val user = userRepository.findById(id)
            .orElseThrow {
                logger.warn("User update failed: userId=$id not found")
                ResourceNotFoundException("User with id $id not found")
            }

        dto.name?.let {user.name = it}
        dto.surname?.let {user.surname = it}
        dto.phone?.let {user.phone = it}
        dto.postalCode?.let {user.postalCode = it}
        dto.active?.let {user.active = it}

        val saved = userRepository.save(user)
        logger.info("User data updated successfully for userId=$id")
        return saved.id
    }

    /**
     * Updates the password of an existing user.
     *
     * Business rules:
     * - This method handles only password updates.
     * - The password is always hashed before being stored.
     * - This operation is considered sensitive and should be restricted and audited.
     *
     * Flow:
     * 1. Retrieve the user by ID.
     * 2. Hash the new password.
     * 3. Update the password field and persist the entity.
     *
     * @param id The ID of the user whose password will be updated.
     * @param dto The DTO containing the new raw password.
     * @return The ID of the updated user.
     * @throws ResourceNotFoundException if the user does not exist.
     */
    fun updateUserPassword(id: Long, dto: UserPasswordUpdateDTO): Long {
        val user = userRepository.findById(id)
            .orElseThrow {
                logger.warn("Password update failed: userId=$id not found")
                ResourceNotFoundException("User with id $id not found")
            }
        val hashedPassword = passwordEncoder.encode(dto.password)!!
        user.password = hashedPassword
        val saved = userRepository.save(user)
        logger.warn("Password updated successfully for userId=$id")
        return saved.id
    }

    /**
     * Updates the role of an existing user.
     *
     * Business rules:
     * - Only administrators should be allowed to call this method.
     * - This method updates only the role field; all other fields must be updated elsewhere.
     * - Role changes affect authorization and should be logged or audited.
     *
     * Flow:
     * 1. Retrieve the user by ID.
     * 2. Apply the new role.
     * 3. Persist the updated entity.
     *
     * @param id The ID of the user whose role will be updated.
     * @param dto The DTO containing the new role.
     * @return The ID of the updated user.
     * @throws ResourceNotFoundException if the user does not exist.
     */
    fun updateUserRole(id:Long, dto: UserRoleUpdateDTO): Long {
        logger.warn("Updating role for userId=$id to role=${dto.role}")
        val user = userRepository.findById(id)
            .orElseThrow {
                logger.warn("Role update failed: userId=$id not found")
                ResourceNotFoundException("User with id $id not found")
            }

        user.role = dto.role
        val saved = userRepository.save(user)
        logger.warn("Role updated successfully for userId=$id to role=${dto.role}")
        return saved.id
    }

    /**
     * Performs a logical deletion of a user by setting the active flag to false.
     *
     * Business rules:
     * - The user is not physically removed from the database.
     * - Only the 'active' field is modified; all other data remains intact.
     * - This operation should be restricted to administrators.
     *
     * Flow:
     * 1. Retrieve the user by ID.
     * 2. Set active = false.
     * 3. Persist the updated entity.
     *
     * @param id The ID of the user to deactivate.
     * @throws ResourceNotFoundException if the user does not exist.
     */
    fun deleteUser(id: Long, currentUserEmail: String) {
        val user = userRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("User with id $id not found") }

        val mail = user.email

        if (currentUserEmail == mail) {
            logger.warn("User $id attempted to delete themselves — operation blocked")
            throw IllegalArgumentException("You cannot delete your own account")
        }

        user.active = false
        userRepository.save(user)
    }

    fun activateUser(id: Long): Long{
        val user = userRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("User with id $id not found") }

        require(!user.active) { "User with id $id is already active" }

        user.active = true
        var saved = userRepository.save(user)

        return saved.id

    }


}

