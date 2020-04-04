package life.majian.community.service;

import life.majian.community.dto.PaginationDTO;
import life.majian.community.dto.QuestionDTO;
import life.majian.community.exception.CustomizeErrorCode;
import life.majian.community.exception.CustomizeException;
import life.majian.community.mapper.QuestionExtMapper;
import life.majian.community.mapper.QuestionMapper;
import life.majian.community.mapper.UserMapper;
import life.majian.community.model.Question;
import life.majian.community.model.QuestionExample;
import life.majian.community.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;

    public PaginationDTO list(Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = (int) questionMapper.countByExample(new QuestionExample());
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
        QuestionExample questionExample=new QuestionExample();
        questionExample.setOrderByClause("gmt_create desc");
        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(questionExample, new RowBounds(offset, size));

        List<QuestionDTO> questionDTOList = new ArrayList<>();//拿到每5个的 数据集合 包括user

        for (Question question : questions) {

            QuestionDTO questionDTO = new QuestionDTO();

            User user = userMapper.selectByPrimaryKey(question.getCreator());


            if (user != null) {

                questionDTO.setUser(user);

            }
            BeanUtils.copyProperties(question, questionDTO);//把第一属性上的属性拷贝到第二个属性上
            questionDTOList.add(questionDTO);
        }

        paginationDTO.setQuestions(questionDTOList);

        return paginationDTO;
    }

    public PaginationDTO list(Long userId, Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(userId);
        Integer totalCount = (int) questionMapper.countByExample(questionExample);
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
        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatorEqualTo(userId);
        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(example, new RowBounds(offset, size));
        List<QuestionDTO> questionDTOList = new ArrayList<>();//拿到每5个的 数据集合 包括user

        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(userId);
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

    public QuestionDTO getById(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question == null) {

            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        BeanUtils.copyProperties(question, questionDTO);
        questionDTO.setUser(user);

        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if (question.getId() == null) {
            //创建
            question.setViewCount(0);
            question.setLikeCount(0);
            question.setCommentCount(0);
            questionMapper.insert(question);
        } else {
            //更新
            question.setGmtModified(System.currentTimeMillis());
            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria().andIdEqualTo(question.getId());
            int updated = questionMapper.updateByExampleSelective(question, questionExample);
            if (updated != 1) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public void incView(Long id) {
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);
    }

    public List<QuestionDTO> selectRelated(QuestionDTO questionDTO) {
        if (StringUtils.isBlank(questionDTO.getTag())){
            return new ArrayList<>();
        }
        String[] tag=StringUtils.split(questionDTO.getTag(),",");
        String regexpTag=Arrays.stream(tag).collect(Collectors.joining("|"));
        Question question=new Question();
        question.setId(questionDTO.getId());
        question.setTag(regexpTag);
        List<Question> questions =questionExtMapper.selectRelated(question);
        List<QuestionDTO> questionDTOS=questions.stream().map(q->{
            QuestionDTO questionDTO1=new QuestionDTO();
            BeanUtils.copyProperties(q,questionDTO1);
            return questionDTO1;
        }).collect(Collectors.toList());
         return questionDTOS;
    }
}
