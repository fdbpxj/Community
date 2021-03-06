package life.majian.community.controller;

import life.majian.community.dto.CommentCreateDTO;
import life.majian.community.dto.CommentDTO;
import life.majian.community.dto.ResultDTO;
import life.majian.community.enums.CommentTypeEnum;
import life.majian.community.exception.CustomizeErrorCode;
import life.majian.community.mapper.CommentMapper;
import life.majian.community.model.Comment;
import life.majian.community.model.User;
import life.majian.community.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private CommentService commentService;

    @ResponseBody
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request){

        User user=(User)request.getSession().getAttribute("user");

        if(user==null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        if(commentCreateDTO==null|| StringUtils.isBlank(commentCreateDTO.getContent())){
            return ResultDTO.errorOf(CustomizeErrorCode.COMMENT_IS_EMPTY);
        }
        Comment comment=new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setCommentator(user.getId());
        comment.setLikeCount(0L);
        comment.setCommentCount(0);


        commentService.insert(comment,user);

        ResultDTO resultDTO = ResultDTO.okOf();
        return resultDTO;
    }

    @ResponseBody
    @RequestMapping(value = "/comment/{id}",method =RequestMethod.GET)
    public ResultDTO comments(@PathVariable(name = "id") Long id){
        List<CommentDTO> commentDTOs=commentService.listByTargetId(id, CommentTypeEnum.Comment);
        return ResultDTO.okOf(commentDTOs);
    }
}


