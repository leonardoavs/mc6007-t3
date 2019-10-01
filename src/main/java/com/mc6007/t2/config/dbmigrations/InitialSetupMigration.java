package com.mc6007.t2.config.dbmigrations;

import com.mc6007.t2.domain.Authority;
import com.mc6007.t2.domain.User;
import com.mc6007.t2.repository.AuthorityRepository;
import com.mc6007.t2.repository.UserRepository;
import com.mc6007.t2.security.AuthoritiesConstants;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Creates the initial database setup.
 */
@Component
public class InitialSetupMigration implements ApplicationListener<ContextRefreshedEvent> {

    private UserRepository userRepository;
    private AuthorityRepository authorityRepository;

    public InitialSetupMigration(UserRepository userRepository, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        addAuthorities();
        addUsers();
    }
    public void addAuthorities() {
        Authority adminAuthority = new Authority();
        adminAuthority.setName(AuthoritiesConstants.ADMIN);
        Authority userAuthority = new Authority();
        userAuthority.setName(AuthoritiesConstants.USER);
        authorityRepository.save(adminAuthority);
        authorityRepository.save(userAuthority);
    }

    public void addUsers() {
        Authority adminAuthority = new Authority();
        adminAuthority.setName(AuthoritiesConstants.ADMIN);
        Authority userAuthority = new Authority();
        userAuthority.setName(AuthoritiesConstants.USER);

        User systemUser = new User();
        systemUser.setId("user-0");
        systemUser.setLogin("system");
        systemUser.setPassword("$2a$10$mE.qmcV0mFU5NcKh73TZx.z4ueI/.bDWbj0T1BYyqP481kGGarKLG");
        systemUser.setFirstName("");
        systemUser.setLastName("System");
        systemUser.setEmail("system@localhost");
        systemUser.setActivated(true);
        systemUser.setLangKey("es");
        systemUser.setCreatedBy(systemUser.getLogin());
        systemUser.setCreatedDate(new Date());
        systemUser.getAuthorities().add(adminAuthority.getName());
        systemUser.getAuthorities().add(userAuthority.getName());
        userRepository.save(systemUser);

        User anonymousUser = new User();
        anonymousUser.setId("user-1");
        anonymousUser.setLogin("anonymoususer");
        anonymousUser.setPassword("$2a$10$j8S5d7Sr7.8VTOYNviDPOeWX8KcYILUVJBsYV83Y5NtECayypx9lO");
        anonymousUser.setFirstName("Anonymous");
        anonymousUser.setLastName("User");
        anonymousUser.setEmail("anonymous@localhost");
        anonymousUser.setActivated(true);
        anonymousUser.setLangKey("es");
        anonymousUser.setCreatedBy(systemUser.getLogin());
        anonymousUser.setCreatedDate(new Date());
        userRepository.save(anonymousUser);

        User adminUser = new User();
        adminUser.setId("user-2");
        adminUser.setLogin("admin");
        adminUser.setPassword("$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC");
        adminUser.setFirstName("admin");
        adminUser.setLastName("Administrator");
        adminUser.setEmail("admin@localhost");
        adminUser.setActivated(true);
        adminUser.setLangKey("es");
        adminUser.setCreatedBy(systemUser.getLogin());
        adminUser.setCreatedDate(new Date());
        adminUser.getAuthorities().add(adminAuthority.getName());
        adminUser.getAuthorities().add(userAuthority.getName());
        userRepository.save(adminUser);

        User userUser = new User();
        userUser.setId("user-3");
        userUser.setLogin("user");
        userUser.setPassword("$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K");
        userUser.setFirstName("");
        userUser.setLastName("User");
        userUser.setEmail("user@localhost");
        userUser.setActivated(true);
        userUser.setLangKey("es");
        userUser.setCreatedBy(systemUser.getLogin());
        userUser.setCreatedDate(new Date());
        userUser.getAuthorities().add(userAuthority.getName());
        userRepository.save(userUser);
    }
}
