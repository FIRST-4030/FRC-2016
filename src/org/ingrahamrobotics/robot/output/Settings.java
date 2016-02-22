package org.ingrahamrobotics.robot.output;

import java.util.HashMap;
import java.util.Map;
import org.ingrahamrobotics.robottables.api.RobotTable;
import org.ingrahamrobotics.robottables.api.RobotTablesClient;
import org.ingrahamrobotics.robottables.api.TableType;
import org.ingrahamrobotics.robottables.api.UpdateAction;
import org.ingrahamrobotics.robottables.api.listeners.ClientUpdateListener;
import org.ingrahamrobotics.robottables.api.listeners.TableUpdateListener;

/**
 * Class for getting driver input settings from the driver station.
 */
public class Settings implements ClientUpdateListener, TableUpdateListener {

    /**
     * Possible keys for input settings
     */
    public static enum Key {
        ARM_PID_P("Arm P", "1.0"),
        ARM_PID_I("Arm I", "0.0"),
        ARM_PID_D("Arm D", "0.0"),

        ARM_SETPOINT("Arm Setpoint", "0"),
        ARM_ZERO_SPEED("Arm Homing Speed", "0.5"),
        ARM_PRESET_COLLECT("Arm Preset: Collect", "-10"),
        ARM_PRESET_UP("Arm Preset: Up", "1000"),

        SHOOTER_PID_P("Shooter P", "0.01"),
        SHOOTER_PID_I("Shooter I", "0.01"),
        SHOOTER_PID_D("Shooter D", "0.00"),

        SHOOTER_SETPOINT("Shooter Setpoint", "0"),
        COLLECT_SPEED("Collection Speed", "-0.4"),
        
        KICKER_SPEED("Kicker Speed", "0.75"),
        KICKER_TIME("Kicker Duration", "500"),

        VISION_H_LOW("Vision Hue Low","200"),
        VISION_H_HIGH("Vision Hue High","260"),
        VISION_S_LOW("Vision Saturation Low","60"),
        VISION_S_HIGH("Vision Saturation High","255"),
        VISION_L_LOW("Vision Lum Low","60"),
        VISION_L_HIGH("Vision Lum High", "255"),
        
        // Comment to let ; be on new line
        ;

        /**
         * this is just so we can easily change this, rather than commenting out values.
         */
        public final boolean actuallyChangeFromDefault;
        public final String name;
        public final String defaultValue;
        private String value;

        private Key(final String name, final String defaultValue) {
            this.name = name;
            this.defaultValue = defaultValue;
            this.value = defaultValue;
            this.actuallyChangeFromDefault = true;
        }

        private Key(final String name, final String defaultValue, boolean actuallyChangeFromDefault) {
            this.name = name;
            this.defaultValue = defaultValue;
            this.value = defaultValue;
            this.actuallyChangeFromDefault = actuallyChangeFromDefault;
        }

        public String get() {
            return value;
        }

        public int getInt() {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException ex) {
                System.err.println("Warning: Value '" + value + "' of '" + name + "' is not valid.");
                return Integer.parseInt(defaultValue);
            }
        }

        public double getDouble() {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException ex) {
                System.err.println("Warning: Value '" + value + "' of '" + name + "' is not valid.");
                return Double.parseDouble(defaultValue);
            }
        }

        public boolean getBoolean() {
            return ((value != null) && (value.startsWith("y") || value.startsWith("t")));
        }

        public long getLong() {
            try {
                return Long.parseLong(value);
            } catch (NumberFormatException ex) {
                System.err.println("Warning: Value '" + value + "' of '" + name + "' is not valid.");
                return Long.parseLong(defaultValue);
            }
        }

        public boolean isInt() {
            try {
                //noinspection ResultOfMethodCallIgnored
                Integer.parseInt(value);
                return true;
            } catch (NumberFormatException ex) {
                return false;
            }
        }

        public boolean isDouble() {
            try {
                //noinspection ResultOfMethodCallIgnored
                Double.parseDouble(value);
                return true;
            } catch (NumberFormatException ex) {
                return false;
            }
        }

        public boolean isBoolean() {
            return value != null && (value.equals("true") || value.equals("false"));
        }
    }

    private final Map<String, Key> keyMap;
    private final RobotTable defaultSettings;
    private final RobotTable driverSettings;
    private final RobotTablesClient client;

    public Settings(final RobotTablesClient client) {
        this.client = client;
        if (client == null) {
            System.err.println("Warning! Failed to initialize settings: RobotTablesClient null.");
            defaultSettings = null;
            driverSettings = null;
            keyMap = null;
            return;
        }
        defaultSettings = client.publishTable("robot-input-default");
        driverSettings = client.subscribeToTable("robot-input");
        keyMap = new HashMap<>(Key.values().length);
        for (Key key : Key.values()) {
            keyMap.put(key.name, key);
        }
    }

    public void subscribeAndPublishDefaults() {
        if (client == null) {
            return;
        }
        client.addClientListener(this, true);
        driverSettings.addUpdateListener(this, true);
        for (Key key : Key.values()) {
            if (key.actuallyChangeFromDefault) {
                defaultSettings.set(key.name, key.defaultValue);
            }
        }
    }

    @Override
    public void onUpdate(final RobotTable table, final String key, final String value, final UpdateAction action) {
        if (!"robot-input".equals(table.getName())) {
            return;
        }
        Key keyObject = keyMap.get(key);
        if (keyObject == null) {
            // They are sending an update to a value we don't know about
            // TODO: Something to prevent this
            return;
        }
        switch (action) {
            case NEW:
            case UPDATE:
                keyObject.value = value;
                break;
            case DELETE:
                keyObject.value = keyObject.defaultValue;
                break;
        }
    }

    @Override
    public void onUpdateAdmin(final RobotTable table, final String key, final String value, final UpdateAction action) {
    }

    @Override
    public void onTableCleared(final RobotTable table) {
        if (!"robot-input".equals(table.getName())) {
            return;
        }
        for (Key key : Key.values()) {
            key.value = key.defaultValue;
        }
    }

    @Override
    public void onTableChangeType(final RobotTable table, final TableType oldType, final TableType newType) {
        if ("robot-input-default".equals(table.getName())) {
            if (newType == TableType.REMOTE) {
                Output.output(OutputLevel.DEBUG, "robot-input-default-local", "No longer local!");
            } else {
                Output.output(OutputLevel.DEBUG, "robot-input-default-local", "Local again now.");
                // Someone else had the table, now we do! Update *all* the values!
                for (Key key : Key.values()) {
                    table.set(key.name, key.defaultValue);
                }
            }
        }
        // We shouldn't ever have this happen for robot-input, because we never attempt to publish it.
    }

    @Override
    public void onTableStaleChange(final RobotTable table, final boolean nowStale) {
        if ("robot-input".equals(table.getName())) {
            Output.output(OutputLevel.DEBUG, "robot-input-stale", nowStale);
            // TODO: Maybe we should have some light on the robot to show this
        }
    }

    @Override
    public void onAllSubscribersStaleChange(final RobotTable table, boolean nowStale) {
        if ("robot-input-default".equals(table.getName())) {
            Output.output(OutputLevel.DEBUG, "robot-input-default-recieved", nowStale);
        }
    }

    @Override
    public void onNewTable(final RobotTable table) {
    }
}
