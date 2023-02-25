public enum WithTagEnum {
    Noah("Hi","USA",1),
    Santos("Hola","Colombia",2),
    Frieda("Hallo","Germany",3),
    Alexis("Привет","Russia",4);

    private String says;
    private String country;
    private int number;

    WithTagEnum(String says, String country, int number) {
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
}
