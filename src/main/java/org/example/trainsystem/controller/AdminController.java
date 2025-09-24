package org.example.trainsystem.controller;

import org.example.trainsystem.entity.User;
import org.example.trainsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    // ✅ View all users
    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/users"; // maps to admin/users.html
    }

    // ✅ Delete user
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") int userId) {
        userService.deleteUser(userId);
        return "redirect:/admin/users";
    }

    // ✅ Edit user (GET form)
    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable("id") int userId, Model model) {
        User user = userService.getUserById(userId);
        model.addAttribute("user", user);
        return "admin/edit-user"; // maps to admin/edit-user.html
    }

    // ✅ Save edited user
    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") User user) {
        userService.updateUser(user);
        return "redirect:/admin/users";
    }
}
