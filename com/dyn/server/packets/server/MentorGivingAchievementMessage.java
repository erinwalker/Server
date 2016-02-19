package com.dyn.server.packets.server;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;

import java.io.IOException;

import com.dyn.achievements.achievement.AchievementType;
import com.dyn.achievements.handlers.AchievementHandler;
import com.dyn.login.LoginGUI;
import com.dyn.server.ServerMod;
import com.dyn.server.packets.PacketDispatcher;
import com.dyn.server.packets.AbstractMessage.AbstractServerMessage;
import com.dyn.server.packets.client.SyncAchievementsMessage;

import cpw.mods.fml.relauncher.Side;

public class MentorGivingAchievementMessage extends AbstractServerMessage<MentorGivingAchievementMessage> {
	private String player_name;
	private int ach_id;

	// The basic, no-argument constructor MUST be included to use the new
	// automated handling
	public MentorGivingAchievementMessage() {
	}

	// if there are any class fields, be sure to provide a constructor that
	// allows
	// for them to be initialized, and use that constructor when sending the
	// packet	
	public MentorGivingAchievementMessage(String username, int id) {
		this.player_name = username;
		this.ach_id = id;
	}

	@Override
	protected void read(PacketBuffer buffer) throws IOException {
		// basic Input/Output operations, very much like DataInputStream
		this.player_name = buffer.readStringFromBuffer(100);
		this.ach_id = buffer.readInt();
	}

	@Override
	protected void write(PacketBuffer buffer) throws IOException {
		// basic Input/Output operations, very much like DataOutputStream
		buffer.writeStringToBuffer(this.player_name);
		buffer.writeInt(this.ach_id);
	}

	@Override
	public void process(EntityPlayer player, Side side) {
		// using the message instance gives access to 'this.id'
		if (side.isServer()) {
			for(EntityPlayerMP p : ServerMod.proxy.getServerUsers()){
				if(p.getDisplayName().equals(this.player_name)){
					PacketDispatcher.sendTo(new SyncAchievementsMessage(
							"" + this.ach_id + " " + AchievementType.MENTOR + " 0", true),
					(EntityPlayerMP) p);
				}
			}
		}
	}
}