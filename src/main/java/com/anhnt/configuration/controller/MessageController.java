package com.anhnt.configuration.controller;

import com.anhnt.common.domain.configuration.response.MessageResponse;
import com.anhnt.common.domain.response.BodyEntity;
import com.anhnt.common.domain.response.ResponseFactory;
import com.anhnt.configuration.repository.MessageRepository;
import com.anhnt.configuration.repository.entity.MessageEntity;
import com.anhnt.configuration.service.mapper.MessageMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class MessageController {

    private MessageRepository messageRepository;
    private MessageMapper messageMapper;

    @GetMapping(value = "/messages")
    public ResponseEntity<BodyEntity<MessageResponse>> getMessages(){
        List<MessageEntity> messages = messageRepository.findAll();
        return ResponseFactory.success(new MessageResponse(messageMapper.toMessage(messages)));

    }
}
