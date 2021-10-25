package com.github.bestheroz.standard.common.file;

import com.github.bestheroz.standard.common.util.FileUtils;
import com.github.bestheroz.standard.context.abstractview.AbstractDownloadView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("api/download")
public class FileDownloadController {

  @GetMapping(value = "file")
  public String downloadFile(
      @RequestParam(value = "filePath") final String filePath,
      @RequestParam(value = "fileName", required = false) final String fileName,
      final Model model) {
    model.addAttribute(AbstractDownloadView.DOWNLOAD_FILE, FileUtils.getFile(filePath));
    if (StringUtils.isNotEmpty(fileName)) {
      model.addAttribute(AbstractDownloadView.DOWNLOAD_ORI_FILE_NAME, FileUtils.getFile(fileName));
    }
    return AbstractDownloadView.VIEW_NAME;
  }
}
