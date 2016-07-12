package hj.chat.net;

import hj.chat.msg.ChatMessage;

import java.io.IOException;


public interface IChatMessageSender {
	public boolean send(ChatMessage msg) throws IOException ;
}
