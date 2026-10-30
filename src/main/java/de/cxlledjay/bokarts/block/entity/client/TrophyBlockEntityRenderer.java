package de.cxlledjay.bokarts.block.entity.client;

import de.cxlledjay.bokarts.block.custom.TrophyBase;
import de.cxlledjay.bokarts.block.entity.custom.TrophyBlockEntity;
import de.cxlledjay.bokarts.component.ModDataComponentTypes;
import de.cxlledjay.bokarts.item.custom.TrackReport;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;

public class TrophyBlockEntityRenderer implements BlockEntityRenderer<TrophyBlockEntity> {

    private final TextRenderer textRenderer;

    public TrophyBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.textRenderer = ctx.getTextRenderer();
    }

    @Override
    public void render(TrophyBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {

        ItemStack stack = entity.getStack(0);

        // nothing worthy inside
        if(stack.isEmpty()) return;
        if(!stack.contains(ModDataComponentTypes.TRACK_REPORT_PLACEMENT)) return;

        // create text
        int placement = stack.getOrDefault(ModDataComponentTypes.TRACK_REPORT_PLACEMENT, 0);
        int placementColor = TrackReport.getPlacementColor(placement) | 0xFF000000;
        Text placementText = Text.literal("#" + placement);

        // rendering
        Direction direction = entity.getCachedState().get(TrophyBase.FACING);
        matrices.push();

        double xOffset = 0.5 + (direction.getOffsetX() * 0.251);
        double zOffset = 0.5 + (direction.getOffsetZ() * 0.251);
        matrices.translate(xOffset, 1.05, zOffset);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-direction.asRotation()));

        float scale = 0.025f;
        matrices.scale(scale, -scale, scale);
        float textWidth = this.textRenderer.getWidth(placementText);
        float textX = -textWidth / 2;

        this.textRenderer.draw(
                placementText,
                textX,
                0,
                placementColor,
                true,
                matrices.peek().getPositionMatrix(),
                vertexConsumers,
                TextRenderer.TextLayerType.POLYGON_OFFSET,
                0,
                light
        );
        matrices.pop();
    }
}
