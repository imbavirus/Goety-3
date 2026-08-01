package za.co.infernos.goety.api.entities;

public interface IHiding {

    default boolean isHiding() {
        return false;
    }
}