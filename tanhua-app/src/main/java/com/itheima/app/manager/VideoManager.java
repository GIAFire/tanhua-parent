package com.itheima.app.manager;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.github.tobato.fastdfs.domain.conn.FdfsWebServer;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.itheima.domain.db.UserInfo;
import com.itheima.domain.mongo.FocusUser;
import com.itheima.domain.mongo.Video;
import com.itheima.service.db.UserInfoService;
import com.itheima.service.mongo.FocusUserService;
import com.itheima.service.mongo.VideoService;
import com.itheima.util.ConstantUtil;
import com.itheima.vo.PageBeanVo;
import com.itheima.vo.VideoVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class VideoManager {

    @DubboReference
    private VideoService videoService;

    @DubboReference
    private UserInfoService userInfoService;

    @DubboReference
    private FocusUserService focusUserService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private FastFileStorageClient client;

    @Autowired
    private FdfsWebServer webServer;

    // 查询推荐视频
    public ResponseEntity findRecommendVideoVoByPage(Long userId, Integer pageNum, Integer pageSize) {
        // 1.查询视频分页
        PageBeanVo pageBeanVo = videoService.findRecommendVideoByPage(userId, pageNum, pageSize);
        // 2.封装vo
        // 2-1 声明voList
        List<VideoVo> voList = new ArrayList<>();
        // 2-2 获取videoList
        List<Video> videoList = (List<Video>) pageBeanVo.getItems();
        // 2-3 遍历videoList
        if (CollectionUtil.isNotEmpty(videoList)) {
            for (Video video : videoList) {
                // 查询userInfo
                UserInfo userInfo = userInfoService.findById(video.getUserId());
                // 创建vo
                VideoVo vo = new VideoVo();
                vo.setUserInfo(userInfo); // 先
                vo.setVideo(video); // 后
                // 添加到集合
                voList.add(vo);
            }
        }
        // 3.将voList设置到分页对象
        pageBeanVo.setItems(voList);

        // 4.返回分页对象
        return ResponseEntity.ok(pageBeanVo);
    }

    // 发布视频
    public void publishVideo(Long userId, MultipartFile videoThumbnail, MultipartFile videoFile) throws IOException {
        // 1.上传封面
        StorePath storePath1 = client.uploadFile(videoThumbnail.getInputStream(), videoThumbnail.getSize(), FileUtil.extName(videoThumbnail.getOriginalFilename()), null);
        String picUrl = webServer.getWebServerUrl() + storePath1.getFullPath();
        // 2.上传视频
        StorePath storePath2 = client.uploadFile(videoFile.getInputStream(), videoFile.getSize(), FileUtil.extName(videoFile.getOriginalFilename()), null);
        String videoUrl = webServer.getWebServerUrl() + storePath2.getFullPath();
        // 3.封装video对象
        Video video = new Video();
        video.setId(ObjectId.get());  // 视频id
        video.setCreated(System.currentTimeMillis()); // 发布时间
        video.setUserId(userId); // 发布人id
        video.setPicUrl(picUrl); // 封面
        video.setVideoUrl(videoUrl); // 视频
        //  4.调用rpc保存
        videoService.publishVideo(video);
    }


    // 关注用户
    public void saveFocusUser(Long userId, Long focusUserId) {
        // 1.封装focusUser实体
        FocusUser focusUser = new FocusUser();
        focusUser.setCreated(System.currentTimeMillis());// 关注时间
        focusUser.setUserId(userId); // 登录人id
        focusUser.setFocusUserId(focusUserId); // 视频发布人id
        // 2.调用rpc保存
        focusUserService.saveFocusUser(focusUser);
        // 3.redis存储关注标记   focus_user:{userId}_{focuUserId}
        stringRedisTemplate.opsForValue().set(StrUtil.format(ConstantUtil.FOCUS_USER, userId, focusUserId), "1");

    }

    // 取消关注
    public void removeFocusUser(Long userId, Long focusUserId) {
        // 1.调用rpc删除
        focusUserService.removeFocusUser(userId, focusUserId);
        // 2.redis移除关注标记
        stringRedisTemplate.delete(StrUtil.format(ConstantUtil.FOCUS_USER, userId, focusUserId));
    }
}
