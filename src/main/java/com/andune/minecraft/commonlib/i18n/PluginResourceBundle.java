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
package com.andune.minecraft.commonlib.i18n;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

/** Custom ResourceBundle which allows resource cascading, to allow for admin-defined
 * overrides for string definitions.
 * 
 * Code borrowed from Hidendra's excellent LWC plugin.
 * 
 * @author morganm
 *
 */
public class PluginResourceBundle extends ResourceBundle {

    /**
     * Stores bundles that override the defaults
     */
    private List<ResourceBundle> extensionBundles = new ArrayList<ResourceBundle>();

    public PluginResourceBundle(ResourceBundle parent) {
        this.parent = parent;
    }

    /**
     * Add a ResourceBundle to the extras
     *
     * @param bundle
     */
    public void addExtensionBundle(ResourceBundle bundle) {
        if (bundle == null) {
            return;
        }

        extensionBundles.add(bundle);
    }

    @Override
    public Enumeration<String> getKeys() {
        Set<String> keys = new HashSet<String>();
        keys.addAll(parent.keySet());

        // add the extension bundles' keys as well
        for (ResourceBundle bundle : extensionBundles) {
            keys.addAll(bundle.keySet());
        }

        return Collections.enumeration(keys);
    }

    /**
     * Get an object from one of the extension bundles
     *
     * @param key
     * @return
     */
    private Object getObjectFromExtensionBundles(String key) {
        try {
            for (ResourceBundle bundle : extensionBundles) {
                Object object = bundle.getObject(key);

                if (object != null) {
                    return object;
                }
            }
        } catch (MissingResourceException e) {
        }

        return null;
    }

    @Override
    protected Object handleGetObject(String key) {
        Object object;

        if ((object = getObjectFromExtensionBundles(key)) != null) {
            return object;
        }

        return parent.getObject(key);
    }

}
