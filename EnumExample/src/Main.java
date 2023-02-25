import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        System.out.println("首先，可以簡單取出一個枚舉內容：");
        System.out.println(NormalEnum.HELLO1);
        System.out.println("");

        System.out.println("再來，使用values()可以調用該枚舉的所有內容：");
        for (NormalEnum normal : NormalEnum.values()
        ) {
            System.out.println(normal);
        }
        System.out.println();
        System.out.println("枚舉本身可以是可以被當作判斷ifElse或者switch的依據的：");
        for (NormalEnum normal : NormalEnum.values()
        ) {
            if (normal.equals(NormalEnum.HELLO3)) System.out.println("取得Hello3");
            switch (normal) {
                case HELLO1:
                    System.out.println("取得Hello1");
                    break;
                default:
                    break;
            }
        }
        System.out.println();
        System.out.println("如果你的枚舉有getter的話，還可以取得數值：");
        System.out.println("來自"+WithTagEnum.Alexis.getCountry()+"的"+WithTagEnum.Alexis
                +"説："+WithTagEnum.Alexis.getSays());
        System.out.println();
        System.out.println("當然，如果你有在枚舉裡面寫過濾的方式的話");
        String country = "Germany";
        Stream<WithTagEnum2> d =  WithTagEnum2.getItemByCountry(country);
        d.forEach(people->{
            System.out.println("來自"+country+"的"+people+"説："+people.getSays());
        });
        System.out.println();
        System.out.println("最後，想要往裡面丟值也當然是做得到的");
        WithTagEnum3.KIN.setInfo("안녕하세요","Korea",8);
        System.out.println("來自"+WithTagEnum3.KIN.getCountry()+"的"+WithTagEnum3.KIN
                +"説："+WithTagEnum3.KIN.getSays());

    }
}
