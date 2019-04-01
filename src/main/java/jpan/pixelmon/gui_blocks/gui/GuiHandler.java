package jpan.pixelmon.gui_blocks.gui;

import javax.annotation.Nullable;

import com.pixelmonmod.pixelmon.gui.ContainerEmpty;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	@Override
	@Nullable
	public
	Object getServerGuiElement(int ID, EntityPlayer player, World world, int x,
			int y, int z) {
		return new ContainerEmpty();
	}

	@Override
	@Nullable
	public
	Object getClientGuiElement(int ID, EntityPlayer player, World world, int x,
			int y, int z) {
		if(ID == 0)
			return new GuiItemVendor(new BlockPos(x,y,z));
		return null;
	}

}
