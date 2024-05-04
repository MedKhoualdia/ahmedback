package com.dance.mo.Entities;

import com.dance.mo.Entities.Enumarations.Status;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Message {
    private Long id;
    private String senderName;
    private String receiverName;
    private String message;
    private String date;
    private Status status;
}
