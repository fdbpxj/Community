package life.majian.community.test;

public enum Note {
        中国(1),美国(56);
    private Integer cs;
     Note(Integer cs){
        this.cs=cs;
    }
    Note(){

    }
    public  Integer getCs(){
        return cs;
    }
}
