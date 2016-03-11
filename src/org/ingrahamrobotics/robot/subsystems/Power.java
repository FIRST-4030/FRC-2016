package org.ingrahamrobotics.robot.subsystems;

import org.ingrahamrobotics.robot.Robot;
import org.ingrahamrobotics.robot.commands.ReadPower;
import org.ingrahamrobotics.robot.output.Output;
import org.ingrahamrobotics.robot.output.OutputLevel;

import edu.wpi.first.wpilibj.ControllerPower;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.command.Subsystem;

/*
 * This does not process all data available from ControllerPower.
 * That data is already exposed in the default driver station
 * and is available via static calls
 */

public class Power extends Subsystem {

	public static final int kNUM_CHANNELS = 16;

	private PowerDistributionPanel pdp;
	private double[] currents;
	private double[] currentsMax;
	private double current;
	private double currentMax;
	private double temp;
	private double voltage;
	private double voltageMin;
	private double usage;

	private double rioVoltage;
	private double rioVoltageMin;
	private double rioCurrent;
	private double rioCurrentMax;
	private int rioFaults;

	public Power() {
		pdp = new PowerDistributionPanel();

		currents = new double[kNUM_CHANNELS];
		currentsMax = new double[kNUM_CHANNELS];
		current = Double.NaN;
		currentMax = Double.NaN;
		temp = Double.NaN;
		voltage = Double.NaN;
		voltageMin = Double.NaN;
		usage = Double.NaN;

		rioVoltage = Double.NaN;
		rioVoltageMin = Double.NaN;
		rioCurrent = Double.NaN;
		rioCurrentMax = Double.NaN;
		rioFaults = 0;

		// Only address the PDP if we are enabled
		if (!Robot.disableReadPower) {
			pdp.clearStickyFaults();
			pdp.resetTotalEnergy();
		}
	}

	public void initDefaultCommand() {
		// Only set a default command if we are enabled
		if (!Robot.disableReadPower) {
			setDefaultCommand(new ReadPower());
		}
	}

	public double getUsage() {
		return usage;
	}

	public double getVoltage() {
		return voltage;
	}

	public double getVoltageMin() {
		return voltageMin;
	}

	public double getTemp() {
		return temp;
	}

	public double getCurrent() {
		return current;
	}

	public double getCurrentMax() {
		return currentMax;
	}

	public double getCurrent(int channel) {
		if (channel >= kNUM_CHANNELS) {
			return Double.NaN;
		}
		return currents[channel];
	}

	public double getCurrentMax(int channel) {
		if (channel >= kNUM_CHANNELS) {
			return Double.NaN;
		}
		return currentsMax[channel];
	}

	public void update() {
		usage = pdp.getTotalEnergy();
		Output.output(OutputLevel.POWER, "Battery Usage", usage);

		current = pdp.getTotalCurrent();
		Output.output(OutputLevel.POWER, "Current", current);
		if (current > currentMax) {
			currentMax = current;
			Output.output(OutputLevel.POWER, "Current Max: ", currentMax);
		}

		for (int i = 0; i < kNUM_CHANNELS; i++) {
			currents[i] = pdp.getCurrent(i);
			Output.output(OutputLevel.POWER, "Current: " + i, currents[i]);

			if (currents[i] > currentsMax[i]) {
				currentsMax[i] = currents[i];
				Output.output(OutputLevel.POWER, "Current Max: " + i, currentsMax[i]);
			}
		}

		voltage = pdp.getVoltage();
		Output.output(OutputLevel.POWER, "Voltage", voltage);
		if (voltage < voltageMin) {
			voltageMin = voltage;
			Output.output(OutputLevel.POWER, "Voltage Min: ", voltageMin);
		}

		temp = pdp.getTemperature();
		Output.output(OutputLevel.POWER, "Temperature", temp);

		rioVoltage = ControllerPower.getInputVoltage();
		Output.output(OutputLevel.POWER, "Rio Voltage", rioVoltage);
		if (rioVoltage < rioVoltageMin) {
			rioVoltageMin = rioVoltage;
			Output.output(OutputLevel.POWER, "Rio Voltage Min", rioVoltageMin);
		}

		rioCurrent = ControllerPower.getInputCurrent();
		Output.output(OutputLevel.POWER, "Rio Current", rioCurrent);
		if (rioCurrent > rioCurrentMax) {
			rioCurrentMax = rioCurrent;
			Output.output(OutputLevel.POWER, "Rio Current Max", rioCurrentMax);
		}

		rioFaults = ControllerPower.getFaultCount3V3() + ControllerPower.getFaultCount5V()
				+ ControllerPower.getFaultCount6V();
		Output.output(OutputLevel.POWER, "Rio Faults", rioFaults);
	}
}
