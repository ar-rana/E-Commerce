package com.practice.ecommerce.repository;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import com.practice.ecommerce.defaultModels.DefaultModels;
import com.practice.ecommerce.model.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

@DataJpaTest
@Rollback
public class ImageRepoTest {

    @Autowired
    private ImagesRepository imagesRepository;

    private Resource file;
    private byte[] imageBytes;

    private Image image;

//    @BeforeEach
//    public void setUp() {
//        try {
//            file = new ClassPathResource("static/testImage.png");
//            imageBytes = Files.readAllBytes(file.getFile().toPath());
//            image = imagesRepository.save(new Image(DefaultModels.DEFAULT_ID, imageBytes, "png"));
//        } catch (IOException e) {
//            fail();
//        }
//    }

//    @Test
    public void testSave() throws IOException {
        // using recursive calling to check if object are same
        assertThat(new Image(DefaultModels.DEFAULT_ID, imageBytes, "png"))
                .usingRecursiveAssertion()
                .ignoringFields("imageId")
                .isEqualTo(image);
    }

//    @Test
    public void testFindByProductIdAndGetImageId() {
        List<Integer> integers = imagesRepository.findByProductIdAndGetImageId(image.getProductId());

        assertEquals(1, integers.size());
        assertEquals(image.getImageId(), integers.getFirst());
    }
}
