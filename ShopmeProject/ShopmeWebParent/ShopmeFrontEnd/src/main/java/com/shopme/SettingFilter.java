package com.shopme;

import com.shopme.common.entity.Setting;
import com.shopme.service.SettingService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class SettingFilter implements Filter {

    @Autowired
    private SettingService settingService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String url = request.getRequestURL().toString();

        if(url.endsWith(".css") || url.endsWith(".js") || url.endsWith(".png")
                || url.endsWith(".jpg") ){
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        List<Setting> generalSettings = settingService.getGeneralSettings();

        generalSettings.forEach(setting -> {
            System.out.println(setting);
            servletRequest.setAttribute(setting.getKey(), setting.getValue());
        });

        filterChain.doFilter(servletRequest, servletResponse);


    }
}
