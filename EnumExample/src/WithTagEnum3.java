import java.util.ArrayList;

public enum WithTagEnum3 {
    Noah("Hi","USA",1),
    Santos("HolLa","Colombia",2),
    Frieda("Hallo","Germany",3),
    Sophia("Hallo","Germany",4),
    LIU("你好","Taiwan",5),
    TANAKA("こんにちは","Japan",6),
    Alexis("Привет","Russia",7),
    KIN("","",0);

    private String says;
    private String country;
    private int number;

    WithTagEnum3(String says, String country, int number) {
        this.says = says;
        this.country = country;
        this.number = number;
    }

    public String getSays() {
        return says;
    }

    public String getCountry() {
        return country;
    }

    public int getNumber() {
        return number;
    }

    public void setInfo(String says, String country, int number) {
        this.says = says;
        this.country = country;
        this.number = number;
    }
}
