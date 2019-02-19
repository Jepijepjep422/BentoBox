package world.bentobox.bentobox.api.commands.island;

import org.bukkit.entity.Player;
import org.eclipse.jdt.annotation.NonNull;
import world.bentobox.bentobox.api.commands.CompositeCommand;
import world.bentobox.bentobox.api.commands.ConfirmableCommand;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.bentobox.database.objects.Island;

import java.util.List;

/**
 * Expels visitor (or all of them) from the island. It teleports them back to their island or the spawn.
 * @author Poslovitch
 * @since 1.3.0
 */
public class IslandExpelCommand extends ConfirmableCommand {

    public IslandExpelCommand(CompositeCommand parent) {
        super(parent, "expel");
    }

    @Override
    public void setup() {
        setPermission("island.expel");
        setOnlyPlayer(true);
        setDescription("commands.island.expel.description");
        setParametersHelp("commands.island.expel.parameters");
    }

    @Override
    public boolean canExecute(User user, String label, List<String> args) {
        if (!getIslands().inTeam(getWorld(), user.getUniqueId()) && !getIslands().hasIsland(getWorld(), user.getUniqueId())) {
            user.sendMessage("general.errors.no-island");
            return false;
        }
        return true;
    }

    @Override
    public boolean execute(User user, String label, List<String> args) {
        if (args.size() != 1) {
            showHelp(this, user);
            return false;
        }

        Island island = getIslands().getIsland(getWorld(), user);
        if (island.hasVisitors()) {
            user.sendMessage("commands.island.expel.no-visitors");
        } else {
            // We have visitors to expel!

            if (args.get(0).equals("@a")) {
                // Expel everyone (easier to handle)
                if (!getSettings().isExpelConfirmation()) {
                    island.getVisitors().forEach(this::expel);
                } else {
                    askConfirmation(user, () -> island.getVisitors().forEach(this::expel));
                }
            } else {
                // there is a target
                // TODO get the target, and expel them!
            }
        }

        return true;
    }

    private void expel(@NonNull Player player) {
        User user = User.getInstance(player);

        // TODO a user method to teleport him to a safer place
    }
}
