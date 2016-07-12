package hj.chat.common;


public class C {

	public static final String TAG = "service";
	public static final class Hosts {
		public static final String DEFAULT_IP = "10.0.0.2";
	}
	public static final class Integers{
		public static final int DEFAULT_SERVER_PORT = 8000;

		public static final int STATUS_SUCCESS = 0;
		public static final int STATUS_FAILD = 1;
		public static final int STATUS_UNKNOWN_ERR =2;
		public static final int STATUS_UNSUPPORT_REQUEST = 3;
		public static final int STATUS_NOTLOGIN = 4;		
		
		public static final int STATUS_SEND_SUCCESS = 101;
		public static final int STATUS_SEND_FAILD = 102;
		public static final int STATUS_SEND_WAIT = 103;


	}

	public static final class Strings{

		public static final String SEND_SUCCESS = "���ͳɹ�";
		public static final String SEND_FAILD = "����ʧ��";
		public static final String SEND_NOT_ONLINE = "�Է�������";
		public static final String UNSUPPORT_REQUEST = "��֧�ֵ�����";
		public static final String LOGIN_SUCCESS = "��¼�ɹ�";
		public static final String NOTLOGIN = "û�е�¼";	
		
	}
}
