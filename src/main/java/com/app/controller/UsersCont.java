package com.app.controller;

import com.app.controller.global.Global;
import com.app.model.User;
import com.app.model.enums.Role;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UsersCont extends Global {

    @GetMapping
    public String users(Model model) {
        addAttributes(model);
        model.addAttribute("users", userRepo.findAll(Sort.by(Sort.Direction.DESC, "id")));
        return "users";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id, @RequestParam Role role) {
        User user = userRepo.getReferenceById(id);
        user.setRole(role);
        userRepo.save(user);
        return "redirect:/users";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        userRepo.deleteById(id);
        return "redirect:/users";
    }
}
