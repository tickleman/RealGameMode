package fr.crafter.tickleman.realgamemode;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

//########################################################################## GameModePlayerListener
public class GameModePlayerListener extends PlayerListener
{

	RealGameModePlugin plugin;

	//------------------------------------------------------------------------ GameModePlayerListener
	public GameModePlayerListener(RealGameModePlugin plugin)
	{
		this.plugin = plugin;
	}

	//---------------------------------------------------------------------------------- onPlayerJoin
	@Override
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		if (GameMode.CREATIVE.equals(player.getGameMode()) && !plugin.canCreative(player.getName())) {
			player.setGameMode(GameMode.SURVIVAL);
			player.sendMessage("You can't switch to CREATIVE mode anymore, sorry");
		}
	}

}
