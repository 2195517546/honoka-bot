package com.uiloalxise.honoka.service.group.bang.impl;

import com.uiloalxise.constants.WebConstant;
import com.uiloalxise.honoka.service.MessageSenderService;
import com.uiloalxise.honoka.service.PictureService;
import com.uiloalxise.honoka.service.group.bang.BangDreamService;
import com.uiloalxise.pojo.entity.commands.GroupMsgCommand;
import com.uiloalxise.pojo.entity.pjsk.EmotionRequestBody;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * @author Uiloalxise
 * @ClassName BangDreamServiceImpl
 * @Description TODO
 */
@Service
public class BangDreamServiceImpl implements BangDreamService {

        @Resource
        MessageSenderService sender;

    @Resource(name = "apiWebClient")
    private WebClient apiWebClient;

    @Resource
    PictureService pictureService;

    /**
     * @param command
     */
    @Override
    public void bangRandomEmoticon(GroupMsgCommand command) {
                String content = command.getContent().replace("bang","");
        EmotionRequestBody requestBody = EmotionRequestBody.builder()
                .openId(command.getAuthorId())
                .content(content)
                .build();

        apiWebClient.post()
                .uri(WebConstant.RANDOM_BANG_EMOTICON_URI)
                .header("Content-Type", "application/json")
                .body(Mono.just(requestBody), EmotionRequestBody.class)
                .retrieve()
                .toBodilessEntity()
                .block();
        //TODO pictureService.getPjskPicture在后续版本改为getBangPicture
        sender.groupPictureMessageSender(command,pictureService.getPjskPicture(command.getAuthorId()),content , 1);
    }
}
