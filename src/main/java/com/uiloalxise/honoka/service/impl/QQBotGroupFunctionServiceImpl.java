package com.uiloalxise.honoka.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.uiloalxise.constants.BotMsgConstant;
import com.uiloalxise.constants.QQBotConstant;
import com.uiloalxise.constants.RegexConstant;
import com.uiloalxise.constants.WebConstant;
import com.uiloalxise.exception.ArgsException;
import com.uiloalxise.exception.NoAppendException;
import com.uiloalxise.honoka.service.QQBotDuelService;
import com.uiloalxise.pojo.entity.PJSKMusicObject;
import com.uiloalxise.pojo.entity.QQGroupsMsg;
import com.uiloalxise.pojo.entity.QQMediaFile;
import com.uiloalxise.utils.PJSKUtil;
import com.uiloalxise.utils.QQBotUtil;
import com.uiloalxise.honoka.mapper.PJSKMusicPaneMapper;
import com.uiloalxise.honoka.service.MsgGeneratorService;
import com.uiloalxise.honoka.service.PictureService;
import com.uiloalxise.honoka.service.QQBotGroupFunctionService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Uiloalxise
 * @ClassName QQBotFunctionServiceServiceImpl
 * @Description QQ机器人可调用的功能总服务类实现
 */
@Service
@Slf4j
@Async
public class QQBotGroupFunctionServiceImpl implements QQBotGroupFunctionService {

    @Resource
    private QQBotDuelService qqBotDuelService;

    @Resource
    private MsgGeneratorService msgGeneratorService;

    @Resource
    private QQBotUtil qqBotUtil;

    @Resource
    private PJSKUtil pjskUtil;

    @Resource
    private PictureService pictureService;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Resource
    private PJSKMusicPaneMapper pjsKMusicPaneMapper;

    @Resource
    private RestTemplate restTemplate;
    /**
     * @param data
     */
    @Override

    /**
     * 随机图片，目前只有宛图功能
     */
    public void randomPic(JSONObject data,Integer msgSeq){
        String url = QQBotConstant.OPENAPI_URL + QQBotConstant.GROUP_SUFFIX + data.getString("group_openid");

        // String memberOpenid = data.getJSONObject("author").getString("member_openid");

        String id = data.getString("id");

        QQMediaFile qqMediaFile = QQMediaFile.builder().url(pictureService.getRandomPicture("wantu"))
                .fileType(1)
                .srvSendMsg(false)
                .build();

        HttpHeaders headers = getHeader();

        HttpEntity<QQMediaFile> qqMediaFileHttpEntity = new HttpEntity<>(qqMediaFile, headers);

        ResponseEntity<JSONObject> mediaFileResp = restTemplate.exchange(url + "/files", HttpMethod.POST, qqMediaFileHttpEntity, JSONObject.class);
        String fileInfo = mediaFileResp.getBody().getString("file_info");
        log.info(fileInfo);

        QQGroupsMsg qqGroupsMsg = QQGroupsMsg.builder()
                .content(" ")
                .msgType(7)
                .eventId("GROUP_AT_MESSAGE_CREATE")
                .media(mediaFileResp.getBody())
                .msgId(id)
                .build();

        HttpEntity<QQGroupsMsg> qqGroupsMsgEntity = new HttpEntity<>(qqGroupsMsg,headers);

        ResponseEntity<JSONObject> groupMsgResp = restTemplate.exchange(url + "/messages", HttpMethod.POST, qqGroupsMsgEntity, JSONObject.class);

        log.info("消息发送成功：{}", groupMsgResp.getBody());
    }

