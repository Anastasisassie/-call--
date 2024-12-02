package com.app.controller;

import com.app.controller.global.Global;
import com.app.model.Category;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/categories")
public class CategoryCont extends Global {

    @GetMapping
    public String categories(Model model) {
        addAttributes(model);
        return "categories";
    }

    @PostMapping("/add")
    public String add(@RequestParam String name) {
        categoryRepo.save(new Category(name));
        return "redirect:/categories";
    }

    @PostMapping("/{id}/update")
    public String update(@RequestParam String name, @PathVariable Long id) {
        Category category = categoryRepo.getReferenceById(id);
        category.update(name);
        categoryRepo.save(category);
        return "redirect:/categories";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        categoryRepo.deleteById(id);
        return "redirect:/categories";
    }

}
