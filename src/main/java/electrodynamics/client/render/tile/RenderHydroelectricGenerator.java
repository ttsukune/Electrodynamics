package electrodynamics.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;

import electrodynamics.client.ClientRegister;
import electrodynamics.common.tile.TileHydroelectricGenerator;
import electrodynamics.prefab.utilities.UtilitiesRendering;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.resources.model.BakedModel;

public class RenderHydroelectricGenerator extends BlockEntityRenderer<TileHydroelectricGenerator> {

    public RenderHydroelectricGenerator(BlockEntityRenderDispatcher rendererDispatcherIn) {
	super(rendererDispatcherIn);
    }

    @Override
    @Deprecated
    public void render(TileHydroelectricGenerator tileEntityIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn,
	    int combinedLightIn, int combinedOverlayIn) {
	BakedModel ibakedmodel = Minecraft.getInstance().getModelManager().getModel(ClientRegister.MODEL_HYDROELECTRICGENERATORBLADES);
	UtilitiesRendering.prepareRotationalTileModel(tileEntityIn, matrixStackIn);
	float partial = (float) (partialTicks * tileEntityIn.rotationSpeed * (tileEntityIn.directionFlag ? 1 : -1));
	matrixStackIn.mulPose(new Quaternion((float) ((tileEntityIn.savedTickRotation + partial) * 5f), 0, 0, true));
	UtilitiesRendering.renderModel(ibakedmodel, tileEntityIn, RenderType.solid(), matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
    }
}
