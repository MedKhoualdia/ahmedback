package com.dance.mo.Monitoring;

import com.dance.mo.Entities.Role;
import com.dance.mo.Entities.User;
import com.dance.mo.Repositories.UserRepository;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Endpoint(id = "count-users-by-role")
public class CustomEndpointUserRoles {

    private final UserRepository userRepository;

    public CustomEndpointUserRoles(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @ReadOperation
    public Map<String, Integer> countUsersByRole() {
        Map<String, Integer> countMap = new HashMap<>();


        List<User> users = userRepository.findAll();


        Map<Role, Long> roleCounts = users.stream()
                .collect(Collectors.groupingBy(User::getRole, Collectors.counting()));


        roleCounts.forEach((role, count) -> countMap.put(role.name(), count.intValue()));

        return countMap;
    }
}
