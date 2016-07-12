package bluemei.java.util;

public class PageMsg {
	
	public int currentPage;
	public int numPerPage;
	
	public PageMsg(){
		this(1,10);
	}
	public PageMsg(int page, int num) {
		this.currentPage=page;
		this.numPerPage=num;
	}
	public int offset() {
		return (currentPage-1)*numPerPage;
	}
}
