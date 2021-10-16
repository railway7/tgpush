package me.kuku.tg.entity

import com.IceCreamQAQ.Yu.annotation.AutoBind
import com.icecreamqaq.yudb.jpa.JPADao
import com.icecreamqaq.yudb.jpa.annotation.Transactional
import org.hibernate.Hibernate
import javax.inject.Inject
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

interface UserDao: JPADao<UserEntity, Int> {
    fun findByKey(key: String): UserEntity?
    fun findByUserid(userid: Long): UserEntity?
}

@AutoBind
interface UserService {
    fun findByKey(key: String): UserEntity?
    fun findByUserid(userid: Long): UserEntity?
    fun save(userEntity: UserEntity)
}

class UserServiceImpl: UserService{

    @Inject
    private lateinit var userDao: UserDao

    @Transactional
    override fun findByKey(key: String) = userDao.findByKey(key)

    @Transactional
    override fun findByUserid(userid: Long) = userDao.findByUserid(userid)

    @Transactional
    override fun save(userEntity: UserEntity) = userDao.saveOrUpdate(userEntity)
}