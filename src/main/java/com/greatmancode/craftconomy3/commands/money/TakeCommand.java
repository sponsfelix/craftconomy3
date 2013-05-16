/*
 * This file is part of Craftconomy3.
 *
 * Copyright (c) 2011-2013, Greatman <http://github.com/greatman/>
 *
 * Craftconomy3 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Craftconomy3 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Craftconomy3.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.greatmancode.craftconomy3.commands.money;

import com.greatmancode.craftconomy3.Cause;
import com.greatmancode.craftconomy3.Common;
import com.greatmancode.craftconomy3.account.Account;
import com.greatmancode.craftconomy3.currency.Currency;
import com.greatmancode.craftconomy3.utils.Tools;
import com.greatmancode.tools.commands.interfaces.CommandExecutor;

public class TakeCommand extends CommandExecutor {
	@Override
	public void execute(String sender, String[] args) {
		if (!Account.isBankAccount(args[0]) && Common.getInstance().getAccountManager().exist(args[0])) {
			if (Tools.isValidDouble(args[1])) {
				double amount = Double.parseDouble(args[1]);
				Currency currency = Common.getInstance().getCurrencyManager().getDefaultCurrency();

				if (args.length > 2) {
					if (Common.getInstance().getCurrencyManager().getCurrency(args[2]) != null) {
						currency = Common.getInstance().getCurrencyManager().getCurrency(args[2]);
					} else {
						Common.getInstance().getServerCaller().sendMessage(sender, Common.getInstance().getLanguageManager().getString("currency_not_exist"));
						return;
					}
				}

				String worldName = Common.getInstance().getAccountManager().getAccount(sender).getWorldGroupOfPlayerCurrentlyIn();
				if (args.length > 3) {
					if (!Common.getInstance().getServerCaller().worldExist(args[3])) {
						Common.getInstance().getServerCaller().sendMessage(sender, Common.getInstance().getLanguageManager().getString("world_not_exist"));
						return;
					}
					worldName = Common.getInstance().getWorldGroupManager().getWorldGroupName(args[3]);
				}

				Common.getInstance().getAccountManager().getAccount(args[0]).withdraw(amount, worldName, currency.getName(), Cause.USER, sender);
				Common.getInstance().getServerCaller().sendMessage(sender, Common.getInstance().getLanguageManager().parse("money_take", Common.getInstance().format(worldName, currency, amount), args[0]));
				if (Common.getInstance().getServerCaller().isOnline(args[0])) {
					Common.getInstance().getServerCaller().sendMessage(args[0], Common.getInstance().getLanguageManager().parse("money_take_other", Common.getInstance().format(worldName, currency, amount), sender));
				}
			} else {
				Common.getInstance().getServerCaller().sendMessage(sender, Common.getInstance().getLanguageManager().getString("invalid_amount"));
			}
		} else {
			Common.getInstance().getServerCaller().sendMessage(sender, Common.getInstance().getLanguageManager().getString("player_not_exist"));
		}
	}

	@Override
	public String help() {
		return Common.getInstance().getLanguageManager().getString("money_take_cmd_help");
	}

	@Override
	public int maxArgs() {
		return 4;
	}

	@Override
	public int minArgs() {
		return 2;
	}

	@Override
	public boolean playerOnly() {
		return false;
	}

	@Override
	public String getPermissionNode() {
		return "craftconomy.money.take";
	}
}
