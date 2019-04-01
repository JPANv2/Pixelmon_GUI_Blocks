package jpan.pixelmon.gui_blocks.messages;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;

import jpan.pixelmon.gui_blocks.PixelmonGUIBlocks;
import jpan.pixelmon.gui_blocks.blocks.ItemVendorBlock;
import net.minecraft.crash.CrashReport;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.economy.IPixelmonBankAccount;
import com.pixelmonmod.pixelmon.blocks.machines.BlockVendingMachine;
import com.pixelmonmod.pixelmon.comm.packetHandlers.vendingMachine.VendingMachinePacket;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ShopItemWithVariation;

public class ItemVendorMessage implements IMessage
{
    String itemID;
    int amount;
    BlockPos pos;
    
    public ItemVendorMessage() {
    }
    
    public ItemVendorMessage(final BlockPos pos, final String itemName, final int amount) {
        this.pos = pos;
        this.itemID = itemName;
        this.amount = amount;
    }
    
    public void fromBytes(final ByteBuf buf) {
        final int x = buf.readInt();
        final int y = buf.readInt();
        final int z = buf.readInt();
        this.pos = new BlockPos(x, y, z);
        this.itemID = ByteBufUtils.readUTF8String(buf);
        this.amount = buf.readInt();
    }
    
    public void toBytes(final ByteBuf buf) {
        buf.writeInt(this.pos.getX());
        buf.writeInt(this.pos.getY());
        buf.writeInt(this.pos.getZ());
        ByteBufUtils.writeUTF8String(buf, this.itemID);
        buf.writeInt(this.amount);
    }
    
    public static boolean checkInventoryCanFit(final InventoryPlayer inventoryIn, final ItemStack itemStackIn) {
        final InventoryPlayer inventory = new InventoryPlayer(inventoryIn.player);
        if (itemStackIn != null && itemStackIn.getItem() != null && itemStackIn.getCount() != 0) {
        	int totalItems = itemStackIn.getCount();
        	for(int i = 0; i< inventory.getSizeInventory(); i++){
        		ItemStack cur = inventory.getStackInSlot(i);
        		if(cur == null || cur.getItem() == Items.AIR){
        			totalItems -= itemStackIn.getMaxStackSize();
        		}
        		else if(itemStackIn.getUnlocalizedName().equals(cur.getUnlocalizedName()) && cur.getCount() < cur.getMaxStackSize()){
        			totalItems -= (cur.getMaxStackSize() - cur.getCount());
        		}
        		if(totalItems <= 0)
        			return true;        		
        	}
        }
        return false;
    }
    
    public static class Handler implements IMessageHandler<ItemVendorMessage, IMessage>
    {
        public IMessage onMessage(final ItemVendorMessage message, final MessageContext ctx) {
            final EntityPlayerMP p = ctx.getServerHandler().player;
            final IPixelmonBankAccount account = (IPixelmonBankAccount)Pixelmon.moneyManager.getBankAccount(p).get();
            if (account != null) {
                final ArrayList<ShopItemWithVariation> itemList = ((ItemVendorBlock)(p.world.getBlockState(message.pos).getBlock())).getShop();
                for (final ShopItemWithVariation s : itemList) {
                    if (s.getBaseShopItem().id.equals(message.itemID)) {
                        s.getBuyCost();
                        if (account.getMoney() < s.getBuyCost()) {
                            continue;
                        }
                        final ItemStack item = s.getItem();
                        final ItemStack buyStack = item.copy();
                        buyStack.setCount(message.amount);
                        if (checkInventoryCanFit(p.inventory, buyStack)) {
                            p.inventory.addItemStackToInventory(buyStack);
                            account.changeMoney(-s.getBuyCost());
                            p.sendContainerToPlayer(p.inventoryContainer);
                           // p.closeScreen();
                            return null;
                        }
                        continue;
                    }
                }
            }
            return null;
        }

    }
}