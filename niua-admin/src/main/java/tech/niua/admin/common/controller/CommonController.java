package tech.niua.admin.common.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tech.niua.common.config.NiuaConfig;
import tech.niua.common.utils.file.FileUploadUtils;
import tech.niua.core.annotation.ApiVersion;
import tech.niua.core.config.ServerConfig;
import tech.niua.common.model.ResultCode;
import tech.niua.common.model.ResultJson;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangzhen
 * @title: UploadController
 * @projectName niua_easy_parent
 * @description: 通用文件上传类
 * @date 2020/12/19 下午10:51
 */
@RestController
@Api(value = "通用接口", tags = {"通用接口"})
@ApiVersion(1)
@RequestMapping("/{version}/admin/common")
public class CommonController {

    @Autowired
    private ServerConfig serverConfig;

    /**
     * 通用上传请求
     */
    @PostMapping("/upload")
    public ResultJson uploadFile(MultipartFile file) throws Exception {
        try {
            // 上传文件路径
            String filePath = NiuaConfig.getUploadPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = serverConfig.getUrl() + fileName;
            Map<String, String> prams = new HashMap<String, String>();
            prams.put("fileName", fileName);
            prams.put("url", url);
            return ResultJson.ok(prams);
        } catch (Exception e) {
            return ResultJson.failure(ResultCode.BAD_REQUEST, e.getMessage());
        }
    }

}
