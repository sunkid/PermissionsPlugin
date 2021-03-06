/**
 * LICENSING
 * 
 * This software is copyright by sunkid <sunkid@iminurnetz.com> and is
 * distributed under a dual license:
 * 
 * Non-Commercial Use:
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Commercial Use:
 *    Please contact sunkid@iminurnetz.com
 */

package com.iminurnetz.bukkit.permissions;

import org.bukkit.entity.Player;

public class NijikokunPermissions implements PermissionHandler {

	private com.nijiko.permissions.PermissionHandler handler;
	
	protected void setHandler(com.nijiko.permissions.PermissionHandler handler) {
		this.handler = handler;
	}

	@Override
	public boolean hasPermission(Player player, String permission) {
		return handler.permission(player, permission);
	}

    @Override
    public String getGroup(Player player) {
        return handler.getGroup(player.getWorld().getName(), player.getName());
    }

}
