package life.majian.community.service;

import life.majian.community.dto.PaginationDTO;
import life.majian.community.dto.QuestionDTO;
import life.majian.community.mapper.QuestionMapper;
import life.majian.community.mapper.UserMapper;
import life.majian.community.model.Question;
import life.majian.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;

    public PaginationDTO list(Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = questionMapper.count();
        paginationDTO.setPagination(totalCount, page, size);
        if (page < 1) {
            page = 1;
        }
        if (paginationDTO.getTotalPage() > 0) {
            if (page > paginationDTO.getTotalPage()) {
                page = paginationDTO.getTotalPage();
            }
        } else {
            page = 1;
        }
        //size*(page-1)  5*（page-1） 0/5 5/5 10/5
        Integer offset = size * (page - 1);//offset代表去数据库拿offset到后面5条的数据
        List<Question> questions = questionMapper.list(offset, size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();//拿到每5个的 数据集合 包括user

        for (Question question : questions) {
            QuestionDTO questionDTO = new QuestionDTO();

            User user = userMapper.findById(question.getCreator());
            if (user != null) {

                questionDTO.setUser(user);

            }
            BeanUtils.copyProperties(question, questionDTO);//把第一属性上的属性拷贝到第二个属性上
            questionDTOList.add(questionDTO);
        }

        paginationDTO.setQuestions(questionDTOList);

        return paginationDTO;
    }

    public PaginationDTO list(Integer userId, Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = questionMapper.countByUserId(userId);
        paginationDTO.setPagination(totalCount, page, size);
        if (page < 1) {
            page = 1;
        }
        if (paginationDTO.getTotalPage() > 0) {
            if (page > paginationDTO.getTotalPage()) {
                page = paginationDTO.getTotalPage();
            }
        } else {
            page = 1;
        }
        //size*(page-1)  5*（page-1） 0/5 5/5 10/5
        Integer offset = size * (page - 1);//offset代表去数据库拿offset到后面5条的数据
        List<Question> questions = questionMapper.listByUserId(userId, offset, size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();//拿到每5个的 数据集合 包括user

        for (Question question : questions) {
            User user = userMapper.findById(userId);
            if (user != null) {
                QuestionDTO questionDTO = new QuestionDTO();
                BeanUtils.copyProperties(question, questionDTO);//把第一属性上的属性拷贝到第二个属性上
                questionDTO.setUser(user);
                questionDTOList.add(questionDTO);
            }
        }

        paginationDTO.setQuestions(questionDTOList);

        return paginationDTO;
    }

    public QuestionDTO getById(Integer id) {

        Question question = questionMapper.getById(id);
        QuestionDTO questionDTO = new QuestionDTO();
        User user = userMapper.findById(question.getCreator());
        BeanUtils.copyProperties(question, questionDTO);
        questionDTO.setUser(user);

        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if(question.getId()==null){
            //创建
            questionMapper.create(question);
        }else {
            //更新
            question.setGmtModified(System.currentTimeMillis());
            questionMapper.update(question);
        }
    }
}
