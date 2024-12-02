package com.app.controller;

import com.app.controller.global.Global;
import com.app.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/profile")
public class ProfileCont extends Global {

    @GetMapping
    public String profile(Model model) {
        addAttributes(model);
        return "profile";
    }

    @PostMapping("/update")
    public String update(@RequestParam String fio, @RequestParam String tel) {
        User user = getUser();
        user.setFio(fio);
        user.setTel(tel);
        userRepo.save(user);
        return "redirect:/profile";
    }

    @PostMapping("/update/img")
    public String updateImg(Model model, @RequestParam MultipartFile img) {
        User user = getUser();
        try {
            user.setImg(saveFile(img, "user"));
        } catch (IOException e) {
            model.addAttribute("message", "Некорректное изображение");
            addAttributes(model);
            return "profile";
        }
        userRepo.save(user);
        return "redirect:/profile";
    }


}
