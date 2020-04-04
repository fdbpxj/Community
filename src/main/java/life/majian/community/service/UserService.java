package life.majian.community.service;

import life.majian.community.mapper.UserMapper;
import life.majian.community.model.User;
import life.majian.community.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public void createOrUpdate(User user) {
  //      User dbUser = userMapper.findByAccountId(user.getAccountId());
            UserExample userExample=new UserExample();
            //SQL语句拼接 2020/3/20/21:31 我对于这段代码的理解是 创建出一个拼接的SQL语句 差不多意思是Where条件是AccountID
            userExample.createCriteria().andAccountIdEqualTo(user.getAccountId());
            //然后把上面的sql语句拼接进去 根据AccountId查找.
            List<User> users=userMapper.selectByExample(userExample);
        if (users.size() == 0) {
            //插入
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        } else {
            //更新
            User dbUser=users.get(0);
            User updateUser=new User();
            UserExample userExample1=new UserExample();
            userExample1.createCriteria().andIdEqualTo(dbUser.getId());
            updateUser.setGmtModified(System.currentTimeMillis());
            updateUser.setAvatarUrl(user.getAvatarUrl());
            updateUser.setName(user.getName());
            updateUser.setToken(user.getToken());
            //更新一个个User对象 前面是更新的内容 把内容插入一个新User对象里 对象的属性如果不是null就回插进去. 第二个属性 我的理解是 去找到那个需要更新的对象
            userMapper.updateByExampleSelective(updateUser,userExample1);
        }
    }
}
