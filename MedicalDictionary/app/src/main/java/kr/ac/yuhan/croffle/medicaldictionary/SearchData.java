package kr.ac.yuhan.croffle.medicaldictionary;

public class SearchData {
    String termEng;             // 의학용어(영어)
    String termKor;             // 의학용어(한글)
    String termExplain;         // 의학용어 설명
    String termRecord;          // 단어장 등록 여부

    public SearchData(String termEng, String termKor, String termExplain, String termRecord){
        this.termEng = termEng;
        this.termKor = termKor;
        this.termExplain = termExplain;
        this.termRecord = termRecord;
    }
    public String getTermEng(){
        return termEng;
    }
    public void setTermEng(){
        this.termEng = termEng;
    }
    public String getTermKor(){
        return termKor;
    }
    public void setTermKor(){
        this.termKor = termKor;
    }
    public String getTermExplain(){
        return termExplain;
    }
    public void setTermExplain(){
        this.termExplain = termExplain;
    }
    public String getTermRecord(){ return termRecord; }
    public void setTermRecord(String record){
        this.termRecord = record;
    }
}
