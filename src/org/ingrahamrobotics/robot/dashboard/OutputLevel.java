package org.ingrahamrobotics.robot.dashboard;

public enum OutputLevel {
    DEBUG(0, "Debug", false),
    POWER(0, "Power", false),
    DRIVE_PID(1, "Drive PID", true),
    ARM_PID(2, "Arm PID", true),
    SHOOTER_PID(2, "Shooter PID", false),
    MOTORS(3, "Motors", true),
    SENSORS(4, "Sensors", true),
    VISION(5, "Vision", true),
    AUTO(5, "Autonomous", true),
    HIGH(6, "Important", true);

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
