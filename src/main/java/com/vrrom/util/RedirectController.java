package com.vrrom.util;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class RedirectController {
    @GetMapping("/agreement/{token}")
    public String redirectToAgreement(@PathVariable String token) {
        return "redirect:/applications/" + token + "/agreement";
    }
}