    /**
     * 随机钉言钉语
     * @param data
     */
    @Override
    public void randomDingTalk(JSONObject data,Integer msgSeq) {
        String url = QQBotConstant.OPENAPI_URL + QQBotConstant.GROUP_SUFFIX + data.getString("group_openid");
        String id = data.getString("id");
        HttpHeaders headers = getHeader();

        ResponseEntity<JSONObject> mediaFileResp = null;

        String content = msgGeneratorService.randomDingTalk();

        Pattern pattern = Pattern.compile(RegexConstant.DINGTALK_PICTURE_REGEX);
        Matcher matcher = pattern.matcher(content);


        if (matcher.find()) {
            log.info("匹配到图片$图片$:{}",matcher.group());
            String pictureUrl = WebConstant.DINGTALK_PIC_URL + matcher.group(2);
            //构造qqmedia文件的json
            QQMediaFile qqMediaFile = QQMediaFile.builder().url(pictureUrl)
                    .fileType(1)
                    .srvSendMsg(false)
                    .build();

            HttpEntity<QQMediaFile> qqMediaFileHttpEntity = new HttpEntity<>(qqMediaFile, headers);

            mediaFileResp = restTemplate.exchange(url + "/files", HttpMethod.POST, qqMediaFileHttpEntity, JSONObject.class);
            String fileInfo = mediaFileResp.getBody().getString("file_info");
            log.info(fileInfo);
        }



        QQGroupsMsg qqGroupsMsg = null;

        if(mediaFileResp == null) {
            qqGroupsMsg = qqBotUtil.qqGroupsTextMsg(content, id, msgSeq);
        }else
        {
            qqGroupsMsg = QQGroupsMsg.builder()
                    .content("钉言钉图")
                    .msgType(7)
                    .eventId("GROUP_AT_MESSAGE_CREATE")
                    .media(mediaFileResp.getBody())
                    .msgId(id)
                    .build();
        }


        HttpEntity<QQGroupsMsg> qqGroupsMsgEntity = new HttpEntity<>(qqGroupsMsg,headers);
        ResponseEntity<JSONObject> groupMsgResp = restTemplate.exchange(url + "/messages", HttpMethod.POST, qqGroupsMsgEntity, JSONObject.class);
        log.info("消息发送成功：{}", groupMsgResp.getBody());

    }

    /**
     * 群规
     * @param data
     */
    @Override
    public void groupRule(JSONObject data,Integer msgSeq) {
        String url = QQBotConstant.OPENAPI_URL + QQBotConstant.GROUP_SUFFIX + data.getString("group_openid");

        // String memberOpenid = data.getJSONObject("author").getString("member_openid");

        String id = data.getString("id");

        //构造qqmedia文件的json
        QQMediaFile qqMediaFile = QQMediaFile.builder().url(BotMsgConstant.GROUP_RULES_URL)
                .fileType(1)
                .srvSendMsg(false)
                .build();

        HttpHeaders headers = getHeader();

        HttpEntity<QQMediaFile> qqMediaFileHttpEntity = new HttpEntity<>(qqMediaFile, headers);

        ResponseEntity<JSONObject> mediaFileResp = restTemplate.exchange(url + "/files", HttpMethod.POST, qqMediaFileHttpEntity, JSONObject.class);
        String fileInfo = mediaFileResp.getBody().getString("file_info");
        log.info(fileInfo);

        QQGroupsMsg qqGroupsMsg = QQGroupsMsg.builder()
                .content("这是群规我希望你遵守！")
                .msgType(7)
                .eventId("GROUP_AT_MESSAGE_CREATE")
                .media(mediaFileResp.getBody())
                .msgId(id)
                .build();

        HttpEntity<QQGroupsMsg> qqGroupsMsgEntity = new HttpEntity<>(qqGroupsMsg,headers);


        ResponseEntity<JSONObject> groupMsgResp = restTemplate.exchange(url + "/messages", HttpMethod.POST, qqGroupsMsgEntity, JSONObject.class);

        log.info("消息发送成功：{}", groupMsgResp.getBody());
    }


