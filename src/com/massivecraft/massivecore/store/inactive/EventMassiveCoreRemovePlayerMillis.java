package com.massivecraft.massivecore.store.inactive;

import com.massivecraft.massivecore.event.EventMassiveCore;
import com.massivecraft.massivecore.store.SenderColl;
import com.massivecraft.massivecore.store.SenderEntity;
import org.bukkit.event.HandlerList;

import java.util.LinkedHashMap;
import java.util.Map;

public class EventMassiveCoreRemovePlayerMillis extends EventMassiveCore
{
	// -------------------------------------------- //
	// REQUIRED EVENT CODE
	// -------------------------------------------- //
	
	private static final HandlerList handlers = new HandlerList();
	@Override public HandlerList getHandlers() { return handlers; }
	public static HandlerList getHandlerList() { return handlers; }
	
	// -------------------------------------------- //
	// FIELD
	// -------------------------------------------- //
	
	private final long now;
	public long getNow() { return now; }
	
	private final SenderEntity<?> entity;
	public SenderEntity<?> getEntity() { return this.entity; }
	
	public SenderColl<?> getColl() { return entity.getColl(); }
	
	private Map<String, Long> causeMillis = new LinkedHashMap<>();
	public Map<String, Long> getCauseMillis() { return this.causeMillis; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public EventMassiveCoreRemovePlayerMillis(boolean async, SenderEntity entity)
	{
		this(async, System.currentTimeMillis(), entity);
	}
	
	public EventMassiveCoreRemovePlayerMillis(boolean async, long now, SenderEntity entity)
	{
		super(async);
		this.now = now;
		this.entity = entity;
	}
	
	// -------------------------------------------- //
	// CONVENIENCE
	// -------------------------------------------- //
	
	public long getToleranceMillis()
	{
		long ret = 0;
		
		for (Long value : causeMillis.values())
		{
			ret += value;
		}
		
		return ret;
	}
	
	public boolean shouldBeRemoved()
	{
		long toleranceMillis = getToleranceMillis();
		
		long now = this.getNow();
		long lastActivityMillis = ((Inactive)this.getEntity()).getLastActivityMillis();
		
		long removeTime = lastActivityMillis + toleranceMillis;
		
		return now >= removeTime;
	}
	
}
