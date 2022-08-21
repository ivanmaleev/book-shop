package com.example.bookshop.data.book;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Layer {
    @JsonProperty("layerId")
    public String getLayerId() {
        return this.layerId;
    }

    public void setLayerId(String layerId) {
        this.layerId = layerId;
    }

    String layerId;

    @JsonProperty("volumeAnnotationsVersion")
    public String getVolumeAnnotationsVersion() {
        return this.volumeAnnotationsVersion;
    }

    public void setVolumeAnnotationsVersion(String volumeAnnotationsVersion) {
        this.volumeAnnotationsVersion = volumeAnnotationsVersion;
    }

    String volumeAnnotationsVersion;
}
