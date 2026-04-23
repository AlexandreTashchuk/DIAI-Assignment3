package pt.unl.fct.iadi.novaevents.security

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import pt.unl.fct.iadi.novaevents.model.AppRole
import pt.unl.fct.iadi.novaevents.model.AppRoleName
import pt.unl.fct.iadi.novaevents.model.AppUser
import pt.unl.fct.iadi.novaevents.repository.AppRoleRepository
import pt.unl.fct.iadi.novaevents.repository.AppUserRepository

@ExtendWith(MockitoExtension::class)
class TestJpaUserDetailsManager {

    @Mock
    lateinit var appUserRepository: AppUserRepository

    @Mock
    lateinit var appRoleRepository: AppRoleRepository

    @InjectMocks
    lateinit var manager: JpaUserDetailsManager

    @Test
    fun `loads user with mapped authorities`() {
        val user = AppUser(id = 1, username = "alice", password = "pwd")
        user.roles.add(AppRole(role = AppRoleName.ROLE_EDITOR, user = user))
        `when`(appUserRepository.findByUsername("alice")).thenReturn(user)

        val loaded = manager.loadUserByUsername("alice")

        assertEquals("alice", loaded.username)
        assertTrue(loaded.authorities.contains(SimpleGrantedAuthority("ROLE_EDITOR")))
    }

    @Test
    fun `user exists delegates to repository`() {
        `when`(appUserRepository.existsByUsername("alice")).thenReturn(true)

        assertTrue(manager.userExists("alice"))
    }

    @Test
    fun `creates user with default editor role when none provided`() {
        val user = User.withUsername("alice").password("pwd").authorities(emptyList()).build()
        `when`(appUserRepository.existsByUsername("alice")).thenReturn(false)
        `when`(appUserRepository.save(org.mockito.ArgumentMatchers.any(AppUser::class.java))).thenAnswer {
            it.arguments[0] as AppUser
        }

        manager.createUser(user)

        @Suppress("UNCHECKED_CAST")
        val captor = ArgumentCaptor.forClass(List::class.java) as ArgumentCaptor<List<AppRole>>
        verify(appRoleRepository).saveAll(captor.capture())
        assertTrue(captor.value.any { it.role == AppRoleName.ROLE_EDITOR })
    }
}

