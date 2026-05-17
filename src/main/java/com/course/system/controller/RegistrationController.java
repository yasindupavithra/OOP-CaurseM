package com.course.system.controller;

import com.course.system.model.Course;
import com.course.system.model.Registration;
import com.course.system.model.User;
import com.course.system.service.CourseService;
import com.course.system.service.RegistrationService;
import com.course.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/registrations")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private UserService userService;

    // 1. Read (Search / View List UI)
    @GetMapping
    public String listRegistrations(@RequestParam(required = false) String search, Model model) throws IOException {
        List<Registration> list = registrationService.getAllRegistrations();

        if (search != null && !search.isEmpty()) {
            list = list.stream()
                    .filter(r -> r.getStudentId().toLowerCase().contains(search.toLowerCase()) ||
                                 r.getCourseId().toLowerCase().contains(search.toLowerCase()) ||
                                 r.getStatus().toLowerCase().contains(search.toLowerCase()))
                    .collect(Collectors.toList());
        }

        model.addAttribute("registrations", list);
        model.addAttribute("search", search);
        return "registrations/list";
    }

    // 2. Create (Add/Register UI)
    @GetMapping("/register")
    public String showRegisterForm(Model model) throws IOException {
        List<Course> openCourses = courseService.getAllCourses().stream()
                .filter(Course::isOpenForRegistration)
                .collect(Collectors.toList());
        List<User> students = userService.getAllUsers().stream()
                .filter(u -> u.getUserType().equals("STUDENT"))
                .collect(Collectors.toList());
        
        model.addAttribute("courses", openCourses);
        model.addAttribute("students", students);
        return "registrations/register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String studentId, @RequestParam String courseId,
                           @RequestParam String studentType, Model model) throws IOException {
        
        List<Course> openCourses = courseService.getAllCourses().stream()
                .filter(Course::isOpenForRegistration)
                .collect(Collectors.toList());
        List<User> students = userService.getAllUsers().stream()
                .filter(u -> u.getUserType().equals("STUDENT"))
                .collect(Collectors.toList());

        String id = "reg_" + UUID.randomUUID().toString().substring(0, 5);
        Registration r = new Registration(id, studentId, courseId, LocalDate.now(), "ENROLLED");
        r.calculateFee(studentType);

        try {
            registrationService.addRegistration(r);
            return "redirect:/registrations";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("courses", openCourses);
            model.addAttribute("students", students);
            return "registrations/register";
        }
    }

    // 3. Update (Edit UI)
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) throws IOException {
        registrationService.getRegistrationById(id).ifPresent(r -> model.addAttribute("registration", r));
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("students", userService.getAllUsers().stream().filter(u -> u.getUserType().equals("STUDENT")).collect(Collectors.toList()));
        return "registrations/edit";
    }

    @PostMapping("/edit")
    public String editRegistration(@RequestParam String id, @RequestParam String studentId,
                                   @RequestParam String courseId, @RequestParam String status,
                                   @RequestParam String studentType, @RequestParam double registrationFee, Model model) throws IOException {
        
        Registration updated = new Registration(id, studentId, courseId, LocalDate.now(), status);
        updated.setRegistrationFee(registrationFee);

        try {
            registrationService.updateRegistration(updated);
            return "redirect:/registrations";
        } catch (IOException e) {
            model.addAttribute("error", "Failed to update enrollment record!");
            model.addAttribute("registration", updated);
            return "registrations/edit";
        }
    }

    // 4. Delete (Remove UI/Page with Confirmation)
    @GetMapping("/delete/{id}")
    public String showDeleteConfirmation(@PathVariable String id, Model model) throws IOException {
        registrationService.getRegistrationById(id).ifPresent(r -> model.addAttribute("registration", r));
        return "registrations/delete";
    }

    @PostMapping("/delete")
    public String deleteRegistration(@RequestParam String id) throws IOException {
        registrationService.deleteRegistration(id);
        return "redirect:/registrations";
    }
}
