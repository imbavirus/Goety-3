package za.co.infernos.goetied.api.entities;

public interface IHiding {

    default boolean isHiding() {
        return false;
    }
}