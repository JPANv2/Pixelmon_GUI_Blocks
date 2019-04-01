package jpan.pixelmon.gui_blocks;


import jpan.pixelmon.gui_blocks.blocks.ItemVendorBlock;
import jpan.pixelmon.gui_blocks.blocks.MoveRelearnerBlock;
import jpan.pixelmon.gui_blocks.gui.GuiHandler;
import jpan.pixelmon.gui_blocks.messages.ItemVendorMessage;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.comm.PacketRegistry;
import com.pixelmonmod.pixelmon.comm.packetHandlers.AcceptNPCTradePacket;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.config.PixelmonItemsPokeballs;
import com.pixelmonmod.pixelmon.config.PixelmonItemsTMs;
import com.pixelmonmod.pixelmon.entities.npcs.registry.BaseShopItem;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

@Mod(modid = PixelmonGUIBlocks.MODID, name = PixelmonGUIBlocks.NAME, version = PixelmonGUIBlocks.VERSION, dependencies = "required-after:pixelmon", acceptableRemoteVersions = "*")
public class PixelmonGUIBlocks {
	  	
		public static final String MODID = "pixelmon_gui_blocks";
	    public static final String NAME = "Pixelmon GUI Blocks";
	    public static final String VERSION = "0.1";
	    
	    
		@Mod.Instance(value = "pixelmon_gui_blocks")
	    public static PixelmonGUIBlocks instance;

		@SidedProxy(serverSide = "jpan.pixelmon.gui_blocks.proxy.CommonProxy", clientSide = "jpan.pixelmon.gui_blocks.proxy.ClientProxy")
		public static jpan.pixelmon.gui_blocks.proxy.CommonProxy proxy;
		
		public static MoveRelearnerBlock move_relearner_block;
		public static ItemBlock move_relearner_block_item;
		
		
		public static ItemVendorBlock tm_vendor_block;
		public static ItemBlock tm_vendor_block_item;
		public static ItemVendorBlock medicine_vendor_block;
		public static ItemBlock medicine_vendor_block_item;
		public static ItemVendorBlock stat_vendor_block;
		public static ItemBlock stat_vendor_block_item;
		public static ItemVendorBlock ball_vendor_block;
		public static ItemBlock ball_vendor_block_item;
		public static ItemVendorBlock evo_vendor_block;
		public static ItemBlock evo_vendor_block_item;
		
		public static MoveRelearnerBlock getMoveRelearnerBlock(){
			if(move_relearner_block == null){
				move_relearner_block = new MoveRelearnerBlock();
				move_relearner_block_item = (ItemBlock) new ItemBlock(move_relearner_block).setCreativeTab(move_relearner_block.getCreativeTabToDisplayOn()).setRegistryName(move_relearner_block.getRegistryName());
			}
			return move_relearner_block;
		}
		public static ItemBlock getMoveRelearnerBlockItem(){
			if(move_relearner_block_item == null){
				move_relearner_block = new MoveRelearnerBlock();
				move_relearner_block_item = (ItemBlock) new ItemBlock(move_relearner_block).setCreativeTab(move_relearner_block.getCreativeTabToDisplayOn()).setRegistryName(move_relearner_block.getRegistryName());
			}
			return move_relearner_block_item;
		}
		
		public static ItemVendorBlock getTMVendorBlock(){
			if(tm_vendor_block == null){
				tm_vendor_block = (ItemVendorBlock)new ItemVendorBlock(ItemVendorBlock::getTMShop).setUnlocalizedName("tm_vendor_block").setRegistryName(new ResourceLocation(PixelmonGUIBlocks.MODID, "tm_vendor_block"));
				tm_vendor_block_item = (ItemBlock) new ItemBlock(tm_vendor_block).setCreativeTab(tm_vendor_block.getCreativeTabToDisplayOn()).setRegistryName(tm_vendor_block.getRegistryName());
			}
			return tm_vendor_block;
		}
		
