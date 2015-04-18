package com.tealcube.minecraft.bukkit.mythicdrops.commands;

/*
 * #%L
 * MythicDrops
 * %%
 * Copyright (C) 2013 - 2015 TealCube
 * %%
 * Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
 * provided that the above copyright notice and this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
 * INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF
 * THIS SOFTWARE.
 * #L%
 */


import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import se.ranzdo.bukkit.methodcommand.ArgumentHandler;
import se.ranzdo.bukkit.methodcommand.CommandArgument;
import se.ranzdo.bukkit.methodcommand.TransformError;

public final class EnchantmentArgumentHandler extends ArgumentHandler<Enchantment> {

    public EnchantmentArgumentHandler() {
        setMessage("parse_error", "There is no Enchantment named %1");
        setMessage("include_error", "There is no Enchantment named %1");
        setMessage("exclude_error", "There is no Enchantment named %1");
    }

    @Override
    public Enchantment transform(CommandSender commandSender, CommandArgument commandArgument, String s) throws TransformError {
        Enchantment e = Enchantment.getByName(s);
        if (e == null) {
            throw new TransformError(commandArgument.getMessage("parse_error", s));
        }
        return e;
    }

}
