package com.example.bookshop.data.book;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class LayerInfo {
    @JsonProperty("layers")
    public List<Layer> getLayers() {
        return this.layers;
    }

    public void setLayers(ArrayList<Layer> layers) {
        this.layers = layers;
    }

    ArrayList<Layer> layers;
}
