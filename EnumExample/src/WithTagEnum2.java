import java.util.Arrays;
import java.util.stream.Stream;

public enum WithTagEnum2 {
    Noah("Hi","USA",1),
    Santos("HolLa","Colombia",2),
    Frieda("Hallo","Germany",3),
    Sophia("Hallo","Germany",4),
    LIU("你好","Taiwan",5),
    TANAKA("こんにちは","Japan",6),
    Alexis("Привет","Russia",7);

    private String says;
    private String country;
    private int number;

    WithTagEnum2(String says, String country, int number) {
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

    public static Stream<WithTagEnum2> getItemByCountry(String country){
        return  Arrays.stream(values()).filter(v1->v1.country.equals(country));
    }
}
