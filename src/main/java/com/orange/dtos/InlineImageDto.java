package com.orange.dtos;

/**
 * Created by Mohamed Gaber on Aug, 2018
 */
public class InlineImageDto {

    private String imageId;
    private String imageBase64String;

    public InlineImageDto() {
    }

    public InlineImageDto(String imageId, String imageBase64String) {
        this.imageId = imageId;
        this.imageBase64String = imageBase64String;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImageBase64String() {
        return imageBase64String;
    }

    public void setImageBase64String(String imageBase64String) {
        this.imageBase64String = imageBase64String;
    }

    @Override
    public String toString() {
        return "InlineImageDto{" +
                "imageId=" + imageId +
                ", imageBase64String='" + imageBase64String + '\'' +
                '}';
    }
}
