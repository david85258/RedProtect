/*
 * Copyright (c) 2012-2023 - @FabioZumbi12
 * Last Modified: 10/05/2023 14:49
 *
 * This class is provided 'as-is', without any express or implied warranty. In no event will the authors be held liable for any
 *  damages arising from the use of this class.
 *
 * Permission is granted to anyone to use this class for any purpose, including commercial plugins, and to alter it and
 * redistribute it freely, subject to the following restrictions:
 * 1 - The origin of this class must not be misrepresented; you must not claim that you wrote the original software. If you
 * use this class in other plugins, an acknowledgment in the plugin documentation would be appreciated but is not required.
 * 2 - Altered source versions must be plainly marked as such, and must not be misrepresented as being the original class.
 * 3 - This notice may not be removed or altered from any source distribution.
 *
 * Esta classe é fornecida "como está", sem qualquer garantia expressa ou implícita. Em nenhum caso os autores serão
 * responsabilizados por quaisquer danos decorrentes do uso desta classe.
 *
 * É concedida permissão a qualquer pessoa para usar esta classe para qualquer finalidade, incluindo plugins pagos, e para
 * alterá-lo e redistribuí-lo livremente, sujeito às seguintes restrições:
 * 1 - A origem desta classe não deve ser deturpada; você não deve afirmar que escreveu a classe original. Se você usar esta
 *  classe em um plugin, uma confirmação de autoria na documentação do plugin será apreciada, mas não é necessária.
 * 2 - Versões de origem alteradas devem ser claramente marcadas como tal e não devem ser deturpadas como sendo a
 * classe original.
 * 3 - Este aviso não pode ser removido ou alterado de qualquer distribuição de origem.
 */

package br.net.fabiozumbi12.RedProtect.Bukkit.commands.SubCommands.RegionHandlers;

import br.net.fabiozumbi12.RedProtect.Bukkit.RedProtect;
import br.net.fabiozumbi12.RedProtect.Bukkit.commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static br.net.fabiozumbi12.RedProtect.Bukkit.commands.CommandHandlers.getRegionforList;
import static br.net.fabiozumbi12.RedProtect.Bukkit.commands.CommandHandlers.handleList;

public class ListCommand implements SubCommand {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender && args.length > 0) {
            //rp list [player]
            if (args.length == 1) {
                getRegionforList(sender, args[0], 1);
                return true;
            }
            //rp list [player] [page]
            if (args.length == 2) {
                try {
                    int Page = Integer.parseInt(args[1]);
                    getRegionforList(sender, args[0], Page);
                    return true;
                } catch (NumberFormatException e) {
                    RedProtect.get().getLanguageManager().sendMessage(sender, "cmdmanager.region.listpage.error");
                    return true;
                }
            }
        } else if (sender instanceof Player player) {

            //rp list
            if (args.length == 0) {
                handleList(player, ((Player) sender).getUniqueId().toString(), 1);
                return true;
            }
            //rp list [player|page]
            if (args.length == 1) {
                try {
                    int Page = Integer.parseInt(args[0]);
                    getRegionforList(sender, ((Player) sender).getUniqueId().toString(), Page);
                    return true;
                } catch (NumberFormatException e) {
                    handleList(player, RedProtect.get().getUtil().PlayerToUUID(args[0]), 1);
                    return true;
                }
            }
            //rp list [player] [page]
            if (args.length == 2) {
                try {
                    int Page = Integer.parseInt(args[1]);
                    handleList(player, RedProtect.get().getUtil().PlayerToUUID(args[0]), Page);
                    return true;
                } catch (NumberFormatException e) {
                    RedProtect.get().getLanguageManager().sendMessage(player, "cmdmanager.region.listpage.error");
                    return true;
                }
            }
        }

        RedProtect.get().getLanguageManager().sendCommandHelp(sender, "list", true);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1 && RedProtect.get().getPermissionHandler().hasPerm(sender, "redprotect.command.admin.list")) {
            if (args[0].isEmpty())
                return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
            else
                return Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(p -> p.toLowerCase().startsWith(args[0].toLowerCase())).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
