package com.course.system.controller;

import com.course.system.model.Instructor;
import com.course.system.model.PermanentInstructor;
import com.course.system.model.VisitingInstructor;
import com.course.system.service.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/instructors")
public class InstructorController {

    @Autowired
    private InstructorService instructorService;

    // 1. Read (Search / View List UI)
    @GetMapping
    public String listInstructors(@RequestParam(required = false) String search, Model model) throws IOException {
        List<Instructor> instructors = instructorService.getAllInstructors();

        if (search != null && !search.isEmpty()) {
            instructors = instructors.stream()
                    .filter(ins -> ins.getName().toLowerCase().contains(search.toLowerCase()) ||
                                   ins.getDepartment().toLowerCase().contains(search.toLowerCase()) ||
                                   ins.getSpecialization().toLowerCase().contains(search.toLowerCase()))
                    .collect(Collectors.toList());
        }

        model.addAttribute("instructors", instructors);
        model.addAttribute("search", search);
        return "instructors/list";
    }

    // 2. Create (AddInstructor UI)
    @GetMapping("/add")
    public String showAddForm() {
        return "instructors/add";
    }

    @PostMapping("/add")
    public String addInstructor(@RequestParam String name, @RequestParam String email,
                                @RequestParam String department, @RequestParam String specialization,
                                @RequestParam String type, @RequestParam(required = false) Double monthlySalary,
                                @RequestParam(required = false) Double allowance,
                                @RequestParam(required = false) Double hourlyRate,
                                @RequestParam(required = false) Double hoursTaught, Model model) {
        // Validations
        if (name.trim().isEmpty() || email.trim().isEmpty() || department.trim().isEmpty() || specialization.trim().isEmpty()) {
            model.addAttribute("error", "All text fields are required!");
            return "instructors/add";
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            model.addAttribute("error", "Invalid email format!");
            return "instructors/add";
        }

        String id = "ins_" + UUID.randomUUID().toString().substring(0, 5);
        Instructor instructor;

        if (type.equals("PERMANENT")) {
            if (monthlySalary == null || allowance == null || monthlySalary <= 0 || allowance < 0) {
                model.addAttribute("error", "Please provide positive salary and allowance details!");
                return "instructors/add";
            }
            instructor = new PermanentInstructor(id, name, email, department, specialization, monthlySalary, allowance);
        } else {
            if (hourlyRate == null || hoursTaught == null || hourlyRate <= 0 || hoursTaught < 0) {
                model.addAttribute("error", "Please provide positive hourly rate and teaching hours!");
                return "instructors/add";
            }
            instructor = new VisitingInstructor(id, name, email, department, specialization, hourlyRate, hoursTaught);
        }

        try {
            instructorService.addInstructor(instructor);
            return "redirect:/instructors";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "instructors/add";
        } catch (IOException e) {
            model.addAttribute("error", "Failed to save instructor to file database!");
            return "instructors/add";
        }
    }

    // 3. Update (Edit Instructor UI)
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) throws IOException {
        instructorService.getInstructorById(id).ifPresent(ins -> model.addAttribute("instructor", ins));
        return "instructors/edit";
    }

    @PostMapping("/edit")
    public String editInstructor(@RequestParam String id, @RequestParam String name,
                                 @RequestParam String email, @RequestParam String department,
                                 @RequestParam String specialization, @RequestParam String type,
                                 @RequestParam(required = false) Double monthlySalary,
                                 @RequestParam(required = false) Double allowance,
                                 @RequestParam(required = false) Double hourlyRate,
                                 @RequestParam(required = false) Double hoursTaught, Model model) throws IOException {
        // Validations
        if (name.trim().isEmpty() || email.trim().isEmpty() || department.trim().isEmpty() || specialization.trim().isEmpty()) {
            model.addAttribute("error", "All fields are required!");
            instructorService.getInstructorById(id).ifPresent(ins -> model.addAttribute("instructor", ins));
            return "instructors/edit";
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            model.addAttribute("error", "Invalid email format!");
            instructorService.getInstructorById(id).ifPresent(ins -> model.addAttribute("instructor", ins));
            return "instructors/edit";
        }

        Instructor updated;
        if (type.equals("PERMANENT")) {
            updated = new PermanentInstructor(id, name, email, department, specialization, monthlySalary, allowance);
        } else {
            updated = new VisitingInstructor(id, name, email, department, specialization, hourlyRate, hoursTaught);
        }

        try {
            instructorService.updateInstructor(updated);
            return "redirect:/instructors";
        } catch (IOException e) {
            model.addAttribute("error", "Failed to update record!");
            model.addAttribute("instructor", updated);
            return "instructors/edit";
        }
    }

    // 4. Delete (Remove UI/Page with Confirmation)
    @GetMapping("/delete/{id}")
    public String showDeleteConfirmation(@PathVariable String id, Model model) throws IOException {
        instructorService.getInstructorById(id).ifPresent(ins -> model.addAttribute("instructor", ins));
        return "instructors/delete";
    }

    @PostMapping("/delete")
    public String deleteInstructor(@RequestParam String id) throws IOException {
        instructorService.deleteInstructor(id);
        return "redirect:/instructors";
    }
}
