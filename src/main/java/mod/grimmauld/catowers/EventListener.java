package mod.grimmauld.catowers;

import mod.grimmauld.catowers.commands.AllCommands;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

@SuppressWarnings("unused")
public class EventListener {
	@SubscribeEvent
	public void serverStarted(FMLServerStartingEvent event) {
		AllCommands.register(event.getCommandDispatcher());
	}
}
