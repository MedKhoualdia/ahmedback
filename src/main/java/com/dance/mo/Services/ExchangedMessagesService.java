package com.dance.mo.Services;

import com.dance.mo.Entities.ExchangedMessages;
import com.dance.mo.Entities.User;
import com.dance.mo.Repositories.ExchangedMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExchangedMessagesService {
    @Autowired
    private ExchangedMessageRepository exchangedMessageRepository;
    public ExchangedMessages saveMessage(ExchangedMessages message){
        return exchangedMessageRepository.save(message);
    }

    public List<ExchangedMessages> findBySenderAndReceiver(User userId, User receiverId) {
        return exchangedMessageRepository.findBySenderAndReceiverOrderBySentTimeAsc(userId,receiverId);
    }
    public List<ExchangedMessages> findByReceiverAndSender(User userId, User receiverId) {
        return exchangedMessageRepository.findByReceiverAndSenderOrderBySentTimeAsc(receiverId,userId);
    }
    public Optional<ExchangedMessages> findById(Long messageId){
        return exchangedMessageRepository.findById(messageId);
    }
}
