package fr.crafter.tickleman.realgamemode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

//############################################################################## RealGameModePlugin
public class RealGameModePlugin extends JavaPlugin
{

	private Set<String> creativePlayers = new HashSet<String>();

	//------------------------------------------------------------------------------------- addPlayer
	public void addPlayer(String playerName)
	{
		creativePlayers.add(playerName);
		saveCreativePlayersFile();
	}

	//----------------------------------------------------------------------------------- canCreative
	public boolean canCreative(String playerName)
	{
		return creativePlayers.contains(playerName);
	}

	//----------------------------------------------------------------------- loadCreativePlayersFile
	private void loadCreativePlayersFile()
	{
		creativePlayers.clear();
		try {
			BufferedReader reader = new BufferedReader(
				new FileReader(getDataFolder() + "/creativeplayers.txt")
			);
			String playerName;
			while ((playerName = reader.readLine()) != null) {
				creativePlayers.add(playerName.trim());
			}
			reader.close();
		} catch (Exception e) {
		}
	}

	//------------------------------------------------------------------------------------- onCommand
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		String command = cmd.getName().toLowerCase();
		if (command.equals("gm")) {
			if (args.length == 0) {
				// switch my own creative mode : simple switch (if i have the permission or if i can)
				Player player = (Player)sender;
				if (canCreative(player.getName()) || player.hasPermission("realgamemode.set")) {
					player.setGameMode(
						GameMode.CREATIVE.equals(player.getGameMode())
						? GameMode.SURVIVAL
						: GameMode.CREATIVE
					);
					return true;
				}
			} else if (sender.isOp() || sender.hasPermission("realgamemode.set")) {
				// switch another player's "can creative" mode (if i have the permission or if i am op)
				String playerName = args[0];
				Player player = getServer().getPlayer(playerName);
				if (canCreative(playerName)) {
					removePlayer(playerName);
					sender.sendMessage(playerName + " is now jailed into SURVIVAL mode");
					if (player != null) {
						player.sendMessage("You can't switch to CREATIVE mode anymore, sorry");
					}
				} else {
					addPlayer(playerName);
					sender.sendMessage(playerName + " can now switch to CREATIVE mode using /gm");
					if (player != null) {
						player.sendMessage("You can now switch to CREATIVE mode using /gm");
					}
				}
				return true;
			}
			// help
			if (!canCreative(sender.getName()) && !sender.hasPermission("realgamemode.set")) {
				sender.sendMessage("You are not allowed to switch to CREATIVE mode");
			}
			return true;
		}
		return false;
	}

	//-------------------------------------------------------------------------------------- onEnable
	@Override
	public void onDisable()
	{
		System.out.println(
			"[RealGameMode] version [" + getDescription().getVersion() + "] ("
			+ getDescription().getAuthors().toString() + ") un-loaded"
		);
	}

	//-------------------------------------------------------------------------------------- onEnable
	@Override
	public void onEnable()
	{
		getDataFolder().mkdirs();
		loadCreativePlayersFile();
		GameModePlayerListener playerListener = new GameModePlayerListener(this);
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Type.PLAYER_JOIN, playerListener, Priority.Normal, this);
		System.out.println(
			"[RealGameMode] version [" + getDescription().getVersion() + "] ("
			+ getDescription().getAuthors().toString() + ") loaded"
		);
	}

	//---------------------------------------------------------------------------------- removePlayer
	public void removePlayer(String playerName)
	{
		Player player = getServer().getPlayer(playerName);
		if (player != null) {
			player.setGameMode(GameMode.SURVIVAL);
		}
		creativePlayers.remove(playerName);
		saveCreativePlayersFile();
	}

	//----------------------------------------------------------------------- saveCreativePlayersFile
	private void saveCreativePlayersFile()
	{
		try {
			BufferedWriter writer = new BufferedWriter(
				new FileWriter(getDataFolder() + "/creativeplayers.txt")
			);
			for (String playerName : creativePlayers) {
				writer.write(playerName + "\n");
			}
			writer.close();
		} catch (Exception e) {
		}
	}

}
