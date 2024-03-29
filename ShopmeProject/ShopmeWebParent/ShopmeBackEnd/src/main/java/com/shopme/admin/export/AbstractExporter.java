package com.shopme.admin.export;

import com.shopme.common.entity.User;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AbstractExporter {

    public void setResponseHeader(HttpServletResponse response, String contentType,
                                  String extension, String prefix) throws IOException {

        DateFormat dataFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String timestamp = dataFormatter.format(new Date());
        String fileName = prefix + timestamp + extension;

        response.setContentType(contentType);

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + fileName;
        response.setHeader(headerKey, headerValue);
    }
}
