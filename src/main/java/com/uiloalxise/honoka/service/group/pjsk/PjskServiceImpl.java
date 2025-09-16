package com.uiloalxise.honoka.service.group.pjsk;

import com.uiloalxise.constants.WebConstant;
import com.uiloalxise.honoka.service.MessageSenderService;
import com.uiloalxise.honoka.service.PictureService;
import com.uiloalxise.pojo.entity.commands.GroupMsgCommand;
import com.uiloalxise.pojo.entity.pjsk.EmotionRequestBody;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * @author Uiloalxise
 * @ClassName PjskServiceImpl
 * @Description TODO
 */
@Service
public class PjskServiceImpl implements PjskService{


    @Resource
    MessageSenderService sender;

    @Resource(name = "apiWebClient")
    private WebClient apiWebClient;

    @Resource
    PictureService pictureService;
    /**
     * pjsk随机表情包带文字
     *
     * @param command - 群消息命令
     */
    @Override
    @Async
    public void pjskRandomEmoticon(GroupMsgCommand command) {

        String content = command.getContent().replace("pjsk","");
        EmotionRequestBody requestBody = EmotionRequestBody.builder()
                .openId(command.getAuthorId())
                .content(content)
                .build();

        apiWebClient.post()
                .uri(WebConstant.RANDOM_PJSK_EMOTICON_URI)
                .header("Content-Type", "application/json")
                .body(Mono.just(requestBody), EmotionRequestBody.class)
                .retrieve()
                .toBodilessEntity()
                .block();
        sender.groupPictureMessageSender(command,pictureService.getPjskPicture(command.getAuthorId()),content , 1);

    }
}
