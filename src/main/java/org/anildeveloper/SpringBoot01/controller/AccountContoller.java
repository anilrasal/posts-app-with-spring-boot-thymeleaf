package org.anildeveloper.SpringBoot01.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.anildeveloper.SpringBoot01.models.Account;
import org.anildeveloper.SpringBoot01.sevices.AccountService;
import org.anildeveloper.SpringBoot01.sevices.EmailService;
import org.anildeveloper.SpringBoot01.util.AppUtil;
import org.anildeveloper.SpringBoot01.util.email.EmailDetails;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
public class AccountContoller {

    @Autowired
    private AccountService accountService;

    @Value("${password.token.reset.timeout.minutes}")
    private int passwordResetTokenMinutes;

    @Value("${site.domain}")
    private String siteDomain;

    @Autowired
    private EmailService emailService;

    @GetMapping("/register")
    public String register(Model model) {
        Account account = new Account();
        model.addAttribute("account", account);
        return "account_views/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute Account account, BindingResult result) {
        if (result.hasErrors()) {
            return "account_views/register";
        }
        Optional<Account> optionalAccount= accountService.findByEmail(account.getEmail());
        if(optionalAccount.isPresent()){
            
        }
        accountService.save(account);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "account_views/login";
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String profile(Model model, Principal principal) {
        String authUser = "email";
        if (principal != null) {
            authUser = principal.getName();
        }
        Optional<Account> optionalAccount = accountService.findByEmail(authUser);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            model.addAttribute("account", account);
            model.addAttribute("photo", account.getPhoto());
            return "account_views/profile";
        } else {
            return "redirect:/?error";
        }
    }

    @PostMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String updateProfile(@Valid @ModelAttribute Account account, BindingResult result, Principal principal) {
        if (result.hasErrors()) {
            return "account_views/profile";
        }
        String authUser = "email";
        if (principal != null) {
            authUser = principal.getName();
        }
        Optional<Account> optionalAccount = accountService.findByEmail(authUser);
        if (optionalAccount.isPresent()) {
            Account account_by_id = accountService.findById(account.getId()).get();
            account_by_id.setAge(account.getAge());
            account_by_id.setDateOfBirth(account.getDateOfBirth());
            account_by_id.setEmail(account.getEmail());
            account_by_id.setFirstName(account.getFirstName());
            account_by_id.setLastName(account.getLastName());
            account_by_id.setPassword(account.getPassword());
            account_by_id.setGender(account.getGender());
            accountService.save(account_by_id);
            SecurityContextHolder.clearContext();
            return "redirect:/logout";

        } else {
            return "redirect:/?error";
        }
    }

    @GetMapping("/test")
    public String test(Model model) {
        return "test";
    }

    @PostMapping("/update_photo")
    @PreAuthorize("isAuthenticated()")
    public String updatePhoto(@RequestParam("file") MultipartFile file,
            RedirectAttributes attributes, Principal principal) {
        if (file.isEmpty()) {
            attributes.addFlashAttribute("error", "File not selected");
            return "redirect:/profile";
        } else {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            try {
                int length = 10;
                boolean useLetters = true;
                boolean userNumbers = true;
                String generatedString = RandomStringUtils.random(length, useLetters, userNumbers);
                String final_photo_name = generatedString + fileName;
                String fileLocation = AppUtil.get_upload_path(final_photo_name);

                Path path = Paths.get(fileLocation);

                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                attributes.addFlashAttribute("message", "File successfully uploaded");

                String authUser = "email";
                if (principal != null) {
                    authUser = principal.getName();
                }
                Optional<Account> optionalAccount = accountService.findByEmail(authUser);
                if (optionalAccount.isPresent()) {
                    Account account = optionalAccount.get();
                    Account account_by_id = accountService.findById(account.getId()).get();
                    String relativeFileLocation = "/assets/uploads/" + final_photo_name;
                    account_by_id.setPhoto(relativeFileLocation);
                    accountService.save(account_by_id);
                }
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                }
                return "redirect:/profile";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "redirect:/profile?error";
    }

    @GetMapping("/forgot_password")
    public String forgotPassword(Model model) {
        return "account_views/forgot_password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("email") String _email, RedirectAttributes attributes, Model model) {

        Optional<Account> optionalAccount = accountService.findByEmail(_email);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            String resetToken = UUID.randomUUID().toString();
            account.setPasswordResetToken(resetToken);
            account.setPasswordResetTokenExpiry(LocalDateTime.now().plusMinutes(passwordResetTokenMinutes));
            accountService.save(account);
            attributes.addFlashAttribute("message", "Password reset email sent");
            String resetMessage = "This is the reset password link: " + siteDomain + "change-password?token="
                    + resetToken;
            EmailDetails emailDetails = new EmailDetails(account.getEmail(), resetMessage,
                    "Reset password AnilDeveloper demo");
            if (emailService.sendSimpleEmail(emailDetails) == false) {
                attributes.addFlashAttribute("error", "Error while sending email. Contact admin!");
                return "redirect:/forgot_password";
            }

            return "redirect:/login";
        } else {
            attributes.addFlashAttribute("error", "No user found with this email");
            return "redirect:/forgot_password";
        }
    }

    @GetMapping("/change-password")
    public String changePassword(Model model, @RequestParam String token, RedirectAttributes attributes) {
        if(token.equals("")){
            attributes.addFlashAttribute("error", "Invalid token");
            return "redirect:/forgot_password";
        }
        Optional<Account> optionAccount = accountService.findByToken(token);
        if (optionAccount.isPresent()) {
            Account account = accountService.findById(optionAccount.get().getId()).get();
            model.addAttribute("account", account);
            LocalDateTime now = LocalDateTime.now();
            if (now.isAfter(optionAccount.get().getPasswordResetTokenExpiry())) {
                attributes.addFlashAttribute("error", "Token expired");
                return "redirect:/forgot_password";
            }
            return "account_views/change_password";
        }
        attributes.addFlashAttribute("error", "Invalid token");
        return "redirect:/forgot_password";
    }

    @PostMapping("/change-password")
    public String updatePassword(@ModelAttribute Account account, RedirectAttributes attributes){
        Account account_by_id = accountService.findById(account.getId()).get();
        account_by_id.setPassword(account.getPassword());
        account_by_id.setPasswordResetToken("");
        accountService.save(account_by_id);
        attributes.addFlashAttribute("message", "Password changed successfully");
        return "redirect:/login";
    }

}
