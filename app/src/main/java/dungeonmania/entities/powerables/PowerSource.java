package dungeonmania.entities.powerables;

public interface PowerSource {
    public boolean isPowered();
    public void setPowered(boolean powered);
    public boolean wasJustPowered();
    public void update();
    public void turnOn();
    public void turnOff();
    public void subscribeSwitchable(Switchable s);
}
