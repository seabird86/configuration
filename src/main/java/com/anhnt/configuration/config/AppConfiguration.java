package com.anhnt.configuration.config;

import com.anhnt.common.domain.configuration.response.Message;
import com.anhnt.common.domain.response.ConfigurationCache;
import com.anhnt.configuration.controller.MessageController;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.stream.Collectors;

@Configuration
public class AppConfiguration{

    public AppConfiguration(MessageController controller){
        ConfigurationCache.messageMap.putAll(controller.getMessages().getBody().getData().getMessages().stream().collect(Collectors.toMap(Message::getCode, Message::toMessageMap)));
    }
}
