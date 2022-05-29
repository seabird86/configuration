package com.anhnt.configuration.service.mapper;

import com.anhnt.common.domain.configuration.response.Message;
import com.anhnt.common.domain.configuration.response.MessageResponse;
import com.anhnt.configuration.repository.entity.MessageEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    public abstract List<Message> toMessage(List<MessageEntity> entity);
}
