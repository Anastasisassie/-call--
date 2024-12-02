package com.app.controller.global;

import com.app.model.User;
import com.app.model.enums.ApplicationStatus;
import com.app.model.enums.Role;
import com.app.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Global {

    @Autowired
    protected UserRepo userRepo;
    @Autowired
    protected CategoryRepo categoryRepo;
    @Autowired
    protected ApplicationRepo applicationRepo;
    @Autowired
    protected ApplicationCommentRepo applicationCommentRepo;

    protected String uploadImg = uploadPath + "/img/";

    public static String uploadPath = getUploadPath();

    private static String getUploadPath() {
        StringBuilder dir = new StringBuilder(System.getProperty("user.dir"));
        for (int j = 0; j < dir.length(); j++) {
            if (dir.charAt(j) == '\\') {
                dir.setCharAt(j, '/');
            }
        }

        dir.append("/src/main/resources");

        return dir.toString();
    }

    public static String saveFile(MultipartFile file, String path) throws IOException {
        if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) {
            String uuidFile = UUID.randomUUID().toString();
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdir();
            String result = path + "/" + uuidFile + "_" + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/img/" + result));
            return "/img/" + result;
        } else throw new IOException();
    }

    public static float round(float value) {
        long factor = (long) Math.pow(10, 2);
        value = value * factor;
        long tmp = Math.round(value);
        return (float) tmp / factor;
    }

    private static String getDatetime() {
        return LocalDateTime.now().toString();
    }

    public static String getDateNow() {
        return getDatetime().substring(0, 10);
    }

    public static String getTimeNow() {
        return getDatetime().substring(11, 19);
    }

    public static String getDateAndTimeNow() {
        return getDateNow() + " " + getTimeNow();
    }

    protected User getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((!(auth instanceof AnonymousAuthenticationToken)) && auth != null) {
            UserDetails userDetail = (UserDetails) auth.getPrincipal();
            return userRepo.findByUsername(userDetail.getUsername());
        }
        return null;
    }

    protected String getRole() {
        User user = getUser();
        if (user == null) return "NOT";
        return user.getRole().toString();
    }

    protected void addAttributes(Model model) {
        model.addAttribute("role", getRole());
        model.addAttribute("user", getUser());

        model.addAttribute("roles", Role.values());
        model.addAttribute("categories", categoryRepo.findAll(Sort.by(Sort.Direction.DESC,"id")));
        model.addAttribute("applicationStatuses", ApplicationStatus.values());
    }

}
