package com.xiaojumao.util;

import javax.mail.Address;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmailSend {

	// QQ邮箱,用户名无法设置,账号bug
//	final private static String username = "1661907160@qq.com"; // 登录SMTP服务器的用户名
//	final private static String password = "hwpematkssbebeba"; // 登录SMTP服务器的密码

	//
	private static ExecutorService executorService = Executors.newFixedThreadPool(5);

	private final static String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	final private static String smtpServer = "smtp.163.com"; // SMTP服务器地址
	final private static String port = "465"; // 端口

	final private static String password = "UHVYEQUZSRCPERLL"; // 登录SMTP服务器的密码
	final private static String sendMail = "hanwei_wu@163.com"; // 登录SMTP服务器的邮箱账号
	final private static String sendName = "快递e栈"; // 发送邮件昵称

	public static String recipient; // 收件人地址
	public static String subject; // 邮件主题
	public static String content; // 邮件正文

	/**
	 * 正式发邮件
	 */
	public static boolean sendMail() {
		Properties properties = new Properties();
		properties.put("mail.smtp.host", smtpServer);
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.socketFactory.class", SSL_FACTORY); // 使用JSSE的SSL
		properties.put("mail.smtp.socketFactory.fallback", "false"); // 只处理SSL的连接,对于非SSL的连接不做处理
		properties.put("mail.smtp.port", port);
		properties.put("mail.smtp.socketFactory.port", port);
		properties.put("mail.smtp.ssl.enable", true);
		Session session = Session.getInstance(properties);
		session.setDebug(true);
		MimeMessage message = new MimeMessage(session);
		try {
			// 发件人
			message.setFrom(new InternetAddress(sendMail, sendName, "UTF-8"));
			// 收件人
			Address toAddress = new InternetAddress(recipient);
			message.setRecipient(MimeMessage.RecipientType.TO, toAddress); // 设置收件人,并设置其接收类型为TO
			/**
			 * TO：代表有健的主要接收者。 CC：代表有健的抄送接收者。 BCC：代表邮件的暗送接收者。
			 */
			// 主题
			message.setSubject(subject);
			// 时间
			message.setSentDate(new Date());
			message.setContent(content,"text/html;charset=UTF-8");
			message.saveChanges();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		try {
			Transport transport = session.getTransport("smtp");
			transport.connect(smtpServer, sendMail, password);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 发送取件码
	 * @param email 发送到的邮箱
	 * @param code 取件码
	 */
	public static void send(String email, String code){
		executorService.execute(() -> {
			EmailSend.recipient = email;
			EmailSend.subject = "快递e栈";
			EmailSend.content = "您有一个快递到达快递e栈，取件验证码为：" + code + "，验证码长期有效，请勿泄露于他人！";
			EmailSend.sendMail();
		});
	}

	/**
	 * 发送登录注册验证码
	 * @param email 发送到的邮箱
	 * @param code 验证码
	 */
	public static void sendLoginVer(String email, String code){
		executorService.execute(() -> {
			EmailSend.recipient = email;
			EmailSend.subject = "快递e栈";
			EmailSend.content = "本次登录/注册的验证码为：" + code + "，打死不要告诉别人！";
			EmailSend.sendMail();
		});
	}

	/**
	 * 发送修改信息验证码
	 * @param email 发送到的邮箱
	 * @param code 验证码
	 */
	public static void sendUpdateVer(String email, String code){
		executorService.execute(() -> {
			EmailSend.recipient = email;
			EmailSend.subject = "快递e栈";
			EmailSend.content = "验证码为：" + code + "，打死不要告诉别人！";
			EmailSend.sendMail();
		});
	}

}