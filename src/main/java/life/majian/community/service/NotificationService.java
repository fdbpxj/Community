package life.majian.community.service;

        import life.majian.community.dto.NotificationDTO;
        import life.majian.community.dto.PaginationDTO;

        import life.majian.community.enums.NotificationStatusEnum;
        import life.majian.community.enums.NotificationTypeEnum;
        import life.majian.community.exception.CustomizeErrorCode;
        import life.majian.community.exception.CustomizeException;
        import life.majian.community.mapper.NotificationMapper;

        import life.majian.community.model.*;
        import org.apache.ibatis.session.RowBounds;
        import org.springframework.beans.BeanUtils;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Service;

        import java.util.ArrayList;
        import java.util.List;


@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;


    public PaginationDTO list(Long userId, Integer page, Integer size) {

        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO<>();
        NotificationExample notificationExample=new NotificationExample();

        notificationExample.createCriteria().andReceiverEqualTo(userId);
        Integer totalCount = (int) notificationMapper.countByExample(notificationExample);
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
        NotificationExample example = new NotificationExample();
        example.createCriteria().andReceiverEqualTo(userId);
        example.setOrderByClause("gmt_create desc");
        //根据用户ID拿所有的通知消息
        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));
        if(notifications.size()==0){
            return paginationDTO;
        }
        List<NotificationDTO> notificationDTOList = new ArrayList<>();//拿到每5个的 数据集合 包括user
        for (Notification notification:notifications){
            NotificationDTO notificationDTO=new NotificationDTO();
            BeanUtils.copyProperties(notification,notificationDTO);
            notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationDTOList.add(notificationDTO);
        }
        paginationDTO.setData(notificationDTOList);

        return paginationDTO;
    }

    public Long unreadCount(Long id) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(id).andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
       return notificationMapper.countByExample(notificationExample);
    }

    public NotificationDTO read(Long id, User user) {
        Notification notification=notificationMapper.selectByPrimaryKey(id);
        if(notification==null){
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if(notification.getReceiver()!=user.getId()){
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);
        NotificationDTO notificationDTO=new NotificationDTO();
        BeanUtils.copyProperties(notification,notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));

        return notificationDTO;
    }
}
