package com.course.system.controller;

import com.course.system.service.CourseService;
import com.course.system.service.InstructorService;
import com.course.system.service.PaymentService;
import com.course.system.service.RegistrationService;
import com.course.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class MainController {

    @Autowired
    private UserService userService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private InstructorService instructorService;
    @Autowired
    private PaymentService paymentService;

    @GetMapping("/")
    public String index(Model model) throws IOException {
        long totalStudents = userService.getAllUsers().stream()
                .filter(u -> u.getUserType().equals("STUDENT")).count();
        long totalCourses = courseService.getAllCourses().size();
        long totalRegistrations = registrationService.getAllRegistrations().size();
        long totalInstructors = instructorService.getAllInstructors().size();
        
        // Sum total amount paid from payments file database
        double totalRevenueCollected = paymentService.getAllPayments().stream()
                .filter(p -> p.getStatus().equals("PAID"))
                .mapToDouble(p -> p.getAmount())
                .sum();

        model.addAttribute("totalStudents", totalStudents);
        model.addAttribute("totalCourses", totalCourses);
        model.addAttribute("totalRegistrations", totalRegistrations);
        model.addAttribute("totalInstructors", totalInstructors);
        model.addAttribute("totalRevenueCollected", totalRevenueCollected);

        return "index";
    }
}
