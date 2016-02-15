package org.ingrahamrobotics.robot.output;

public enum OutputLevel {
    DEBUG(0, "Debug", false),
    PID(1, "PID", false),
    MOTORS(2, "Motors", true),
    SENSORS(3, "Sensors", true),
    AUTO(4, "Autonomous", true),
    HIGH(5, "Important", true);

    public final boolean enabled;
    public final int level;
    public final String name;
    public final String networkName;

    private OutputLevel(int level, String name, boolean enabled) {
        this.level = level;
        this.name = name;
        this.enabled = enabled;
        this.networkName = "o:" + level + "-" + name;
    }

    public String toString() {
        return name;
    }
}