		public static ItemBlock getTMVendorBlockItem(){
			if(tm_vendor_block_item == null){
				tm_vendor_block = (ItemVendorBlock)new ItemVendorBlock(ItemVendorBlock::getTMShop).setUnlocalizedName("tm_vendor_block").setRegistryName(new ResourceLocation(PixelmonGUIBlocks.MODID, "tm_vendor_block"));
				tm_vendor_block_item = (ItemBlock) new ItemBlock(tm_vendor_block).setCreativeTab(tm_vendor_block.getCreativeTabToDisplayOn()).setRegistryName(tm_vendor_block.getRegistryName());
			}
			return tm_vendor_block_item;
		}
		
		public static ItemVendorBlock getMedicineVendorBlock(){
			if(medicine_vendor_block == null){
				medicine_vendor_block = (ItemVendorBlock)new ItemVendorBlock(ItemVendorBlock::getMedicineShop).setUnlocalizedName("medicine_vendor_block").setRegistryName(new ResourceLocation(PixelmonGUIBlocks.MODID, "medicine_vendor_block"));
				medicine_vendor_block_item = (ItemBlock) new ItemBlock(medicine_vendor_block).setCreativeTab(medicine_vendor_block.getCreativeTabToDisplayOn()).setRegistryName(medicine_vendor_block.getRegistryName());
			}
			return medicine_vendor_block;
		}
		
		public static ItemBlock getMedicineVendorBlockItem(){
			if(medicine_vendor_block_item == null){
				medicine_vendor_block = (ItemVendorBlock)new ItemVendorBlock(ItemVendorBlock::getMedicineShop).setUnlocalizedName("medicine_vendor_block").setRegistryName(new ResourceLocation(PixelmonGUIBlocks.MODID, "medicine_vendor_block"));
				medicine_vendor_block_item = (ItemBlock) new ItemBlock(medicine_vendor_block).setCreativeTab(medicine_vendor_block.getCreativeTabToDisplayOn()).setRegistryName(medicine_vendor_block.getRegistryName());
			}
			return medicine_vendor_block_item;
		}
		
		public static ItemVendorBlock getStatVendorBlock(){
			if(stat_vendor_block == null){
				stat_vendor_block = (ItemVendorBlock)new ItemVendorBlock(ItemVendorBlock::getStatShop).setUnlocalizedName("stat_vendor_block").setRegistryName(new ResourceLocation(PixelmonGUIBlocks.MODID, "stat_vendor_block"));
				stat_vendor_block_item = (ItemBlock) new ItemBlock(stat_vendor_block).setCreativeTab(stat_vendor_block.getCreativeTabToDisplayOn()).setRegistryName(stat_vendor_block.getRegistryName());
			}
			return stat_vendor_block;
		}
		
		public static ItemBlock getStatVendorBlockItem(){
			if(stat_vendor_block_item == null){
				stat_vendor_block = (ItemVendorBlock)new ItemVendorBlock(ItemVendorBlock::getStatShop).setUnlocalizedName("stat_vendor_block").setRegistryName(new ResourceLocation(PixelmonGUIBlocks.MODID, "stat_vendor_block"));
				stat_vendor_block_item = (ItemBlock) new ItemBlock(stat_vendor_block).setCreativeTab(stat_vendor_block.getCreativeTabToDisplayOn()).setRegistryName(stat_vendor_block.getRegistryName());
			}
			return stat_vendor_block_item;
		}
		
		public static ItemVendorBlock getBallVendorBlock(){
			if(ball_vendor_block == null){
				ball_vendor_block = (ItemVendorBlock)new ItemVendorBlock(ItemVendorBlock::getBallShop).setUnlocalizedName("ball_vendor_block").setRegistryName(new ResourceLocation(PixelmonGUIBlocks.MODID, "ball_vendor_block"));
				ball_vendor_block_item = (ItemBlock) new ItemBlock(ball_vendor_block).setCreativeTab(ball_vendor_block.getCreativeTabToDisplayOn()).setRegistryName(ball_vendor_block.getRegistryName());
			}
			return ball_vendor_block;
		}
		
