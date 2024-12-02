package com.app.controller;

import com.app.controller.global.Global;
import com.app.model.User;
import com.app.model.enums.Role;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/clients")
public class ClientCont extends Global {

    @GetMapping
    public String clients(Model model) {
        addAttributes(model);
        List<User> clients = userRepo.findAllByRole(Role.USER);
        clients.sort(Comparator.comparing(User::getId));
        Collections.reverse(clients);
        model.addAttribute("clients", clients);
        return "clients";
    }

    @GetMapping("/filter")
    public String filter(Model model, @RequestParam(defaultValue = "") String fio) {
        model.addAttribute("fio", fio);
        addAttributes(model);
        List<User> clients = userRepo.findAllByRole(Role.USER);
        clients = clients.stream().filter(user -> user.getFio().toLowerCase().contains(fio.toLowerCase())).collect(Collectors.toList());
        clients.sort(Comparator.comparing(User::getId));
        Collections.reverse(clients);
        model.addAttribute("clients", clients);
        return "clients";
    }


    @GetMapping("/{id}")
    public String client(Model model, @PathVariable Long id) {
        addAttributes(model);
        model.addAttribute("client", userRepo.getReferenceById(id));
        model.addAttribute("managers", userRepo.findAllByRole(Role.MANAGER));
        return "client";
    }

}
