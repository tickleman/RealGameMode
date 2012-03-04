package fr.crafter.tickleman.realgamemode;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

//########################################################################## GameModePlayerListener
public class GameModePlayerListener implements Listener
{

	RealGameModePlugin plugin;

	//------------------------------------------------------------------------ GameModePlayerListener
	public GameModePlayerListener(RealGameModePlugin plugin)
	{
		this.plugin = plugin;
	}

	//---------------------------------------------------------------------------------- onPlayerJoin
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		if (GameMode.CREATIVE.equals(player.getGameMode()) && !plugin.canCreative(player.getName())) {
			player.setGameMode(GameMode.SURVIVAL);
			player.sendMessage("You can't switch to CREATIVE mode anymore, sorry");
		}
	}

}
