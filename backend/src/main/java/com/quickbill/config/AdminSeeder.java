package com.quickbill.config;

import lombok.RequiredArgsConstructor;
import com.quickbill.user.entity.Role;
import com.quickbill.user.repository.UserRepository;
import com.quickbill.user.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {

    private static final Logger log =
            LoggerFactory.getLogger(AdminSeeder.class);
private final UserRepository userRepository;
private final PasswordEncoder passwordEncoder;
    @Override
    public void run(String... args) throws Exception {

      log.info("Admin seeder started...");

    boolean adminExists =
            userRepository.existsByRole(Role.ADMIN);

    if (adminExists) {

        log.info("Admin already exists. Seeder skipped.");

    }
    else {

    log.info("No admin found. Creating default admin...");

    User admin = User.builder()
            .fullName("Nene owner")
            .email("admin@quickbill.com")
            .phone("9705261019")
            .passwordHash(
                    passwordEncoder.encode("Admin@0226")
            )
            .role(Role.ADMIN)
            .isActive(true)
            .build();

    userRepository.save(admin);

    log.info("Default admin created successfully");
}
}
}