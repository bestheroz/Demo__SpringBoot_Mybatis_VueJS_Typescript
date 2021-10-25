package com.github.bestheroz.standard.context.abstractview;

import com.github.bestheroz.standard.common.exception.BusinessException;
import com.github.bestheroz.standard.common.exception.ExceptionCode;
import com.github.bestheroz.standard.common.util.FileUtils;
import com.github.bestheroz.standard.common.util.FileUtils.FileType;
import com.github.bestheroz.standard.common.util.LogUtils;
import com.github.bestheroz.standard.common.util.NullUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.io.FilenameUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;

@Slf4j
public class AbstractDownloadView extends AbstractView {

  public static final String DOWNLOAD_FILE = "downloadFile";
  public static final String VIEW_NAME = "downloadView";
  public static final String DOWNLOAD_ORI_FILE_NAME = "oriDownloadFileName";

  @Override
  protected void renderMergedOutputModel(
      final Map<String, Object> model,
      final HttpServletRequest request,
      final HttpServletResponse response) {
    final File file = (File) model.get(DOWNLOAD_FILE);
    if (!NullUtils.exists(file)) {
      log.warn("{} {}", ExceptionCode.ERROR_FILE_NOT_FOUND.getMessage(), file.getAbsolutePath());
      response.setContentType("text/html;charset=utf-8");
      try (final PrintWriter pw = response.getWriter()) {
        pw.println("<script>");
        pw.println("alert('파일이 없습니다.');");
        pw.println("history.back();");
        pw.println("</script>");
        return;
      } catch (final IOException e) {
        log.warn(LogUtils.getStackTrace(e));
        throw new BusinessException(e);
      }
    }
    try (final OutputStream out = response.getOutputStream();
        final FileInputStream fis = new FileInputStream(file)) {
      log.info("file.getPath() : {}", file.getPath());
      log.info("file.getName() : {}", FilenameUtils.getName(file.getName()));

      if (FileUtils.isFileType(file, FileType.PDF)) {
        response.setContentType("application/pdf");
      } else if (FileUtils.isFileType(file, FileType.IMAGE)) {
        response.setContentType(FileUtils.getMimeType(file));
      } else {
        response.setContentType("application/download;charset=utf-8");

        response.setContentLength((int) file.length());

        String oriFileName = (String) model.get(AbstractDownloadView.DOWNLOAD_ORI_FILE_NAME);
        if (StringUtils.isEmpty(oriFileName)) {
          oriFileName =
              FileUtils.getEncodedFileName(request, FilenameUtils.getName(file.getName()));
        }
        response.setHeader(
            "Content-Disposition",
            "attachment; filename=\"" + FileUtils.getEncodedFileName(request, oriFileName) + "\";");
        response.setHeader("Content-Transfer-Encoding", "binary");
      }

      FileCopyUtils.copy(fis, out);
      out.flush();
    } catch (final IOException e) {
      log.warn(LogUtils.getStackTrace(e));
      throw new BusinessException(e);
    }
  }
}
