package com.github.bestheroz.standard.common.file;

import com.github.bestheroz.standard.common.response.ApiResult;
import com.github.bestheroz.standard.common.response.Result;
import com.github.bestheroz.standard.common.util.FileUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileDeleteController {

  @DeleteMapping(value = "/common/file/delete/fileDelete")
  ResponseEntity<ApiResult> deleteFile(
    @RequestParam("filePath") final String filePath
  ) {
    FileUtils.deleteFile(filePath);
    return Result.ok();
  }

  @DeleteMapping(value = "/common/file/delete/deleteAllFiles")
  ResponseEntity<ApiResult> deleteAllFiles(
    @RequestParam("filePath") final String filePath
  ) {
    FileUtils.deleteDirectory(filePath);
    return Result.ok();
  }
}
