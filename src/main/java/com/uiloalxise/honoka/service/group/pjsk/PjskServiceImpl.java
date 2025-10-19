package com.uiloalxise.honoka.service.group.pjsk;

import com.uiloalxise.constants.WebConstant;
import com.uiloalxise.honoka.service.MessageSenderService;
import com.uiloalxise.honoka.service.PictureService;
import com.uiloalxise.pojo.entity.commands.GroupMsgCommand;
import com.uiloalxise.pojo.entity.pjsk.EmotionRequestBody;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * @author Uiloalxise
 * @ClassName PjskServiceImpl
 * @Description 世界计划的服务类impl
 */
@Slf4j
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

    /**
     * @param command
     */
    @Override
    public void pjskEmoticonMenu(GroupMsgCommand command) {
        String content = command.getContent();
        if ("pjskf".equals(content)) {
            content = "menu";
            log.info("默认菜单:{}", content);
        } else if (content.startsWith("pjskf")) {
            // 移除"pjskf"前缀
            String rawContent = content.substring(5);
            log.info("提取原始参数:{}", rawContent);

            // 使用正则表达式将xxx011234格式转换为xxx 01 1234格式
            if (rawContent.matches("^[a-zA-Z]+\\d{2}.*$")) {
                // 将xxx01xxx转换为xxx 01 xxx
                content = rawContent.replaceFirst("([a-zA-Z]+)(\\d{2})(.*)", "$1 $2 $3");
                log.info("格式化后参数:{}", content);
            } else {
                content = rawContent;
            }
        } else {
            content = "menu";
            log.info("默认菜单:{}", content);
        }

        EmotionRequestBody requestBody = EmotionRequestBody.builder()
                .openId(command.getAuthorId())
                .content(content)
                .build();

        apiWebClient.post()
                .uri("/api/v1/pjsk/emoticon")
                .header("Content-Type", "application/json")
                .header("Authorization","Bearer dingding10")
                .body(Mono.just(requestBody), EmotionRequestBody.class)
                .retrieve()
                .toBodilessEntity()
                .block();
        sender.groupPictureMessageSender(command,pictureService.getPjskPicture(command.getAuthorId()),"\n1.使用[pjskf airi 01 你好]可以输出想要的表情包" +
                "2.使用[pjskf airi]可以查询想要的表情包角色" , 1);

    }


}
