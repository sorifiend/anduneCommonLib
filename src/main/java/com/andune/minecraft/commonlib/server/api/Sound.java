/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (c) 2013 Andune (andune.alleria@gmail.com)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer
 * in the documentation and/or other materials provided with the
 * distribution.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */
/**
 * 
 */
package com.andune.minecraft.commonlib.server.api;

/**
 * An Enum of Sounds we support that the server can send to the client.
 * 
 * @author andune
 *
 */
public enum Sound {
    THUNDER("thunder"),
    ENDERMAN_TELEPORT("endermanTeleport"),
    FIRE("fire"),
    EXPLODE("explode"),
    FIZZ("fizz"),
    PORTAL_TRIGGER("portalTrigger"),
    ENDERDRAGON_GROWL("enderdragonGrowl");
    
    private String name;
    private Sound(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
}
