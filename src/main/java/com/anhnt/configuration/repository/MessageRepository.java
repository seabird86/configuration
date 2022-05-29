package com.anhnt.configuration.repository;

import com.anhnt.configuration.repository.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<MessageEntity, String> {
}
