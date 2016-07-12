package bluemei.java.util.db;

public class SelectRelation {
	private String operate = null;
	private String logic = null;
	private Object value = null;

	SelectRelation() {
		;
	}
	SelectRelation(Object value) {
		this.value = value;
	}

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	public String getLogic() {
		return logic;
	}

	public void setLogic(String logic) {
		this.logic = logic;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
