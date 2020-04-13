package life.majian.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode{
    QUESTION_NOT_FOUND(2001,"该问题已经被删除！"),
    TARGET_NOT_FOUND(2002,"未选中任何问题或评论进行回复"),
    NO_LOGIN(2003,"未登录不能进行评论,请先登录"),
    SYS_ERROR(2004,"服务器炸了，稍后再来！"),
    TYPE_PARAM_WRONG(2005,"评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006,"你评论的问题已删除！"),
    COMMENT_IS_EMPTY(2007,"输入内容不能为空"),
    READ_NOTIFICATION_FAIL(2008,"朋友,你这是读别人的信息呢"),
    NOTIFICATION_NOT_FOUND(2009,"消息被送到北太平洋去了~~~~"),
    FILE_UPLOAD_FAIL(2010,"图片上传失败");
    private Integer code;
    private String message;

    CustomizeErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }


    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
