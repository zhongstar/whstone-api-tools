package com.whstone.utils.file;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * Created by weijun on 2017/8/9.
 */
public class FileUploadUtil {


    public static boolean uploadFile(HttpServletRequest request, MultipartFile file) {
        System.out.println("开始");

        System.out.println(request.getSession().getServletContext().getRealPath("upload"));
        String path = "C:\\files";
        String fileName = file.getOriginalFilename();
        System.out.println(path);
        File targetFile = new File(path, fileName);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        // 保存
        try {
            file.transferTo(targetFile);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

}
