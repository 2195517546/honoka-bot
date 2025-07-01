package com.uiloalxise.honoka.service.group.pjsk;

import com.uiloalxise.constants.WebConstant;
import com.uiloalxise.honoka.service.MessageSenderService;
import com.uiloalxise.honoka.service.PictureService;
import com.uiloalxise.pojo.entity.commands.GroupMsgCommand;
import com.uiloalxise.pojo.entity.pjsk.EmotionRequestBody;
import jakarta.annotation.Resource;
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
    public void pjskRandomEmoticon(GroupMsgCommand command) {


        String content = command.getContent().replace("/pjsk","");
        EmotionRequestBody requestBody = EmotionRequestBody.builder()
                .openId(command.getAuthorId())
                .content(content)
                .build();


        // 发送 POST 请求并获取响应数据
        String response = apiWebClient.post()
                .uri(WebConstant.RANDOM_PJSK_EMOTICON_URI)
                .header("Content-Type", "application/json")
                .body(Mono.just(requestBody), EmotionRequestBody.class)
                .retrieve()
                .bodyToMono(String.class)
                .block();  // 阻塞等待结果


        sender.groupPictureMessageSender(command,pictureService.getPjskPicture(command.getGroupId()),content , null);

    }
}