    /**
     * 世界计划查谱面
     * @param data
     * @param msgSeq
     */
    @Override
    public void pjskPaneInfo(JSONObject data,Integer msgSeq)
    {
        String url = QQBotConstant.OPENAPI_URL + QQBotConstant.GROUP_SUFFIX + data.getString("group_openid");
        String id = data.getString("id");
        HttpHeaders headers = getHeader();

        String content = data.getString("content");
        content = content.trim();
        String difficulty = null;
        String musicName = null;
        PJSKMusicObject pjskMusicObject = null;
        ResponseEntity<JSONObject> mediaFileResp = null;

        try{
            Pattern pattern = Pattern.compile(RegexConstant.MUSIC_PANE_REGEX);
            Matcher matcher = pattern.matcher(content);

            if (matcher.find())
            {
                musicName = matcher.group(2);
                difficulty = matcher.group(3);
                List<PJSKMusicObject> pjskMusicObjects = pjsKMusicPaneMapper.selectByTitle("%" + musicName + "%");

                if (pjskMusicObjects.getFirst() == null)
                {
                    throw new NoSuchElementException();
                }
                else {
                    for (int i = 0; i < pjskMusicObjects.size(); i++) {
                        if (pjskMusicObjects.get(i).getLikeTitle().contains(musicName)) {
                            pjskMusicObject = pjskMusicObjects.get(i);
                            break;
                        }

                        if (i == pjskMusicObjects.size() - 1) {
                            pjskMusicObject = pjskMusicObjects.getFirst();
                        }
                    }
                    QQMediaFile qqMediaFile = QQMediaFile.builder()
                            .url(pjskUtil.getDifficultUrl(pjskMusicObject,pjskUtil.getDifficult(difficulty)))
                            .fileType(1)
                            .srvSendMsg(false)
                            .build();

                    headers = getHeader();

                    HttpEntity<QQMediaFile> qqMediaFileHttpEntity = new HttpEntity<>(qqMediaFile, headers);
                    //构造qqmedia文件的json
                    mediaFileResp = restTemplate.exchange(url + "/files", HttpMethod.POST, qqMediaFileHttpEntity, JSONObject.class);
                    String fileInfo = mediaFileResp.getBody().getString("file_info");
                    log.info("获取谱面图片成功:{}\n\n获取图片地址:{}", fileInfo,qqMediaFile.getUrl());



                    content = "\n" + pjskMusicObject.getTitle() + "\n难度" + pjskUtil.getDifficult(difficulty) ;

                    if (pjskMusicObject.getLevelMessage().contains("APD0")) {
                        throw new NoAppendException();
                    }
                }
            }
            else {
                throw new ArgsException();
            }
        }catch (ArgsException e) {
            log.info("参数错误");
            content = "参数错误，请按正确格式";

        }catch (NoAppendException e)
        {
            log.info("没有APD难度！");
            content = "\n" + pjskMusicObject.getTitle() + "没有Append难度";
        }
        catch (NoSuchElementException e)
        {
            log.error("歌曲不存在或者未添加，查询失败");
            content = "歌曲不存在或者未添加，查询失败";
        } catch (Exception e){
            log.error(e.getMessage());
            content = "未知错误请稍后尝试";
        }



        QQGroupsMsg qqGroupsMsg = null;
        if (mediaFileResp !=null) {

            qqGroupsMsg = QQGroupsMsg.builder()
                    .content(content)
                    .msgType(7)
                    .eventId("GROUP_AT_MESSAGE_CREATE")
                    .media(mediaFileResp.getBody())
                    .msgId(id)
                    .build();
        }
        else
        {
            qqGroupsMsg = QQGroupsMsg.builder()
                    .content(content)
                    .msgType(0)
                    .eventId("GROUP_AT_MESSAGE_CREATE")
                    .msgId(id)
                    .build();
        }
        HttpEntity<QQGroupsMsg> qqGroupsMsgEntity = new HttpEntity<>(qqGroupsMsg,headers);
        ResponseEntity<JSONObject> groupMsgResp = restTemplate.exchange(url + "/messages", HttpMethod.POST, qqGroupsMsgEntity, JSONObject.class);
        log.info("消息发送成功：{}", groupMsgResp.getBody());
    }

