package life.majian.community.controller;

import life.majian.community.dto.CommentDTO;
import life.majian.community.dto.QuestionDTO;

import life.majian.community.enums.CommentTypeEnum;
import life.majian.community.service.CommentService;
import life.majian.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Long id,
                           Model model) {
        QuestionDTO questionDTO = questionService.getById(id);
        List<QuestionDTO> relateQuestion=questionService.selectRelated(questionDTO);
        List<CommentDTO> comments = commentService.listByTargetId(id, CommentTypeEnum.Question);

        //累加阅读数功能
        questionService.incView(id);

        model.addAttribute("question", questionDTO);
        model.addAttribute("relateQuestion",relateQuestion);
        model.addAttribute("comments", comments);
        return "question";
    }
}


