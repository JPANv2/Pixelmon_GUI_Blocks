package jpan.pixelmon.gui_blocks.blocks;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import org.apache.logging.log4j.Level;

import jpan.pixelmon.gui_blocks.PixelmonGUIBlocks;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.blocks.enums.ColorEnum;
import com.pixelmonmod.pixelmon.blocks.machines.BlockVendingMachine;
import com.pixelmonmod.pixelmon.blocks.machines.VendingMachineShop;
import com.pixelmonmod.pixelmon.comm.packetHandlers.vendingMachine.SetVendingMachineData;
import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.config.PixelmonItemsHeld;
import com.pixelmonmod.pixelmon.config.PixelmonItemsPokeballs;
import com.pixelmonmod.pixelmon.config.PixelmonItemsTMs;
import com.pixelmonmod.pixelmon.config.PixelmonItemsTools;
import com.pixelmonmod.pixelmon.entities.npcs.registry.BaseShopItem;
import com.pixelmonmod.pixelmon.entities.npcs.registry.DropItemRegistry;
import com.pixelmonmod.pixelmon.entities.npcs.registry.NPCRegistryShopkeepers;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ShopItem;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ShopItemWithVariation;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ShopkeeperData;
import com.pixelmonmod.pixelmon.enums.EnumEvolutionStone;
import com.pixelmonmod.pixelmon.enums.EnumGui;
import com.pixelmonmod.pixelmon.items.ItemEvolutionStone;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ItemVendorBlock extends Block{

	public static boolean loadedShopData = false;
	protected ArrayList<ShopItemWithVariation> shop;
	protected Callable<ArrayList<ShopItemWithVariation>> shopInit;
	
	public ItemVendorBlock(Callable<ArrayList<ShopItemWithVariation>> init){
		super(Material.ROCK);
		this.shopInit = init;
		this.shop = null;
		this.setHardness(3.5F);
	    this.setResistance(5.0F);
	    this.setCreativeTab(PixelmonCreativeTabs.utilityBlocks);
	}
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!world.isRemote && hand != EnumHand.OFF_HAND) {
            Pixelmon.network.sendTo((IMessage)new SetVendingMachineData(getShop()), (EntityPlayerMP)player);
            player.openGui((Object)PixelmonGUIBlocks.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
        return true;
    }
	
	public static ArrayList<ShopItemWithVariation> getBallShop(){
		ArrayList<ShopItemWithVariation> ans = new ArrayList<ShopItemWithVariation>();
		for(BaseShopItem itm : getPokeballsForSale()){
			addShopItem(ans, itm, 1.25f);
		}
		return ans;
	}
	
	public static ArrayList<ShopItemWithVariation> getTMShop(){
		ArrayList<ShopItemWithVariation> ans = new ArrayList<ShopItemWithVariation>();
		for(Item itm : PixelmonItemsTMs.TMs){
			addShopItem(ans, itm, getTMPrice(itm), 0);
		}
		for(Item itm : PixelmonItemsTMs.HMs){
			addShopItem(ans, itm, getTMPrice(itm)*10, 0);
		}
		return ans;
	}
	public static int getTMPrice(Item itm) {
		for(ItemStack drops: DropItemRegistry.tier3){
			if(drops.getItem().getUnlocalizedName().equals(itm.getUnlocalizedName()))
				return 50000;
		}
		for(ItemStack drops: DropItemRegistry.tier2){
			if(drops.getItem().getUnlocalizedName().equals(itm.getUnlocalizedName()))
				return 25000;
		}	
		return 10000;
	}
	
    public static ArrayList<ShopItemWithVariation> getMedicineShop() {
    	ArrayList<ShopItemWithVariation> ans = new ArrayList<ShopItemWithVariation>();
		
		for(BaseShopItem itm : getMedicineForSale()){
			addShopItem(ans, itm, 1.5f);
		}
		for(BaseShopItem itm : getRepelsForSale()){
			addShopItem(ans, itm, 1.5f);
		}
		return ans;
	}
    
    
	public static ArrayList<ShopItemWithVariation> getStatShop() {
		ArrayList<ShopItemWithVariation> ans = new ArrayList<ShopItemWithVariation>();
		for(BaseShopItem itm : getStatUpForSale()){
			addShopItem(ans, itm, 1.5f);
		}
		for(BaseShopItem itm : getPermanentStatUpForSale()){
			addShopItem(ans, itm, 1.5f);
		}
		for(BaseShopItem itm : getPermanentStatDownForSale()){
			addShopItem(ans, itm, 1.5f);
		}
		return ans;
	}
    
	public static ArrayList<ShopItemWithVariation> getEvoShop() {
		ArrayList<ShopItemWithVariation> ans = new ArrayList<ShopItemWithVariation>();
		for(BaseShopItem itm : getEvoStonesForSale()){
			addShopItem(ans, itm, 1.5f);
		}
		for(BaseShopItem itm : getEvoHeldItemsForSale()){
			addShopItem(ans, itm, 1.5f);
		}
		for(BaseShopItem itm : getIncensesForSale()){
			addShopItem(ans, itm, 1.5f);
		}
		return ans;
	}
    
	
	public static ArrayList<BaseShopItem> getMedicineForSale() {
	        final ArrayList<BaseShopItem> list = new ArrayList<BaseShopItem>();
	        list.add(new BaseShopItem(PixelmonItems.potion.getUnlocalizedName(),
	        				new ItemStack(PixelmonItems.potion, 1),
	        				300, 150));
	        list.add(new BaseShopItem(PixelmonItems.superPotion.getUnlocalizedName(),
    				new ItemStack(PixelmonItems.superPotion, 1),
    				700, 350));
	        list.add(new BaseShopItem(PixelmonItems.hyperPotion.getUnlocalizedName(),
    				new ItemStack(PixelmonItems.hyperPotion, 1),
    				1200, 600));
	        list.add(new BaseShopItem(PixelmonItems.maxPotion.getUnlocalizedName(),
    				new ItemStack(PixelmonItems.maxPotion, 1),
    				2500, 1250));
	        list.add(new BaseShopItem(PixelmonItems.ether.getUnlocalizedName(),
    				new ItemStack(PixelmonItems.ether, 1),
    				1200, 600));
	        list.add(new BaseShopItem(PixelmonItems.maxEther.getUnlocalizedName(),
    				new ItemStack(PixelmonItems.maxEther, 1),
    				2000, 1000));
	        list.add(new BaseShopItem(PixelmonItems.elixir.getUnlocalizedName(),
    				new ItemStack(PixelmonItems.elixir, 1),
    				3000, 1500));
	        list.add(new BaseShopItem(PixelmonItems.maxElixir.getUnlocalizedName(),
    				new ItemStack(PixelmonItems.maxElixir, 1),
    				4500, 2250));
	        list.add(new BaseShopItem(PixelmonItems.fullRestore.getUnlocalizedName(),
    				new ItemStack(PixelmonItems.fullRestore, 1),
    				3000, 1500));
	        list.add(new BaseShopItem(PixelmonItems.revive.getUnlocalizedName(),
    				new ItemStack(PixelmonItems.revive, 1),
    				1500, 750));
	        list.add(new BaseShopItem(PixelmonItems.maxRevive.getUnlocalizedName(),
    				new ItemStack(PixelmonItems.maxRevive, 1),
    				10000, 2000));
	        list.add(new BaseShopItem(PixelmonItems.antidote.getUnlocalizedName(),
    				new ItemStack(PixelmonItems.antidote, 1),
    				100, 50));	        
	        list.add(new BaseShopItem(PixelmonItems.parlyzHeal.getUnlocalizedName(),
    				new ItemStack(PixelmonItems.parlyzHeal, 1),
    				200, 100));	        
	        list.add(new BaseShopItem(PixelmonItems.awakening.getUnlocalizedName(),
    				new ItemStack(PixelmonItems.awakening, 1),
    				250, 125));
	        list.add(new BaseShopItem(PixelmonItems.burnHeal.getUnlocalizedName(),
    				new ItemStack(PixelmonItems.burnHeal, 1),
    				250, 125));
	        list.add(new BaseShopItem(PixelmonItems.iceHeal.getUnlocalizedName(),
    				new ItemStack(PixelmonItems.iceHeal, 1),
    				250, 125));
	        list.add(new BaseShopItem(PixelmonItems.fullHeal.getUnlocalizedName(),
    				new ItemStack(PixelmonItems.fullHeal, 1),
    				600, 300));
	        list.add(new BaseShopItem(PixelmonItems.healPowder.getUnlocalizedName(),
    				new ItemStack(PixelmonItems.healPowder, 1),
    				450, 275));
	        list.add(new BaseShopItem(PixelmonItems.energyPowder.getUnlocalizedName(),
    				new ItemStack(PixelmonItems.energyPowder, 1),
    				500, 200));
	        list.add(new BaseShopItem(PixelmonItems.energyRoot.getUnlocalizedName(),
    				new ItemStack(PixelmonItems.energyRoot, 1),
    				800, 400));
	        list.add(new BaseShopItem(PixelmonItems.revivalHerb.getUnlocalizedName(),
    				new ItemStack(PixelmonItems.revivalHerb, 1),
    				2800, 1400));
	        return list;
	}
	
	public static ArrayList<BaseShopItem> getRepelsForSale() {
	        final ArrayList<BaseShopItem> list = new ArrayList<BaseShopItem>();
	        list.add(new BaseShopItem(PixelmonItems.repel.getUnlocalizedName(),
	        				new ItemStack(PixelmonItems.repel, 1),
	        				350, 175));
	        list.add(new BaseShopItem(PixelmonItems.superRepel.getUnlocalizedName(),
    				new ItemStack(PixelmonItems.superRepel, 1),
    				500, 250));
	        list.add(new BaseShopItem(PixelmonItems.maxRepel.getUnlocalizedName(),
    				new ItemStack(PixelmonItems.maxRepel, 1),
    				700, 350));
	        return list;
	}
	
	public static ArrayList<BaseShopItem> getPermanentStatUpForSale(){
		final ArrayList<BaseShopItem> list = new ArrayList<BaseShopItem>();
		 list.add(new BaseShopItem(PixelmonItems.HpUp.getUnlocalizedName(),
 				new ItemStack(PixelmonItems.HpUp, 1),
 				9800, 4900));
		 list.add(new BaseShopItem(PixelmonItems.Protein.getUnlocalizedName(),
	 				new ItemStack(PixelmonItems.Protein, 1),
	 				9800, 4900));
		 list.add(new BaseShopItem(PixelmonItems.Iron.getUnlocalizedName(),
	 				new ItemStack(PixelmonItems.Iron, 1),
	 				9800, 4900));
		 list.add(new BaseShopItem(PixelmonItems.Calcium.getUnlocalizedName(),
	 				new ItemStack(PixelmonItems.Calcium, 1),
	 				9800, 4900));
		 list.add(new BaseShopItem(PixelmonItems.Zinc.getUnlocalizedName(),
	 				new ItemStack(PixelmonItems.Zinc, 1),
	 				9800, 4900));
		 list.add(new BaseShopItem(PixelmonItems.Carbos.getUnlocalizedName(),
	 				new ItemStack(PixelmonItems.Carbos, 1),
	 				9800, 4900));
	        return list;
	}
	
	public static ArrayList<BaseShopItem>  getStatUpForSale() {
		final ArrayList<BaseShopItem>  list = new ArrayList<BaseShopItem> ();
        list.add(new BaseShopItem(PixelmonItems.xAttack.getUnlocalizedName(),
 				new ItemStack(PixelmonItems.xAttack, 1),
 				500, 250));
        list.add(new BaseShopItem(PixelmonItems.xDefence.getUnlocalizedName(),
   				new ItemStack(PixelmonItems.xDefence, 1),
   				550, 275));
        list.add(new BaseShopItem(PixelmonItems.xSpecialAttack.getUnlocalizedName(),
   				new ItemStack(PixelmonItems.xSpecialAttack, 1),
   				350, 175));
        list.add(new BaseShopItem(PixelmonItems.xSpecialDefence.getUnlocalizedName(),
 				new ItemStack(PixelmonItems.xSpecialDefence, 1),
 				350, 175));
        list.add(new BaseShopItem(PixelmonItems.xSpeed.getUnlocalizedName(),
   				new ItemStack(PixelmonItems.xSpeed, 1),
   				350, 175));
        list.add(new BaseShopItem(PixelmonItems.xAccuracy.getUnlocalizedName(),
 				new ItemStack(PixelmonItems.xAccuracy, 1),
 				950, 475));
        list.add(new BaseShopItem(PixelmonItems.direHit.getUnlocalizedName(),
 				new ItemStack(PixelmonItems.direHit, 1),
 				650, 325));
        return list;
	}
	
	public static ArrayList<BaseShopItem> getPermanentStatDownForSale(){
		final ArrayList<BaseShopItem> list = new ArrayList<BaseShopItem>();
		 list.add(new BaseShopItem(PixelmonItems.pomegBerry.getUnlocalizedName(),
	 				new ItemStack(PixelmonItems.pomegBerry, 1),
	 				1000, 50));
		 list.add(new BaseShopItem(PixelmonItems.kelpsyBerry.getUnlocalizedName(),
	 				new ItemStack(PixelmonItems.kelpsyBerry, 1),
	 				1000, 50));
		 list.add(new BaseShopItem(PixelmonItems.qualotBerry.getUnlocalizedName(),
	 				new ItemStack(PixelmonItems.qualotBerry, 1),
	 				1000, 50));
		 list.add(new BaseShopItem(PixelmonItems.hondewBerry.getUnlocalizedName(),
	 				new ItemStack(PixelmonItems.hondewBerry, 1),
	 				1000, 50));
		 list.add(new BaseShopItem(PixelmonItems.grepaBerry.getUnlocalizedName(),
	 				new ItemStack(PixelmonItems.grepaBerry, 1),
	 				1000, 50));
		 list.add(new BaseShopItem(PixelmonItems.tamatoBerry.getUnlocalizedName(),
	 				new ItemStack(PixelmonItems.tamatoBerry, 1),
	 				1000, 50));
        return list;
	}
	
	public static ArrayList<BaseShopItem> getPokeballsForSale(){
			final ArrayList<BaseShopItem> list2 = new ArrayList<BaseShopItem>();
			list2.add(new BaseShopItem(PixelmonItemsPokeballs.pokeBall.getUnlocalizedName(),
    				new ItemStack(PixelmonItemsPokeballs.pokeBall, 1),
    				200, 100));
			list2.add(new BaseShopItem(PixelmonItemsPokeballs.greatBall.getUnlocalizedName(),
    				new ItemStack(PixelmonItemsPokeballs.greatBall, 1),
    				600, 300));
			list2.add(new BaseShopItem(PixelmonItemsPokeballs.ultraBall.getUnlocalizedName(),
    				new ItemStack(PixelmonItemsPokeballs.ultraBall, 1),
    				1200, 600));
			final ArrayList<Item> list = new ArrayList<Item>();
	        list.add(PixelmonItemsPokeballs.levelBall);
	        list.add(PixelmonItemsPokeballs.moonBall);
	        list.add(PixelmonItemsPokeballs.friendBall);
	        list.add(PixelmonItemsPokeballs.loveBall);
	        list.add(PixelmonItemsPokeballs.safariBall);
	        list.add(PixelmonItemsPokeballs.heavyBall);
	        list.add(PixelmonItemsPokeballs.fastBall);
	        list.add(PixelmonItemsPokeballs.repeatBall);
	        list.add(PixelmonItemsPokeballs.timerBall);
	        list.add(PixelmonItemsPokeballs.nestBall);
	        list.add(PixelmonItemsPokeballs.netBall);
	        list.add(PixelmonItemsPokeballs.diveBall);
	        list.add(PixelmonItemsPokeballs.luxuryBall);
	        list.add(PixelmonItemsPokeballs.duskBall);
	        list.add(PixelmonItemsPokeballs.sportBall);
	        list.add(PixelmonItemsPokeballs.lureBall);
	        list.add(PixelmonItemsPokeballs.quickBall);
	        for(Item itm: list){
	        	list2.add(new BaseShopItem(itm.getUnlocalizedName(), 
	        			new ItemStack(itm, 1), 
	        			1000,500));
	        }
	        list2.add(new BaseShopItem(PixelmonItemsPokeballs.premierBall.getUnlocalizedName(),
    				new ItemStack(PixelmonItemsPokeballs.premierBall, 1),
    				200, 100));
	        list2.add(new BaseShopItem(PixelmonItemsPokeballs.healBall.getUnlocalizedName(),
    				new ItemStack(PixelmonItemsPokeballs.healBall, 1),
    				300, 150));
	    	list2.add(new BaseShopItem(PixelmonItemsPokeballs.cherishBall.getUnlocalizedName(),
    				new ItemStack(PixelmonItemsPokeballs.cherishBall, 1),
    				10000, 100));
	    	list2.add(new BaseShopItem(PixelmonItemsPokeballs.beastBall.getUnlocalizedName(),
    				new ItemStack(PixelmonItemsPokeballs.beastBall, 1),
    				25000, 100));
	        return list2;
	}
	
	public static ArrayList<BaseShopItem> getEvoStonesForSale(){
		ArrayList<Item> list = new ArrayList<Item>();
		final ArrayList<BaseShopItem> list2 = new ArrayList<BaseShopItem>();
        list.add(PixelmonItems.fireStone);
        list.add(PixelmonItems.waterStone);
        list.add(PixelmonItems.thunderStone);
        list.add(PixelmonItems.leafStone);
        list.add(PixelmonItems.moonStone);
        list.add(PixelmonItems.sunStone);
        list.add(PixelmonItems.shinyStone);
        list.add(PixelmonItems.duskStone);
        list.add(PixelmonItems.dawnStone);
        list.add(PixelmonItems.iceStone);
        list.add(PixelmonItemsHeld.ovalStone);
        for(Item itm: list){
        	list2.add(new BaseShopItem(itm.getUnlocalizedName(), 
        			new ItemStack(itm, 1), 
        			5000,2500));
        }
        list2.add(new BaseShopItem(PixelmonItemsHeld.everStone.getUnlocalizedName(), 
    			new ItemStack(PixelmonItemsHeld.everStone, 1), 
    			1000,500));
        return list2;
	}
	
	public static ArrayList<BaseShopItem> getEvoHeldItemsForSale(){
		ArrayList<Item> list = new ArrayList<Item>();
		final ArrayList<BaseShopItem> list2 = new ArrayList<BaseShopItem>();
        list.add(PixelmonItemsHeld.metalCoat);
        list.add(PixelmonItemsHeld.kingsRock);
        list.add(PixelmonItemsHeld.upGrade);
        list.add(PixelmonItemsHeld.dubiousDisc);
        list.add(PixelmonItemsHeld.protector);
        list.add(PixelmonItemsHeld.reaperCloth);
        list.add(PixelmonItemsHeld.electirizer);
        list.add(PixelmonItemsHeld.magmarizer);
        list.add(PixelmonItemsHeld.sachet);
        list.add(PixelmonItemsHeld.whippedDream);
        list.add(PixelmonItemsHeld.dragonScale);
        list.add(PixelmonItemsHeld.prismScale);
        list.add(PixelmonItemsHeld.deepSeaScale);
        list.add(PixelmonItemsHeld.deepSeaTooth);
        list.add(PixelmonItemsHeld.razorClaw);
        list.add(PixelmonItemsHeld.razorFang);
        for(Item itm: list){
        	list2.add(new BaseShopItem(itm.getUnlocalizedName(), 
        			new ItemStack(itm, 1), 
        			20000,100));
        }
        return list2;
	}
	
	public static ArrayList<BaseShopItem> getIncensesForSale(){
		ArrayList<Item> list = new ArrayList<Item>();
		final ArrayList<BaseShopItem> list2 = new ArrayList<BaseShopItem>();
        list.add(PixelmonItemsHeld.fullIncense);
        list.add(PixelmonItemsHeld.laxIncense);
        list.add(PixelmonItemsHeld.oddIncense);
        list.add(PixelmonItemsHeld.pureIncense);
        list.add(PixelmonItemsHeld.rockIncense);
        list.add(PixelmonItemsHeld.roseIncense);
        list.add(PixelmonItemsHeld.seaIncense);
        list.add(PixelmonItemsHeld.waveIncense);
        for(Item itm: list){
        	list2.add(new BaseShopItem(itm.getUnlocalizedName(), 
        			new ItemStack(itm, 1), 
        			10000,100));
        }
        list2.add(new BaseShopItem(PixelmonItemsHeld.luckIncense.getUnlocalizedName(), 
    			new ItemStack(PixelmonItemsHeld.luckIncense, 1), 
    			30000,100));
        
        return list2;
	}
	
	public static void addShopItem(ArrayList<ShopItemWithVariation> toAdd, final Item item, final int buyPrice, final int sellPrice) {
        final BaseShopItem baseItem = new BaseShopItem(item.getUnlocalizedName(), new ItemStack(item, 1), buyPrice, sellPrice);
        addShopItem(toAdd, baseItem,1.0f);
    }
	
	public  static void addShopItem(ArrayList<ShopItemWithVariation> toAdd, final Item item, final int buyPrice, final int sellPrice, final float buyMultiplier) {
        final BaseShopItem baseItem = new BaseShopItem(item.getUnlocalizedName(), new ItemStack(item, 1), buyPrice, sellPrice);
        addShopItem(toAdd, baseItem, buyMultiplier);
    }
	
	public static void addShopItem(ArrayList<ShopItemWithVariation> toAdd, final BaseShopItem item, final float buyMultiplier) {
        final ShopItem shopItem = new ShopItem(item, buyMultiplier, 1.0f, false);
        final ShopItemWithVariation shopItemWithVariation = new ShopItemWithVariation(shopItem, 1.0f);
        toAdd.add(shopItemWithVariation);
    }
	public ArrayList<ShopItemWithVariation> getShop() {
		if(this.shop == null){
			if(this.shopInit != null){
				try {
					shop = this.shopInit.call();
				} catch (Exception e) {
					PixelmonGUIBlocks.modLogger.log(Level.ERROR,"Vendor block " + this.getLocalizedName() + " failed to create a shop list.");
					shop = new ArrayList<ShopItemWithVariation>();
				}
			}
			else{
				shop = new ArrayList<ShopItemWithVariation>();
			}
		}
		return shop;
	}
    
}
