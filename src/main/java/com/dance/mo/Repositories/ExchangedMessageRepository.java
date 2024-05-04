package com.dance.mo.Repositories;

import com.dance.mo.Entities.ExchangedMessages;
import com.dance.mo.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExchangedMessageRepository extends JpaRepository<ExchangedMessages, Long> {
    List<ExchangedMessages> findBySenderAndReceiverOrderBySentTimeAsc(User sender, User receiver);
    List<ExchangedMessages> findByReceiverAndSenderOrderBySentTimeAsc(User receiver, User sender);



}
