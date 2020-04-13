package life.majian.community.controller;

import life.majian.community.dto.PaginationDTO;
import life.majian.community.mapper.UserMapper;
import life.majian.community.model.User;
import life.majian.community.service.NotificationService;
import life.majian.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action") String action,
                          Model model,
                          HttpServletRequest request,
                          @RequestParam(name = "page", defaultValue = "1") Integer page,
                          @RequestParam(name = "size", defaultValue = "5") Integer size) {

        User user = (User) request.getSession().getAttribute("user");

        if (user == null) {
            model.addAttribute("error", "用户未登录");
            return "redirect:/";
        }

            if ("questions".equals(action)) {
                model.addAttribute("section", "questions");
                model.addAttribute("sectionName", "我的提问");
                PaginationDTO paginationDTO = questionService.list(user.getId(), page, size);
                model.addAttribute("pagination", paginationDTO);
            } else if ("replies".equals(action)) {
                PaginationDTO paginationDTO=notificationService.list(user.getId(),page,size);
                model.addAttribute("pagination", paginationDTO);
                model.addAttribute("section", "replies");
                model.addAttribute("sectionName", "最新回复");

            }


        return "profile";
    }

}
