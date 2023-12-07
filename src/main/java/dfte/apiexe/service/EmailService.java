package dfte.apiexe.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import dfte.apiexe.helper.EmailObj;
import dfte.apiexe.utils.SampleList;
import dfte.apiexe.utils.sample;
import freemarker.template.Configuration;
import freemarker.template.Template;

@Service
public class EmailService {

	@Autowired
	private Configuration config;

	public void sendEmail(EmailObj emailObj, Map<String, Object> model)
			throws AddressException, MessagingException, IOException, Exception {

		Properties props = new Properties();
		props.put("mail.host", "evergreencity.in");
		props.put("mail.port", "465");
		props.put("mail.username", "sridhar@evergreencity.in");
		props.put("mail.password", "Green@123");
		props.put("mail.protocol", "smtp");

		props.put("mail.smtp.auth", "true");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("sridhar@evergreencity.in", "Green@123");
			}
		});
		MimeMessage message = new MimeMessage(session);

		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());
			SampleList list = new SampleList();
			list.setItems();
			ArrayList<sample> listItems = list.getItems();
			model.put("inventory", listItems);
			Template template = config.getTemplate("email-template.ftl");
			String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
			helper.setFrom("sridhar@evergreencity.in");
			helper.setTo(InternetAddress.parse(emailObj.getToAddresses()));
			helper.setSubject(emailObj.getSubject());
			helper.setText(html, true);
			Transport.send(message);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
}
