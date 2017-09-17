package iss.java.mail;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
/**
 * Created by Mr.Chang on 2015/11/22
 * 
 */
public class Mail2014302580254 implements IMailService
{
	private String smtpserver = new String("smtp.sina.cn");
	private String user = new String("");
	private String password = new String("");
	private String pop3server = new String("pop3.sina.cn");
	private final transient Properties prop = new Properties();
	private transient Session session;
	private transient Transport transport;
	/**
	 * 邮箱session
	 * @return
	 */
	public Session getSession()
	{
		prop.put("mail.smtp.host", smtpserver);
		prop.put("mail.smtp.auth", "true");
		session = Session.getInstance(prop);
		return session;
	}
	/**
	 * 邮箱transport
	 * @return
	 * @throws MessagingException
	 */
	public Transport setTransport() throws MessagingException
	{
		session = getSession();
		Transport transport = session.getTransport("smtp");
		transport.connect(smtpserver,user,password);
		return transport;
	}
	/**
	 * 从邮箱中获取自动回复的邮件
	 * @return
	 * @throws MessagingException
	 */
	public Message[] getMessage() throws MessagingException
	{
		Store store = getSession().getStore("pop3");
		store.connect(pop3server,user,password);
		Folder folder = store.getFolder("INBOX");
		folder.open(Folder.READ_ONLY);
		Message[] messages = folder.getMessages();
		return messages;
	}
	/**
	 * 初始化并连接所有的邮箱服务器
	 * 
	 */

	@Override
	public void connect() throws MessagingException {
		// TODO Auto-generated method stub
		try{
			transport = setTransport();
			System.out.println("连接成功");
		}catch(Exception e)
		{
			System.out.println("连接失败");
			e.printStackTrace(); 
		}
	}
	/**
	 * @param recipient
	 *              收件人邮箱地址
	 * @param subject
	 *              邮件主题
	 * @param content
	 *              邮件正文
	 * 
	 */
	@Override
	public void send(String recipient, String subject, Object content)
			throws MessagingException {
		// TODO Auto-generated method stub
		try{
			
		    MimeMessage message = new MimeMessage(session);
			InternetAddress from= new InternetAddress(user); 
			message.setFrom(from);
			InternetAddress to = new InternetAddress(recipient);
			message.setRecipient(Message.RecipientType.TO,to);
			message.setSubject(subject);
			message.setContent(content.toString(),"text/html;charset=utf-8");
			message.saveChanges();
			System.out.println("发送成功");
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
		}catch(Exception e)
		{
			System.out.println("发送失败");
			e.printStackTrace(); 
		}
	}
	/*
	 * 询问服务器是否有新邮件到达
	 * (non-Javadoc)
	 * @see iss.java.mail.IMailService#listen()
	 */

	@Override
	public boolean listen() throws MessagingException {
		// TODO Auto-generated method stub
		try
		{
			Message[] messages = getMessage();
			if(messages.length == 0)
			{
				System.out.println("邮箱为空");
				return false;
			}
			else
			{
				System.out.println("邮箱不为空");
				return true;
			   
			}
		}catch(Exception e)
		{
			System.out.println("监听失败");
			return false;
		}
		
	} 

	/**
	 * @param sender
	 *           自动回复的发件人的邮箱地址
	 * @param subject
	 *           自动回复的主题
	 *           
	 * 
	 */
	@Override
	public String getReplyMessageContent(String sender, String subject)
			throws MessagingException, IOException {
		// TODO Auto-generated method stub
		try{
			Message[] messages = getMessage();
			//StringBuffer se = new StringBuffer();
			StringBuffer su = new StringBuffer();
			for(int i = 0;i<messages.length;i++)
			{
				//se.append(messages[i].getFrom());
				su.append(messages[i].getContent());
			}
			//sender = se.toString();
			//subject = su.toString();
			String m = su.toString();
			return m;//sender+subject;
		}catch(Exception e)
		{
			System.out.println("输出失败");
			return null;
		}
	}

}

