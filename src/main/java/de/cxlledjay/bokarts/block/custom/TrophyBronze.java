package de.cxlledjay.bokarts.block.custom;

import com.mojang.serialization.MapCodec;

public class TrophyBronze extends TrophyBase {

    // codec
    public static final MapCodec<TrophyBronze> CODEC = TrophyBronze.createCodec(TrophyBronze::new);


    public TrophyBronze(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<TrophyBronze> getCodec() {
        return CODEC;
    }


    public boolean isValidPlacement(int placement) {
        return placement == 3;
    }
}
