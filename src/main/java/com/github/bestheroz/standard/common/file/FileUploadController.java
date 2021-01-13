package com.github.bestheroz.standard.common.file;

import com.github.bestheroz.standard.common.response.ApiResult;
import com.github.bestheroz.standard.common.response.Result;
import com.github.bestheroz.standard.common.util.FileUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@RestController
@RequestMapping("api/upload")
public class FileUploadController {
  @PostMapping(value = "/file")
  ResponseEntity<ApiResult> uploadFile(@RequestParam("file") final MultipartFile multipartFile) {
    return Result.ok(FileUtils.uploadFile(multipartFile, "uploaded/"));
  }

  @PostMapping(value = "files")
  ResponseEntity<ApiResult> uploadFiles(final MultipartHttpServletRequest mRequest) {
    FileUtils.uploadAllFiles(mRequest, "uploaded/");
    return Result.ok();
  }
}
