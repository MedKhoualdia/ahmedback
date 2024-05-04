package com.dance.mo.Monitoring;

import com.dance.mo.Repositories.ReclamationRepository;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Endpoint(id = "count-reclamations-by-status")
public class CustomEndpointReclamationStatus {
    private ReclamationRepository reclamationRepository;

    public CustomEndpointReclamationStatus(ReclamationRepository reclamationRepository) {
        this.reclamationRepository = reclamationRepository;
    }
    @ReadOperation
    public Map<String, Integer> countReclamationsByStatus() {
        Map<String, Integer> countMap = new HashMap<>();

        int urgentCount = (int) reclamationRepository.countByStatus("urgent");
        int notUrgentCount = (int) reclamationRepository.countByStatus("not urgent");

        countMap.put("URGENT", urgentCount);
        countMap.put("NOT_URGENT", notUrgentCount);
        return countMap;


    }

}
