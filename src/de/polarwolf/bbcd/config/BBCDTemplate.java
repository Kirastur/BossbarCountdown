package de.polarwolf.bbcd.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.configuration.ConfigurationSection;

import de.polarwolf.bbcd.exception.BBCDException;

public class BBCDTemplate {

	protected final String name;
	protected BarColor color;
	protected BarStyle style;
	protected Set<BarFlag> flags;
	protected String title;
	protected double left;
	protected double right;
	protected double start;
	protected double speed;
	protected double end;
	protected boolean autostart;
	protected boolean show;
	protected String command;

	protected BBCDTemplate(String name) {
		this.name = name;
		loadFromDefaults();
	}

	public BBCDTemplate(String name, Map<String, String> parameters) throws BBCDException {
		this.name = name;
		loadFromMap(parameters);
	}

	public BBCDTemplate(ConfigurationSection fileSection) throws BBCDException {
		this.name = fileSection.getName();
		loadFromFile(fileSection);
	}

	public String getName() {
		return name;
	}

	public BarColor getColor() {
		return color;
	}

	public BarStyle getStyle() {
		return style;
	}

	public Set<BarFlag> getFlags() {
		return flags;
	}

	public String getTitle() {
		return title;
	}

	public double getLeft() {
		return left;
	}

	public double getRight() {
		return right;
	}

	public double getStart() {
		return start;
	}

	public double getSpeed() {
		return speed;
	}

	public double getEnd() {
		return end;
	}

	public boolean isAutostart() {
		return autostart;
	}

	public boolean isShow() {
		return show;
	}

	public String getCommand() {
		return command;
	}

	protected boolean isAttribute(String attributeName) {
		List<ConfigParam> configParams = Arrays.asList(ConfigParam.values());
		for (ConfigParam myConfigParam : configParams) {
			if (myConfigParam.getAttributeName().equalsIgnoreCase(attributeName)) {
				return true;
			}
		}
		return false;

	}

	protected void validateMap(Map<String, String> parameters) throws BBCDException {
		for (String myAttributeName : parameters.keySet()) {
			if (!isAttribute(myAttributeName)) {
				throw new BBCDException(getName(), "Unknown Attribute", myAttributeName);
			}
		}
	}

	protected String loadStringValue(ConfigParam attribute, Map<String, String> parameters) {
		for (Entry<String, String> myEntry : parameters.entrySet()) {
			if (attribute.getAttributeName().equalsIgnoreCase(myEntry.getKey())) {
				String myValue = myEntry.getValue();
				if (myValue != null) {
					return myValue;
				}
			}
		}
		return attribute.getDefaultValue();
	}

	protected int loadIntValue(ConfigParam attribute, Map<String, String> parameters) throws BBCDException {
		String value = loadStringValue(attribute, parameters);
		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
			throw new BBCDException(getName() + "." + attribute.getAttributeName(), "Value is not integer", value);
		}
	}

	protected double loadDoubleValue(ConfigParam attribute, Map<String, String> parameters) throws BBCDException {
		String value = loadStringValue(attribute, parameters);
		try {
			return Double.parseDouble(value);
		} catch (Exception e) {
			throw new BBCDException(getName() + "." + attribute.getAttributeName(), "Value is not numeric", value);
		}
	}

	protected boolean loadBoolValue(ConfigParam attribute, Map<String, String> parameters) throws BBCDException {
		String value = loadStringValue(attribute, parameters);
		if ((value.equalsIgnoreCase("TRUE")) || (value.equalsIgnoreCase("YES"))) {
			return true;
		}
		if ((value.equalsIgnoreCase("FALSE")) || (value.equalsIgnoreCase("NO"))) {
			return false;
		}
		throw new BBCDException(getName() + "." + attribute.getAttributeName(), "Value is not boolean", value);
	}

	protected BarColor loadBarColorValue(ConfigParam attribute, Map<String, String> parameters) throws BBCDException {
		String value = loadStringValue(attribute, parameters);
		if (value.isEmpty()) {
			return null;
		}
		try {
			return BarColor.valueOf(value);
		} catch (Exception e) {
			throw new BBCDException(getName() + "." + attribute.getAttributeName(), "Unknown Bossbar Color", value);
		}
	}

	protected BarStyle loadBarStyleValue(ConfigParam attribute, Map<String, String> parameters) throws BBCDException {
		String value = loadStringValue(attribute, parameters);
		if (value.isEmpty()) {
			return null;
		}
		try {
			return BarStyle.valueOf(value);
		} catch (Exception e) {
			throw new BBCDException(getName() + "." + attribute.getAttributeName(), "Unknown Bossbar Style", value);
		}
	}

	protected Set<BarFlag> loadBarFlagsValue(ConfigParam attribute, Map<String, String> parameters)
			throws BBCDException {
		Set<BarFlag> myFlags = new HashSet<>();
		String value = loadStringValue(attribute, parameters);
		if (value.isEmpty()) {
			return myFlags;
		}

		try {
			for (String myFlagName : value.split(" ")) {
				myFlags.add(BarFlag.valueOf(myFlagName));
			}
			return myFlags;
		} catch (Exception e) {
			throw new BBCDException(getName() + "." + attribute.getAttributeName(), "Unknown Bossbar Flag", value);
		}
	}

	protected void loadFromMap(Map<String, String> parameters) throws BBCDException {
		validateMap(parameters);
		this.color = loadBarColorValue(ConfigParam.COLOR, parameters);
		this.style = loadBarStyleValue(ConfigParam.STYLE, parameters);
		this.flags = loadBarFlagsValue(ConfigParam.FLAGS, parameters);
		this.title = loadStringValue(ConfigParam.TITLE, parameters);
		this.left = loadDoubleValue(ConfigParam.LEFT, parameters);
		this.right = loadDoubleValue(ConfigParam.RIGHT, parameters);
		this.start = loadDoubleValue(ConfigParam.START, parameters);
		this.speed = loadDoubleValue(ConfigParam.SPEED, parameters);
		this.end = loadDoubleValue(ConfigParam.END, parameters);
		this.autostart = loadBoolValue(ConfigParam.AUTOSTART, parameters);
		this.show = loadBoolValue(ConfigParam.SHOW, parameters);
		this.command = loadStringValue(ConfigParam.COMMAND, parameters);
	}

	protected void loadFromDefaults() {
		Map<String, String> dummyMap = new HashMap<>();
		try {
			loadFromMap(dummyMap);
		} catch (BBCDException e) {
			e.printStackTrace();
		}
	}

	protected void loadFromFile(ConfigurationSection fileSection) throws BBCDException {
		Map<String, String> parameters = new HashMap<>();
		for (String myAttributeName : fileSection.getKeys(false)) {
			String myAttributeValue = fileSection.getString(myAttributeName);
			parameters.put(myAttributeName, myAttributeValue);
		}
		loadFromMap(parameters);
	}

}
