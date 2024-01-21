package example

@Dao interface UserDao {
    data class User(val id: Long, val name: String, val role: String)

    @Query("select * from users where role = :role order by name")
    fun findByRole(role: String): List<User>
}