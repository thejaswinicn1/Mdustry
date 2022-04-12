package support;

import java.util.ArrayList;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import config.Constants;

public class Emailer {

	 public static void main(String[] args) throws Exception {
	
	  String host = "192.168.1.15"; 
	  String from = "notifications@healthasyst.com";
	 			
				
		// Adding Receipient
		ArrayList<String> Receipnt = new ArrayList<String>();
		Receipnt.add(Constants.Receipient1);
		/*
		 * Receipnt.add(Constants.Receipient2); Receipnt.add(Constants.Receipient3);
		 */
		// Adding TESTREPORT FILE
		ArrayList<String> filename = new ArrayList<String>();
		filename.add(Constants.Report4);
		


		for (int j = 0; j < Receipnt.size(); j++) {
			Properties properties = new Properties();
			properties.put("mail.smtp.auth", "true");
			properties.put("mail.smtp.starttls.enable", "false");
			properties.put("mail.smtp.host", host);
			properties.put("mail.smtp.port", "587");
			properties.put("mail.smtp.ssl.trust", host);
			properties.setProperty("mail.user", from);
			properties.setProperty("mail.password", "Notifications");
			
				Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
				
					
					  protected PasswordAuthentication getPasswordAuthentication() { return new
					  PasswordAuthentication("notifications@healthasyst.com", "Notifications");
					  
					  }
					 
			});
			try {

				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(from));
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(Receipnt.get(j)));
				message.setSubject("Directory Attachments");

				BodyPart messageBodyPart = new MimeBodyPart();
				messageBodyPart = new MimeBodyPart();
				Multipart multipart = new MimeMultipart();

				BodyPart messageBodyPart1 = new MimeBodyPart();
				messageBodyPart1 = new MimeBodyPart();
				messageBodyPart1.setText("Hi All,\n"
						+ "\n This email was triggered by Automation tool. Please see the attached report for the today's Sanity test Results. Automation Team is still analyzing these results \n"
						+ "\n Thanks\n" + "\n ATG Team");
				multipart.addBodyPart(messageBodyPart1);
			
			
				
				
				String Subjectname = "Execution Report OF ";

				for (int k = 0; k < filename.size(); k++) {
					Subjectname += filename.get(k).substring(filename.get(k).lastIndexOf("\\") + 1) + ",";

					
				}
				message.setSubject(Subjectname);
				for (int i = 0; i < filename.size(); i++) {

				
					System.out.println("Adding: " + filename.get(i));
					messageBodyPart = new MimeBodyPart();
					DataSource source = new FileDataSource(filename.get(i));
					messageBodyPart.setDataHandler(new DataHandler(source));
					messageBodyPart.setFileName(filename.get(i).substring(filename.get(i).lastIndexOf("\\") + 1));
					multipart.addBodyPart(messageBodyPart);
				}
				message.setContent(multipart);

				Transport.send(message);
			} catch (MessagingException e) {
				throw new RuntimeException(e);
			}
		}
	}
}