package com.vibeapp.vibe.controllers;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.vibeapp.vibe.models.EmailService;
import com.vibeapp.vibe.models.EmailServiceImpl;
import com.vibeapp.vibe.models.PasswordResetToken;
import com.vibeapp.vibe.models.PasswordResetTokenRepository;
import com.vibeapp.vibe.models.User;
import com.vibeapp.vibe.models.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@Controller
public class PasswordResetTokenController {
    @Autowired 
    UserRepository userRepo;

    @Autowired
    PasswordResetTokenRepository tokenRepo;

    @Autowired
    EmailService emailService;

    @PostMapping("/users/forgotpassword")
        public String resetPassword(HttpServletRequest request, @RequestParam String email) {
            User user = userRepo.findByEmail(email);
            if (user==null){
                return "users/findEmailFailed";
                // return "users/loginFailed"; //replace later
            }

            String token = UUID.randomUUID().toString();
            tokenRepo.save(new PasswordResetToken(email, token));
            
            String bodyMessage = "Your token is " + token;
            String subject = "Vibe Music Reset Password Token";
            emailService.sendSimpleMessage("vibemusicwebsite@gmail.com", subject, bodyMessage);
            
            //return "users/editPassword";
            return "users/editPassword";
        }
        
    @PostMapping("/users/tokenrequest")
        public String changePassword(@RequestParam String token, @RequestParam String password) {
            PasswordResetToken userToken = tokenRepo.findByToken(token);
            if (userToken==null){
                return "users/editPassword";
            }
            User user = userRepo.findByEmail(userToken.getEmail());
            user.setPassword(password);
            userRepo.save(user);

            return "users/login";
        }


    }


