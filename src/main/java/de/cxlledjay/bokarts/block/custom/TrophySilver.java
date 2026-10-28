package de.cxlledjay.bokarts.block.custom;

import com.mojang.serialization.MapCodec;

public class TrophySilver extends TrophyBase {

    // codec
    public static final MapCodec<TrophySilver> CODEC = TrophySilver.createCodec(TrophySilver::new);


    public TrophySilver(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<TrophySilver> getCodec() {
        return CODEC;
    }


    public boolean isValidPlacement(int placement) {
        return placement == 2;
    }
}
