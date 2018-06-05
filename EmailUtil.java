import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailUtil {

	/**
	 * Utility method to send simple HTML email
	 * 
	 * @param session
	 * @param toEmail
	 * @param subject
	 * @param body
	 */
	public static void sendEmail(Session session, Transport transport, String toEmail, String subject, String body) {
		try {
			MimeMessage msg = new MimeMessage(session);
			// set message headers
			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");

			msg.setFrom(new InternetAddress("joe@onebillsoftware.com", "NoReply-JD"));

			msg.setReplyTo(InternetAddress.parse("joe@onebillsoftware.com", false));

			msg.setSubject(subject, "UTF-8");

			msg.setText(body, "UTF-8");

			msg.setSentDate(new Date());

			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
			System.out.println("Message is ready");
			msg.saveChanges(); // implicit with send()
			// repeat if necessary
			for (int i = 0; i < 10; i ++ ) {
			
				transport.sendMessage(msg, msg.getAllRecipients());
				System.out.println("EMail Sent Successfully!!");
		
				Thread.sleep(60000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		System.out.println("SimpleEmail Start");

		String host = "smtp.gmail.com";
		String username = "joe@onebillsoftware.com";
		String password = "";
		int port = 587;

		Properties props = System.getProperties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.timeout","20000");
		props.put("mail.smtp.connectiontimeout", "20000");

		Session session = Session.getInstance(props, null);
		
		try {
			// Get transport for session
			Transport transport = session.getTransport("smtp");
			// Connect
			System.out.println("Trying to connect....");
			transport.connect(host, port, username, password);
			System.out.print("Connection successful!");
			// Done, close the connection
			EmailUtil.sendEmail(session, transport, "joemmuthu@gmail.com", "SimpleEmail Testing Subject",
				"SimpleEmail Testing Body");
			transport.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}