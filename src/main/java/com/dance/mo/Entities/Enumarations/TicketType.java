package com.dance.mo.Entities.Enumarations;

public enum TicketType {
    PARTICIPANT_STANDARD("PS"),  // Participant
    SPECTATOR_STANDARD("SS"),    // Spectator Standard
    SPECTATOR_VIP("SV");         // Spectator VIP

    private final String code;

    TicketType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}