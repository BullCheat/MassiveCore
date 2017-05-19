package com.massivecraft.massivecore.cmd;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.container.TypeSet;
import com.massivecraft.massivecore.command.type.store.TypeSenderColl;
import com.massivecraft.massivecore.store.SenderColl;
import com.massivecraft.massivecore.store.inactive.InactiveUtil;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.command.CommandSender;

import java.util.Collection;
import java.util.Set;

public class CmdMassiveCoreStoreSenderClean extends MassiveCoreCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreStoreSenderClean()
	{
		// Parameters
		this.addParameter(TypeSet.get(TypeSenderColl.get()), "sender colls", "all", true).setDesc("the colls to clean inactive senders from");
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		Collection<SenderColl<?>> colls = this.readArg(SenderColl.getSenderInstances());
		
		Set<CommandSender> receivers = MUtil.set(sender, IdUtil.getConsole());
		
		msg("<i>Considering inactive senders to remove...");
		InactiveUtil.considerRemoveInactive(colls, receivers);
		msg("<i>...finished considering inactive senders to remove.");
	}
	
}
