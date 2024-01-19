package com.thesis.ecommerceweb.service;

import com.thesis.ecommerceweb.dto.UserDTO;
import com.thesis.ecommerceweb.model.User;
import com.thesis.ecommerceweb.repository.UserRepository;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;


    @Override
    public User updateUser(UserDTO userDTO) {
        User existingUser = userRepository.findUserByUsername(userDTO.getUsername());
        existingUser.setName(userDTO.getName());
        if (!userDTO.getPassword().equals(existingUser.getPassword())) {
            existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setPhoneNumber(userDTO.getPhoneNumber());
        existingUser.setAddress(userDTO.getAddress());
        return userRepository.save(existingUser);
    }

    @Override
    public User save(UserDTO userDTO, String path) {
        User user = new User(userDTO.getUsername(), passwordEncoder.encode(userDTO.getPassword()), userDTO.getName(), userDTO.getEmail(), userDTO.getPhoneNumber(), userDTO.getAddress(), userDTO.getRole(), userDTO.getVerificationCode(), userDTO.isConfirm());
        sendEmail(user, path);
        return userRepository.save(user);
    }

    @Override
    public User saveManager(UserDTO userDTO) {
        User user = new User(userDTO.getUsername(), passwordEncoder.encode(userDTO.getPassword()), userDTO.getName(), userDTO.getEmail(), userDTO.getPhoneNumber(), userDTO.getAddress(), userDTO.getRole(), userDTO.getVerificationCode(), userDTO.isConfirm());
        return userRepository.save(user);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public void sendEmail(User user, String path) {
        String from = "runningstore2023@gmail.com";
        String to = user.getEmail();
        String subject = "Account Verification";
        String content = "Dear [[name]],<br>" + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>" + "Thank you,<br>" + "Running Store";

        try {

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom(from, "Running Store");
            helper.setTo(to);
            helper.setSubject(subject);

            content = content.replace("[[name]]", user.getUsername());
            String siteUrl = path + "/verify?code=" + user.getVerificationCode();


            content = content.replace("[[URL]]", siteUrl);

            helper.setText(content, true);

            mailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean verifyAccount(String verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode);

        if (user == null) {
            return false;
        } else {

            user.setConfirm(true);
            user.setVerificationCode(null);

            userRepository.save(user);

            return true;
        }
    }

    @Override
    public void sendEmailGetPassword(String email, String path) {
        User user = userRepository.findUserByEmail(email);
        if (user != null) {
            String from = "runningstore2023@gmail.com";
            String to = email;
            String subject = "Reset Password";
            String content = "Dear [[name]],<br>" + "Please click the link below to reset your password:<br>"
                    + "<h3><a href=\"[[URL]]\" target=\"_self\">REST PASSWORD</a></h3>" + "Thank you,<br>" + "Running Store";

            try {

                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message);

                helper.setFrom(from, "Running Store");
                helper.setTo(to);
                helper.setSubject(subject);

                content = content.replace("[[name]]", user.getUsername());
                String siteUrl = path + "/resetPassword?username=" + user.getUsername();


                content = content.replace("[[URL]]", siteUrl);

                helper.setText(content, true);

                mailSender.send(message);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public User updatePassword(String username, String confirmPassword) {
        User existingUser = userRepository.findUserByUsername(username);
        existingUser.setPassword(passwordEncoder.encode(confirmPassword));
        return userRepository.save(existingUser);
    }

    @Override
    public int countAllUser() {
        return userRepository.countAllByRole("USER");
    }
}
