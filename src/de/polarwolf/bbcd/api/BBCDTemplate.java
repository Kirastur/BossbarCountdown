package de.polarwolf.bbcd.api;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;

import de.polarwolf.bbcd.config.ConfigParam;

public class BBCDTemplate {
	
	protected final String name;
	protected final BarColor color;
	protected final BarStyle style;
	protected final Set<BarFlag> flags;
	protected final String title;
	protected final double left;
	protected final double right;
	protected final double start;
	protected final double speed;
	protected final double end;
	protected final boolean autostart;
	protected final boolean show;
	protected final String command;
	

	public BBCDTemplate(String name, Map<String,String> parameters) throws BBCDException {
		this.name = name;
		verifyMap(parameters);

		this.color  = loadBarColorValue(ConfigParam.COLOR, parameters);
		this.style  = loadBarStyleValue(ConfigParam.STYLE, parameters);
		this.flags  = loadBarFlagsValue(ConfigParam.FLAGS, parameters);
		this.title  = loadStringValue(ConfigParam.TITLE, parameters);
		this.left   = loadDoubleValue(ConfigParam.LEFT, parameters);
		this.right   = loadDoubleValue(ConfigParam.RIGHT, parameters);
		this.start   = loadDoubleValue(ConfigParam.START, parameters);
		this.speed   = loadDoubleValue(ConfigParam.SPEED, parameters);
		this.end   = loadDoubleValue(ConfigParam.END, parameters);
		this.autostart = loadBoolValue(ConfigParam.AUTOSTART, parameters); 
		this.show    = loadBoolValue(ConfigParam.SHOW, parameters); 
		this.command   = loadStringValue(ConfigParam.COMMAND, parameters);
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
		for (ConfigParam configParam : configParams) {
			if (configParam.getAttributeName().equalsIgnoreCase(attributeName)) {
				return true;
			}
		}
		return false;
		
	}
	

	protected void verifyMap (Map<String,String> parameters) throws BBCDException {
		for (String attributeName : parameters.keySet()) {
			if (!isAttribute(attributeName)) {
				throw new BBCDException (getName(), "Unknown Attribute", attributeName);
			}
		}		
	}
	
	
	protected String loadStringValue(ConfigParam attribute, Map<String,String> parameters) {
		for (Entry<String,String> entry : parameters.entrySet()) {
			if (attribute.getAttributeName().equalsIgnoreCase(entry.getKey())) {
				String value = entry.getValue();
				if (value != null) {
					return value;
				}
			}
		}
		return attribute.getDefaultValue();
	}
	
	
	protected int loadIntValue(ConfigParam attribute, Map<String,String> parameters) throws BBCDException {
		String value = loadStringValue(attribute, parameters);
		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
			throw new BBCDException(getName()+"."+attribute.getAttributeName(), "Value is not integer", value);
		}
	}
		

	protected double loadDoubleValue(ConfigParam attribute, Map<String,String> parameters) throws BBCDException {
		String value = loadStringValue(attribute, parameters);
		try {
			return Double.parseDouble(value);
		} catch (Exception e) {
			throw new BBCDException(getName()+"."+attribute.getAttributeName(), "Value is not numeric", value);
		}
	}

	
	protected boolean loadBoolValue(ConfigParam attribute, Map<String,String> parameters) throws BBCDException {
		String value = loadStringValue(attribute, parameters);
		if ((value.equalsIgnoreCase("TRUE")) || (value.equalsIgnoreCase("YES"))) {
			return true;
		}
		if ((value.equalsIgnoreCase("FALSE")) || (value.equalsIgnoreCase("NO"))) {
			return false;
		}
		throw new BBCDException(getName()+"."+attribute.getAttributeName(), "Value is not boolean", value);
	}
	
	
	protected BarColor loadBarColorValue(ConfigParam attribute, Map<String,String> parameters) throws BBCDException {
		String value = loadStringValue(attribute, parameters);
		if (value.isEmpty()) {
			return null;
		}
		try {
			return BarColor.valueOf(value);
		} catch (Exception e) {
			throw new BBCDException(getName()+"."+attribute.getAttributeName(), "Unknown Bossbar Color", value);
		}
	}


	protected BarStyle loadBarStyleValue(ConfigParam attribute, Map<String,String> parameters) throws BBCDException {
		String value = loadStringValue(attribute, parameters);
		if (value.isEmpty()) {
			return null;
		}
		try {
			return BarStyle.valueOf(value);
		} catch (Exception e) {
			throw new BBCDException(getName()+"."+attribute.getAttributeName(), "Unknown Bossbar Style", value);
		}
	}


	protected Set<BarFlag> loadBarFlagsValue(ConfigParam attribute, Map<String,String> parameters) throws BBCDException {
		Set<BarFlag> myFlags= new HashSet<>();		
		String value = loadStringValue(attribute, parameters);
		if (value.isEmpty()) {
			return myFlags;
		}
		
		try {
			for (String flagName: value.split(" ")) {
				myFlags.add(BarFlag.valueOf(flagName));
			}
			return myFlags;
		} catch (Exception e) {
			throw new BBCDException(getName()+"."+attribute.getAttributeName(), "Unknown Bossbar Flag", value);
		}
	}
	
}
