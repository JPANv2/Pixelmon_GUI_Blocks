package jpan.pixelmon.gui_blocks.proxy;

import jpan.pixelmon.gui_blocks.PixelmonGUIBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ClientProxy extends CommonProxy {
	@Override
	public void registerItemRenderer(Item item, int meta, String name) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(PixelmonGUIBlocks.MODID + ":" + name, "inventory"));
	}
	
	public void registerItemRenderer(Item item) {
		String name = ForgeRegistries.ITEMS.getKey(item).toString();
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(name, "inventory"));
	}
	
	public void registerBlockModel(Block b)
	{
		String name = ForgeRegistries.BLOCKS.getKey(b).toString();
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(b), 0, new ModelResourceLocation(name, "inventory"));
	}
	
}
