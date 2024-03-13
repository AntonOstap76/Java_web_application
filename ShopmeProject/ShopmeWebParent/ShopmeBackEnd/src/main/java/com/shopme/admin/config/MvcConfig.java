package com.shopme.admin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        exposeDirectory("user-photos", registry);
//        String dirName = "user-photos";
//       Path userPhotosDir = Paths.get(dirName);
//
//       String userPhotosPath = userPhotosDir.toFile().getAbsolutePath();
//
//       registry.addResourceHandler("/"+dirName+"/**")
//               .addResourceLocations("file:/"+userPhotosPath+"/");


        exposeDirectory("../category-images", registry);
//
//        String categoryImagesDirName = "../category-images";
//        Path categoryImagesDir = Paths.get(categoryImagesDirName);
//
//        String categoryImagesPath = categoryImagesDir.toFile().getAbsolutePath();
//
//        registry.addResourceHandler("/category-images/**")
//                .addResourceLocations("file:/"+categoryImagesPath+"/");


        exposeDirectory("../brands-logos", registry);
//
//        String brandLogosDirName="../brands-logos";
//        Path brandLogosDir = Paths.get(brandLogosDirName);
//
//        String brandLogosPath = brandLogosDir.toFile().getAbsolutePath();
//
//        registry.addResourceHandler("/brands-logos/**")
//                .addResourceLocations("file:/"+brandLogosPath+"/");

        exposeDirectory("../product-images", registry);
        exposeDirectory("../site-logo", registry);

    }

    private void exposeDirectory(String pathPattern, ResourceHandlerRegistry registry ) {
        Path path = Paths.get(pathPattern);
        String absolutePath = path.toFile().getAbsolutePath();

        String logicalPath = pathPattern.replace("../", "")+"/**";

        registry.addResourceHandler(logicalPath)
                .addResourceLocations("file:/"+absolutePath+"/");
    }
}
