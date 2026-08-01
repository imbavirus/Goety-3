package za.co.infernos.goety.api.blocks.entities;

public interface IWindPowered {
    int activeTicks();

    void activate(int tick);

    default int windPower() {
        return 0;
    }

    default void setWindPower(int power) {
    }
}