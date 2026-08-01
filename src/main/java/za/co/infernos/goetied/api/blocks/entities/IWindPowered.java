package za.co.infernos.goetied.api.blocks.entities;

public interface IWindPowered {
    int activeTicks();

    void activate(int tick);

    default int windPower() {
        return 0;
    }

    default void setWindPower(int power) {
    }
}