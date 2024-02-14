package com.sendback.global;

import com.sendback.global.exception.type.ImageException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.sendback.global.config.image.exception.ImageExceptionType.AWS_S3_UPLOAD_FAIL;

@ExtendWith(MockitoExtension.class)
public abstract class ServiceTest {

    protected MultipartFile mockingMultipartFile(String fileName) {
        return new MockMultipartFile(
                "images",
                fileName,
                MediaType.IMAGE_JPEG_VALUE,
                generateMockImage()
        );
    }

    private byte[] generateMockImage() {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            ImageIO.write(image, "jpg", byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new ImageException(AWS_S3_UPLOAD_FAIL);
        }
    }

}
