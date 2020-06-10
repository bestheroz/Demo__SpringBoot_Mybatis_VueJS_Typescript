package com.github.bestheroz.standard.common.authenticate;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserDetailsService {

    private final RegistrationManagement registrationManagement;
    private final DomainEventPublisher domainEventPublisher;
    private final MailManager mailManager;
    private final UserRepository userRepository;

    public UserServiceImpl(final RegistrationManagement registrationManagement,
                           final DomainEventPublisher domainEventPublisher,
                           final MailManager mailManager,
                           final UserRepository userRepository) {
        this.registrationManagement = registrationManagement;
        this.domainEventPublisher = domainEventPublisher;
        this.mailManager = mailManager;
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        if (StringUtils.isEmpty(username)) {
            throw new UsernameNotFoundException("No user found");
        }
        final User user;
        if (username.contains("@")) {
            user = this.userRepository.findByEmailAddress(username);
        } else {
            user = this.userRepository.findByUsername(username);
        }
        if (user == null) {
            throw new UsernameNotFoundException("No user found by `" + username + "`");
        }
        return new SimpleUser(user);
    }

    @Override
    public User findById(final UserId userId) {
        return this.userRepository.findById(userId);
    }

    @Override
    public void register(final RegisterCommand command) throws RegistrationException {
        Assert.notNull(command, "Parameter `command` must not be null");
        final User newUser = this.registrationManagement.register(
                command.getUsername(),
                command.getEmailAddress(),
                command.getFirstName(),
                command.getLastName(),
                command.getPassword());

        this.sendWelcomeMessage(newUser);
        this.domainEventPublisher.publish(new UserRegisteredEvent(newUser, command));
    }

    private void sendWelcomeMessage(final User user) {
        this.mailManager.send(
                user.getEmailAddress(),
                "Welcome to TaskAgile",
                "welcome.ftl",
                MessageVariable.from("user", user)
        );
    }
}
