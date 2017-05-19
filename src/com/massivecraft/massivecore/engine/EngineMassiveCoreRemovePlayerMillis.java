package com.massivecraft.massivecore.engine;

import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.mixin.MixinMassiveCraftPremium;
import com.massivecraft.massivecore.store.inactive.EventMassiveCoreRemovePlayerMillis;
import com.massivecraft.massivecore.store.inactive.InactiveUtil;
import com.massivecraft.massivecore.util.IdUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.Collections;

public class EngineMassiveCoreRemovePlayerMillis extends Engine
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static EngineMassiveCoreRemovePlayerMillis i = new EngineMassiveCoreRemovePlayerMillis();
	public static EngineMassiveCoreRemovePlayerMillis get() { return i; }
	public EngineMassiveCoreRemovePlayerMillis()
	{
		if (!MassiveCore.isTaskServer()) return;
		// 10 minutes
		this.setPeriod(10 * 60 * 20L);
	}
	
	// -------------------------------------------- //
	// OVERRIDE: RUNNABLE
	// -------------------------------------------- //
	
	@Override
	public void run()
	{
		InactiveUtil.considerRemoveInactive(Collections.<CommandSender>singleton(IdUtil.getConsole()));
	}
	
	// -------------------------------------------- //
	// DEFAULT
	// -------------------------------------------- //
	
	@EventHandler(priority =  EventPriority.LOWEST, ignoreCancelled = true)
	public void defaultMillis(EventMassiveCoreRemovePlayerMillis event)
	{
		event.getCauseMillis().put("Default", event.getColl().getRemovePlayerMillisDefault());
	}
	
	// -------------------------------------------- //
	// PREMIUMS
	// -------------------------------------------- //
	
	@EventHandler(priority =  EventPriority.NORMAL, ignoreCancelled = true)
	public void premiumBonus(EventMassiveCoreRemovePlayerMillis event)
	{
		// If the coll keeps inactive premiums ...
		if (event.getColl().getRemovePlayerPremiumBonus() <= 0) return;
		
		// ... and the player is a premium ...
		if (!MixinMassiveCraftPremium.get().isPremium(event.getEntity())) return;
		
		// ... then give them more time.
		event.getCauseMillis().put("Premium", event.getColl().getRemovePlayerPremiumBonus());
	}
	
}
