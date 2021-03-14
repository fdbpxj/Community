package life.majian.community.enums;

public enum CommentTypeEnum {
    Question(1), Comment(2);

    private Integer type;
    public Integer getType(){
        return type;
    }
    CommentTypeEnum(Integer type) {
        this.type = type;

    }

    public static boolean isExist(Integer type){
        for(CommentTypeEnum commentTypeEnum:CommentTypeEnum.values()){
            if(commentTypeEnum.getType().equals(type)){
                return true;
            }
        }
        return false;
    }
}
