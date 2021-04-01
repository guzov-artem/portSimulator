package service_1;

public class Cargo implements Cloneable{
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    public enum CargoType implements Cloneable{
        LOOSE(0),
        LIQUID(1),
        CONTAINERS(2);
        CargoType(int num){
            number_ = num;
        }
        int getInt(){
            return number_;
        }
        private int number_;
        private double speed;
    }
    Cargo(CargoType type, Double weight, Double speed) {
        type_ = type;
        weight_ = weight;
        type.speed = speed;
    }

    public CargoType getType() {
        return type_;
    }

    public double getWeight() {
        return weight_;
    }

    public double getSpeed_() {
        return type_.speed;
    }

    public void changeWeight(double number) {
        if (-number > weight_){
            weight_ = 0.0;
        }
        else {
            weight_ += number;
        }
    }

    @Override
    public String toString() {
        return "Cargo{" +
                "type_=" + type_ +
                ", weight_=" + weight_ +
                ", speed_=" + type_.speed +
                '}';
    }

    private final CargoType type_;
    private double weight_;
}