		public static ItemBlock getBallVendorBlockItem(){
			if(ball_vendor_block_item == null){
				ball_vendor_block = (ItemVendorBlock)new ItemVendorBlock(ItemVendorBlock::getBallShop).setUnlocalizedName("ball_vendor_block").setRegistryName(new ResourceLocation(PixelmonGUIBlocks.MODID, "ball_vendor_block"));
				ball_vendor_block_item = (ItemBlock) new ItemBlock(ball_vendor_block).setCreativeTab(ball_vendor_block.getCreativeTabToDisplayOn()).setRegistryName(ball_vendor_block.getRegistryName());
			}
			return ball_vendor_block_item;
		}
		
		public static ItemVendorBlock getEvoVendorBlock(){
			if(evo_vendor_block == null){
				evo_vendor_block = (ItemVendorBlock)new ItemVendorBlock(ItemVendorBlock::getEvoShop).setUnlocalizedName("evo_vendor_block").setRegistryName(new ResourceLocation(PixelmonGUIBlocks.MODID, "evo_vendor_block"));
				evo_vendor_block_item = (ItemBlock) new ItemBlock(evo_vendor_block).setCreativeTab(evo_vendor_block.getCreativeTabToDisplayOn()).setRegistryName(evo_vendor_block.getRegistryName());
			}
			return evo_vendor_block;
		}
		
		public static ItemBlock getEvoVendorBlockItem(){
			if(evo_vendor_block_item == null){
				evo_vendor_block = (ItemVendorBlock)new ItemVendorBlock(ItemVendorBlock::getEvoShop).setUnlocalizedName("evo_vendor_block").setRegistryName(new ResourceLocation(PixelmonGUIBlocks.MODID, "evo_vendor_block"));
				evo_vendor_block_item = (ItemBlock) new ItemBlock(evo_vendor_block).setCreativeTab(evo_vendor_block.getCreativeTabToDisplayOn()).setRegistryName(evo_vendor_block.getRegistryName());
			}
			return evo_vendor_block_item;
		}
		
		public static Logger modLogger; 
		
		@EventHandler
		public void preInit(FMLPreInitializationEvent event){
			modLogger = event.getModLog();
			NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
			Pixelmon.network.registerMessage(ItemVendorMessage.Handler.class, ItemVendorMessage.class, 10000, Side.SERVER);
		}
		
