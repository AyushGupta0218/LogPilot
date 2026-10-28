package com.logpilot.web;
import com.logpilot.logs.LogRepository;import org.springframework.stereotype.Controller;import org.springframework.ui.Model;import org.springframework.web.bind.annotation.GetMapping;
@Controller public class HomeController {private final LogRepository logs;public HomeController(LogRepository logs){this.logs=logs;}@GetMapping("/")public String home(Model model){model.addAttribute("logs",logs.search(null,null,null,100));return "index";}}
