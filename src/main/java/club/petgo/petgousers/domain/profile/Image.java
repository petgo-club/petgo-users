package club.petgo.petgousers.domain.profile;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@NoArgsConstructor
public class Image {

    private String imageName;
    private String imageLocation;

    @Enumerated(EnumType.STRING)
    private  ImageFormat imageFormat;

    public Image(String imageName, String imageLocation, String imageFormat) {
        this.imageName = imageName;
        this.imageLocation = imageLocation;
        this.imageFormat = ImageFormat.valueOf(imageFormat);
    }

    public enum ImageFormat {
        JPG,
        PNG,
        TIF
    }
}
