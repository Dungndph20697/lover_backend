package com.codegym.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailNotificationService {

    @Autowired
    private JavaMailSender mailSender;

    // email xác thực đơn thuê
    public void sendOrderConfirmationEmail(String recipientEmail, String recipientName,
                                           String ccdvName, Long sessionId) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(recipientEmail);
            helper.setSubject("Đơn thuê của bạn đã được xác nhận");

            String htmlContent = buildEmailContent(recipientName, ccdvName, sessionId);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            System.out.println("Email đã được gửi thành công tới: " + recipientEmail);

        } catch (MessagingException e) {
            System.err.println("Lỗi khi gửi email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String buildEmailContent(String recipientName, String ccdvName, Long sessionId) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "    <style>" +
                "        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                "        .container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                "        .header { background: linear-gradient(to right, #ff9a9e 0%, #ffd1dc 45%, #ffe3e3 100%); " +
                "                 padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }" +
                "        .header h1 { color: white; margin: 0; font-size: 24px; }" +
                "        .content { background: #ffffff; padding: 30px; border: 1px solid #e0e0e0; }" +
                "        .message-box { background: #fff5f7; padding: 20px; border-radius: 8px; " +
                "                      border-left: 4px solid #ff6b9d; margin: 20px 0; }" +
                "        .message-box p { margin: 0; font-size: 16px; color: #333; }" +
                "        .info { background: #f8f9fa; padding: 15px; border-radius: 8px; margin: 20px 0; }" +
                "        .info p { margin: 5px 0; }" +
                "        .footer { text-align: center; padding: 20px; color: #666; font-size: 12px; }" +
                "        .button { display: inline-block; padding: 12px 30px; background: #ff6b9d; " +
                "                 color: white; text-decoration: none; border-radius: 8px; " +
                "                 margin: 20px 0; font-weight: bold; }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class='container'>" +
                "        <div class='header'>" +
                "            <h1>🎉 Thông Báo Xác Nhận Đơn</h1>" +
                "        </div>" +
                "        <div class='content'>" +
                "            <p>Xin chào <strong>" + recipientName + "</strong>,</p>" +
                "            <div class='message-box'>" +
                "                <p><strong>💕 Người yêu mà bạn thuê đã xác nhận đơn rồi!</strong></p>" +
                "            </div>" +
                "            <div class='info'>" +
                "                <p><strong>Thông tin đơn thuê:</strong></p>" +
                "                <p>📝 Mã đơn: #" + sessionId + "</p>" +
                "                <p>👤 Người cung cấp dịch vụ: " + ccdvName + "</p>" +
                "                <p>✅ Trạng thái: Đã xác nhận</p>" +
                "            </div>" +
                "            <p>Đơn thuê của bạn đã được <strong>" + ccdvName + "</strong> xác nhận. " +
                "               Vui lòng kiểm tra chi tiết đơn và chuẩn bị cho buổi hẹn.</p>" +
                "            <p>Chúc bạn có một trải nghiệm tuyệt vời! ❤️</p>" +
                "        </div>" +
                "        <div class='footer'>" +
                "            <p>Email này được gửi tự động từ hệ thống</p>" +
                "            <p>Vui lòng không trả lời email này</p>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }

    //  GỬI OTP RÚT TIỀN
    public void sendWithdrawOtp(String email, String name, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("Mã OTP xác nhận rút tiền");

            String html = """
                    <html>
                    <body style='font-family:Arial'>
                        <h2 style='color:#ff4d6d;'>🔐 Xác nhận yêu cầu rút tiền</h2>
                        <p>Xin chào <b>%s</b>,</p>
                        <p>Đây là mã OTP để xác thực yêu cầu rút tiền của bạn:</p>
                        <div style='
                            font-size:32px;
                            font-weight:bold;
                            padding:10px 20px;
                            background:#ffe5ec;
                            border-radius:10px;
                            display:inline-block;
                            margin:15px 0;
                            color:#ff2d55;
                        '>
                            %s
                        </div>
                        <p>Mã OTP có hiệu lực trong 5 phút.</p>
                        <br/>
                        <i>Vui lòng không chia sẻ mã OTP cho bất kỳ ai.</i>
                    </body></html>
                    """.formatted(name, otp);

            helper.setText(html, true);
            mailSender.send(message);

            System.out.println("Đã gửi OTP đến: " + email);

        } catch (Exception e) {
            System.out.println("Lỗi gửi OTP: " + e.getMessage());
        }
    }

    // 3️⃣ GỬI EMAIL KHI ADMIN DUYỆT RÚT TIỀN
    public void sendWithdrawApproved(String email, String name, Double amount, Double realReceived) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("Yêu cầu rút tiền đã được duyệt");

            String html = """
                    <html>
                    <body style='font-family:Arial'>
                        <h2 style='color:#4CAF50;'>💸 Rút tiền thành công</h2>
                        <p>Xin chào <b>%s</b>,</p>
                        <p>Yêu cầu rút tiền của bạn đã được Admin duyệt.</p>

                        <p><b>Số tiền yêu cầu:</b> %,.0f VND</p>
                        <p><b>Số tiền nhận được (đã trừ phí 5%%):</b> %,.0f VND</p>

                        <p>Hãy kiểm tra tài khoản ngân hàng của bạn.</p>
                        <p>Cảm ơn bạn đã sử dụng dịch vụ ❤️</p>
                    </body>
                    </html>
                    """.formatted(name, amount, realReceived);

            helper.setText(html, true);
            mailSender.send(message);

            System.out.println("Email duyệt rút tiền gửi đến: " + email);

        } catch (Exception e) {
            System.out.println("Lỗi gửi mail duyệt rút: " + e.getMessage());
        }
    }

    public void sendSimpleEmail(String recipientEmail, String recipientName,
                                String ccdvName, Long sessionId) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(recipientEmail);
            message.setSubject("Đơn thuê của bạn đã được xác nhận");
            message.setText(
                    "Xin chào " + recipientName + ",\n\n" +
                            "Người yêu mà bạn thuê đã xác nhận đơn rồi!\n\n" +
                            "Thông tin đơn thuê:\n" +
                            "- Mã đơn: #" + sessionId + "\n" +
                            "- Người cung cấp dịch vụ: " + ccdvName + "\n" +
                            "- Trạng thái: Đã xác nhận\n\n" +
                            "Chúc bạn có trải nghiệm tuyệt vời!\n\n" +
                            "---\n" +
                            "Email này được gửi tự động từ hệ thống"
            );

            mailSender.send(message);
            System.out.println("Email text đã được gửi thành công tới: " + recipientEmail);

        } catch (Exception e) {
            System.err.println("Lỗi khi gửi email text: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // admin gửi email khi rut tiền thành công
    public void sendWithdrawRejected(String email, String name, Double amount) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("Yêu cầu rút tiền đã bị từ chối");

            String html = """
                    <html>
                    <body style='font-family:Arial'>
                        <h2 style='color:#FF3B30;'>❌ Rút tiền bị từ chối</h2>
                        <p>Xin chào <b>%s</b>,</p>
                        <p>Yêu cầu rút tiền của bạn đã bị Admin từ chối.</p>

                        <p><b>Số tiền yêu cầu:</b> %,.0f VND</p>

                        <p>Nếu bạn có thắc mắc, vui lòng liên hệ bộ phận hỗ trợ.</p>
                        <p>Cảm ơn bạn đã sử dụng dịch vụ ❤️</p>
                    </body>
                    </html>
                    """.formatted(name, amount);

            helper.setText(html, true);
            mailSender.send(message);

            System.out.println("Email từ chối rút tiền gửi đến: " + email);

        } catch (Exception e) {
            System.out.println("Lỗi gửi mail từ chối rút: " + e.getMessage());
        }
    }
}