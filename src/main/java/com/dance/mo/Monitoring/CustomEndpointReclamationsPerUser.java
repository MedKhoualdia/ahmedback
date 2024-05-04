package com.dance.mo.Monitoring;

import com.dance.mo.Entities.User;
import com.dance.mo.Repositories.ReclamationRepository;
import com.dance.mo.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Component
@Endpoint(id = "custom-reclamations-per-user")
public class CustomEndpointReclamationsPerUser {

    private final ReclamationRepository reclamationRepository;
    private final UserRepository userRepository;

    @Autowired
    public CustomEndpointReclamationsPerUser(ReclamationRepository reclamationRepository, UserRepository userRepository) {
        this.reclamationRepository = reclamationRepository;
        this.userRepository = userRepository;
    }

    @ReadOperation
    public Map<String, Map<String, Integer>> countReclamationsPerUser() {
        List<Object[]> reclamationsPerUser = reclamationRepository.countReclamationsPerUser();

        Map<String, Map<String, Integer>> result = new HashMap<>();

        for (Object[] row : reclamationsPerUser) {
            Long userId = (Long) row[0];
            LocalDate reclamationDate = (LocalDate) row[1];
            Integer count = ((Number) row[2]).intValue();

            Optional<User> optionalUser = userRepository.findById(userId);

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                String fullName = user.getFirstName() + " " + user.getLastName();

                // Get or create the map for the current date
                String dateKey = reclamationDate.toString();
                Map<String, Integer> dateMap = result.computeIfAbsent(dateKey, k -> new HashMap<>());

                // Update the count for the user on the current date
                String countKey = fullName;
                Integer currentCount = dateMap.getOrDefault(countKey, 0);
                dateMap.put(countKey, currentCount + count);
            }
        }

        // Combine names if they have the same count on the same date
        combineNames(result);

        return result;
    }

    private void combineNames(Map<String, Map<String, Integer>> result) {
        for (Map<String, Integer> dateMap : result.values()) {
            Set<String> names = new HashSet<>(dateMap.keySet());
            for (String name : names) {
                Integer count = dateMap.get(name);
                if (count > 1) {
                    String combinedName = String.join(" / ", names);
                    dateMap.clear();
                    dateMap.put(combinedName, count);
                    break;
                }
            }
        }
    }
}
