package com.app.controller;

import com.app.controller.global.Global;
import com.app.model.Application;
import com.app.model.ApplicationComment;
import com.app.model.Category;
import com.app.model.User;
import com.app.model.enums.ApplicationStatus;
import com.app.model.enums.Role;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/applications")
public class ApplicationCont extends Global {

    @GetMapping
    public String applications(Model model) {
        addAttributes(model);
        User user = getUser();
        List<Application> applications;

        switch (user.getRole()) {
            case USER -> applications = user.getApplicationsClient();
            case MANAGER -> applications = user.getApplicationsManager();
            case EMPLOYEE -> applications = user.getApplicationsEmployee();
            default -> applications = new ArrayList<>();
        }

        model.addAttribute("applications", applications);
        return "applications";
    }

    @GetMapping("/filter")
    public String filter(Model model, @RequestParam ApplicationStatus status, @RequestParam(defaultValue = "0") Long categoryId) {
        model.addAttribute("status", status);
        model.addAttribute("categoryId", categoryId);
        addAttributes(model);
        User user = getUser();
        List<Application> applications;

        switch (user.getRole()) {
            case USER -> applications = user.getApplicationsClient();
            case MANAGER -> applications = user.getApplicationsManager();
            case EMPLOYEE -> applications = user.getApplicationsEmployee();
            default -> applications = new ArrayList<>();
        }

        applications = applications.stream().filter(application -> application.getStatus() == status).collect(Collectors.toList());
        if (categoryId != 0) {
            applications = applications.stream().filter(application -> application.getCategory().getId().equals(categoryId)).collect(Collectors.toList());
        }

        model.addAttribute("applications", applications);
        return "applications";
    }

    @GetMapping("/{id}")
    public String application(Model model, @PathVariable Long id) {
        addAttributes(model);
        model.addAttribute("app", applicationRepo.getReferenceById(id));
        return "application";
    }

    @GetMapping("/{id}/solved")
    public String solved(@PathVariable Long id) {
        Application application = applicationRepo.getReferenceById(id);
        application.setStatus(ApplicationStatus.SOLVED);
        applicationRepo.save(application);
        return "redirect:/applications/" + id;
    }

    @PostMapping("/{id}/comment")
    public String comment(@PathVariable Long id, @RequestParam String text) {
        applicationCommentRepo.save(new ApplicationComment(text, applicationRepo.getReferenceById(id), getUser()));
        return "redirect:/applications/" + id;
    }

    @PostMapping("/add")
    public String save(Model model,
            @RequestParam Long clientId, @RequestParam int number, @RequestParam String date,
            @RequestParam Long categoryId, @RequestParam ApplicationStatus status, @RequestParam Long managerId,
            @RequestParam String description
    ) {
        if (status == ApplicationStatus.REGISTERED && managerId == 0){
            model.addAttribute("message", "Некорректный данные");
            addAttributes(model);
            model.addAttribute("client", userRepo.getReferenceById(clientId));
            model.addAttribute("managers", userRepo.findAllByRole(Role.MANAGER));
            return "client";
        }

        Category category = categoryRepo.getReferenceById(categoryId);
        User client = userRepo.getReferenceById(clientId);
        User employee = getUser();
        User manager;

        if (managerId == 0) manager = null;
        else manager = userRepo.getReferenceById(managerId);

        Application application = applicationRepo.save(new Application(number, date, status, description, category, client, employee, manager));

        return "redirect:/applications/" + application.getId();
    }


}
