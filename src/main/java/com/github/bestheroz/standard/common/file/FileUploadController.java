package com.github.bestheroz.standard.common.file;

import com.github.bestheroz.standard.common.response.ApiResult;
import com.github.bestheroz.standard.common.response.Result;
import com.github.bestheroz.standard.common.util.FileUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@RestController
public class FileUploadController {
    @PostMapping(value = "/common/file/upload/fileUpload")
    ResponseEntity<ApiResult> uploadFile(@RequestParam("file") final MultipartFile multipartFile, final String targetDirPath) {
        FileUtils.uploadFile(multipartFile, targetDirPath);
        return Result.ok();
    }

    @PostMapping(value = "/common/file/upload/uploadAllFiles")
    ResponseEntity<ApiResult> uploadAllFiles(final MultipartHttpServletRequest mRequest, final String targetDirPath) {
        FileUtils.uploadAllFiles(mRequest, targetDirPath);
        return Result.ok();
    }
}
