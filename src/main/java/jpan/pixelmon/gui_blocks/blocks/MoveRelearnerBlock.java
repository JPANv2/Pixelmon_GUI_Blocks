package jpan.pixelmon.gui_blocks.blocks;

import java.util.ArrayList;
import java.util.List;

import jpan.pixelmon.gui_blocks.PixelmonGUIBlocks;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.InteractNPC;
import com.pixelmonmod.pixelmon.comm.packetHandlers.vendingMachine.SetVendingMachineData;
import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import com.pixelmonmod.pixelmon.entities.npcs.NPCRelearner;
import com.pixelmonmod.pixelmon.enums.EnumGui;
import com.pixelmonmod.pixelmon.enums.EnumNPCType;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandException;
import net.minecraft.command.server.CommandSummon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MoveRelearnerBlock extends Block{

	public MoveRelearnerBlock(){
		super(Material.ROCK);
		this.setHardness(3.5F);
	    this.setResistance(5.0F);
	    this.setCreativeTab(PixelmonCreativeTabs.utilityBlocks);
	    this.setUnlocalizedName("move_relearner_block");
	    this.setRegistryName(new ResourceLocation(PixelmonGUIBlocks.MODID, "move_relearner_block"));
	}
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!world.isRemote && hand != EnumHand.OFF_HAND) {
        	int npcID = getRelearnerID(world, (EntityPlayerMP)player);
            Pixelmon.network.sendTo((IMessage)new InteractNPC(npcID, EnumNPCType.Relearner), (EntityPlayerMP)player);
            player.openGui((Object)Pixelmon.instance, (int)EnumGui.ChooseRelearnMove.getIndex(), world, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
        return true;
    }
	protected int getRelearnerID(World world, EntityPlayerMP player) {
		List<NPCRelearner> existingRelearners = world.getEntities(NPCRelearner.class, x -> true);
    	if(existingRelearners == null){
    		existingRelearners = new ArrayList<NPCRelearner>();
    	}
    	if(existingRelearners.isEmpty()){
    		CommandSummon cs = new CommandSummon();
    		try {
				cs.execute(world.getMinecraftServer(), player, new String[] {"Pixelmon:npc_relearner"});
			} catch (CommandException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		existingRelearners = world.getEntities(NPCRelearner.class, x -> true);
    		return existingRelearners.get(0).getId();
    	}
		return existingRelearners.get(0).getId();
	}
	
}
