package venom.toolbot.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Value("${login.password}")
    private String correctPassword;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/")
    public String root(HttpSession session) {
        // 检查是否已登录
        if (session.getAttribute("authenticated") != null) {
            return "redirect:/dashboard"; // 已登录则跳转到仪表板
        }
        return "redirect:/login"; // 未登录则跳转到登录页
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam String password, 
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        if (correctPassword.equals(password)) {
            // 登录成功
            session.setAttribute("authenticated", true);
            return "redirect:/dashboard";
        } else {
            // 登录失败
            redirectAttributes.addAttribute("error", true);
            return "redirect:/login";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session) {
        // 检查是否已登录
        if (session.getAttribute("authenticated") == null) {
            return "redirect:/login";
        }
        return "dashboard"; // 需要创建一个dashboard.html模板
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
