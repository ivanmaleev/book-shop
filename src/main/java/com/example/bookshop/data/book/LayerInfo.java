package com.example.bookshop.data.book;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class LayerInfo {
    @JsonProperty("layers")
    public ArrayList<Layer> getLayers() {
        return this.layers;
    }

    public void setLayers(ArrayList<Layer> layers) {
        this.layers = layers;
    }

    ArrayList<Layer> layers;
}
