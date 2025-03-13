package com.bgrfacile.bgrsignapi.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class QRCodeService {

    public String generateQRCodeBase64(String attendanceUrl, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.MARGIN, 0);  // Supprime les marges
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);  // Correction d'erreur élevée

        BitMatrix bitMatrix = qrCodeWriter.encode(attendanceUrl, BarcodeFormat.QR_CODE, width, height, hints);

        // Configurer pour avoir un QR code sans bordure blanche
        MatrixToImageConfig config = new MatrixToImageConfig(MatrixToImageConfig.BLACK, MatrixToImageConfig.WHITE);
        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix, config);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        javax.imageio.ImageIO.write(qrImage, "PNG", pngOutputStream);

        return Base64.getEncoder().encodeToString(pngOutputStream.toByteArray());
    }
}
