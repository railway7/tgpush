package me.kuku.tgpush.entity

import org.hibernate.Hibernate
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Service
import javax.persistence.*

@Entity
@Table(name = "users")
data class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,
    @Column(unique = true)
    var key: String = "",
    @Column(unique = true)
    var userid: Long = 0
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as UserEntity

        return id != null && id == other.id
    }

    override fun hashCode(): Int = 0

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , key = $key , userid = $userid )"
    }

}

interface UserRepository: JpaRepository<UserEntity, Int> {
    fun findByKey(key: String): UserEntity?
    fun findByUserid(userid: Long): UserEntity?
}

interface UserService {
    fun findByKey(key: String): UserEntity?
    fun findByUserid(userid: Long): UserEntity?
    fun save(userEntity: UserEntity): UserEntity
}

@Service
class UserServiceImpl(val userRepository: UserRepository): UserService{

    override fun findByKey(key: String) = userRepository.findByKey(key)

    override fun findByUserid(userid: Long) = userRepository.findByUserid(userid)

    override fun save(userEntity: UserEntity) = userRepository.save(userEntity)
}