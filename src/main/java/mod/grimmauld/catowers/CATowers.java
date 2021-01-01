package mod.grimmauld.catowers;

import mod.grimmauld.catowers.generator.Rules;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(CATowers.MODID)
@SuppressWarnings("unused")
public class CATowers {
    public static final String MODID = "catowers";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public CATowers() {
        MinecraftForge.EVENT_BUS.register(new EventListener());
        Rules.register();
    }
}
