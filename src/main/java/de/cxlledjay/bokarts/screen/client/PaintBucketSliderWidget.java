package de.cxlledjay.bokarts.screen.client;

import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

import java.util.function.Consumer;

public class PaintBucketSliderWidget extends SliderWidget {

    private final String prefix;
    private final int range;
    private final Consumer<Integer> valueChangedListener;

    public PaintBucketSliderWidget(int x, int y, int width, int height, String prefix, int range, int initialValue, Consumer<Integer> valueChangedListener) {
        super(x, y, width, height, Text.empty(), (double) initialValue / range);

        this.prefix = prefix;
        this.range = range;
        this.valueChangedListener = valueChangedListener;
        this.updateMessage();
    }

    @Override
    protected void updateMessage() {
        int currentInt = (int) (this.value * (double) this.range);
        this.setMessage(Text.literal( this.prefix + ": " + currentInt));
    }

    @Override
    protected void applyValue() {
        int currentInt = (int) (this.value * (double) this.range);
        this.valueChangedListener.accept(currentInt);
    }

    public int getValue() {
        return  (int) (this.value * (double) this.range);
    }

    public double getPercentage() {
        return this.value;
    }

    public void setValue(int value) {
        if(value < 0 || value > this.range) value = 0;
        this.value = (double) value / (double) this.range;
        this.updateMessage();
    }
}