    /**
     * 世界计划查歌
     * @param data
     * @param msgSeq
     */
    @Override
    public void pjskMusicInfo(JSONObject data,Integer msgSeq)
    {
        String url = QQBotConstant.OPENAPI_URL + QQBotConstant.GROUP_SUFFIX + data.getString("group_openid");
        String id = data.getString("id");
        HttpHeaders headers = getHeader();
        String content = data.getString("content");
        content = content.trim();
        String musicName = null;
        ResponseEntity<JSONObject> mediaFileResp = null;

        try{
            Pattern pattern = Pattern.compile(RegexConstant.MUSIC_REGEX);
            Matcher matcher = pattern.matcher(content);

            if (matcher.find())
            {
                musicName = matcher.group(2);
                List<PJSKMusicObject> pjskMusicObjects = pjsKMusicPaneMapper.selectByTitle("%" + musicName + "%");
                PJSKMusicObject pjskMusicObject = null;

                if (pjskMusicObjects.getFirst() == null)
                {
                    throw new NoSuchElementException();
                }
                else {
                    for (int i = 0; i < pjskMusicObjects.size(); i++) {
                        if (pjskMusicObjects.get(i).getLikeTitle().contains(musicName)) {
                            pjskMusicObject = pjskMusicObjects.get(i);
                            break;
                        }

                        if (i == pjskMusicObjects.size() - 1) {
                            pjskMusicObject = pjskMusicObjects.getFirst();
                        }
                    }
                    QQMediaFile qqMediaFile = QQMediaFile.builder()
                            .url(pjskUtil.getPaneUrl(pjskMusicObject))
                            .fileType(1)
                            .srvSendMsg(false)
                            .build();

                    headers = getHeader();

                    HttpEntity<QQMediaFile> qqMediaFileHttpEntity = new HttpEntity<>(qqMediaFile, headers);
                    //构造qqmedia文件的json
                    mediaFileResp = restTemplate.exchange(url + "/files", HttpMethod.POST, qqMediaFileHttpEntity, JSONObject.class);
                    String fileInfo = mediaFileResp.getBody().getString("file_info");
                    log.info("获取乐曲封面成功:{}", fileInfo);



                    String levelMsg = pjskUtil.PJSKMusicLevelCutter(pjskMusicObject);


                    content = "查询成功信息如下\n" + "谱名【" + pjskMusicObject.getTitle().split("\\|")[0]
                            +"】\n中文名【" + pjskMusicObject.getTitle().split("\\|")[1]
                            + "】\n标签【" + pjskMusicObject.getLabel().split("\\|")[1]
                            + "】\n编曲【" + pjskMusicObject.getEditor()
                            + "】\n作曲【" + pjskMusicObject.getComposer()
                            + "】\n作词【" + pjskMusicObject.getLyrics()
                            + "】\n开放时间【" + pjskMusicObject.getOpeningTime()
                            + "】\nAPD状态【" + (pjskMusicObject.getLevelMessage().contains("APDCULCCN")?"无APPEND谱面":"有APPEND谱面")
                            +"】\n难度状态:\n" + levelMsg
                    ;
                }

            }else {
                throw new ArgsException();
            }
        }catch (ArgsException e) {
            content= "参数错误";
        }
        catch (NoSuchElementException e)
        {
            content = "歌曲不存在或者未添加，查询失败";
        } catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            content = "未知错误,请稍后再试";
        }

