package com.app.controller;

import com.app.controller.global.Global;
import com.app.model.Category;
import com.app.model.User;
import com.app.model.enums.ApplicationStatus;
import com.app.model.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
@RequestMapping("/stats")
@RequiredArgsConstructor
public class StatsCont extends Global {

    @GetMapping
    public String Statistics(Model model) {
        addAttributes(model);

        Map<String, List<?>> res = new HashMap<>();

        List<ApplicationStatus> applicationStatuses = List.of(ApplicationStatus.values());

        List<String> applicationStatusesNames = new ArrayList<>();
        List<Integer> applicationStatusesValues = new ArrayList<>();

        for (ApplicationStatus i : applicationStatuses) {
            applicationStatusesNames.add(i.getName());
            applicationStatusesValues.add(applicationRepo.findAllByStatus(i).size());
        }

        res.put("applicationStatusesNames", applicationStatusesNames);
        res.put("applicationStatusesValues", applicationStatusesValues);


        List<Category> categories = categoryRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));

        List<String> categoriesNames = new ArrayList<>();
        List<Integer> categoriesValues = new ArrayList<>();

        for (Category i : categories) {
            categoriesNames.add(i.getName());
            categoriesValues.add(applicationRepo.findAllByCategory_Id(i.getId()).size());
        }

        res.put("categoriesNames", categoriesNames);
        res.put("categoriesValues", categoriesValues);


        List<User> managers = userRepo.findAllByRole(Role.MANAGER);

        managers.sort(Comparator.comparing(User::getApplicationsManagerSolvedCount));
        Collections.reverse(managers);

        List<String> managersNames = new ArrayList<>();
        List<Integer> managersValues = new ArrayList<>();

        for (int i = 0; i < managers.size(); i++) {
            managersNames.add(managers.get(i).getFio());
            managersValues.add(managers.get(i).getApplicationsManagerSolvedCount());
        }

        res.put("managersNames", managersNames);
        res.put("managersValues", managersValues);


        List<User> employees = userRepo.findAllByRole(Role.EMPLOYEE);

        employees.sort(Comparator.comparing(User::getApplicationsEmployeeSolvedCount));
        Collections.reverse(employees);

        List<String> employeesNames = new ArrayList<>();
        List<Integer> employeesValues = new ArrayList<>();

        for (int i = 0; i < employees.size(); i++) {
            employeesNames.add(employees.get(i).getFio());
            employeesValues.add(employees.get(i).getApplicationsEmployeeSolvedCount());
        }

        res.put("employeesNames", employeesNames);
        res.put("employeesValues", employeesValues);

        model.addAttribute("res", res);

        return "stats";
    }
}
