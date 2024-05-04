package com.dance.mo.Services;

import com.dance.mo.Entities.Enumarations.RelationshipStatus;
import com.dance.mo.Entities.Relationship;
import com.dance.mo.Entities.User;
import com.dance.mo.Repositories.RelationshipRepository;
import com.dance.mo.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelationshipService {
    @Autowired
    private RelationshipRepository relationshipRepository;
    @Autowired
    private UserRepository userRepository;
    public List<Relationship> getPendingFriendRequests(User user) {
        return relationshipRepository.findByReceiverAndStatus(user, RelationshipStatus.PENDING);
    }
    public void sendFriendRequest(User sender, User receiver) {
        Relationship existingRequest = relationshipRepository.findBySenderAndReceiverAndStatus(
                sender, receiver, RelationshipStatus.PENDING);

        if (existingRequest == null) {
            existingRequest = relationshipRepository.findBySenderAndReceiverAndStatus(
                    receiver, sender, RelationshipStatus.PENDING);
        }

        if (existingRequest != null) {
            throw new IllegalStateException("Friend request already sent");
        }
        Relationship relationship = new Relationship();
        relationship.setSender(sender);
        relationship.setReceiver(receiver);
        relationship.setStatus(RelationshipStatus.PENDING);
        relationshipRepository.save(relationship);
    }

    public void acceptFriendRequest(User sender, User receiver) {
        Relationship relationship = relationshipRepository.findBySenderAndReceiver(sender, receiver);
        if (relationship != null && relationship.getStatus() == RelationshipStatus.PENDING) {
            relationship.setStatus(RelationshipStatus.ACCEPTED);
            relationshipRepository.save(relationship);

            addFriend(receiver, sender);
            addFriend(sender, receiver);
        }
    }

    private void addFriend(User currentUser, User friendToAdd) {
        if (!currentUser.getFriends().contains(friendToAdd)) {
            currentUser.getFriends().add(friendToAdd);
            userRepository.save(currentUser);
        }
        if (!friendToAdd.getFriends().contains(currentUser)) {
            friendToAdd.getFriends().add(currentUser);
            userRepository.save(friendToAdd);
        }
    }

    public void blockUser(User currentUser, User userToBlock) {
        // Find the relationship where the current user is the sender and the user to block is the receiver
        Relationship relationship1 = relationshipRepository.findBySenderAndReceiver(currentUser, userToBlock);
        if (relationship1 == null) {
            relationship1 = new Relationship();
            relationship1.setSender(currentUser);
            relationship1.setReceiver(userToBlock);
        }
        relationship1.setStatus(RelationshipStatus.BLOCKED);
        relationshipRepository.save(relationship1);

        // Find the relationship where the user to block is the sender and the current user is the receiver
        Relationship relationship2 = relationshipRepository.findBySenderAndReceiver(userToBlock, currentUser);
        if (relationship2 == null) {
            relationship2 = new Relationship();
            relationship2.setSender(userToBlock);
            relationship2.setReceiver(currentUser);
        }
        relationship2.setStatus(RelationshipStatus.BLOCKED);
        relationshipRepository.save(relationship2);
    }

    public RelationshipStatus checkRelationshipStatus(User currentUserId, User otherUserId) {
        Relationship relationship = relationshipRepository.findBySenderAndReceiver(currentUserId, otherUserId);
        if (relationship == null) {
            return RelationshipStatus.NONE;
        } else {
            return relationship.getStatus();
        }
    }
    public void unblockUser(User currentUser, User userToUnblock) {
        // Retrieve the relationship from current user to the user to unblock
        Relationship relationshipCurrentToUnblock = relationshipRepository.findBySenderAndReceiver(currentUser, userToUnblock);

        // Retrieve the relationship from user to unblock to the current user
        Relationship relationshipUnblockToCurrent = relationshipRepository.findBySenderAndReceiver(userToUnblock, currentUser);

        // If there is a blocked relationship from current user to user to unblock, update its status to ACCEPTED
        if (relationshipCurrentToUnblock != null && relationshipCurrentToUnblock.getStatus() == RelationshipStatus.BLOCKED) {
            relationshipCurrentToUnblock.setStatus(RelationshipStatus.ACCEPTED);
            relationshipRepository.save(relationshipCurrentToUnblock);
            System.out.println("Updated relationship from current user to user to unblock to ACCEPTED.");
        }

        // If there is a blocked relationship from user to unblock to current user, update its status to ACCEPTED
        if (relationshipUnblockToCurrent != null && relationshipUnblockToCurrent.getStatus() == RelationshipStatus.BLOCKED) {
            relationshipUnblockToCurrent.setStatus(RelationshipStatus.ACCEPTED);
            relationshipRepository.save(relationshipUnblockToCurrent);
            System.out.println("Updated relationship from user to unblock to current user to ACCEPTED.");
        }
    }



    public boolean canSendMessage(User sender, User receiver) {
        Relationship relationship1 = relationshipRepository.findBySenderAndReceiver(sender, receiver);
        boolean isAcceptedFromSenderToReceiver = relationship1 != null && relationship1.getStatus() == RelationshipStatus.ACCEPTED;

        Relationship relationship2 = relationshipRepository.findBySenderAndReceiver(receiver, sender);
        boolean isAcceptedFromReceiverToSender = relationship2 != null && relationship2.getStatus() == RelationshipStatus.ACCEPTED;

        return isAcceptedFromSenderToReceiver || isAcceptedFromReceiverToSender;
    }

}
