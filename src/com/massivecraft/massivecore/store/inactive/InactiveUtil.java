package com.massivecraft.massivecore.store.inactive;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.mixin.MixinMessage;
import com.massivecraft.massivecore.store.SenderColl;
import com.massivecraft.massivecore.store.SenderEntity;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Collection;

public class InactiveUtil
{
	public static void considerRemoveInactive(Iterable<CommandSender> receivers)
	{
		considerRemoveInactive(SenderColl.getSenderInstances(), receivers);
	}
	
	public static void considerRemoveInactive(Iterable<SenderColl<?>> colls, Iterable<CommandSender> receivers)
	{
		for (SenderColl<?> coll : colls)
		{
			considerRemoveInactive(coll, receivers);
		}
	}
	
	public static void considerRemoveInactive(final SenderColl<? extends SenderEntity<?>> coll, final Iterable<CommandSender> receivers)
	{
		final long removePlayerMillisDefault = coll.getRemovePlayerMillisDefault();
		if (removePlayerMillisDefault <= 0) return;
		
		final Collection<? extends SenderEntity<?>> senderEntitiesOffline = coll.getAllOffline();
		
		Bukkit.getScheduler().runTaskAsynchronously(MassiveCore.get(), new Runnable()
		{
			@Override
			public void run()
			{
				// For each offline player ...
				for (SenderEntity entity : senderEntitiesOffline)
				{
					// ... see if they should be removed.
					
					considerRemoveInactive(entity, true, receivers);
				}
			}
		});
	}
	
	public static boolean considerRemoveInactive(SenderEntity<?> entity, boolean async, Iterable<CommandSender> receivers)
	{
		if ( ! (entity instanceof Inactive)) return false;
		if (entity.detached()) return false;
		
		long lastActivityMillis = ((Inactive) entity).getLastActivityMillis();
		
		// Consider
		if (!shouldBeRemoved(entity, async)) return false;
		
		String message = Txt.parse("<i>Player <h>%s<i> with id %s was removed due to inactivity.", entity.getName(), entity.getId());
		
		for (CommandSender receiver : receivers)
		{
			MixinMessage.get().messageOne(receiver, message);
		}
		
		// Apply
		entity.detach();
		
		return true;
	}
	
	public static boolean shouldBeRemoved(SenderEntity entity, boolean async)
	{
		EventMassiveCoreRemovePlayerMillis event = new EventMassiveCoreRemovePlayerMillis(async, entity);
		event.run();
		return event.shouldBeRemoved();
	}
	
}
