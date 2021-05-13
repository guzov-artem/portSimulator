package service_1;

public class Cargo implements Cloneable {

    public enum CargoType implements Cloneable {
        LOOSE(0),
        LIQUID(1),
        CONTAINERS(2);
        CargoType(int num){
            number = num;
        }
        int getInt(){
            return number;
        }
        private final int number;
        private double speed;
    }

    private final CargoType type;
    private double weight;

    Cargo(CargoType type, Double weight, Double speed) {
        this.type = type;
        this.weight = weight;
        type.speed = speed;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public CargoType getType() {
        return type;
    }

    public double getWeight() {
        return weight;
    }

    public double getSpeed_() {
        return type.speed;
    }

    public void changeWeight(double number) {
        if (-number > weight) {
            weight = 0.0;
        }
        else {
            weight += number;
        }
    }

    @Override
    public String toString() {
        return "Cargo{" +
                "type_=" + type +
                ", weight_=" + weight +
                ", speed_=" + type.speed +
                '}';
    }
}
