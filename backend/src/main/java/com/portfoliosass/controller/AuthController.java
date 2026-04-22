package com.portfoliosass.controller;

import com.portfoliosass.dto.AuthResponse;
import com.portfoliosass.dto.LoginRequest;
import com.portfoliosass.dto.RegisterRequest;
import com.portfoliosass.security.JwtUtil;
import com.portfoliosass.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    @Value("${server.cookie.secure:true}")
    private boolean cookieSecure;

    // ── Páginas ──────────────────────────────────────────────────────────────

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
            Model model) {
        if (error != null)
            model.addAttribute("error", "Credenciais inválidas.");
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest("", "", ""));
        return "auth/register";
    }

    // ── Ações ─────────────────────────────────────────────────────────────────

    @PostMapping("/auth/login")
    public String login(@Valid @ModelAttribute LoginRequest request,
            BindingResult bindingResult,
            HttpServletResponse response,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Preencha todos os campos.");
            return "auth/login";
        }
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password()));

            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            String token = jwtUtil.generateToken(userDetails);
            addJwtCookie(response, token);

            return "redirect:/dashboard";
        } catch (AuthenticationException e) {
            model.addAttribute("error", "Usuário ou senha inválidos.");
            return "auth/login";
        }
    }

    @PostMapping("/auth/register")
    public String register(@Valid @ModelAttribute RegisterRequest request,
            BindingResult bindingResult,
            HttpServletResponse response,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("registerRequest", request);
            return "auth/register";
        }
        try {
            userService.register(request);

            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password()));
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            String token = jwtUtil.generateToken(userDetails);
            addJwtCookie(response, token);

            return "redirect:/dashboard";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("registerRequest", request);
            return "auth/register";
        }
    }

    @GetMapping("/auth/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt_token", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(cookieSecure);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/login";
    }

    // ── API REST ───────────────────────────────────────────────────────────────

    @PostMapping("/api/auth/login")
    @ResponseBody
    public AuthResponse loginApi(@Valid @RequestBody LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);
        return new AuthResponse(token, userDetails.getUsername());
    }

    // ── Utilitário ─────────────────────────────────────────────────────────────

    private void addJwtCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("jwt_token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(cookieSecure);
        cookie.setPath("/");
        cookie.setMaxAge((int) (expirationMs / 1000));
        response.addCookie(cookie);
    }
}
