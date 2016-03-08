package org.ingrahamrobotics.robot.subsystems;

import java.lang.management.ManagementFactory;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

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
 * 
 * There's also some system load processing in here.
 * That's slightly cross-function but I'm gonna live with it for now.
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

	private double loadJVM;
	private double loadSys;

	public Power() {
		pdp = new PowerDistributionPanel();
		pdp.clearStickyFaults();
		pdp.resetTotalEnergy();

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

		loadJVM = Double.NaN;
		loadSys = Double.NaN;
	}

	public void initDefaultCommand() {
		setDefaultCommand(new ReadPower());
	}

	public void getLoad() {

		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		ObjectName name = null;
		AttributeList list = null;
		try {
			name = ObjectName.getInstance("java.lang:type=OperatingSystem");
			list = mbs.getAttributes(name, new String[] { "ProcessCpuLoad", "SystemCpuLoad" });
		} catch (MalformedObjectNameException | NullPointerException | InstanceNotFoundException
				| ReflectionException e) {
			e.printStackTrace();
		}
		if (list == null || list.isEmpty() || list.size() < 2) {
			return;
		}

		// Process load
		Attribute att = (Attribute) list.get(0);
		double value = (Double) att.getValue();
		if (value > 0) {
			loadJVM = ((int) (value * 1000) / 10.0);
			Output.output(OutputLevel.POWER, "Process Load", loadJVM);
		}

		// System load
		att = (Attribute) list.get(1);
		value = (Double) att.getValue();
		if (value > 0) {
			loadSys = ((int) (value * 1000) / 10.0);
			Output.output(OutputLevel.POWER, "System Load", loadSys);
		}
	}
	
	public double getLoadJVM() {
		return loadJVM;
	}
	
	public double getLoadSystem() {
		return loadSys;
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
		
		getLoad();
	}
}