		@EventHandler
		public void postInit(FMLPostInitializationEvent event){
			for(Item itm: PixelmonItemsPokeballs.getPokeballListWithMaster()){
				OreDictionary.registerOre("pokeball_all", itm);
			}
			for(BaseShopItem itm: ItemVendorBlock.getMedicineForSale()){
				OreDictionary.registerOre("medicine_all", itm.getItem());
			}
			for(BaseShopItem itm: ItemVendorBlock.getRepelsForSale()){
				OreDictionary.registerOre("medicine_all", itm.getItem());
			}
			for(BaseShopItem itm: ItemVendorBlock.getPermanentStatDownForSale()){
				OreDictionary.registerOre("stat_item_all", itm.getItem());
			}
			for(BaseShopItem itm: ItemVendorBlock.getPermanentStatUpForSale()){
				OreDictionary.registerOre("stat_item_all", itm.getItem());
			}
			for(BaseShopItem itm: ItemVendorBlock.getStatUpForSale()){
				OreDictionary.registerOre("stat_item_all", itm.getItem());
			}
			
			for(BaseShopItem itm: ItemVendorBlock.getEvoStonesForSale()){
				OreDictionary.registerOre("evo_item_all", itm.getItem());
			}
			for(BaseShopItem itm: ItemVendorBlock.getEvoHeldItemsForSale()){
				OreDictionary.registerOre("evo_item_all", itm.getItem());
			}
			for(BaseShopItem itm: ItemVendorBlock.getIncensesForSale()){
				OreDictionary.registerOre("evo_item_all", itm.getItem());
			}
			
			for(Item itm: PixelmonItemsTMs.TMs){
				OreDictionary.registerOre("tms_all", itm);
			}
			GameRegistry.addShapedRecipe(new ResourceLocation(MODID, "moveRelearnerRecipe"), null, new ItemStack(getMoveRelearnerBlock()), new Object[]{"XZX","YCY","XZX", 'X', new ItemStack(PixelmonItems.aluminiumPlate), 'Y', new OreIngredient("pokeball_all"),'Z', new OreIngredient("tms_all"), 'C', new ItemStack(Items.NETHER_STAR)});
			GameRegistry.addShapedRecipe(new ResourceLocation(MODID, "tmVendorRecipe"), null, new ItemStack(getTMVendorBlock()), new Object[]{"XZX","YCY","XZX", 'X', new ItemStack(PixelmonItems.aluminiumPlate), 'Z', new ItemStack(Blocks.COBBLESTONE),'Y', new OreIngredient("tms_all"),'C', new ItemStack(Items.NETHER_STAR)});
			GameRegistry.addShapedRecipe(new ResourceLocation(MODID, "ballVendorRecipe"), null, new ItemStack(getBallVendorBlock()), new Object[]{"XYX","YCY","XYX", 'X', new ItemStack(PixelmonItems.aluminiumPlate), 'Y', new OreIngredient("pokeball_all"),'C', new ItemStack(Blocks.GOLD_BLOCK)});
			GameRegistry.addShapedRecipe(new ResourceLocation(MODID, "statVendorRecipe"), null, new ItemStack(getStatVendorBlock()), new Object[]{"XYX","YCY","XYX", 'X', new ItemStack(PixelmonItems.aluminiumPlate), 'Y', new OreIngredient("stat_item_all"),'C', new ItemStack(Blocks.GOLD_BLOCK)});
			GameRegistry.addShapedRecipe(new ResourceLocation(MODID, "medicineVendorRecipe"), null, new ItemStack(getMedicineVendorBlock()), new Object[]{"XYX","YCY","XYX", 'X', new ItemStack(PixelmonItems.aluminiumPlate), 'Y', new OreIngredient("medicine_all"),'C', new ItemStack(Blocks.GOLD_BLOCK)});
			GameRegistry.addShapedRecipe(new ResourceLocation(MODID, "evoVendorRecipe"), null, new ItemStack(getEvoVendorBlock()), new Object[]{"XYX","YCY","XYX", 'X', new ItemStack(PixelmonItems.aluminiumPlate), 'Y', new OreIngredient("evo_item_all"),'C', new ItemStack(Blocks.GOLD_BLOCK)});
		}
		
		@Mod.EventBusSubscriber
		public static class RegistrationHandler {
		
			@SubscribeEvent
			public static void registerBlocks(RegistryEvent.Register<Block> event){
				event.getRegistry().register(getMoveRelearnerBlock());
				event.getRegistry().register(getTMVendorBlock());
				event.getRegistry().register(getMedicineVendorBlock());
				event.getRegistry().register(getStatVendorBlock());
				event.getRegistry().register(getBallVendorBlock());
				event.getRegistry().register(getEvoVendorBlock());
			}
			
			@SubscribeEvent
			public static void registerItems(RegistryEvent.Register<Item> event) {
				event.getRegistry().register(getMoveRelearnerBlockItem());
				event.getRegistry().register(getTMVendorBlockItem());
				event.getRegistry().register(getMedicineVendorBlockItem());
				event.getRegistry().register(getStatVendorBlockItem());
				event.getRegistry().register(getBallVendorBlockItem());
				event.getRegistry().register(getEvoVendorBlockItem());
			}
			
			

			@SubscribeEvent
			public static void registerItems(ModelRegistryEvent event) {
						proxy.registerBlockModel(getMoveRelearnerBlock());
						proxy.registerItemRenderer(getMoveRelearnerBlockItem());
						proxy.registerBlockModel(getTMVendorBlock());
						proxy.registerItemRenderer(getTMVendorBlockItem());
						proxy.registerBlockModel(getMedicineVendorBlock());
						proxy.registerItemRenderer(getMedicineVendorBlockItem());
						proxy.registerBlockModel(getStatVendorBlock());
						proxy.registerItemRenderer(getStatVendorBlockItem());
						proxy.registerBlockModel(getBallVendorBlock());
						proxy.registerItemRenderer(getBallVendorBlockItem());
						proxy.registerBlockModel(getEvoVendorBlock());
						proxy.registerItemRenderer(getEvoVendorBlockItem());
			}
		}
}
