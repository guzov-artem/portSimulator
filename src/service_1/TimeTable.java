package service_1;

import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class TimeTable {
    public TimeTable(int size)
    {
        size_ = size;
        shipArrayList_ = new ArrayList[3];
        for (int i = 0; i < shipArrayList_.length; i++){
            shipArrayList_[i] = new ArrayList<Ship>();
        }
    }
    public void generateShips() {
        for (int i = 0; i < size_.intValue(); i++) {
            Ship ship = makeRandomShip();
            shipArrayList_[ship.getCargo_().getType().getInt()].add(ship);
        }
    }

    public ArrayList<Ship>[] getShipArrayList_() {
        return shipArrayList_;
    }

    private static Ship makeRandomShip()
    {
      return new Ship(names[nameNumber++],makeRandomCargo(), new Date(121,
        3, 0, 0,
        random.nextInt(20) + 40, random.nextInt(60)));
    }
    private static Cargo makeRandomCargo()
    {
        Integer temp = new Integer(random.nextInt(3));
        Cargo.CargoType type = Cargo.CargoType.LOOSE;
        switch (temp)
        {
            case 0: type = Cargo.CargoType.LOOSE;
            break;

            case 1: type = Cargo.CargoType.LIQUID;
            break;

            case 2: type = Cargo.CargoType.CONTAINERS;
            break;
        }
        return new Cargo(type, random.nextDouble()* 100, speed[temp.intValue()]);
    }

    public static long getMinutes(Date date) {
        return (date.getTime()/1000)/60;
    }
    static final Random random = new Random();

    static int nameNumber = 0;
    static String[] names = {"Черная Жемчужина","Месть Королевы Анны", "Аврора", "Инкремент", "Победа", "Доминатор", "Нагибатор",
            "Ночной бродяга", "Чмо", "Морской беспредел", "Дендрариум", "Сердцеедка", "Запанка", "Тамбовский мститель", "Путана",
            "Земфира", "Оксана", "Крутоз", "Щетинистый Гончая", "Божественный Акула", "Мутный Фенрир", "Чёрный Гончая", "Красивый Искупитель", "Бесстрашный Дух", "Золотой Ястреб", "Соломенный Молот", "Военный Свинка", "Чёрный Акула", "Алмазный Астрал", " Ржавый Вампир",
    "Красный Сатир", " Военный Оборотень", "Красный Забияка", "Дерзкий Ящер", "Божественный Оса", "Бесстрашный Эскалибур", " Галера для двоих", "Щетинистый Ястреб", "Дерзкий Минотавр", "Трусливый Тесей", "Стыдливый Сатир", "Одетый Апполон", "Двуглазый Кутузов", "Высокий Наполеон",
            "Вкусный Наполеон", "Бессмысленная Жизнь", "Красивая Смерть", "Красная Салфетка", "Разморозить мясо", "Вытереть со Стола", "Смыть за Собой", "Бессмысленный будильник", "Азартный Юнга", "Священная Черемуха", "Пыльца", "Легендарная Пыль", "Ты горишь как Огонь", "Человек за бортом", "Борт за человеком", "Ща бы в мак"};
    static Double[] speed = {5.0, 2.0, 4.0};
    private ArrayList<Ship>[] shipArrayList_;
    private Integer size_;
}
