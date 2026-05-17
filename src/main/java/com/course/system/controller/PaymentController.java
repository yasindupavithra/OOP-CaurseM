package com.course.system.controller;

import com.course.system.model.BankTransferPayment;
import com.course.system.model.CardPayment;
import com.course.system.model.Payment;
import com.course.system.model.Registration;
import com.course.system.service.PaymentService;
import com.course.system.service.RegistrationService;
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
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private RegistrationService registrationService;

    // 1. Read (Search / View List UI)
    @GetMapping
    public String listPayments(@RequestParam(required = false) String search, Model model) throws IOException {
        List<Payment> payments = paymentService.getAllPayments();

        if (search != null && !search.isEmpty()) {
            payments = payments.stream()
                    .filter(p -> p.getRegistrationId().toLowerCase().contains(search.toLowerCase()) ||
                                 p.getId().toLowerCase().contains(search.toLowerCase()) ||
                                 p.getPaymentMethod().toLowerCase().contains(search.toLowerCase()))
                    .collect(Collectors.toList());
        }

        model.addAttribute("payments", payments);
        model.addAttribute("search", search);
        return "payments/list";
    }

    // 2. Create (Add Payment UI)
    @GetMapping("/add")
    public String showAddForm(Model model) throws IOException {
        List<Registration> registrations = registrationService.getAllRegistrations().stream()
                .filter(r -> r.getStatus().equals("ENROLLED"))
                .collect(Collectors.toList());
        model.addAttribute("registrations", registrations);
        return "payments/add";
    }

    @PostMapping("/add")
    public String addPayment(@RequestParam String registrationId, @RequestParam double amount,
                             @RequestParam String method, @RequestParam(required = false) String cardNumber,
                             @RequestParam(required = false) String cardType,
                             @RequestParam(required = false) String bankName,
                             @RequestParam(required = false) String referenceNumber, Model model) throws IOException {
        
        List<Registration> registrations = registrationService.getAllRegistrations();

        // Validations
        if (amount <= 0) {
            model.addAttribute("error", "Payment amount must be greater than zero!");
            model.addAttribute("registrations", registrations);
            return "payments/add";
        }

        String id = "pay_" + UUID.randomUUID().toString().substring(0, 5);
        Payment payment;

        if (method.equals("CARD")) {
            if (cardNumber.trim().isEmpty() || cardType.trim().isEmpty()) {
                model.addAttribute("error", "Card details cannot be empty!");
                model.addAttribute("registrations", registrations);
                return "payments/add";
            }
            if (!cardNumber.matches("^\\d{16}$")) {
                model.addAttribute("error", "Card number must be exactly 16 digits!");
                model.addAttribute("registrations", registrations);
                return "payments/add";
            }
            payment = new CardPayment(id, registrationId, amount, LocalDate.now(), "PAID", cardNumber, cardType);
        } else {
            if (bankName.trim().isEmpty() || referenceNumber.trim().isEmpty()) {
                model.addAttribute("error", "Bank transfer details cannot be empty!");
                model.addAttribute("registrations", registrations);
                return "payments/add";
            }
            payment = new BankTransferPayment(id, registrationId, amount, LocalDate.now(), "PAID", bankName, referenceNumber);
        }

        try {
            paymentService.addPayment(payment);
            return "redirect:/payments";
        } catch (IOException e) {
            model.addAttribute("error", "Failed to save payment record!");
            model.addAttribute("registrations", registrations);
            return "payments/add";
        }
    }

    // 3. Update (Edit Payment UI)
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) throws IOException {
        paymentService.getPaymentById(id).ifPresent(p -> model.addAttribute("payment", p));
        return "payments/edit";
    }

    @PostMapping("/edit")
    public String editPayment(@RequestParam String id, @RequestParam String registrationId,
                              @RequestParam double amount, @RequestParam String status,
                              @RequestParam String method, @RequestParam(required = false) String cardNumber,
                              @RequestParam(required = false) String cardType,
                              @RequestParam(required = false) String bankName,
                              @RequestParam(required = false) String referenceNumber, Model model) throws IOException {
        
        if (amount <= 0) {
            model.addAttribute("error", "Payment amount must be greater than zero!");
            paymentService.getPaymentById(id).ifPresent(p -> model.addAttribute("payment", p));
            return "payments/edit";
        }

        Payment updated;
        if (method.equals("CARD")) {
            updated = new CardPayment(id, registrationId, amount, LocalDate.now(), status, cardNumber, cardType);
        } else {
            updated = new BankTransferPayment(id, registrationId, amount, LocalDate.now(), status, bankName, referenceNumber);
        }

        try {
            paymentService.updatePayment(updated);
            return "redirect:/payments";
        } catch (IOException e) {
            model.addAttribute("error", "Failed to update payment record!");
            model.addAttribute("payment", updated);
            return "payments/edit";
        }
    }

    // 4. Delete (Remove UI/Page with Confirmation)
    @GetMapping("/delete/{id}")
    public String showDeleteConfirmation(@PathVariable String id, Model model) throws IOException {
        paymentService.getPaymentById(id).ifPresent(p -> model.addAttribute("payment", p));
        return "payments/delete";
    }

    @PostMapping("/delete")
    public String deletePayment(@RequestParam String id) throws IOException {
        paymentService.deletePayment(id);
        return "redirect:/payments";
    }
}
