package com.github.bestheroz.standard.common.file;

import com.github.bestheroz.standard.common.response.ApiResult;
import com.github.bestheroz.standard.common.response.Result;
import com.github.bestheroz.standard.common.util.FileUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/delete/file")
public class FileDeleteController {
  @DeleteMapping(value = "{fileName}")
  ResponseEntity<ApiResult> deleteFile(@PathVariable("fileName") final String fileName) {
    FileUtils.deleteFile("uploaded/" + fileName);
    return Result.ok();
  }
}
