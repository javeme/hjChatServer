package bluemei.java.util.db;

import java.util.Date;

public class Field {
	private String name;
	private Class type;
	private int length;
	private boolean nullAble;
	private String defaultValue;
	private boolean isPrimaryKey;
	private String comment;

	public Field(String name, Class type) {
		this(name, type, type.equals(String.class) ? 20 : 0);
	}
	public Field(String name, Class type, int len) {
		this(name, type, len, true, null, false, null);
	}
	public Field(String name, Class type, int len, boolean nullAble,
			String defaultValue, boolean isPrimaryKey, String comment) {
		this.name = name;
		this.type = type;
		this.length = len;
		this.nullAble = nullAble;
		this.defaultValue = defaultValue;
		this.isPrimaryKey = isPrimaryKey;
		this.comment = comment;
		
		if(this.getType()==null){//Íâ¼ü
			this.type=int.class;
			this.name+="_id";
		}
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		String strType = null;
		if (type.equals(Integer.TYPE)) {
			strType = "int";
		}
		else if (type.equals(String.class)) {
			strType = "varchar";
		}
		else if (type.equals(Date.class)) {
			strType = "datetime";
		}
		else if (type.equals(Boolean.TYPE)) {
			strType = "bit";
		}
		else if (type.equals(Byte.TYPE)) {
			strType = "tinyint";
		}
		else if (type.equals(Short.TYPE)) {
			strType = "smallint";
		}
		else if (type.equals(Long.TYPE)) {
			strType = "bigint";
		}
		else if (type.equals(Float.TYPE)) {
			strType = "real";
		}
		else if (type.equals(Double.TYPE)) {
			strType = "double";
		}
		else if (type.equals(StringBuilder.class)
				|| type.equals(StringBuffer.class)) {
			strType = "text";
		}
		return strType;
	}
	public void setType(Class type) {
		this.type = type;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public boolean isNullAble() {
		return nullAble;
	}
	public void setNullAble(boolean nullAble) {
		this.nullAble = nullAble;
	}
	public String getDefaultValue() {
		return defaultValue == null ? "NULL" : ("'" + defaultValue + "'");
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public boolean isPrimaryKey() {
		return isPrimaryKey;
	}
	public void setPrimaryKey(boolean isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}

}