        QQGroupsMsg qqGroupsMsg = null;
        if (mediaFileResp !=null) {

            qqGroupsMsg = QQGroupsMsg.builder()
                    .content(content)
                    .msgType(7)
                    .eventId("GROUP_AT_MESSAGE_CREATE")
                    .media(mediaFileResp.getBody())
                    .msgId(id)
                    .build();
        }
        else
        {
            qqGroupsMsg = QQGroupsMsg.builder()
                    .content(content)
                    .msgType(0)
                    .eventId("GROUP_AT_MESSAGE_CREATE")
                    .msgId(id)
                    .build();
        }
        HttpEntity<QQGroupsMsg> qqGroupsMsgEntity = new HttpEntity<>(qqGroupsMsg,headers);
        ResponseEntity<JSONObject> groupMsgResp = restTemplate.exchange(url + "/messages", HttpMethod.POST, qqGroupsMsgEntity, JSONObject.class);
        log.info("消息发送成功：{}", groupMsgResp.getBody());
    }


    /**
     * 帮助菜单
     * @param data
     */
    @Override
    public void helpMenu(JSONObject data,Integer msgSeq) {
        String url = QQBotConstant.OPENAPI_URL + "/v2/groups/" + data.getString("group_openid");
        String id = data.getString("id");
        HttpHeaders headers = getHeader();

        String content = BotMsgConstant.MENU_TEMPLATE_MSG;


        QQGroupsMsg qqGroupsMsg = QQGroupsMsg.builder()
                .content(content)
                .msgType(0)
                .eventId("GROUP_AT_MESSAGE_CREATE")
                .msgId(id)
                .build();

        HttpEntity<QQGroupsMsg> qqGroupsMsgEntity = new HttpEntity<>(qqGroupsMsg,headers);
        ResponseEntity<JSONObject> groupMsgResp = restTemplate.exchange(url + "/messages", HttpMethod.POST, qqGroupsMsgEntity, JSONObject.class);
        log.info("消息发送成功：{}", groupMsgResp.getBody());
    }

    /**
     * 决斗功能
     * @param data
     * @param msgSeq
     */
    @Override
    public void honokaDuel(JSONObject data, Integer msgSeq) {
        String command = data.getString("content");

        if (command.contains("决斗菜单"))
        {
            qqBotDuelService.menu(data,msgSeq);
            return;
        }

        if (command.contains("决斗排名"))
        {
            qqBotDuelService.rankKD(data, msgSeq);
            return;
        }

        if (command.contains("决斗注册"))
        {
            qqBotDuelService.registerAccount(data,msgSeq);
            return;
        }

        if (command.contains("决斗改名"))
        {
            qqBotDuelService.rename(data,msgSeq);
            return;
        }

        if (command.contains("决斗信息"))
        {
            qqBotDuelService.information(data,msgSeq);
            return;
        }

        if (command.contains("决斗审核全部"))
        {
            if ("280ABEC05AD07F3BA29F7C55A13C7C23".equals(qqBotUtil.getOpenId(data)))
            {
                qqBotDuelService.processAll();
            }
            return;
        }

        if ("/决斗".equals(command.trim()))
        {
            //构造群iD用于匹配决斗串key
            String groupId = "[Duel:" + data.getString("group_openid") + "]";
            //通过groupId获取duelId，如果没有则说明是发起决斗
            String duelId =(String) redisTemplate.opsForValue().get(groupId);
            if (duelId != null)
            {
                qqBotDuelService.duel(data,msgSeq);
                return;
            }
        }

        Pattern duelPattern = Pattern.compile(RegexConstant.DUEL_REGEX);
        Matcher duelMatcher = duelPattern.matcher(command);

        String id = data.getString("id");
        String url = QQBotConstant.OPENAPI_URL + "/v2/groups/" + data.getString("group_openid");
        HttpHeaders headers = getHeader();



        if (duelMatcher.find()) {
           qqBotDuelService.duel(data,msgSeq);
        }else{
            QQGroupsMsg qqGroupsMsg = QQGroupsMsg.builder()
                    .content("决斗格式 :/决斗[金额],消耗一定的金额进行决斗")
                    .msgType(0)
                    .eventId("GROUP_AT_MESSAGE_CREATE")
                    .msgId(id)
                    .build();

            HttpEntity<QQGroupsMsg> qqGroupsMsgEntity = new HttpEntity<>(qqGroupsMsg,headers);
            ResponseEntity<JSONObject> groupMsgResp = restTemplate.exchange(url + "/messages", HttpMethod.POST, qqGroupsMsgEntity, JSONObject.class);
            log.info("消息发送成功：{}", groupMsgResp.getBody());
        }
    }

    /**
     * 查卡749单独为钉钉十号添加呵呵
     * @param data
     * @param msgSeq
     */
    @Override
    public void check749(JSONObject data, Integer msgSeq) {
        String url = QQBotConstant.OPENAPI_URL + QQBotConstant.GROUP_SUFFIX + data.getString("group_openid");

        // String memberOpenid = data.getJSONObject("author").getString("member_openid");

        String id = data.getString("id");

        //构造qqmedia文件的json
        QQMediaFile qqMediaFile = QQMediaFile.builder().url(BotMsgConstant.CHECK_CARD_749)
                .fileType(1)
                .srvSendMsg(false)
                .build();

        HttpHeaders headers = getHeader();

        HttpEntity<QQMediaFile> qqMediaFileHttpEntity = new HttpEntity<>(qqMediaFile, headers);

        ResponseEntity<JSONObject> mediaFileResp = restTemplate.exchange(url + "/files", HttpMethod.POST, qqMediaFileHttpEntity, JSONObject.class);
        String fileInfo = mediaFileResp.getBody().getString("file_info");
        log.info(fileInfo);

        QQGroupsMsg qqGroupsMsg = QQGroupsMsg.builder()
                .content("查询成功，结果如下")
                .msgType(7)
                .eventId("GROUP_AT_MESSAGE_CREATE")
                .media(mediaFileResp.getBody())
                .msgId(id)
                .build();

        HttpEntity<QQGroupsMsg> qqGroupsMsgEntity = new HttpEntity<>(qqGroupsMsg,headers);


        ResponseEntity<JSONObject> groupMsgResp = restTemplate.exchange(url + "/messages", HttpMethod.POST, qqGroupsMsgEntity, JSONObject.class);

        log.info("消息发送成功：{}", groupMsgResp.getBody());
    }

    /**
     * 默认回复
     * @param data
     */
    @Override
    public void defaultMessage(JSONObject data,Integer msgSeq) {
        String url = QQBotConstant.OPENAPI_URL + QQBotConstant.GROUP_SUFFIX + data.getString("group_openid");
        String id = data.getString("id");
        HttpHeaders headers = getHeader();



        String content = msgGeneratorService.defaultReply(data.getString("content"));

        if (content == null)
        {
            content = BotMsgConstant.DEFAULT_REPLY_MSG;
        }


        QQGroupsMsg qqGroupsMsg = QQGroupsMsg.builder()
                .content(content)
                .msgType(0)
                .eventId("GROUP_AT_MESSAGE_CREATE")
                .msgId(id)
                .msgSeq(msgSeq)
                .build();

        HttpEntity<QQGroupsMsg> qqGroupsMsgEntity = new HttpEntity<>(qqGroupsMsg,headers);
        ResponseEntity<JSONObject> groupMsgResp = restTemplate.exchange(url + "/messages", HttpMethod.POST, qqGroupsMsgEntity, JSONObject.class);
        log.info("消息发送成功：{}", groupMsgResp.getBody());
    }

    /**
     * @param data
     * @param msgSeq
     */
    @Override
    public void testFunction(JSONObject data, Integer msgSeq) {
        String url = QQBotConstant.OPENAPI_URL + QQBotConstant.GROUP_SUFFIX + data.getString("group_openid");

        String memberOpenid = data.getJSONObject("author").getString("member_openid");

        String id = data.getString("id");

        String testUrl = "https://www.faceroundcloud.site/game/executed/"+memberOpenid;

        //构造qqmedia文件的json
        QQMediaFile qqMediaFile = QQMediaFile.builder().url(testUrl)
                .fileType(1)
                .srvSendMsg(false)
                .build();

        HttpHeaders headers = getHeader();

        HttpEntity<QQMediaFile> qqMediaFileHttpEntity = new HttpEntity<>(qqMediaFile, headers);

        ResponseEntity<JSONObject> mediaFileResp = restTemplate.exchange(url + "/files", HttpMethod.POST, qqMediaFileHttpEntity, JSONObject.class);
        String fileInfo = mediaFileResp.getBody().getString("file_info");
        log.info(fileInfo);

        QQGroupsMsg qqGroupsMsg = QQGroupsMsg.builder()
                .content("测试成功")
                .msgType(7)
                .eventId("GROUP_AT_MESSAGE_CREATE")
                .media(mediaFileResp.getBody())
                .msgId(id)
                .build();

        HttpEntity<QQGroupsMsg> qqGroupsMsgEntity = new HttpEntity<>(qqGroupsMsg,headers);


        ResponseEntity<JSONObject> groupMsgResp = restTemplate.exchange(url + "/messages", HttpMethod.POST, qqGroupsMsgEntity, JSONObject.class);

        log.info("消息发送成功：{}", groupMsgResp.getBody());
    }



    /**
     * 获取httpHeader模板
     * @return
     */
    private HttpHeaders getHeader()
    {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type"," application/json");
        headers.set("Authorization", qqBotUtil.getAuthorization());
        return headers;
    }
}
