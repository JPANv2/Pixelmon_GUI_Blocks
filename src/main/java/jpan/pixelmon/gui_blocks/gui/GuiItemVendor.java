package jpan.pixelmon.gui_blocks.gui;

import java.io.IOException;

import jpan.pixelmon.gui_blocks.messages.ItemVendorMessage;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.gui.npc.GuiShopContainer;


public class GuiItemVendor extends GuiShopContainer {

		BlockPos blockLocation;
	    
	    public GuiItemVendor(final BlockPos pos) {
	        this.blockLocation = pos;
	        this.allowMultiple = true;
	    }
	    
	    @Override
	    public void handleMouseInput() throws IOException {
	        super.handleMouseInput();
	        this.handleMouseScroll();
	    }
	    @Override
	    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
	        super.mouseClicked(mouseX, mouseY, mouseButton);
	        this.clickBuyScreen(mouseX, mouseY);
	    }
	    
	    @Override
	    protected void sendBuyPacket() {
	        Pixelmon.network.sendToServer((IMessage)new ItemVendorMessage(this.blockLocation, GuiItemVendor.buyItems.get(this.selectedItem).getItemID(), this.quantity));
	    }
	    
	    @Override
	    public void drawGuiContainerBackgroundLayer(final float f, final int mouseX, final int mouseY) {
	        this.renderBuyScreen(mouseX, mouseY);
	        int colour = 16777215;
	        final String buyLabel = I18n.translateToLocal("gui.shopkeeper.buy");
	        this.drawString(this.mc.fontRenderer, buyLabel, this.width / 2 - 2 - this.mc.fontRenderer.getStringWidth(buyLabel) / 2, this.height / 2 - 82, colour);
	        colour = 16777215;
	        if (mouseX > this.width / 2 + 28 && mouseX < this.width / 2 + 28 + 58 && mouseY > this.height / 2 - 93 && mouseY < this.height / 2 - 93 + 30) {
	            colour = 13421772;
	        }
	    }


}

