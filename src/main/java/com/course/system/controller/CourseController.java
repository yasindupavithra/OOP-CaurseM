package com.course.system.controller;

import com.course.system.model.Course;
import com.course.system.model.OnlineCourse;
import com.course.system.model.OnsiteCourse;
import com.course.system.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    // 1. Read (Search / View List  User interfacce)
    @GetMapping
    public String listCourses(@RequestParam(required = false) String search, Model model) throws IOException {
        List<Course> courses = courseService.getAllCourses();
        
        if (search != null && !search.isEmpty()) {
            courses = courses.stream()
                    .filter(c -> c.getTitle().toLowerCase().contains(search.toLowerCase()) || 
                                 c.getCourseCode().toLowerCase().contains(search.toLowerCase()) ||
                                 c.getInstructor().toLowerCase().contains(search.toLowerCase()))
                    .collect(Collectors.toList());
        }
        
        model.addAttribute("courses", courses);
        model.addAttribute("search", search);
        return "courses/list";
    }

    // 2. Create (Add Course UI)
    @GetMapping("/add")
    public String showAddForm() {
        return "courses/add";
    }

    @PostMapping("/add")
    public String addCourse(@RequestParam String title, @RequestParam String instructor,
                            @RequestParam int credits, @RequestParam String code,
                            @RequestParam String type, @RequestParam(required = false) String room,
                            @RequestParam(required = false) String location,
                            @RequestParam(required = false) String platform,
                            @RequestParam(required = false) String link, Model model) {
        
        // Validation checks
        if (title.trim().isEmpty() || instructor.trim().isEmpty() || code.trim().isEmpty()) {
            model.addAttribute("error", "Course Title, Instructor Name, and Code are required!");
            return "courses/add";
        }
        if (credits <= 0 || credits > 6) {
            model.addAttribute("error", "Credits must be a positive integer between 1 and 6!");
            return "courses/add";
        }
        if (type.equals("ONLINE") && (platform.trim().isEmpty() || link.trim().isEmpty())) {
            model.addAttribute("error", "Platform and Meeting Link are required for Online Courses!");
            return "courses/add";
        }
        if (type.equals("ONSITE") && (room.trim().isEmpty() || location.trim().isEmpty())) {
            model.addAttribute("error", "Room Number and Campus Location are required for On-site Courses!");
            return "courses/add";
        }

        String id = "crs_" + UUID.randomUUID().toString().substring(0, 5);
        Course course;
        if (type.equals("ONLINE")) {
            course = new OnlineCourse(id, title, instructor, credits, code, true, platform, link);
        } else {
            course = new OnsiteCourse(id, title, instructor, credits, code, true, room, location);
        }
        
        try {
            courseService.addCourse(course);
            return "redirect:/courses";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "courses/add";
        } catch (IOException e) {
            model.addAttribute("error", "Failed to write course to flat file!");
            return "courses/add";
        }
    }

    // 3. Update (Edit Course UI)
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) throws IOException {
        courseService.getCourseById(id).ifPresent(c -> model.addAttribute("course", c));
        return "courses/edit";
    }

    @PostMapping("/edit")
    public String editCourse(@RequestParam String id, @RequestParam String title,
                             @RequestParam String instructor, @RequestParam int credits,
                             @RequestParam String code, @RequestParam String type,
                             @RequestParam(required = false) String room, @RequestParam(required = false) String location,
                             @RequestParam(required = false) String platform, @RequestParam(required = false) String link,
                             @RequestParam(required = false) boolean openForRegistration, Model model) throws IOException {
        
        // Validation checks
        if (title.trim().isEmpty() || instructor.trim().isEmpty() || code.trim().isEmpty()) {
            model.addAttribute("error", "All text fields are required!");
            courseService.getCourseById(id).ifPresent(c -> model.addAttribute("course", c));
            return "courses/edit";
        }
        if (credits <= 0 || credits > 6) {
            model.addAttribute("error", "Credits must be a positive integer between 1 and 6!");
            courseService.getCourseById(id).ifPresent(c -> model.addAttribute("course", c));
            return "courses/edit";
        }

        Course updated;
        if (type.equals("ONLINE")) {
            updated = new OnlineCourse(id, title, instructor, credits, code, openForRegistration, platform, link);
        } else {
            updated = new OnsiteCourse(id, title, instructor, credits, code, openForRegistration, room, location);
        }

        try {
            courseService.updateCourse(updated);
            return "redirect:/courses";
        } catch (IOException e) {
            model.addAttribute("error", "Failed to update record!");
            model.addAttribute("course", updated);
            return "courses/edit";
        }
    }

    // 4. Delete (Remove UI/Page with Confirmation)
    @GetMapping("/delete/{id}")
    public String showDeleteConfirmation(@PathVariable String id, Model model) throws IOException {
        courseService.getCourseById(id).ifPresent(c -> model.addAttribute("course", c));
        return "courses/delete";
    }

    @PostMapping("/delete")
    public String deleteCourse(@RequestParam String id) throws IOException {
        courseService.deleteCourse(id);
        return "redirect:/courses";
    }
}
